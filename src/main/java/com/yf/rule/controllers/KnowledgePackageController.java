package com.yf.rule.controllers;

import com.bstek.urule.Configure;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.cache.CacheUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class KnowledgePackageController extends BaseController {

    @RequestMapping("/knowledgepackagereceiver")
    public void knowledgePackageReceiver() throws Exception{
        String packageId=request.getParameter("packageId");
        if(StringUtils.isEmpty(packageId)){
            return;
        }
        packageId= URLDecoder.decode(packageId, "utf-8");
        if(packageId.startsWith("/")){
            packageId=packageId.substring(1,packageId.length());
        }
        String content=request.getParameter("content");
        if(StringUtils.isEmpty(content)){
            return;
        }
        content=URLDecoder.decode(content, "utf-8");
        ObjectMapper mapper=new ObjectMapper();

        mapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
        KnowledgePackageWrapper wrapper=mapper.readValue(content, KnowledgePackageWrapper.class);
        wrapper.buildDeserialize();
        KnowledgePackage knowledgePackage=wrapper.getKnowledgePackage();
        Map<String, FlowDefinition> flowMap=knowledgePackage.getFlowMap();
        if(flowMap!=null && flowMap.size()>0){
            for(FlowDefinition fd:flowMap.values()){
                fd.buildConnectionToNode();
            }
        }
        CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("["+sd.format(new Date())+"] "+"Successfully receive the server side to pushed package:"+packageId);
        response.setContentType("text/plain");
        PrintWriter pw=response.getWriter();
        pw.write("ok");
        pw.flush();
        pw.close();
    }
}
