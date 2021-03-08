package com.baiyi.caesar.facade.impl;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.dingtalk.DingtalkDecorator;
import com.baiyi.caesar.dingtalk.config.DingtalkConfig;
import com.baiyi.caesar.dingtalk.content.DingtalkContent;
import com.baiyi.caesar.dingtalk.handler.DingtalkHandler;
import com.baiyi.caesar.dingtalk.model.TestMessage;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsDingtalk;
import com.baiyi.caesar.domain.param.dingtalk.DingtalkParam;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.facade.DingtalkFacade;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:22 下午
 * @Version 1.0
 */
@Service
public class DingtalkFacadeImpl implements DingtalkFacade {

    @Resource
    private CsDingtalkService csDingtalkService;

    @Resource
    private DingtalkDecorator dingtalkDecorator;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private DingtalkHandler dingtalkHandler;

    @Resource
    private DingtalkConfig dingtalkConfig;

    @Override
    public DataTable<DingtalkVO.Dingtalk> queryDingtalkPage(DingtalkParam.DingtalkPageQuery pageQuery) {
        DataTable<CsDingtalk> table = csDingtalkService.queryCsDingtalkByParam(pageQuery);
        List<DingtalkVO.Dingtalk> page = BeanCopierUtil.copyListProperties(table.getData(), DingtalkVO.Dingtalk.class);
        return new DataTable<>(page.stream().map(e -> dingtalkDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addDingtalk(DingtalkVO.Dingtalk dingtalk) {
        CsDingtalk csDingtalk = BeanCopierUtil.copyProperties(dingtalk, CsDingtalk.class);
        csDingtalk.setDingtalkToken(stringEncryptor.encrypt(dingtalk.getDingtalkToken()));
        csDingtalkService.addCsDingtalk(csDingtalk);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public String acqDingtalkTokenById(int id) {
        CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(id);
        return stringEncryptor.decrypt(csDingtalk.getDingtalkToken());
    }

    @Override
    public BusinessWrapper<Boolean> updateDingtalk(DingtalkVO.Dingtalk dingtalk) {
        CsDingtalk pre = BeanCopierUtil.copyProperties(dingtalk, CsDingtalk.class);
        if (StringUtils.isEmpty(pre.getDingtalkToken())) {
            CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(dingtalk.getId());
            pre.setDingtalkToken(csDingtalk.getDingtalkToken());
        } else {
            pre.setDingtalkToken(stringEncryptor.encrypt(dingtalk.getDingtalkToken()));
        }
        csDingtalkService.updateCsDingtalk(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteDingtalkById(int id) {
        csDingtalkService.deleteCsDingtalkById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> testDingtalkById(int id) {
        DingtalkContent content = DingtalkContent.builder()
                .msg(JSON.toJSONString(TestMessage.builder().build()))
                .webHook(dingtalkConfig.getWebHook(acqDingtalkTokenById(id)))
                .build();
        try {
            dingtalkHandler.doNotify(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BusinessWrapper.SUCCESS;
    }
}
