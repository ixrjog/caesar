package com.baiyi.caesar.service.tag;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcTag;
import com.baiyi.caesar.domain.param.tag.TagParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:04 下午
 * @Version 1.0
 */
public interface OcTagService {

    OcTag queryOcTagById(Integer id);

    OcTag queryOcTagByKey(String tagKey);

    DataTable<OcTag> queryOcTagByParam(TagParam.PageQuery pageQuery);

    void addOcTag(OcTag ocTag);

    void updateOcTag(OcTag ocTag);

    void deleteOcTagById(int id);

    List<OcTag> queryOcTagByParam(TagParam.BusinessQuery businessQuery);

    List<OcTag> queryOcTagNotInByParam(TagParam.BusinessQuery businessQuery);

}
