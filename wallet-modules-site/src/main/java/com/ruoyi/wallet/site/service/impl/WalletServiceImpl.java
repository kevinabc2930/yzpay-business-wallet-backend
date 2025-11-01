package com.ruoyi.wallet.site.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.dto.RequestDTO;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.wallet.api.constants.WalletMqKey;
import com.ruoyi.wallet.api.constants.WalletRedisKey;
import com.ruoyi.wallet.api.dto.BalanceChangeDTO;
import com.ruoyi.wallet.api.dto.BalanceRecordDTO;
import com.ruoyi.wallet.site.domain.BalanceRecord;
import com.ruoyi.wallet.site.domain.Currency;
import com.ruoyi.wallet.site.domain.Wallet;
import com.ruoyi.wallet.site.mapper.CurrencyMapper;
import com.ruoyi.wallet.site.mapper.WalletMapper;
import com.ruoyi.wallet.site.service.IBalanceRecordService;
import com.ruoyi.wallet.site.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 钱包息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
@Slf4j
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements IWalletService {
    @Autowired
    private CurrencyMapper currencyMapper;
    @Autowired
    private IBalanceRecordService balanceRecordService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Lock4j(name = "memberCreate", keys = {"#siteId", "#memberId"})
    public List<Wallet> getWallets(Long siteId, Long memberId) {
        List<Wallet> list = baseMapper.selectList(new LambdaQueryWrapper<Wallet>()
                .eq(Wallet::getSiteId,siteId).eq(Wallet::getMemberId, memberId));
        if (list.isEmpty()) {
            List<Currency> currencyList = currencyMapper.selectList(new LambdaQueryWrapper<Currency>()
                    .eq(Currency::getSiteId, siteId));
            log.info("创建用户同步钱包,站点货币:{}", JSONObject.toJSONString(currencyList));

            if (currencyList.isEmpty()) {
                log.error("创建用户同步钱包,站点货币配置为空");
                throw new ServiceException("10000", "站点货币配置为空");
            }
            list = currencyList.stream().map(e -> {
                Wallet wallet = new Wallet();
                wallet.setMemberId(memberId);
                wallet.setSiteId(siteId);
                wallet.setCurrency(e.getCurrency());
                wallet.setBalance(BigDecimal.ZERO);
                return wallet;
            }).collect(Collectors.toList());

            saveBatch(list);
        }
        return list;
    }

    @Override
    public Wallet getWallet(Long siteId, Long memberId, String currency) {
        Wallet wallet = baseMapper.selectOne(new LambdaQueryWrapper<Wallet>()
                .eq(Wallet::getSiteId, siteId)
                .eq(Wallet::getMemberId, memberId)
                .eq(Wallet::getCurrency, currency)
        );
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setCurrency(currency);
            wallet.setSiteId(siteId);
            wallet.setMemberId(memberId);
            baseMapper.insert(wallet);
        }
        return wallet;
    }

    @Override
    @Transactional
    @Lock4j(name = WalletRedisKey.PREFIX + "balanceChange", keys = {"#balanceChangeDTO.siteId", "#balanceChangeDTO.memberId", "#balanceChangeDTO.currency"})
    public BigDecimal balanceChange(BalanceChangeDTO balanceChangeDTO) {
        log.info("余额变更:{}", JSONObject.toJSONString(balanceChangeDTO));
        Wallet wallet = getWallet(balanceChangeDTO.getSiteId(), balanceChangeDTO.getMemberId(), balanceChangeDTO.getCurrency());
        if (balanceChangeDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            if (wallet.getBalance().compareTo(balanceChangeDTO.getAmount().abs()) < 0) {
                throw new ServiceException("10001", "余额不足");
            }
        }
        BalanceRecord balanceRecord = new BalanceRecord();
        balanceRecord.setSiteId(balanceChangeDTO.getSiteId());
        balanceRecord.setMemberId(balanceChangeDTO.getMemberId());
        balanceRecord.setBusinessId(balanceChangeDTO.getBusinessId());
        balanceRecord.setOrderNo(balanceChangeDTO.getOrderNo());
        balanceRecord.setCurrency(balanceChangeDTO.getCurrency());
        balanceRecord.setAmount(balanceChangeDTO.getAmount().toPlainString());
        balanceRecord.setBeforeAmount(wallet.getBalance().toPlainString());
        //余额变更
        wallet.setBalance(wallet.getBalance().add(balanceChangeDTO.getAmount()));
        baseMapper.updateById(wallet);
        balanceRecord.setAfterAmount(wallet.getBalance().toPlainString());
        balanceRecord.setExt(balanceChangeDTO.getExt());
        balanceRecord.setTimeLong(System.currentTimeMillis() + "");
        //插入账变记录
        balanceRecordService.add(balanceRecord);
        BalanceRecordDTO balanceRecordDTO = new BalanceRecordDTO();
        BeanUtils.copyProperties(balanceRecord, balanceRecordDTO);
        //发送账变记录到mq，数据服务消费
        rabbitTemplate.convertAndSend(WalletMqKey.MQ_WALLET_BALANCE_CHANGE_RECORD_EXCHANGE, WalletMqKey.MQ_WALLET_BALANCE_CHANGE_RECORD_ROUTING_KEY,
                RequestDTO.build(balanceRecordDTO));
        return wallet.getBalance();
    }
}
