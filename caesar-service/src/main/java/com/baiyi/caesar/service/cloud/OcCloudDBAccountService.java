package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.generator.caesar.OcCloudDbAccount;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/3 10:15 上午
 * @Version 1.0
 */
public interface OcCloudDBAccountService {

    List<OcCloudDbAccount> queryOcCloudDbAccountByCloudDbId(int cloudDbId);

    OcCloudDbAccount queryOcCloudDbAccountByUniqueKey(OcCloudDbAccount ocCloudDbAccount);

    void addOcCloudDbAccount(OcCloudDbAccount ocCloudDbAccount);

    void delOcCloudDbAccountById(int id);
}
