package com.yf.rule.qsq;

import com.bstek.urule.Utils;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.service.KnowledgeService;
import com.yf.rule.qsq.entity.Customer;

public class InvokeTest {
    public void test() throws Exception{
        Customer c1=new Customer();
        c1.setLevel(11);
        c1.setMarried(false);
        c1.setName("张三");
         
        Customer c2=new Customer();
        c2.setLevel(0);
        c2.setAge(16);
        c2.setName("李四");
         
        Customer c3=new Customer();
        c3.setLevel(4);
        c3.setMarried(false);
        c3.setName("赵六");
         
//        Customer c4=new Customer();
//        c4.setLevel(4);
//        c4.setAge(22);
//        c4.setName("王五");
        Customer c4=new Customer();
        c4.setLevel(15);
        c4.setMarried(false);
        c4.setAge(22);
        c4.setName("王五");
         
        Customer c5=new Customer();
        c5.setLevel(6);
        c5.setAge(30);
        c5.setMarried(true);
        c5.setName("刘二");
         
        //从spring上下文中获取KnowledgeService对象实例
        KnowledgeService knowledgeService=(KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
         
        //通过KnowledgeService获取指定的知识包（项目名+"/"+知识包ID）
        KnowledgePackage knowledgePackage=knowledgeService.getKnowledge("demo/ruleset-ref");
         
        //创建一个KnowledgeSession对象
        KnowledgeSession session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
         
        //将上面五个Customer实例插入到KnowledgeSession中进行计算
        session.insert(c1);
        session.insert(c2);
        session.insert(c3);
        session.insert(c4);
        session.insert(c5);
        //触发规则，进行计算
        session.fireRules();
    }
}