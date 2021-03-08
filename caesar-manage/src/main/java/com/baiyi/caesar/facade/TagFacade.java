package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcBusinessTag;
import com.baiyi.caesar.domain.param.tag.TagParam;
import com.baiyi.caesar.domain.vo.tag.BusinessTagVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:17 下午
 * @Version 1.0
 */
public interface TagFacade {

    DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery);

    BusinessWrapper<Boolean> addTag(TagVO.Tag tag);

    BusinessWrapper<Boolean> updateTag(TagVO.Tag tag);

    BusinessWrapper<Boolean> deleteTagById(int id);

    List<TagVO.Tag> queryBusinessTag(TagParam.BusinessQuery businessQuery);

    List<TagVO.Tag> queryNotInBusinessTag(TagParam.BusinessQuery businessQuery);

    BusinessWrapper<Boolean> updateBusinessTag(BusinessTagVO.BusinessTag businessTag);

    List<OcBusinessTag> queryOcBusinessTagByBusinessTypeAndBusinessId(int businessType, int businessId);

    /**
     * 清除所有业务标签
     * @param businessType
     * @param businessId
     */
    void clearBusinessTags(int businessType, int businessId);

    void deleteTagByList(List<OcBusinessTag> ocBusinessTagList);

}
