package com.yf.rule.qsq.entity;

import com.bstek.urule.model.Label;
import java.util.Date;
import java.util.List;

public class Customer
{
  @Label("������")
  private String name;
  @Label("������")
  private int age;
  @Label("������������")
  private Date birthday;
  @Label("������")
  private int level;
  @Label("���������")
  private String mobile;
  @Label("������")
  private boolean gender;
  @Label("������������")
  private boolean car;
  @Label("������")
  private boolean married;
  @Label("������������")
  private boolean house;
  @Label("������")
  private List<Order> orders;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public int getAge()
  {
    return this.age;
  }
  
  public void setAge(int age)
  {
    this.age = age;
  }
  
  public Date getBirthday()
  {
    return this.birthday;
  }
  
  public void setBirthday(Date birthday)
  {
    this.birthday = birthday;
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  public void setLevel(int level)
  {
    this.level = level;
  }
  
  public String getMobile()
  {
    return this.mobile;
  }
  
  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }
  
  public boolean isGender()
  {
    return this.gender;
  }
  
  public void setGender(boolean gender)
  {
    this.gender = gender;
  }
  
  public boolean isCar()
  {
    return this.car;
  }
  
  public void setCar(boolean car)
  {
    this.car = car;
  }
  
  public boolean isMarried()
  {
    return this.married;
  }
  
  public void setMarried(boolean married)
  {
    this.married = married;
  }
  
  public boolean isHouse()
  {
    return this.house;
  }
  
  public void setHouse(boolean house)
  {
    this.house = house;
  }
  
  public List<Order> getOrders()
  {
    return this.orders;
  }
  
  public void setOrders(List<Order> orders)
  {
    this.orders = orders;
  }
}
