package com.baiyi.caesar.bo.kubernetes;

import com.baiyi.caesar.domain.vo.kubernetes.KubernetesPodVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/4 4:18 下午
 * @Version 1.0
 */
@Data
@Builder
public class KubernetesPodBO {

    private String instanceName;
    private String name;
    private List<KubernetesPodVO.Container> containers;
    private String hostIP;
    private String podIP;
    private String phase;
}
