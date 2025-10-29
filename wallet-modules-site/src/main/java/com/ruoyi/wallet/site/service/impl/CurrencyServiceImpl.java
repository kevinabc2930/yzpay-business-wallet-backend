package com.ruoyi.wallet.site.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wallet.site.domain.Currency;
import com.ruoyi.wallet.site.mapper.CurrencyMapper;
import com.ruoyi.wallet.site.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 站点用户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements ICurrencyService {
    @Autowired
    private CurrencyMapper currencyMapper;

}
