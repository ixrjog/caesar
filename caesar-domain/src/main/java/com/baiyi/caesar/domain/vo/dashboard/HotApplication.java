package com.baiyi.caesar.domain.vo.dashboard;

import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/11 11:28 上午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class HotApplication implements Serializable {

    private static final long serialVersionUID = -3514380339186133629L;

    @ApiModelProperty(value = "应用标签")
    private List<TagVO.Tag> tags;

    @ApiModelProperty(value = "应用id")
    private int id;

    @ApiModelProperty(value = "应用名称")
    private String name;

    private String comment;

    @ApiModelProperty(value = "构建总数")
    private int count;
}
