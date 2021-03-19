package com.baiyi.caesar.decorator.tag;

import com.baiyi.caesar.domain.param.tag.TagParam;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import com.baiyi.caesar.facade.TagFacade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/21 11:02 上午
 * @Version 1.0
 */
@Component
public class TagDecorator {

    @Resource
    private TagFacade tagFacade;

    public List<TagVO.Tag> acqTags(Integer businessType, Integer businessId) {
        // 装饰 标签
        TagParam.BusinessQuery businessQuery = TagParam.BusinessQuery.builder()
                .businessType(businessType)
                .businessId(businessId)
                .build();
        return tagFacade.queryBusinessTag(businessQuery);
    }

    public void decorator(TagVO.ITags iTags) {
        iTags.setTags(acqTags(iTags.getBusinessType(), iTags.getBusinessId()));
    }

}
