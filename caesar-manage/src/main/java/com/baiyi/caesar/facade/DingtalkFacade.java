package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.dingtalk.DingtalkParam;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:22 下午
 * @Version 1.0
 */
public interface DingtalkFacade {

    DataTable<DingtalkVO.Dingtalk> queryDingtalkPage(DingtalkParam.DingtalkPageQuery pageQuery);

    BusinessWrapper<Boolean> addDingtalk(DingtalkVO.Dingtalk dingtalk);

    BusinessWrapper<Boolean> updateDingtalk(DingtalkVO.Dingtalk dingtalk);

    BusinessWrapper<Boolean> deleteDingtalkById(int id);
}
