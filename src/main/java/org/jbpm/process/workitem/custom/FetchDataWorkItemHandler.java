package org.jbpm.process.workitem.custom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

public class FetchDataWorkItemHandler implements WorkItemHandler,Serializable {
	
	Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 14232123234L;
	private KieSession ksession;
	public FetchDataWorkItemHandler() {
		
	}
	public FetchDataWorkItemHandler(KieSession ksession) {
		this.ksession = ksession;
	}
	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
	}

	/**
	 * use json to communicate with external server to exchange data
	 */
	public void executeWorkItem(WorkItem workitem, WorkItemManager workitemmanger) {
		try {
			log.info("FetchDataWorkItemHandler:"+workitem.getId()+" ProcessInstanceId:"+workitem.getProcessInstanceId());
			
			Map paramMap = new HashMap();
			Object url = workitem.getParameter("url");//fixed in setup
            Object param = workitem.getParameter("param");//fixed in setup
            Object keys = workitem.getParameter("keys");//use to get dynamic value for per process instance
            log.info("url: "+url);
            log.info("param"+param);
            
			WorkflowProcessInstance instance = (WorkflowProcessInstance) ksession.getProcessInstance(workitem.getProcessInstanceId());
			
			if(url == null || StringUtils.isBlank(url.toString())){
			    log.info("url is null ");   
			}
            if (param == null || StringUtils.isBlank(param.toString())) {
                log.info("param is null ");
            } else {
                paramMap = JSON.parseObject(param.toString(), Map.class);
            }
            
            if (keys == null || StringUtils.isBlank(keys.toString())) {
                log.info("keys is null ");
            } else {
                String keyarray[] = keys.toString().split(",");
                for (int i = 0; i < keyarray.length; i++) {
                    String key = keyarray[i];
                    Object value= instance.getVariable(key);
                    if(value == null){
                        log.info("getVariable null, key:"+key);
                    }else{
                        paramMap.put(key, value);
                    }
                }
            }
			
            String result = post(url.toString().trim(), JSON.toJSONString(paramMap), "application/json");
            Map resultMap = JSON.parseObject(result, Map.class);
            for (Object key : resultMap.keySet()) {
                instance.setVariable(key.toString(),resultMap.get(key));
                log.info("setVariable key:"+key.toString()+",value:"+resultMap.get(key));
            }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("unexpected error,",e);
		}finally{
			workitemmanger.completeWorkItem(workitem.getId(), null);
		}
	}
	

	public String post(String url, String param, String contentType) {
	    
	    String result = null;
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
			result =  readResponse(in);
			// }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("unexpected error",e);
			result="{\"error\":\""+e.getMessage()+"\"}";
		}
		log.info("url:"+url+",param:"+param+",contentType:"+contentType+",result:"+result);
		return result;

	}
	   
	private static String readResponse(InputStream in) throws Exception {
		StringBuffer sbf = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			sbf.append(line);
		}
		return sbf.toString();
	}
}
