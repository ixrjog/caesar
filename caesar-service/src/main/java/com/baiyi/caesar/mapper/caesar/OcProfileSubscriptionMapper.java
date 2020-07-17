package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcProfileSubscription;
import com.baiyi.caesar.domain.param.profile.ProfileSubscriptionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcProfileSubscriptionMapper extends Mapper<OcProfileSubscription> {

    List<OcProfileSubscription> queryOcProfileSubscriptionParam(ProfileSubscriptionParam.PageQuery pageQuery);
}