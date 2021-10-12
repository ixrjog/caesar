package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.aliyun.CrParam;
import com.baiyi.caesar.domain.vo.aliyun.AliyunCRVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 4:17 下午
 * @Since 1.0
 */
public interface AliyunCRFacade {

    DataTable<AliyunCRVO.Instance> queryCRInstancePage(CrParam.InstancePageQuery pageQuery);

    void syncCRInstance();

    BusinessWrapper<Boolean> deleteInstanceById(int id);

    BusinessWrapper<Boolean> setInstanceActiveById(int id);

}
