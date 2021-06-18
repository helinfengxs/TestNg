package com.helinfengxs.util;
import com.alibaba.fastjson.JSONObject;
import io.qameta.allure.Attachment;

/*
 *测试步骤，测试报告中展现
 */
public class TestStep {

    public static void requestAndRespondBody(String URL, String Body,String Respond){
        requestBody(URL,Body);
        respondBody(Respond);
    }
    @Attachment("接口信息")
    public static StringBuffer requestBody(String url,String method,String header,String parm,StringBuffer validateStr) {
        StringBuffer str = new StringBuffer();
        str.append("请求接口地址："+url);
        str.append("\n");
        str.append("请求方式："+method);
        str.append("\n");
        str.append("请求头部："+header);
        str.append("\n");
        str.append("请求参数："+parm);
        str.append("\n");
        str.append("期望结果："+validateStr);
        return str;
    }
    @Attachment("响应结果")
    public static String responseResutl(String response) {
        return response;
    }

    @Attachment("测试结果")
    public static String assertReult(String result) {

        return result;
    }











    @Attachment("请求内容")
    public static String requestBody(String URL, String body) {

        //格式化json串
        boolean prettyFormat = true; //格式化输出
        JSONObject jsonObject = JSONObject.parseObject(body);
        String str = JSONObject.toJSONString(jsonObject,prettyFormat);

        //报告展现请求报文
        return URL+"\n"+str;
    }

    @Attachment("响应结果")
    public static String respondBody(String respond) {

        //格式化json串
        boolean prettyFormat = true; //格式化输出
        JSONObject jsonObject = JSONObject.parseObject(respond);
        String str = JSONObject.toJSONString(jsonObject,prettyFormat);

        //报告展现响应报文
        return str;
    }

    @Attachment("数据库断言结果")
    public static StringBuffer databaseAssertResult(StringBuffer assertResult){
        //报告展现数据库断言结果
        return assertResult;
    }

    @Attachment("响应报文断言结果")
    public static StringBuffer assertRespond(StringBuffer assertResult){
        //报告展现数据库断言结果
        return assertResult;
    }
}
