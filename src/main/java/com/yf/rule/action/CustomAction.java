package com.yf.rule.action;

import java.util.Random;

import com.bstek.urule.model.ExposeAction;

public class CustomAction {

	@ExposeAction("随机数")
	public int randNum(int max){
		Random random = new Random();
		int num = random.nextInt(max);
		return num;
	}
	
	@ExposeAction("京东模型分")
	public long jdScore(int max){
		return randNum(max);
	}
	
	@ExposeAction("通用模型分")
	public long comScore(int max){
		return randNum(max);
	}
	
	@ExposeAction("聚信立模型分")
	public long jxlScore(int max){
		return randNum(max);
	}
}
