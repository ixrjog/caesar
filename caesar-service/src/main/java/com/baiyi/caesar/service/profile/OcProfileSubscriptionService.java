package com.baiyi.caesar.service.profile;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcProfileSubscription;
import com.baiyi.caesar.domain.param.profile.ProfileSubscriptionParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:05 上午
 * @Version 1.0
 */
public interface OcProfileSubscriptionService {

    DataTable<OcProfileSubscription> queryOcProfileSubscriptionParam(ProfileSubscriptionParam.PageQuery pageQuery);

    void addOcProfileSubscription(OcProfileSubscription ocProfileSubscription);

    void updateOcProfileSubscription(OcProfileSubscription ocProfileSubscription);

    void deleteOcProfileSubscriptionById(int id);

    OcProfileSubscription queryOcProfileSubscriptionById(int id);

    List<OcProfileSubscription> queryOcProfileSubscriptionBySubscriptionType(String subscriptionType);
}
