package com.ruoyi.wallet.site.config;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.dto.RequestDTO;
import com.ruoyi.member.api.constants.MemberMqKey;
import com.ruoyi.member.api.dto.MemberCreateDTO;
import com.ruoyi.wallet.api.constants.WalletMqKey;
import com.ruoyi.wallet.api.dto.BalanceChangeDTO;
import com.ruoyi.wallet.site.service.IWalletService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
@Slf4j
public class WalletMqConsumer {

    @Autowired
    IWalletService walletService;
    /**
     * 创建用户同步钱包
     */
//    @Trace
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MemberMqKey.MQ_MEMBER_CREATE_QUEUE, durable = "true", autoDelete = "false", exclusive = "false"),
            key = MemberMqKey.MQ_MEMBER_CREATE_ROUTING_KEY,
            exchange = @Exchange(name = MemberMqKey.MQ_MEMBER_CREATE_EXCHANGE, type = ExchangeTypes.TOPIC))
            , concurrency = "4-16")
//    @Lock4j(name =  "memberCreate", keys = {"#requestt.data.id"},expire = 10000,acquireTimeout = 3000)
    public void memberCreate(RequestDTO<MemberCreateDTO> request) {
        try {
            MemberCreateDTO memberCreateDTO = request.getData();
            log.info("创建用户同步钱包,收到消息：{}", JSONObject.toJSONString(memberCreateDTO));
            //walletService.getWallets(memberCreateDTO.getSiteId(), memberCreateDTO.getId());
        } catch (Exception e) {
            log.error("创建用户同步钱包,处理异常", e);
        }
    }
    /**
     * 账变请求消息消费
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = WalletMqKey.MQ_WALLET_BALANCE_QUEUE, durable = "true", autoDelete = "false", exclusive = "false"),
            key = WalletMqKey.MQ_WALLET_BALANCE_CHANGE_ROUTING_KEY,
            exchange = @Exchange(name = WalletMqKey.MQ_WALLET_BALANCE_CHANGE_EXCHANGE, type = ExchangeTypes.TOPIC))
            , concurrency = "4-16")
    public void balanceChange(RequestDTO<BalanceChangeDTO> request) {
        try {
            BalanceChangeDTO balanceChangeDTO = request.getData();
            log.info("账变请求,收到消息：{}", JSONObject.toJSONString(balanceChangeDTO));
            walletService.balanceChange(balanceChangeDTO);
        } catch (Exception e) {
            log.error("账变请求,处理异常", e);
        }
    }

}
