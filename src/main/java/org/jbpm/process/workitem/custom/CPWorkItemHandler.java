package org.jbpm.process.workitem.custom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class CPWorkItemHandler implements WorkItemHandler,Serializable {
	
	Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 14232123234L;
	private KieSession ksession;
	public CPWorkItemHandler() {
		
	}
	public CPWorkItemHandler(KieSession ksession) {
		this.ksession = ksession;
	}
	
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
	}

	
	public void executeWorkItem(WorkItem workitem, WorkItemManager workitemmanger) {
		try {
			log.info("CPWorkItemHandler:"+workitem.getId()+" ProcessInstanceId:"+workitem.getProcessInstanceId());
			
			WorkflowProcessInstance instance = (WorkflowProcessInstance) ksession.getProcessInstance(workitem.getProcessInstanceId());
			String variableJson=(String) instance.getVariable("variableMap");
			log.info("variableJSON: "+variableJson);
			
			Object local = workitem.getParameter("local");
			Object cpoe = workitem.getParameter("cpoe");
			Object day = workitem.getParameter("day");
			Object subprocessId= workitem.getParameter("subprocessId");
			log.info("local: "+local);
			log.info("cpoe: "+cpoe);
			log.info("day: "+day);
			log.info("subprocessId: "+subprocessId);
			
			Map paramMap=JSON.parseObject(variableJson,Map.class);
			
			if(paramMap==null){
				log.info("paramMap is null ");	
			}
			Object oidO=paramMap.get("oid");
			String oid="";
			if (oidO == null) {
				log.info("oid is null.");
			} else {
				oid = oidO.toString();
			}
			if(subprocessId!=null&&!"".equals(subprocessId)){
				Map pm=new HashMap();
				pm.put("oid", oid);
				ksession.startProcess(subprocessId.toString(),pm);
			}
			
			Map g_ctx = (Map) ksession.getGlobal("g_ctx");
			
//			g_ctx.put(instance.getId()+"_currentWorkItemId", workitem.getId()); can't get it
			
			if (cpoe != null && !"".equals(cpoe) && !"-1".equals(oid)) {
				// post(cpoe.toString(), getres, "application/json");
			    String getres = post(cpoe.toString().trim(), JSON.toJSONString(paramMap),"application/json");
			    log.info("resp:"+getres);
			}
			
			g_ctx.remove("checkList-"+oid);
			log.info("checklist map removed.");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("unexpected error,",e);
		}
//		finally{
//			workitemmanger.completeWorkItem(workitem.getId(), null);
//		}
		log.info("workitem exec success !!!150316");
	}
	

	public String post(String url, String param, String contentType) {
		try {
			// (1) 创建Httpost实例
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", contentType);
			httpPost.setHeader("Accept", contentType);
			// List<NameValuePair> params = new ArrayList<NameValuePair>();

			// for (Entry<String, String> b : paramMap.entrySet()) {
			// params.add(new BasicNameValuePair(b.getKey(), b.getValue()));
			// }

			// 设置HTTP POST请求参数
			// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			httpPost.setEntity(new StringEntity(param, "UTF-8"));
			// (2) 使用HttpClient发送post请求，获得返回结果HttpResponse
			HttpClient http = new DefaultHttpClient();
			HttpResponse response = http.execute(httpPost);
			// (3) 读取返回结果
			// if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();
			return readResponse(in);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String readResponse(InputStream in) throws Exception {
		StringBuffer sbf = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			sbf.append(line);
		}
		return sbf.toString();
	}
}
