package com.baiyi.caesar.facade.impl;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.ansible.bo.TaskResult;
import com.baiyi.caesar.ansible.config.AnsibleConfig;
import com.baiyi.caesar.ansible.factory.ExecutorFactory;
import com.baiyi.caesar.ansible.handler.AnsibleTaskHandler;
import com.baiyi.caesar.ansible.handler.TaskLogRecorder;
import com.baiyi.caesar.ansible.impl.AnsibleCommandExecutor;
import com.baiyi.caesar.ansible.impl.AnsiblePlaybookExecutor;
import com.baiyi.caesar.ansible.impl.AnsibleScriptExecutor;
import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.base.ServerTaskStopType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.common.util.UUIDUtil;
import com.baiyi.caesar.decorator.ansible.AnsiblePlaybookDecorator;
import com.baiyi.caesar.decorator.ansible.AnsibleScriptDecorator;
import com.baiyi.caesar.decorator.server.ServerTaskDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.caesar.domain.param.ansible.AnsibleScriptParam;
import com.baiyi.caesar.domain.param.ansible.ServerTaskHistoryParam;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.caesar.domain.vo.ansible.AnsibleVersionVO;
import com.baiyi.caesar.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.caesar.domain.vo.ansible.AnsibleScriptVO;
import com.baiyi.caesar.domain.vo.preview.PreviewFileVO;
import com.baiyi.caesar.domain.vo.server.ServerTaskVO;
import com.baiyi.caesar.facade.AttributeFacade;
import com.baiyi.caesar.facade.ServerTaskFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.service.ansible.OcAnsiblePlaybookService;
import com.baiyi.caesar.service.ansible.OcAnsibleScriptService;
import com.baiyi.caesar.service.server.OcServerTaskMemberService;
import com.baiyi.caesar.service.server.OcServerTaskService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
@Service
public class ServerTaskFacadeImpl implements ServerTaskFacade {

    @Resource
    private OcServerTaskService ocServerTaskService;

    @Resource
    private UserFacade userFacade;

    @Resource
    private OcServerTaskMemberService ocServerTaskMemberService;

    @Resource
    private ServerTaskDecorator serverTaskDecorator;

    @Resource
    private AttributeFacade attributeFacade;

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    @Resource
    private OcAnsibleScriptService ocAnsibleScriptService;

    @Resource
    private AnsiblePlaybookDecorator ansiblePlaybookDecorator;

    @Resource
    private AnsibleScriptDecorator ansibleScriptDecorator;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private AnsibleTaskHandler ansibleTaskHandler;

    @Resource
    private AnsibleConfig ansibleConfig;

    @Resource
    private TaskLogRecorder taskLogRecorder;

