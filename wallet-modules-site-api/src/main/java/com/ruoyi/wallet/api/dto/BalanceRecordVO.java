package com.ruoyi.wallet.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BalanceRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long siteId;
    private String currency;
    private Long memberId;
    private Integer businessId;
    private String orderNo;
    private String amount;
    private String beforeAmount;
    private String afterAmount;
    private String timeLong;
    private String ext;
    private String delFlag;

}
