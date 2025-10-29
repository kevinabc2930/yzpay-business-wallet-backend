package com.ruoyi.wallet.api.dto;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

@Data
public class WalletReq extends BaseEntity {

    private Long id;
    private String username;
    private String nickname;
    private Long siteId;
}
