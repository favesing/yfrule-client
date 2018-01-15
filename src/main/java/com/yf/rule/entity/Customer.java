package com.yf.rule.entity;

import java.util.Date;
import java.util.List;

import com.bstek.urule.model.Label;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
	@Label("姓名")
	private String name;
	@Label("年龄")
	private int age;
	@Label("出生日期")
	private Date birthday;
	@Label("手机号")
	private String mobile;
	@Label("性别")
	private boolean gender;
	@Label("身份证号")
	private String idCard;
	@Label("职业")
	private String job;
	@Label("工作单位")
	private String company;
	@Label("工作单位地址")
	private String companyAddress;
	@Label("芝麻分")
	private int zmscore;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public int getZmscore() {
		return zmscore;
	}
	public void setZmscore(int zmscore) {
		this.zmscore = zmscore;
	}

	@JsonUnwrapped()
	private List<Order> orders;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
