package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcBusinessTag;
import com.baiyi.caesar.domain.vo.tag.BusinessTagVO;
import tk.mybatis.mapper.common.Mapper;

public interface OcBusinessTagMapper extends Mapper<OcBusinessTag> {



    int deleteOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag);
}