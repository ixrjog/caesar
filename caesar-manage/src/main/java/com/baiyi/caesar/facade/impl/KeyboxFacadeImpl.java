package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.SSHUtil;
import com.baiyi.caesar.decorator.keybox.KeyboxDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.bo.SSHKeyCredential;
import com.baiyi.caesar.domain.generator.caesar.OcKeybox;
import com.baiyi.caesar.domain.param.keybox.KeyboxParam;
import com.baiyi.caesar.domain.vo.keybox.KeyboxVO;
import com.baiyi.caesar.facade.KeyboxFacade;
import com.baiyi.caesar.service.keybox.OcKeyboxService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:17 下午
 * @Version 1.0
 */
@Service
public class KeyboxFacadeImpl implements KeyboxFacade {

    @Resource
    private OcKeyboxService ocKeyboxService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private KeyboxDecorator keyboxDecorator;

    @Override
    public SSHKeyCredential getSSHKeyCredential(String systemUser) {
        OcKeybox ocKeybox = ocKeyboxService.queryOcKeyboxBySystemUser(systemUser);
        if (ocKeybox == null)
            return null;
        return SSHKeyCredential.builder()
                .systemUser(systemUser)
                .privateKey(stringEncryptor.decrypt(ocKeybox.getPrivateKey()))
                .publicKey(ocKeybox.getPublicKey())
                .passphrase(StringUtils.isEmpty(ocKeybox.getPassphrase()) ? "" : stringEncryptor.decrypt(ocKeybox.getPassphrase()))
                .build();
    }

    @Override
    public DataTable<KeyboxVO.Keybox> queryKeyboxPage(KeyboxParam.PageQuery pageQuery) {
        DataTable<OcKeybox> table = ocKeyboxService.queryOcKeyboxByParam(pageQuery);
        List<KeyboxVO.Keybox> page = BeanCopierUtil.copyListProperties(table.getData(), KeyboxVO.Keybox.class);
        return new DataTable<>(page.stream().map(e -> keyboxDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addKeybox(KeyboxVO.Keybox keybox) {
        OcKeybox ocKeybox = BeanCopierUtil.copyProperties(keybox, OcKeybox.class);
        if (ocKeybox.getKeyType() == 0) { // sshKey
            if (StringUtils.isEmpty(ocKeybox.getPublicKey()))
                return new BusinessWrapper<>(ErrorEnum.KEYBOX_PUBLIC_KEY_IS_EMPTY);
            if (StringUtils.isEmpty(ocKeybox.getPrivateKey()))
                return new BusinessWrapper<>(ErrorEnum.KEYBOX_PRIVATE_KEY_IS_EMPTY);
            ocKeybox.setPrivateKey(stringEncryptor.encrypt(keybox.getPrivateKey()));
            if (!StringUtils.isEmpty(ocKeybox.getPassphrase()))
                ocKeybox.setPassphrase(stringEncryptor.encrypt(keybox.getPassphrase()));
            ocKeybox.setFingerprint(SSHUtil.getFingerprint(ocKeybox.getPublicKey()));
        } else { // user/password
            if (StringUtils.isEmpty(ocKeybox.getPassphrase()))
                return new BusinessWrapper<>(ErrorEnum.KEYBOX_PASSPHRASE_IS_EMPTY);
            ocKeybox.setPassphrase(stringEncryptor.encrypt(keybox.getPassphrase()));
        }
        ocKeyboxService.addOcKeybox(ocKeybox);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKeyboxById(int id) {
        ocKeyboxService.deleteOcKeyboxById(id);
        return BusinessWrapper.SUCCESS;
    }

}
