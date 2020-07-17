package com.baiyi.caesar.service.profile.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcProfileSubscription;
import com.baiyi.caesar.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.caesar.mapper.caesar.OcProfileSubscriptionMapper;
import com.baiyi.caesar.service.profile.OcProfileSubscriptionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:06 上午
 * @Version 1.0
 */
@Service
public class OcProfileSubscriptionServiceImpl implements OcProfileSubscriptionService {

    @Resource
    private OcProfileSubscriptionMapper ocProfileSubscriptionMapper;

    @Override
    public DataTable<OcProfileSubscription> queryOcProfileSubscriptionParam(ProfileSubscriptionParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcProfileSubscription> list = ocProfileSubscriptionMapper.queryOcProfileSubscriptionParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcProfileSubscription(OcProfileSubscription ocProfileSubscription) {
        ocProfileSubscriptionMapper.insert(ocProfileSubscription);
    }

    @Override
    public void updateOcProfileSubscription(OcProfileSubscription ocProfileSubscription) {
        ocProfileSubscriptionMapper.updateByPrimaryKey(ocProfileSubscription);
    }

    @Override
    public void deleteOcProfileSubscriptionById(int id) {
        ocProfileSubscriptionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcProfileSubscription queryOcProfileSubscriptionById(int id) {
        return ocProfileSubscriptionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcProfileSubscription> queryOcProfileSubscriptionBySubscriptionType(String subscriptionType){
        Example example = new Example(OcProfileSubscription.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("subscriptionType", subscriptionType);
        return ocProfileSubscriptionMapper.selectByExample(example);
    }

}
