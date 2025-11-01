package com.ruoyi.wallet.api;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.wallet.api.dto.BalanceChangeDTO;
import com.ruoyi.wallet.api.dto.BalanceRecordVO;
import com.ruoyi.wallet.api.dto.WalletVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户服务降级处理
 * 
 * @author ruoyi
 */
@Component
public class RemoteSiteWalletFallbackFactory implements FallbackFactory<RemoteWalletService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteSiteWalletFallbackFactory.class);

    @Override
    public RemoteWalletService create(Throwable throwable)
    {
        log.error("钱包服务调用失败:{}", throwable.getMessage());
        return new RemoteWalletService()
        {

            @Override
            public R<WalletVO> getWallet(Long siteId, Long memberId, String currency, String source) {
                return R.fail("获取钱包失败:" + throwable.getMessage());
            }

            @Override
            public R<BigDecimal> balance(BalanceChangeDTO balanceDTO, String source) {
                return R.fail("修改钱包失败:" + throwable.getMessage());
            }

            @Override
            public R<Map<String, WalletVO>> getWallets(Long siteId, Long memberId, String source) {
                return R.fail("获取钱包失败:" + throwable.getMessage());
            }

            @Override
            public R<List<BalanceRecordVO>> getBalanceRecord(Long siteId, Long memberId, Integer businessId, Date startTime, Date endTime, String source) {
                return R.fail("获取钱包账变记录失败:" + throwable.getMessage());
            }
        };
    }
}
