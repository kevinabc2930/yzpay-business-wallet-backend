package com.ruoyi.wallet.site.config;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.dto.RequestDTO;
import com.ruoyi.member.api.constants.MemberMqKey;
import com.ruoyi.member.api.dto.MemberCreateDTO;
import com.ruoyi.wallet.site.domain.Wallet;
import com.ruoyi.wallet.site.service.IWalletService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
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
            walletService.getWallets(memberCreateDTO.getSiteId(), memberCreateDTO.getId());
        } catch (Exception e) {
            log.error("创建用户同步钱包,处理异常", e);
        }
    }


}
