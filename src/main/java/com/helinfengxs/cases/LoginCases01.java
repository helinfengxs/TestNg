package com.helinfengxs.cases;

import com.helinfengxs.data.CaseRequestParam;
import com.helinfengxs.data.GlobalParams;
import com.helinfengxs.util.Util;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LoginCases01 {
    //定义所有模块数据map
    public HashMap<String,Object> loginData = new HashMap<>();

    @BeforeClass(description = "根据yaml文件中的模块名得到该模块所有用例数据以及初始化url")
    public void init(){
        Util.readYaml("D:\\iderproject\\kitchen_api\\test.yaml");
        //获取到所有模块数据
        loginData = Util.analyseData("LoginCases");
        //设置URL
        RestAssured.baseURI= GlobalParams.request.get("base_url");
    }
    @DataProvider(name = "data")
    public Object[][]getData(){
        Set<String> keys = loginData.keySet();
        ArrayList<Object> list = new ArrayList<>();
        for (String key : keys) {
            list.add(loginData.get(key));
        }
        return Util.getArray(list);
    }
    @Test(description = "正常登录")

    public void loginCorrect(){

        //传入所有模块数据对象，再根据yaml文件中的用例方法名获取到获取单个用例数据，
        CaseRequestParam loginCorrect = Util.getCaseData(loginData,"loginCorrect");
        HashMap<String, Object> param = loginCorrect.getParam();

        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
        Response response = Util.sendRequest(loginCorrect);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));

//        //得到带提取的参数列表例表
//        ArrayList<String> extractList = loginCorrect.getExtract();
//
//        //传入响应对象，以及待提取的参数列表
//        Util.getExtract(response,extractList);
//        //传入响应对象，以及单个用例实体类，进行断言
//        Util.testNgAssert(response,loginCorrect);

    }



    @Test(dataProvider = "data",description = "登录密码错误")
    public void passError(Object param){
        CaseRequestParam caseData = Util.getCaseData(param);
        //调用sendRequest 传入单个用例数据对象，发送http请求，得到响应
//        Response response = Util.sendRequest(caseData);
//        System.out.println(response.asString());
//        response.then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
        /**
        //得到带提取的参数列表例表
        ArrayList<String> extractList = caseData.getExtract();

        //传入响应对象，以及待提取的参数列表
        Util.getExtract(response,extractList);
        //传入响应对象，以及单个用例实体类，进行断言
        Util.testNgAssert(response,caseData);
         */

    }
    @Test
    public void test01(){
        String s = "$docker";
        char c = s.charAt(0);
        System.out.println(c);
    }

}
