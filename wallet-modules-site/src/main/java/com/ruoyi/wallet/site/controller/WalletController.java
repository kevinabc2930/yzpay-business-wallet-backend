package com.ruoyi.wallet.site.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.dto.LoginMember;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.wallet.api.RemoteWalletService;
import com.ruoyi.wallet.api.dto.BalanceChangeDTO;
import com.ruoyi.wallet.api.dto.BalanceRecordVO;
import com.ruoyi.wallet.api.dto.WalletVO;
import com.ruoyi.wallet.site.domain.Wallet;
import com.ruoyi.wallet.site.service.IBalanceRecordService;
import com.ruoyi.wallet.site.service.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    IBalanceRecordService iBalanceRecordService;


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
     * 查询账变记录
     * @return 结果
     */
    @Override
    public R<List<BalanceRecordVO>> getBalanceRecord(@RequestParam("siteId") Long siteId, @RequestParam("memberId") Long memberId, @RequestParam("businessId") Integer businessId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        return R.ok(iBalanceRecordService.getBalanceRecord(siteId, memberId, businessId, startTime ,endTime));
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
    public R<BigDecimal> balance(@RequestBody @Valid BalanceChangeDTO balanceDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        BigDecimal result = walletService.balanceChange(balanceDTO);
        return R.ok(result);
    }

}