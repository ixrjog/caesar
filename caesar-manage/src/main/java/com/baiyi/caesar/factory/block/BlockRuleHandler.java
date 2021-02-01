package com.baiyi.caesar.factory.block;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsBlockPlatform;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.service.platform.CsBlockPlatformService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/1/27 4:25 下午
 * @Version 1.0
 */
@Component
public class BlockRuleHandler {

    @Resource
    private CsBlockPlatformService csBlockPlatformService;

    public BusinessWrapper<Boolean> verify(CsCiJob csCiJob) {
        for (CsBlockPlatform rule : queryBlockPlatformByValid()) {
            IBlockRule blockRule = BlockRuleFactory.getBlockRuleByLevel(rule.getBlockLevel());
            if (blockRule == null) continue;
            BusinessWrapper<Boolean> wrapper = blockRule.verify(csCiJob);
            if(!wrapper.isSuccess())
                return wrapper;
        }
        return BusinessWrapper.SUCCESS;
    }

    public BusinessWrapper<Boolean> verify(CsCdJob csCdJob) {
        for (CsBlockPlatform rule : queryBlockPlatformByValid()) {
            IBlockRule blockRule = BlockRuleFactory.getBlockRuleByLevel(rule.getBlockLevel());
            if (blockRule == null) continue;
            BusinessWrapper<Boolean> wrapper = blockRule.verify(csCdJob);
            if(!wrapper.isSuccess())
                return wrapper;
        }
        return BusinessWrapper.SUCCESS;
    }



    public List<CsBlockPlatform> queryBlockPlatformByValid() {
        return filter(csBlockPlatformService.queryCsBlockPlatformByValid());
    }

    /**
     * 有效的封网配置
     *
     * @param csBlockPlatforms
     * @return
     */
    private List<CsBlockPlatform> filter(List<CsBlockPlatform> csBlockPlatforms) {

        return csBlockPlatforms.stream().filter(e -> {
            Date nowDate = new Date();
            // 封网时间必须配置
            if (e.getStartTime() == null || e.getEndTime() == null)
                return false;
            return nowDate.after(e.getStartTime()) && nowDate.before(e.getEndTime());
        }).collect(Collectors.toList());
    }
}
