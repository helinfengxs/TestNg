package com.helinfengxs.cases.employees;

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

public class employeeGetCases {
    //定义所有模块数据map
    public HashMap<String,Object> data = new HashMap<>();

    @BeforeClass(description = "根据yaml文件中的模块名得到该模块所有用例数据以及初始化url")
    public void init(){
        //获取到所有模块数据
        data = Util.analyseData("employeeGetCases");
        //设置URL
        RestAssured.baseURI= GlobalParams.request.get("base_url");
    }
    @Test(description = "控制台账号密码登录")
    public void getEmployee(){

        //传入所有模块数据对象，再根据yaml文件中的用例方法名获取到获取单个用例数据，
        CaseRequestParam currencydata = Util.getCaseData(data,"getEmployee");
        System.out.println(currencydata);
        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
        Response response = Util.sendRequest(currencydata);
        System.out.println(response.asString());
        //得到带提取的参数列表例表
        ArrayList<String> extractList = currencydata.getExtract();

        //得到预期结果列表
        List<HashMap<String, List<Object>>> validate = currencydata.getValidate();

        //传入响应对象，以及待提取的参数列表
        Util.getExtract(response,extractList);

        //传入响应对象，以及单个用例实体类，进行断言
        Util.testNgAssert(response,currencydata);

    }
}
