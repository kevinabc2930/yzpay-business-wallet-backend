package com.ruoyi.wallet.site.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "balance_record", description = "账变记录")
@TableName("balance_record")
public class BalanceRecord extends BaseEntity {
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private String delFlag;

}
