package com.yf.rule.controllers;

import com.bstek.urule.runtime.BatchSession;
import com.bstek.urule.runtime.Business;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yf.rule.core.Code;
import com.yf.rule.utils.ObjectUtil;
import com.yf.rule.utils.RuleEngineUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RuleController extends BaseController {

    @RequestMapping("/")
    public String index() {
        return "Rule Engine With Spring Boot!";
    }

    @RequestMapping(value = "/rule")
    public String doRule(@RequestParam(name = "packageName") String packageName,
                         @RequestParam(name = "facts") String facts,
                         @RequestParam(name = "params") String params,
                         @RequestParam(name = "options") String options) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final List<Object> factsList;
        final Map<String, Object> paramsMap;
        final Map<String, Object> optionsMap;

        if (params != null && !params.isEmpty()) {
            paramsMap = ObjectUtil.jsonDeserializeMap(params, mapper);
        }else{
            paramsMap = new HashMap<>();
        }
        if (options != null && !options.isEmpty()) {
            optionsMap = ObjectUtil.jsonDeserializeMap(options, mapper);
        }else{
            optionsMap = new HashMap<>();
        }
        if (facts != null && !facts.isEmpty()) {
            factsList = ObjectUtil.jsonDeserialize(facts, mapper);
        }else{
            factsList = new ArrayList<>();
        }
        KnowledgeSession session = RuleEngineUtil.getSessionByPackageName(packageName);
        RuleEngineUtil.insertFlattening(factsList, session);

        RuleExecutionResponse response = session.fireRules(paramsMap);
        Map<String, Object> data = RuleEngineUtil.transformRuleExecutionResponseToData(session, response, paramsMap, optionsMap);

        return ObjectUtil.result(Code.SUCCESS.getValue(), Code.SUCCESS.getName(), data);
    }

    @RequestMapping(value = "/flow")
    public String doFlow(@RequestParam(name = "packageName") String packageName,
                         @RequestParam(name = "flowId") String flowId,
                         @RequestParam(name = "facts") String facts,
                         @RequestParam(name = "params") String params,
                         @RequestParam(name = "options") String options) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final List<Object> factsList;
        final Map<String, Object> paramsMap;
        final Map<String, Object> optionsMap;

        if (params != null && !params.isEmpty()) {
            paramsMap = ObjectUtil.jsonDeserializeMap(params, mapper);
        }else{
            paramsMap = new HashMap<>();
        }
        if (options != null && !options.isEmpty()) {
            optionsMap = ObjectUtil.jsonDeserializeMap(options, mapper);
        }else{
            optionsMap = new HashMap<>();
        }
        if (facts != null && !facts.isEmpty()) {
            factsList = ObjectUtil.jsonDeserialize(facts, mapper);
        }else{
            factsList = new ArrayList<>();
        }

        KnowledgeSession session = RuleEngineUtil.getSessionByPackageName(packageName);
        RuleEngineUtil.insertFlattening(factsList, session);

        FlowExecutionResponse response = session.startProcess(flowId, paramsMap);
        Map<String, Object> data = RuleEngineUtil.transformFlowExecutionResponseToData(session, response, paramsMap, optionsMap);

        return ObjectUtil.result(Code.SUCCESS.getValue(), Code.SUCCESS.getName(), data);
    }


    @RequestMapping(value = "/batchRule")
    public String doBatchRule(@RequestParam(name = "packageName") String packageName,
                              @RequestParam(name = "batchFacts") String batchFacts,
                              @RequestParam(name = "params") String params,
                              @RequestParam(name = "options") String options) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final List<Object> batchFactsList;
        final Map<String, Object> paramsMap;
        final Map<String, Object> optionsMap;

        if (params != null && !params.isEmpty()) {
            paramsMap = ObjectUtil.jsonDeserializeMap(params, mapper);
        }else{
            paramsMap = new HashMap<>();
        }
        if (options != null && !options.isEmpty()) {
            optionsMap = ObjectUtil.jsonDeserializeMap(options, mapper);
        }else{
            optionsMap = new HashMap<>();
        }
        if (batchFacts != null && !batchFacts.isEmpty()) {
            batchFactsList = ObjectUtil.jsonDeserialize(batchFacts, mapper);
        }else{
            batchFactsList = new ArrayList<>();
        }

        BatchSession session = RuleEngineUtil.getBatchSessionByPackageName(packageName, optionsMap);
        final List<Map<String, Object>> dataList = new ArrayList<>();

        batchFactsList.forEach(f -> session.addBusiness(new Business() {
            @Override
            public void execute(KnowledgeSession session) {
                RuleEngineUtil.insertFlattening(f, session);
                RuleExecutionResponse response = session.fireRules(paramsMap);
                Map<String, Object> data = RuleEngineUtil.transformRuleExecutionResponseToData(session, response, paramsMap, optionsMap);
                dataList.add(data);
            }
        }));
        RuleEngineUtil.waitFor(session, optionsMap);

        return ObjectUtil.result(Code.SUCCESS.getValue(), Code.SUCCESS.getName(), dataList);
    }

    @RequestMapping(value = "/batchFlow")
    public String doBatchFlow(@RequestParam(name = "packageName") String packageName,
                              @RequestParam(name = "flowId") String flowId,
                              @RequestParam(name = "facts") String batchFacts,
                              @RequestParam(name = "params") String params,
                              @RequestParam(name = "options") String options) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final List<Object> batchFactsList;
        final Map<String, Object> paramsMap;
        final Map<String, Object> optionsMap;

        if (params != null && !params.isEmpty()) {
            paramsMap = ObjectUtil.jsonDeserializeMap(params, mapper);
        }else{
            paramsMap = new HashMap<>();
        }
        if (options != null && !options.isEmpty()) {
            optionsMap = ObjectUtil.jsonDeserializeMap(options, mapper);
        }else{
            optionsMap = new HashMap<>();
        }
        if (batchFacts != null && !batchFacts.isEmpty()) {
            batchFactsList = ObjectUtil.jsonDeserialize(batchFacts, mapper);
        }else{
            batchFactsList = new ArrayList<>();
        }

        BatchSession session = RuleEngineUtil.getBatchSessionByPackageName(packageName, optionsMap);
        final List<Map<String, Object>> dataList = new ArrayList<>();

        batchFactsList.forEach(f -> session.addBusiness(new Business() {
            @Override
            public void execute(KnowledgeSession session) {
                RuleEngineUtil.insertFlattening(f, session);
                FlowExecutionResponse response = session.startProcess(flowId, paramsMap);
                Map<String, Object> data = RuleEngineUtil.transformFlowExecutionResponseToData(session, response, paramsMap, optionsMap);
                dataList.add(data);
            }
        }));
        RuleEngineUtil.waitFor(session, optionsMap);

        return ObjectUtil.result(Code.SUCCESS.getValue(), Code.SUCCESS.getName(), dataList);
    }
}
