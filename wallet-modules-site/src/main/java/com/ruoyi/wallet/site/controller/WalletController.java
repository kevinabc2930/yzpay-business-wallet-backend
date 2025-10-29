package com.ruoyi.wallet.site.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.dto.LoginMember;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.wallet.api.RemoteWalletService;
import com.ruoyi.wallet.api.dto.MemberBalanceChangeDTO;
import com.ruoyi.wallet.api.dto.WalletVO;
import com.ruoyi.wallet.site.domain.Wallet;
import com.ruoyi.wallet.site.service.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 站点用户信息Controller
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/wallet")
public class WalletController extends BaseController implements RemoteWalletService {

    @Autowired
    IWalletService walletService;


    /**
     * 获取用户钱包
     *
     * @return 结果
     */
    @GetMapping("/getWallet")
    public R<WalletVO> getWallet(@RequestParam("siteId") Long siteId, @RequestParam("memberId") Long memberId, @RequestParam("currency") String currency, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        Wallet wallet =  walletService.getWallet(siteId,memberId,currency);
        return R.ok(BeanUtil.copyProperties(wallet, WalletVO.class));
    }

    /**
     * 获取用户钱包s
     *
     * @return 结果
     */
    @GetMapping("/getWallets")
    public R<Map<String, WalletVO>> getWallets(@RequestParam("siteId") Long siteId, @RequestParam("memberId") Long memberId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        List<Wallet> list =  walletService.getWallets(siteId,memberId);
        List<WalletVO> voList = BeanUtil.copyToList(list,WalletVO.class);
        return R.ok(voList.stream().collect(Collectors.toMap(WalletVO::getCurrency, e->e)));
    }


    /**
     * 余额
     *
     * @return 结果
     */
    @GetMapping("/getBalance")
    public R<Map<String, WalletVO>> getBalance() {
        LoginMember loginMember = SecurityUtils.getLoginMember();
        List<Wallet> list =  walletService.getWallets(loginMember.getSiteId(),loginMember.getId());
        List<WalletVO> voList = BeanUtil.copyToList(list,WalletVO.class);
        return R.ok(voList.stream().collect(Collectors.toMap(WalletVO::getCurrency, e->e)));
    }


    /**
     * 玩家余额变动
     *
     * @param balanceDTO 请求参数
     * @return 返回结果
     */
    @PostMapping("/balance")
    public R<BigDecimal> balance(@RequestBody @Valid MemberBalanceChangeDTO balanceDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        BigDecimal result = walletService.updateMemberWallet(balanceDTO);
        return R.ok(result);
    }

}