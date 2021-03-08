package com.baiyi.caesar.decorator.ansible;

import com.baiyi.caesar.ansible.config.AnsibleConfig;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.PlaybookUtil;
import com.baiyi.caesar.domain.generator.caesar.OcAnsiblePlaybook;
import com.baiyi.caesar.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.caesar.domain.vo.ansible.playbook.PlaybookTags;
import com.baiyi.caesar.domain.vo.ansible.playbook.PlaybookTask;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/4/14 6:26 下午
 * @Version 1.0
 */
@Component
public class AnsiblePlaybookDecorator {

    @Resource
    private AnsibleConfig ansibleConfig;

    public AnsiblePlaybookVO.AnsiblePlaybook decorator(OcAnsiblePlaybook ocAnsiblePlaybook) {
        AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook = BeanCopierUtil.copyProperties(ocAnsiblePlaybook, AnsiblePlaybookVO.AnsiblePlaybook.class);
        try {
            PlaybookTags tags = PlaybookUtil.buildTags(ansiblePlaybook.getTags());
            assembleTags(ansiblePlaybook, tags);
            ansiblePlaybook.setTasks(tags.getTasks());
            ansiblePlaybook.setFormatError(false);
        } catch (Exception e) {
            ansiblePlaybook.setFormatError(true);
        }
        ansiblePlaybook.setPath(ansibleConfig.getPlaybookPath(ocAnsiblePlaybook));
        return ansiblePlaybook;
    }

    private void assembleTags(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook, PlaybookTags tags) {
        Set<String> selectedTags = Sets.newHashSet();
        List<PlaybookTask> tasks = tags.getTasks();
        if (tasks == null || tasks.isEmpty()) {
            ansiblePlaybook.setTasks(Lists.newArrayList());
            ansiblePlaybook.setSelectedTags(selectedTags);
            return;
        }
        assembleSelectedTags(selectedTags, tags);
        ansiblePlaybook.setTasks(tags.getTasks());
        ansiblePlaybook.setSelectedTags(selectedTags);
    }

    private void assembleSelectedTags(Set<String> selectedTags, PlaybookTags tags) {
        tags.getTasks().parallelStream().forEach(e -> {
            if (e.getChoose() == null)
                e.setChoose(true);
            if (e.getChoose())
                selectedTags.add(e.getTags());

        });
    }

}
