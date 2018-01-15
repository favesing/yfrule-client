package com.yf.rule.entity;

import com.bstek.urule.model.Label;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyRisk {
	@Label("风控申请ID")
	private int apply_risk_id;
	@Label("风控申请编号")
	private String apply_risk_code;
	@Label("申请来源")
	private String apply_risk_source;
	@Label("贷款类型")
	private String apply_risk_type;
	@Label("放款类型")
	private String apply_risk_loan_type;
	@Label("资产状态")
	private String apply_risk_status;
	
	public int getApply_risk_id() {
		return apply_risk_id;
	}
	public void setApply_risk_id(int apply_risk_id) {
		this.apply_risk_id = apply_risk_id;
	}
	public String getApply_risk_code() {
		return apply_risk_code;
	}
	public void setApply_risk_code(String apply_risk_code) {
		this.apply_risk_code = apply_risk_code;
	}
	public String getApply_risk_source() {
		return apply_risk_source;
	}
	public void setApply_risk_source(String apply_risk_source) {
		this.apply_risk_source = apply_risk_source;
	}
	public String getApply_risk_type() {
		return apply_risk_type;
	}
	public void setApply_risk_type(String apply_risk_type) {
		this.apply_risk_type = apply_risk_type;
	}
	public String getApply_risk_loan_type() {
		return apply_risk_loan_type;
	}
	public void setApply_risk_loan_type(String apply_risk_loan_type) {
		this.apply_risk_loan_type = apply_risk_loan_type;
	}
	
}
