package com.ruoyi.wallet.site.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.wallet.api.constants.WalletRedisKey;
import com.ruoyi.wallet.api.dto.MemberBalanceChangeDTO;
import com.ruoyi.wallet.site.domain.Currency;
import com.ruoyi.wallet.site.domain.Wallet;
import com.ruoyi.wallet.site.mapper.CurrencyMapper;
import com.ruoyi.wallet.site.mapper.WalletMapper;
import com.ruoyi.wallet.site.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 站点用户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
@Slf4j
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements IWalletService {
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private CurrencyMapper currencyMapper;

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
    @Lock4j(name = WalletRedisKey.PREFIX + "updateMemberWallet", keys = {"#balanceDTO.siteId", "#balanceDTO.memberId", "#balanceDTO.currency"})
    public BigDecimal updateMemberWallet(MemberBalanceChangeDTO balanceDTO) {
        log.info("玩家余额变更:{}", JSONObject.toJSONString(balanceDTO));

        //强制走主库, 防止主从同步问题
//            if (!HintManager.isMasterRouteOnly()) {
//                HintManager.clear();
//                HintManager hintManager = HintManager.getInstance();
//                hintManager.setMasterRouteOnly();
//            }

        Wallet wallet = getWallet(balanceDTO.getSiteId(), balanceDTO.getMemberId(), balanceDTO.getCurrency());
        if (balanceDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            if (wallet.getBalance().compareTo(balanceDTO.getAmount().abs()) < 0) {
                throw new ServiceException("10001", "余额不足");
            }
        }
        Wallet update = new Wallet();
        update.setId(wallet.getId());
        update.setBalance(wallet.getBalance().add(balanceDTO.getAmount()));
        baseMapper.updateById(update);

        //TODO 账变记录

        return update.getBalance();
    }
}
