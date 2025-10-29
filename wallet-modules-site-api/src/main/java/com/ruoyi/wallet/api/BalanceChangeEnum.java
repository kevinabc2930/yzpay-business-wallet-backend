package com.ruoyi.wallet.api;

import lombok.Getter;

/**
 * 玩家余额变动枚举
 */
@Getter
public enum BalanceChangeEnum {
    //充值相关赠送
    CURR_BETTING(10000, "游戏投注"),
    CURR_REBATE(10001, "游戏返奖"),
    CURR_BANK_RECHARGE(10007, "银行卡充值"),
    CURR_ONLINE_RECHARGE(10008, "线上充值"),
    CURR_PERSON_RECHARGE(10009, "人工充值"),
    CURR_WITHDRAW(10010, "提现"),
    CURR_RECHARGE_GIFT(10019, "充值赠送"),
    CURR_REGISTER_GIFT(10020, "注册赠送"),
    CURR_OFFLINE_GIFT(10023, "线下赠送"),
    CURR_WITHDRAW_HANDLING_FEE(10028, "提现手续费"),
    CURR_BETTING_RETURN(10034, "投注返还"),

    ;

    BalanceChangeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

}
