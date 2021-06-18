package com.helinfengxs.cases;

import com.helinfengxs.data.CaseRequestParam;
import com.helinfengxs.data.GlobalParams;
import com.helinfengxs.util.Util;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectCases {
    public HashMap<String,Object> loginData = new HashMap<>();
    @BeforeClass
    public void init(){
        loginData = Util.analyseData("projectCases");
        RestAssured.baseURI = GlobalParams.request.get("base_url");
    }
    @Test
    public void getAll(){
        //传入所有模块数据对象，再根据yaml文件中的用例方法名获取到获取单个用例数据，
        CaseRequestParam loginCorrect = Util.getCaseData(loginData,"getAll");
        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
        Response response = Util.sendRequest(loginCorrect);
        //得到带提取的参数列表例表
        ArrayList<String> extractList = loginCorrect.getExtract();
        //得到预期结果列表
        List<HashMap<String, List<Object>>> validate = loginCorrect.getValidate();
        //传入响应对象，以及待提取的参数列表
        Util.getExtract(response,extractList);
        //传入响应对象，以及单个用例实体类，进行断言
        Util.testNgAssert(response,loginCorrect);
    }
    @Test
    public void pageProject(){
        //传入所有模块数据对象，再根据yaml文件中的用例方法名获取到获取单个用例数据，
        CaseRequestParam loginCorrect = Util.getCaseData(loginData,"pageProject");
        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
        Response response = Util.sendRequest(loginCorrect);
        //得到带提取的参数列表例表
        ArrayList<String> extractList = loginCorrect.getExtract();
        //得到预期结果列表
        List<HashMap<String, List<Object>>> validate = loginCorrect.getValidate();
        //传入响应对象，以及待提取的参数列表
        Util.getExtract(response,extractList);
        //传入响应对象，以及单个用例实体类，进行断言
        Util.testNgAssert(response,loginCorrect);
    }
    @Test

    public void pageProjectCondition(){

        //传入所有模块数据对象，再根据yaml文件中的用例方法名获取到获取单个用例数据，
        CaseRequestParam loginCorrect = Util.getCaseData(loginData,"pageProjectCondition");

        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
        Response response = Util.sendRequest(loginCorrect);
        //得到带提取的参数列表例表
        ArrayList<String> extractList = loginCorrect.getExtract();
        //得到预期结果列表
        List<HashMap<String, List<Object>>> validate = loginCorrect.getValidate();
        //传入响应对象，以及待提取的参数列表
        Util.getExtract(response,extractList);
        //传入响应对象，以及单个用例实体类，进行断言
        Util.testNgAssert(response,loginCorrect);
    }
}
