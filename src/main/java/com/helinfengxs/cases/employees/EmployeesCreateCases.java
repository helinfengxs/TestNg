package com.helinfengxs.cases.employees;

import com.helinfengxs.data.CaseRequestParam;
import com.helinfengxs.data.GlobalParams;
import com.helinfengxs.util.Util;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

@Feature("创建员工")
public class EmployeesCreateCases  {
    public HashMap<String,Object> loginData = new HashMap<>();
    @BeforeClass
    public void init(){
        loginData = Util.analyseData("EmployeesCreateCases");
        RestAssured.baseURI = GlobalParams.request.get("base_url");
    }

    @Test(description = "创建员工")
    public void employeesCreate(){
        //传入所有模块数据对象，再根据yaml文件中的用例方法名获取到获取单个用例数据，
        CaseRequestParam loginCorrect = Util.getCaseData(loginData,"employeesCreate");
        Response response = Util.sendRequest(loginCorrect);
        System.out.println(response.asString());

//        TestStep.requestBody(loginCorrect.getUrl(),s);
//        Map body = (Map)loginCorrect.getParam().get("body");
//        String s = JSON.toJSONString(body.get("param"));

//        Response response = Util.sendRequest(loginCorrect);
//        TestStep.respondBody(response.asString());
//        System.out.println(response.asString());
//        System.out.println(response.asString());

//        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
//        Response response = Util.sendRequest(loginCorrect);
//        //得到带提取的参数列表例表
//        ArrayList<String> extractList = loginCorrect.getExtract();
//        //得到预期结果列表
//        List<HashMap<String, List<Object>>> validate = loginCorrect.getValidate();
//        //传入响应对象，以及待提取的参数列表
//        Util.getExtract(response,extractList);
//        //传入响应对象，以及单个用例实体类，进行断言
//        Util.testNgAssert(response,loginCorrect);
    }
}
