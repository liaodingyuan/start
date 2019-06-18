package com.liaody.sty.constants;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultMessage {
    public static final int SUCCESS = 1;
    public static final int OTHER = 2;
    public static final int FAIL = 0;
    public static final String OPERATION_SUCCESS = "操作成功!";
    public static final String OPERATION_FAILURE = "操作失败!";
    public static final String OPERATION_ERROR = "参数不合法！";
    private int result = 1;
    private String message = "";
    private Object data;

    public ResultMessage() {
    }

    public ResultMessage(int result, String message) {
        this.result = result;
        this.message = message;
        this.data = new ArrayList<>();
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return this.result == 1;
    }
    public ResultMessage (int result,String message,Object data){
        this.result = result;
        this.message = message;
        // 如果数据为空，默认给一个空数组
        this.setData(data==null? Lists.newArrayList():data);
    }

    @Override
    public String toString(){
        Map<String,Object> resultMap = new HashMap<>(4);
        resultMap.put("success",this.isSuccess());
        resultMap.put("data",this.getData());
        resultMap.put("message",this.message);
        return JSON.toJSONString(resultMap);
    }

    public void setData(Object data){
        this.data = data;
    }
    public Object getData(){
        return this.data;
    }

    public static ResultMessage success(String  message){
        return  new ResultMessage(1,message);
    }
    public static ResultMessage success(String  message,Object data){
        return  new ResultMessage(1,message,data);
    }
    public static ResultMessage fail(String message){
        return new ResultMessage(0,message);
    }
    public static ResultMessage fail(String message,Object object){
        return new ResultMessage(0,message,object);
    }


}