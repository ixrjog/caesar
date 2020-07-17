package com.baiyi.caesar.service.cloud.impl;

import com.baiyi.caesar.domain.generator.caesar.OcCloudDbAccount;
import com.baiyi.caesar.mapper.caesar.OcCloudDbAccountMapper;
import com.baiyi.caesar.service.cloud.OcCloudDBAccountService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/3 10:16 上午
 * @Version 1.0
 */
@Service
public class OcCloudDBAccountServiceImpl implements OcCloudDBAccountService {

    @Resource
    private OcCloudDbAccountMapper ocCloudDbAccountMapper;

    @Override
    public List<OcCloudDbAccount> queryOcCloudDbAccountByCloudDbId(int cloudDbId) {
        Example example = new Example(OcCloudDbAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbId", cloudDbId);
        return ocCloudDbAccountMapper.selectByExample(example);
    }

    @Override
    public OcCloudDbAccount queryOcCloudDbAccountByUniqueKey(OcCloudDbAccount ocCloudDbAccount) {
        Example example = new Example(OcCloudDbAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbId", ocCloudDbAccount.getCloudDbId());
        criteria.andEqualTo("accountName", ocCloudDbAccount.getAccountName());
        criteria.andEqualTo("accountPrivilege", ocCloudDbAccount.getAccountPrivilege());
        return ocCloudDbAccountMapper.selectOneByExample(example);
    }

    @Override
    public void addOcCloudDbAccount(OcCloudDbAccount ocCloudDbAccount) {
        ocCloudDbAccountMapper.insert(ocCloudDbAccount);
    }

    @Override
    public void delOcCloudDbAccountById(int id) {
        ocCloudDbAccountMapper.deleteByPrimaryKey(id);
    }
}
