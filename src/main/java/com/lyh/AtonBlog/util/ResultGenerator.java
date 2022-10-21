package com.lyh.AtonBlog.util;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;
    
    /**
     * 设置一个默认的返回信息
     */
    public static Result genSuccessResult(){
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }
    
    /**
     * 成功  只要成功信息
     */
    public static Result genSuccessResult(String message){
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 成功 携带数据
     */
    public static Result genSuccessResult(Object data){
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }
    
    /**
     * 失败 判断信息是否为空 然后填入默认或自定义的信息
     */
    public static Result genFailResult(String message){
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        if (message.isEmpty()){
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        }else {
            result.setMessage(message);
        }
        return result;
    }
    
    
    /**
     * 错误信息
     */
    public static Result genErrorMessage(int code,String message){
    
        Result result = new Result();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
        
    } 
    
    
}
