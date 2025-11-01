/**
 * @filename:PlayerLevel 2023年05月16日 11:02:09
 * @project PlayerLevel  V1.0
 * Copyright(c) 2023年05月16日 11:02:09 author Co. Ltd.
 * All right reserved.
 */
package com.ruoyi.wallet.api.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@Slf4j
public class BalanceChangeDTO implements Serializable {
    private static final long serialVersionUID = 7505317431350714801L;

    /**
     * 玩家id
     */
    @NotNull
    private Long memberId;
    /**
     * 站点id
     */
    @NotNull
    private Long siteId;
    /**
     * 变动金额(加钱金额为正, 减钱金额为负)
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 账变类型
     */
    @NotNull
    private Integer businessId;
    /**
     * 币种
     */
    @NotBlank
    private String currency;
    private String orderNo;
    private String ext;
}
