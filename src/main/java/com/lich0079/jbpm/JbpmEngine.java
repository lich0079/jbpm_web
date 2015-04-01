package com.lich0079.jbpm;

import java.util.List;
import java.util.Map;

import org.jbpm.process.workitem.custom.CPWorkItemHandler;
import org.jbpm.process.workitem.custom.FetchDataWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lich0079.util.BaseLogAble;


@Component
@DependsOn(value = {"runtimeManager"})
public class JbpmEngine extends BaseLogAble{
    
    @Autowired
    private RuntimeManager runtimeManager;
    
    private volatile KieSession ksession;
    
    private TaskService taskService;
    
    private RuntimeEngine engine;
    
    public ProcessInstance startProcess(String processId, Map<String, Object> parameter) {
        ProcessInstance processInstance = getSession().startProcess(processId, parameter);
        return processInstance;
    }
    
    public void completeWorkItem(long workitemId, Map<String, Object> parameter) {
        getSession().getWorkItemManager().completeWorkItem(workitemId, parameter);
    }
    
    public void completeTask(String userId, Map<String, Object> parameter){
        List<TaskSummary> tasks = getTaskService().getTasksAssignedAsPotentialOwner(userId, "en-UK");
        log("tasks size:"+tasks.size());
        if(tasks.size() > 0){
            TaskSummary task = tasks.get(0);
            taskService.start(task.getId(), userId);
            log(userId+" start task " + task.getName() + ": " + task.getDescription());
            taskService.complete(task.getId(), userId, parameter);
            log(userId+" complete task " + task.getName() + ": " + task.getDescription());
        }else{
            log("no task for "+userId);
        }
    }
    
    
    private KieSession getSession(){
        if(ksession == null){
            synchronized(this){
                if(ksession == null){
                    ksession = getEngine().getKieSession();
                    ksession.getWorkItemManager().registerWorkItemHandler("cp", new CPWorkItemHandler(ksession));
                    ksession.getWorkItemManager().registerWorkItemHandler("fd", new FetchDataWorkItemHandler(ksession));
                }
            }
        }
        return ksession;
    }
    
    
    public TaskService getTaskService() {
        if(taskService == null){
            synchronized(this){
                if(taskService == null){
                    taskService = getEngine().getTaskService();
                }
            }
        }
        return taskService;
    }

    private RuntimeEngine getEngine(){
        if(engine == null){
            synchronized(this){
                if(engine == null){
                    engine = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get());
                }
            }
        }
        return engine;
    }
}
