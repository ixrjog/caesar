package com.baiyi.caesar.service.platform.impl;

import com.baiyi.caesar.domain.generator.caesar.CsBlockPlatform;
import com.baiyi.caesar.mapper.caesar.CsBlockPlatformMapper;
import com.baiyi.caesar.service.platform.CsBlockPlatformService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/28 9:59 上午
 * @Version 1.0
 */
@Service
public class CsBlockPlatformServiceImpl implements CsBlockPlatformService {

    @Resource
    private CsBlockPlatformMapper csBlockPlatformMapper;

    @Override
    public List<CsBlockPlatform> queryCsBlockPlatformByValid() {
        Example example = new Example(CsBlockPlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        return csBlockPlatformMapper.selectByExample(example);
    }
}
