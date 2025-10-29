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

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "wallet", description = "钱包")
@TableName("site_wallet")
public class Wallet extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long siteId;
    private Long memberId;
    private String currency;
    private BigDecimal balance;

    @TableLogic
    private String delFlag;

}
