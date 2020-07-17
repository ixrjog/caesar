package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.bo.SSHKeyCredential;
import com.baiyi.caesar.domain.param.keybox.KeyboxParam;
import com.baiyi.caesar.domain.vo.keybox.KeyboxVO;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:17 下午
 * @Version 1.0
 */
public interface KeyboxFacade {

    SSHKeyCredential getSSHKeyCredential(String systemUser);

    DataTable<KeyboxVO.Keybox> queryKeyboxPage(KeyboxParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addKeybox(KeyboxVO.Keybox keybox);

    BusinessWrapper<Boolean> deleteKeyboxById(int id);
}
