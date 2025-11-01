package com.ruoyi.wallet.api;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.context.OpenFeignCongregation;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.wallet.api.dto.BalanceChangeDTO;
import com.ruoyi.wallet.api.dto.BalanceRecordVO;
import com.ruoyi.wallet.api.dto.WalletVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 钱包服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteWalletUserService", value = ServiceNameConstants.WALLET_SITE,
        fallbackFactory = RemoteSiteWalletFallbackFactory.class, path = "/wallet", configuration = OpenFeignCongregation.class)
public interface RemoteWalletService {
    /**
     * 获取用户钱包
     *
     * @return 结果
     */
    @GetMapping("/getWallet")
    public R<WalletVO> getWallet(@RequestParam("siteId") Long siteId, @RequestParam("memberId") Long memberId, @RequestParam("currency") String currency, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 余额变动
     *
     * @return 结果
     */
    @PostMapping("/balance")
    R<BigDecimal> balance(@RequestBody BalanceChangeDTO balanceDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取用户钱包s
     *
     * @return 结果
     */
    @GetMapping("/getWallets")
    public R<Map<String, WalletVO>> getWallets(@RequestParam("siteId") Long siteId, @RequestParam("memberId") Long memberId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取用户账变记录
     *
     * @return 结果
     */
    @GetMapping("/getBalanceRecord")
    public R<List<BalanceRecordVO>> getBalanceRecord(@RequestParam("siteId") Long siteId, @RequestParam("memberId") Long memberId, @RequestParam("businessId") Integer businessId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
