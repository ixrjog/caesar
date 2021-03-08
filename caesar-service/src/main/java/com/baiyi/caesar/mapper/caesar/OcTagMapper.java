package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcTag;
import com.baiyi.caesar.domain.param.tag.TagParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcTagMapper extends Mapper<OcTag> {

    List<OcTag> queryOcTagByParam(TagParam.TagPageQuery pageQuery);

    /**
     * 查询业务相关标签
     * @param businessQuery
     * @return
     */
    List<OcTag> queryOcTagByBusinessParam(TagParam.BusinessQuery businessQuery);

    /**
     * 查询业务不相关标签
     * @param businessQuery
     * @return
     */
    List<OcTag> queryOcTagNotInByBusinessParam(TagParam.BusinessQuery businessQuery);

}