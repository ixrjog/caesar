package com.baiyi.caesar.service.keybox;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcKeybox;
import com.baiyi.caesar.domain.param.keybox.KeyboxParam;

/**
 * @Author baiyi
 * @Date 2020/5/3 10:30 上午
 * @Version 1.0
 */
public interface OcKeyboxService {

    OcKeybox queryOcKeyboxBySystemUser(String systemUser);

    OcKeybox queryOcKeyboxById(int id);

    void updateOcKeybox(OcKeybox ocKeybox);

    void addOcKeybox(OcKeybox ocKeybox);

    void deleteOcKeyboxById(int id);

    DataTable<OcKeybox> queryOcKeyboxByParam(KeyboxParam.PageQuery pageQuery);
}
