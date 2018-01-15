package com.yf.rule.qsq.entity;

import com.bstek.urule.model.Label;

public class Order
{
  @Label("������")
  private String name;
  @Label("������")
  private float price;
  @Label("������")
  private int amount;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public float getPrice()
  {
    return this.price;
  }
  
  public void setPrice(float price)
  {
    this.price = price;
  }
  
  public int getAmount()
  {
    return this.amount;
  }
  
  public void setAmount(int amount)
  {
    this.amount = amount;
  }
}
