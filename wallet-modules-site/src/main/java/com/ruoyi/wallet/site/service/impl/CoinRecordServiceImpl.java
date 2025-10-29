package com.ruoyi.wallet.site.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wallet.site.domain.CoinRecord;
import com.ruoyi.wallet.site.mapper.CoinRecordMapper;
import com.ruoyi.wallet.site.service.ICoinRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 站点用户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
public class CoinRecordServiceImpl extends ServiceImpl<CoinRecordMapper, CoinRecord> implements ICoinRecordService {
    @Autowired
    private CoinRecordMapper coinRecordMapper;

}
