package com.yf.rule.test;

import java.util.List;
import java.util.Map;

import com.yf.rule.entity.Customer;
import org.apache.commons.lang.math.RandomUtils;

import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.BatchSession;
import com.bstek.urule.runtime.Business;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.yf.rule.utils.RuleEngineUtil;

public class InvokeTest {
	
    public void test() throws Exception{
       System.out.println("invoke test");
    }
    
    public String doRule(String name) throws Exception{
    	name = RuleEngineUtil.getDefaultName(name, "元方/default_rule");
        KnowledgeSession session = RuleEngineUtil.getSession(name);
        
        Customer c = new Customer();
        c.setAge(19);
        c.setJob(RuleEngineUtil.getDemoJob());
        c.setZmscore(600);
        session.insert(c);
        
        Map<String, Object> params = RuleEngineUtil.getDemoParams();
        RuleExecutionResponse r1 = session.fireRules(params);
        
        StringBuilder sb = new StringBuilder();
        for (RuleInfo rule : r1.getFiredRules()) {
        	 System.out.printf("rule=%s,", rule.getName());
        	 System.out.println();
        	 
        	 sb.append(String.format("rule=%s,", rule.getName()));
        	 sb.append("<br/>");
		}
        
        Object ruleResult = session.getParameter("ruleResult");
        System.out.printf("执行时间=%d, ruleReuls=%s", r1.getDuration(), ruleResult);
        
        sb.append(String.format("执行时间=%d, ruleReuls=%s", r1.getDuration(), ruleResult));
        return sb.toString();
    }
    
    public String doFlow(String name) throws Exception{
    	name = RuleEngineUtil.getDefaultName(name, "元方/default_flow");
    	KnowledgeSession session = RuleEngineUtil.getSession(name);
        
        Customer c = new Customer();
        c.setAge(RandomUtils.nextInt(100));
        c.setJob(RuleEngineUtil.getDemoJob());
        c.setZmscore(RandomUtils.nextInt(6000));
        session.insert(c);
        
        Map<String, Object> params = RuleEngineUtil.getDemoParams();
        FlowExecutionResponse r1 = session.startProcess("新客审核", params);
        
        StringBuilder sb = new StringBuilder();
        List<String> names = r1.getNodeNames();
        for (int i = 0; i < names.size(); i++) {
        	if(i <= 0) continue;
        	String nodeName = names.get(i);
        	System.out.printf(">>> %s,", nodeName);
        	System.out.println();
        	
        	sb.append(String.format(">>> %s,", nodeName));
       	 	sb.append("<br/>");
		}
        
        Object ruleResult = session.getParameter("ruleResult");
        System.out.printf("执行时间=%d, ruleReuls=%s", r1.getDuration(), ruleResult);
        
        sb.append(String.format("执行时间=%d, ruleReuls=%s", r1.getDuration(), ruleResult));
        return sb.toString();
    }
    
    public void doBatchFlow(String name, int threadSize, int batchSize) throws Exception{
    	name = RuleEngineUtil.getDefaultName(name, "元方/default_flow");
        final String flowName = "新客审核";
    	BatchSession session = RuleEngineUtil.getBatchSession(name);
        
    	long st = System.currentTimeMillis();
    	int count = 10000;

    	for (int i = 0; i < count; i++) {
    		session.addBusiness(new Business() {
				@Override
				public void execute(KnowledgeSession session) {
					Customer c = new Customer();
		            c.setAge(RandomUtils.nextInt(36));
		            c.setJob(RuleEngineUtil.getDemoJob());
		            c.setZmscore(RandomUtils.nextInt(20000));
		            session.insert(c);
		            session.startProcess(flowName);
				}
			});
		}
    	session.waitForCompletion();

        System.out.printf("执行时间=%d", System.currentTimeMillis() - st);
    }
}