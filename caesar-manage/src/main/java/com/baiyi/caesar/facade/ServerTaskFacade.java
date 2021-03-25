package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.caesar.domain.param.ansible.AnsibleScriptParam;
import com.baiyi.caesar.domain.param.ansible.ServerTaskHistoryParam;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.caesar.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.caesar.domain.vo.ansible.AnsibleScriptVO;
import com.baiyi.caesar.domain.vo.ansible.AnsibleVersionVO;
import com.baiyi.caesar.domain.vo.preview.PreviewFileVO;
import com.baiyi.caesar.domain.vo.server.ServerTaskVO;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

    DataTable<ServerTaskVO.ServerTask> queryTaskHistoryPage(@Valid ServerTaskHistoryParam.PageQuery pageQuery);

    DataTable<AnsiblePlaybookVO.AnsiblePlaybook> queryPlaybookPage(AnsiblePlaybookParam.PageQuery pageQuery);

    DataTable<AnsibleScriptVO.AnsibleScript> queryScriptPage(AnsibleScriptParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addScript(AnsibleScriptVO.AnsibleScript ansibleScript);

    BusinessWrapper<Boolean> updateScript(AnsibleScriptVO.AnsibleScript ansibleScript);

    BusinessWrapper<Boolean> deleteScriptById(int id);

    BusinessWrapper<Boolean> addPlaybook(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook);

    BusinessWrapper<Boolean> updatePlaybook(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook);

    BusinessWrapper<Boolean> deletePlaybookById(int id);

    BusinessWrapper<OcServerTask> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor);

    BusinessWrapper<OcServerTask> executorScript(ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor);

    BusinessWrapper<OcServerTask> executorPlaybook(ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor);

    ServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId);

    BusinessWrapper<Boolean> abortServerTask(int taskId);

    BusinessWrapper<Boolean> abortServerTaskMember(int memberId);

    BusinessWrapper<AnsibleVersionVO.AnsibleVersion> queryAnsibleVersion();

    BusinessWrapper<PreviewFileVO> previewAnsibleHosts();
}
