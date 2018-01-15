package com.yf.rule.utils;

import com.bstek.urule.Utils;
import com.bstek.urule.runtime.BatchSession;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.service.KnowledgeService;
import com.yf.rule.core.MergeType;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleEngineUtil {

	public static KnowledgeSession getSession(String packageId) throws Exception{
		KnowledgeService kservice = (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage kpackage = kservice.getKnowledge(packageId);
        KnowledgeSession ksession = KnowledgeSessionFactory.newKnowledgeSession(kpackage);
        return ksession;
	}
	public static KnowledgeSession getSession(String[] packageIds) throws Exception{
		KnowledgeService kservice = (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
		KnowledgePackage[] kpackages = kservice.getKnowledges(packageIds);
		KnowledgeSession ksession = KnowledgeSessionFactory.newKnowledgeSession(kpackages);
		return ksession;
	}
	public static BatchSession getBatchSession(String packageId) throws Exception{
        BatchSession ksession = getBatchSession(packageId, BatchSession.DEFAULT_THREAD_SIZE, BatchSession.DEFAULT_BATCH_SIZE);
        return ksession;
	}
	public static BatchSession getBatchSession(String packageId, int threadSize, int batchSize) throws Exception{
		if(threadSize <= 0) threadSize = BatchSession.DEFAULT_THREAD_SIZE;
		if(batchSize <= 0) threadSize = BatchSession.DEFAULT_BATCH_SIZE;
		KnowledgeService kservice= (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
		KnowledgePackage kpackage = kservice.getKnowledge(packageId);
		BatchSession ksession = KnowledgeSessionFactory.newBatchSession(kpackage, threadSize, batchSize);
		return ksession;
	}
	public static BatchSession getBatchSession(String[] packageId) throws Exception{
		BatchSession session = getBatchSession(packageId, BatchSession.DEFAULT_THREAD_SIZE, BatchSession.DEFAULT_BATCH_SIZE);
		return session;
	}
	public static BatchSession getBatchSession(String[] packageIds, int threadSize, int batchSize) throws Exception{
		if(threadSize <= 0) threadSize = BatchSession.DEFAULT_THREAD_SIZE;
		if(batchSize <= 0) threadSize = BatchSession.DEFAULT_BATCH_SIZE;
		KnowledgeService kservice = (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
		KnowledgePackage[] kpackages = kservice.getKnowledges(packageIds);
		BatchSession ksession = KnowledgeSessionFactory.newBatchSession(kpackages, threadSize, batchSize);
		return ksession;
	}

	public static KnowledgeSession getSessionByPackageName(String packageName) throws Exception {
		String[] packageIds = packageName.split(",");
		KnowledgeSession session;
		if(packageIds.length == 1){
			session = RuleEngineUtil.getSession(packageIds[0]);
		}else{
			session = RuleEngineUtil.getSession(packageIds);
		}
		return session;
	}
	public static BatchSession getBatchSessionByPackageName(String packageName, Map<String,Object> optionsMap) throws Exception {
		String[] packageIds = packageName.split(",");
		int threadSize = (int)ObjectUtil.tryGet("threadSize", optionsMap, 0);
		int batchSize = (int)ObjectUtil.tryGet("batchSize", optionsMap, 0);
		BatchSession session;
		if(packageIds.length == 1){
			session = RuleEngineUtil.getBatchSession(packageIds[0], threadSize, batchSize);
		}else{
			session = RuleEngineUtil.getBatchSession(packageIds, threadSize, batchSize);
		}
		return session;
	}

	public static Map<String, Object> transformRuleExecutionResponseToData(KnowledgeSession session, RuleExecutionResponse response, Map<String, Object> paramsMap, Map<String, Object> optionsMap){
		Map<String, Object> data = RuleEngineUtil.mergeMap(paramsMap, session.getParameters(), MergeType.EXIST);
		Map<String, Object> exts = new HashMap<>();

		List<String> firedRuleNames =  response.getFiredRules().stream().map(ruleInfo -> ruleInfo.getName()).collect(Collectors.toList());
		List<String> matchedRuleNames =  response.getMatchedRules().stream().map(ruleInfo -> ruleInfo.getName()).collect(Collectors.toList());

		exts.put("firedRuleNames", firedRuleNames);
		exts.put("matchedRuleNames", matchedRuleNames);
		exts.put("duration", response.getDuration());

		Object detailObj = optionsMap.get("detail");
		if (detailObj != null && Boolean.parseBoolean(detailObj.toString())) {
			data.put("detail", response);
		}
		data.put("ext", exts);

		return data;
	}
	public static Map<String, Object> transformFlowExecutionResponseToData(KnowledgeSession session, FlowExecutionResponse response, Map<String, Object> paramsMap, Map<String, Object> optionsMap){
		Map<String, Object> data = RuleEngineUtil.mergeMap(paramsMap, session.getParameters(), MergeType.EXIST);
		Map<String, Object> exts = new HashMap<>();
		exts.put("nodeNames", response.getNodeNames());
		exts.put("flowId", response.getFlowId());
		exts.put("duration", response.getDuration());
		exts.put("flowsCount", response.getFlowExecutionResponses().size());
		exts.put("rulesCount", response.getRuleExecutionResponses().size());

		Object detailObj = optionsMap.get("detail");
		if (detailObj != null && Boolean.parseBoolean(detailObj.toString())) {
			data.put("__detail", response);
		}
		data.put("ext", exts);

		return data;
	}

	public static void waitFor(BatchSession session, Map<String,Object> optionsMap) throws Exception{
		Object timeoutObj = ObjectUtil.tryGet("timeout", optionsMap, 0);
		long timeout = Long.valueOf(timeoutObj.toString());
		if(timeout > 0){
			session.wait(timeout);
		}else{
			session.waitForCompletion();
		}
	}

	public static List<Object> flattening(List<Object> list){
		List<Object> result = new ArrayList<>();
		for (Object obj:list) {
			if(!ObjectUtil.isCollectionType(obj.getClass())){
				result.add(obj);
			}else{
				List<Object> objects = flattening((List<Object>)obj);
				result.addAll(objects);
			}
		}
		return result;
	}
	public static void insertFlattening(Object obj, KnowledgeSession session){
		if(!ObjectUtil.isCollectionType(obj.getClass())){
			session.insert(obj);
		}else{
			((Iterable)obj).forEach(o -> insertFlattening(o, session));
		}
	}
	public static <T,V> Map<T, V> mergeMap(Map<T, V> source, Map<T, V> map, MergeType type){
		for (Map.Entry<T, V> entry: map.entrySet()) {
			T key = entry.getKey();
			switch (type){
				case EXIST:
					if(source.containsKey(key)){
						source.replace(key, map.get(key));
					}
					break;
				case UNEXIST:
					if(!source.containsKey(key)){
						source.put(key, map.get(key));
					}
				default:
					if(source.containsKey(key)){
						source.replace(key, map.get(key));
					}else{
						source.put(key, map.get(key));
					}
			}
		}
		return source;
	}

	public static String getDefaultName(String name, String defName){
		if(name == null || name.isEmpty()){
			return defName;
		}
		return name;
	}
 	public static Map<String, Object> getDemoParams(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jdScore", 0);
		map.put("hasJdScore", false);
		map.put("supplierBranch", 0);
		map.put("comScore", 0);
		map.put("jxlScore", 0);
		map.put("jxlBranch", 0);
		map.put("ruleResult", 0);
		
		return map;
	}
	public static String getDemoJob(){
		List<String> jobs = new ArrayList<String>();
    	jobs.add("学生");
    	jobs.add("警察");
    	jobs.add("老师");
    	jobs.add("IT");
    	jobs.add("个体户");
    	
    	int index = RandomUtils.nextInt(4);
    	return jobs.get(index);
	}
}
