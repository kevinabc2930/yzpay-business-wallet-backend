package com.ruoyi.wallet.api.dto;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@Slf4j
public class BalanceRecordDTO extends BaseEntity {
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
