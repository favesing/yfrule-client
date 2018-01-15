package com.yf.rule.core;

public enum Code {
    SUCCESS("成功", 0),
    FAIL("失败", 1),
    PROCESSING("处理中", 2);

    private String name;
    private int value;
    private Code(String name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getValue(){
        return this.value;
    }
    public void setValue(int value){
        this.value = value;
    }

    public static String getName(int value){
        for (Code code: Code.values()) {
            if(code.value == value) return code.name;
        }
        return null;
    }
    public static int getValue(String name){
        Code code = Code.valueOf(name);
        return code != null ? code.value : -1;
    }
}
