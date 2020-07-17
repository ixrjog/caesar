package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKeybox;
import com.baiyi.caesar.domain.param.keybox.KeyboxParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKeyboxMapper extends Mapper<OcKeybox> {

   List<OcKeybox> queryOcKeyboxByParam(KeyboxParam.PageQuery pageQuery);
}