package com.ruoyi.wallet.site.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wallet.api.dto.BalanceRecordVO;
import com.ruoyi.wallet.site.domain.BalanceRecord;
import com.ruoyi.wallet.site.mapper.BalanceRecordMapper;
import com.ruoyi.wallet.site.service.IBalanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 钱包流水业务处理层
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
public class BalanceRecordServiceImpl extends ServiceImpl<BalanceRecordMapper, BalanceRecord> implements IBalanceRecordService {
    @Autowired
    private BalanceRecordMapper balanceRecordMapper;

    @Override
    public boolean add(BalanceRecord balanceRecord) {
        return balanceRecordMapper.insert(balanceRecord) > 0;
    }

    @Override
    public List<BalanceRecordVO> getBalanceRecord(Long siteId, Long memberId, Integer businessId, Date startTime, Date endTime) {
        List<BalanceRecord> list = baseMapper.selectList(new LambdaQueryWrapper<BalanceRecord>()
                .eq(BalanceRecord::getSiteId,siteId).eq(BalanceRecord::getMemberId, memberId).eq(BalanceRecord::getBusinessId, businessId).gt(BalanceRecord::getUpdateTime, startTime).lt(BalanceRecord::getUpdateTime, endTime));
        List<BalanceRecordVO> voList = BeanUtil.copyToList(list,BalanceRecordVO.class);
        return voList;
    }
}
