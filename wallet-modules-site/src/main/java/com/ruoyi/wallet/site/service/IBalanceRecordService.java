package com.ruoyi.wallet.site.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.wallet.api.dto.BalanceRecordVO;
import com.ruoyi.wallet.site.domain.BalanceRecord;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author ruoyi
 * @date 2024-05-09
 */
public interface IBalanceRecordService extends IService<BalanceRecord>
{
    boolean add(BalanceRecord balanceRecord);
    public List<BalanceRecordVO> getBalanceRecord(Long siteId, Long memberId, Integer businessId, Date startTime, Date endTime);
}