    @Override
    public DataTable<ServerTaskVO.ServerTask> queryTaskHistoryPage(@Valid ServerTaskHistoryParam.PageQuery pageQuery) {
        DataTable<OcServerTask> table = ocServerTaskService.queryOcServerTaskByParam(pageQuery);
        return new DataTable<>(table.getData().stream().map(e -> serverTaskDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<AnsiblePlaybookVO.AnsiblePlaybook> queryPlaybookPage(AnsiblePlaybookParam.PageQuery pageQuery) {
        DataTable<OcAnsiblePlaybook> table = ocAnsiblePlaybookService.queryOcAnsiblePlaybookByParam(pageQuery);
        return new DataTable<>(table.getData().stream().map(e -> ansiblePlaybookDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<AnsibleScriptVO.AnsibleScript> queryScriptPage(AnsibleScriptParam.PageQuery pageQuery) {
        DataTable<OcAnsibleScript> table = ocAnsibleScriptService.queryOcAnsibleScriptByParam(pageQuery);
        return new DataTable<>(table.getData().stream().map(e -> ansibleScriptDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addScript(AnsibleScriptVO.AnsibleScript ansibleScript) {
        OcAnsibleScript ocAnsibleScript = BeanCopierUtil.copyProperties(ansibleScript, OcAnsibleScript.class);
        ocAnsibleScript.setScriptUuid(UUIDUtil.getUUID());
        OcUser ocUser = userFacade.getOcUserBySession();
        ocUser.setPassword("");
        ocAnsibleScript.setUserId(ocUser.getId());
        ocAnsibleScript.setUserDetail(JSON.toJSONString(ocUser));
        ocAnsibleScriptService.addOcAnsibleScript(ocAnsibleScript);
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 鉴权
     *
     * @return
     */
    private BusinessWrapper<Boolean> checkAuth(OcAnsibleScript ocAnsibleScript) {
        // 无需鉴权
        if (ocAnsibleScript.getScriptLock() == 0)
            return BusinessWrapper.SUCCESS;
        OcUser ocUser = userFacade.getOcUserBySession();
        return userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel());
    }

    @Override
    public BusinessWrapper<Boolean> updateScript(AnsibleScriptVO.AnsibleScript ansibleScript) {
        OcAnsibleScript preAnsibleScript = ocAnsibleScriptService.queryOcAnsibleScriptById(ansibleScript.getId());
        BusinessWrapper<Boolean> wrapper = checkAuth(preAnsibleScript);
        if (!wrapper.isSuccess())
            return wrapper;
        preAnsibleScript.setName(ansibleScript.getName());
        preAnsibleScript.setScriptLang(ansibleScript.getScriptLang());
        preAnsibleScript.setComment(ansibleScript.getComment());
        preAnsibleScript.setScriptLock(ansibleScript.getScriptLock());
        preAnsibleScript.setScriptContent(ansibleScript.getScriptContent());
        ocAnsibleScriptService.updateOcAnsibleScript(preAnsibleScript);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteScriptById(int id) {
        OcAnsibleScript preAnsibleScript = ocAnsibleScriptService.queryOcAnsibleScriptById(id);
        BusinessWrapper<Boolean> wrapper = checkAuth(preAnsibleScript);
        if (!wrapper.isSuccess())
            return wrapper;
        ocAnsibleScriptService.deleteOcAnsibleScriptById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addPlaybook(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        OcAnsiblePlaybook ocAnsiblePlaybook = BeanCopierUtil.copyProperties(ansiblePlaybook, OcAnsiblePlaybook.class);
        ocAnsiblePlaybook.setPlaybookUuid(UUIDUtil.getUUID());
        OcUser ocUser = userFacade.getOcUserBySession();
        ocUser.setPassword("");
        ocAnsiblePlaybook.setUserId(ocUser.getId());
        ocAnsiblePlaybook.setUserDetail(JSON.toJSONString(ocUser));
        ocAnsiblePlaybookService.addOcAnsiblePlaybook(ocAnsiblePlaybook);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updatePlaybook(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(ansiblePlaybook.getId());

        ocAnsiblePlaybook.setComment(ansiblePlaybook.getComment());
        ocAnsiblePlaybook.setName(ansiblePlaybook.getName());
        ocAnsiblePlaybook.setPlaybook(ansiblePlaybook.getPlaybook());
        ocAnsiblePlaybook.setExtraVars(ansiblePlaybook.getExtraVars());
        ocAnsiblePlaybook.setTags(ansiblePlaybook.getTags());

        ocAnsiblePlaybookService.updateOcAnsiblePlaybook(ocAnsiblePlaybook);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deletePlaybookById(int id) {
        ocAnsiblePlaybookService.deleteOcAnsiblePlaybookById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<OcServerTask> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor) {
        return ExecutorFactory.getAnsibleExecutorByKey(AnsibleCommandExecutor.COMPONENT_NAME).executorByParam(serverTaskCommandExecutor);
    }

    @Override
    public BusinessWrapper<OcServerTask> executorScript(ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor) {
        return ExecutorFactory.getAnsibleExecutorByKey(AnsibleScriptExecutor.COMPONENT_NAME).executorByParam(serverTaskScriptExecutor);
    }

    @Override
    public BusinessWrapper<OcServerTask> executorPlaybook(ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor) {
        return ExecutorFactory.getAnsibleExecutorByKey(AnsiblePlaybookExecutor.COMPONENT_NAME).executorByParam(serverTaskPlaybookExecutor);
    }

    @Override
    public ServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId) {
        OcServerTask ocServerTask = ocServerTaskService.queryOcServerTaskById(taskId);
        ServerTaskVO.ServerTask serverTask = BeanCopierUtil.copyProperties(ocServerTask, ServerTaskVO.ServerTask.class);
        return serverTaskDecorator.decorator(serverTask);
    }

    @Override
    public BusinessWrapper<Boolean> createAnsibleHosts() {
        attributeFacade.createAnsibleHostsTask();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> abortServerTask(int taskId) {
        OcServerTask ocServerTask = ocServerTaskService.queryOcServerTaskById(taskId);
        if (ocServerTask == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_NOT_EXIST);
        if (ocServerTask.getFinalized() == 1)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_HAS_FINALIIZED_AND_CANNOT_BE_MODIFIED);
        ocServerTask.setFinalized(1);
        ocServerTask.setStopType(ServerTaskStopType.SERVER_TASK_STOP.getType());
        ocServerTaskService.updateOcServerTask(ocServerTask);
        taskLogRecorder.abortTask(taskId);
        ocServerTaskMemberService.queryOcServerTaskMemberByTaskId(taskId).forEach(e -> {
            taskLogRecorder.abortTaskMember(e.getId(), ServerTaskStopType.SERVER_TASK_STOP.getType());
        });
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> abortServerTaskMember(int memberId) {
        OcServerTaskMember ocServerTaskMember = ocServerTaskMemberService.queryOcServerTaskMemberById(memberId);
        if (ocServerTaskMember == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_MEMBER_NOT_EXIST);
        taskLogRecorder.abortTaskMember(memberId, ServerTaskStopType.MEMBER_TASK_STOP.getType());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<AnsibleVersionVO.AnsibleVersion> queryAnsibleVersion() {
        AnsibleVersionVO.AnsibleVersion version = new AnsibleVersionVO.AnsibleVersion();
        try {
            TaskResult ansibleVersion = ansibleTaskHandler.getAnsibleVersion();
            version.setVersion(ansibleVersion.getOutputStream().toString("utf8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        try {
            TaskResult playbookVersion = ansibleTaskHandler.getAnsiblePlaybookVersion();
            version.setPlaybookVersion(playbookVersion.getOutputStream().toString("utf8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        return new BusinessWrapper(version);
    }

    @Override
    public BusinessWrapper<PreviewFileVO> previewAnsibleHosts() {
        String path = Joiner.on("/").join(ansibleConfig.acqInventoryPath(), AnsibleConfig.ANSIBLE_HOSTS);
        String ansibleHosts = IOUtil.readFile(path);
        PreviewFileVO previewFile = PreviewFileVO.builder()
                .name(AnsibleConfig.ANSIBLE_HOSTS)
                .path(path)
                .content(ansibleHosts)
                .comment("Ansible主机配置文件")
                .build();
        return new BusinessWrapper(previewFile);
    }


}
