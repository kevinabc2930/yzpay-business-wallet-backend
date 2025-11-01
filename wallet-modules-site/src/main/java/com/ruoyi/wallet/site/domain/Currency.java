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


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "currency", description = "货币")
@TableName("currency")
public class Currency extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long siteId;
    private String currency;
    @TableLogic
    private String delFlag;

}
