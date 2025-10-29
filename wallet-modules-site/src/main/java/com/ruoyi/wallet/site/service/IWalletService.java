package com.ruoyi.wallet.site.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.member.api.dto.MemberCreateDTO;
import com.ruoyi.wallet.api.dto.MemberBalanceChangeDTO;
import com.ruoyi.wallet.site.domain.Wallet;

import java.math.BigDecimal;
import java.util.List;

/**
 * 站点用户信息Service接口
 *
 * @author ruoyi
 * @date 2024-05-09
 */
public interface IWalletService extends IService<Wallet>
{

    List<Wallet> getWallets(Long siteId, Long memberId);
    Wallet getWallet(Long siteId, Long memberId, String currency);

    BigDecimal updateMemberWallet(MemberBalanceChangeDTO memberBalanceChangeDTO);
}
