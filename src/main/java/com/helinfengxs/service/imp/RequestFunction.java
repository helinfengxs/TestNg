package com.helinfengxs.service.imp;

import com.alibaba.fastjson.JSON;
import com.helinfengxs.data.CaseRequestParam;
import com.helinfengxs.service.RequestFunctionInterFace;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestFunction  implements RequestFunctionInterFace {

    @Override
    public Response post(CaseRequestParam caseRequestParam) {
        String url = caseRequestParam.getUrl();
        HashMap<String, Object> param = caseRequestParam.getParam();
        Map<String, String> headers = caseRequestParam.getHeaders();
        Response response = null;
        if(headers != null){
            if(param != null){
                Set<String> strings = param.keySet();
                Object[] objects = strings.toArray();
                String paramType = (String) objects[0];

                if("json".equals(paramType)){
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .headers(headers)
                            .body(param.get("json"))
                            .when()
                            .post(url);
                }else {
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .headers(headers)
                            .formParams((Map)param.get("body"))
                            .when()
                            .post(url);
                }
            }
            else {
                response = RestAssured.given()
                        .relaxedHTTPSValidation("TLS")
                        .headers(headers)
                        .when()
                        .post(url);
            }
        }else {
            if(param != null){
                Set<String> strings = param.keySet();
                Object[] objects = strings.toArray();
                String paramType = (String) objects[0];

                if("json".equals(paramType)){
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .body(param.get("json"))
                            .when()
                            .post(url);
                }else {
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .formParams((Map)param.get("body"))
                            .when()
                            .post(url);
                }
            }
            else {
                response = RestAssured.given()
                        .relaxedHTTPSValidation("TLS")
                        .when()
                        .post(url);
            }
        }

        return response;
    }

    @Override
    public Response get(CaseRequestParam caseRequestParam) {
        String url = caseRequestParam.getUrl();
        HashMap<String, Object> param = caseRequestParam.getParam();
        Map<String, String> headers = caseRequestParam.getHeaders();
        Response response = null;
        if(headers != null){
            if(param != null){
                response = RestAssured.given()
                        .relaxedHTTPSValidation("TLS")
                        .headers(headers)
                        .formParams((Map)param.get("body"))
                        .when()
                        .get(url);
                return  response;
            }
            else {
                response = RestAssured.given()
                        .relaxedHTTPSValidation("TLS")
                        .headers(headers)
                        .when()
                        .get(url);
            }
        }else {
            if(param != null){
                response = RestAssured.given()
                        .relaxedHTTPSValidation("TLS")
                        .formParams((Map)param.get("body"))
                        .when()
                        .get(url);
                return  response;
            }
            else {
                response = RestAssured.given()
                        .relaxedHTTPSValidation("TLS")
                        .when()
                        .post(url);
            }
        }

        return response;
    }



    /**
     * post 带请求路径、参数、头部,设置默认证书，given后面添加.relaxedHTTPSValidation("TLS")
     * @param url 请求路径
     * @param body 请求参数
     * @param headers 请求头部
     * @return 返回响应结果对象
     */
    public  Response post(String url, HashMap<String,Object> body, Map<String,Object> headers){
        Response response = null;
        Set<String> strings = body.keySet();
        Object[] objects = strings.toArray();
        String paramType = (String) objects[0];

        if("json".equals(paramType)){
            response = RestAssured.given()
                    .relaxedHTTPSValidation("TLS")
                    .headers(headers)
                    .body(body.get("json"))
                    .when()
                    .post(url);
        }else {
            response = RestAssured.given()
                    .relaxedHTTPSValidation("TLS")
                    .headers(headers)
                    .formParams((Map)body.get("body"))
                    .when()
                    .post(url);
        }
        return response;
    }

    /**
     * post 带请求路径、参数
     * @param url 请求路径
     * @param body  请求餐宿
     * @return 返回响应结果
     */
    public  Response post(String url,HashMap<String,Object> body){
        Response response = null;
        Set<String> strings = body.keySet();
        Object[] objects = strings.toArray();
        String paramType = (String) objects[0];
        if("json".equals(paramType)){
            response =RestAssured.given()
                    .relaxedHTTPSValidation("TLS")
                    .body(body.get("json"))
                    .when()
                    .post(url);
        }else {
            response = RestAssured.given()
                    .relaxedHTTPSValidation("TLS")
                    .formParams(body)
                    .when()
                    .post(url);
        }
        return response;
    }

    /**
     * post 带请求路径，头部
     * @param url 请求路径
     * @param headers 请求头部
     * @return 返回结果响应对象
     */
    public Response post(String url,Map<String,Object> headers){
        Response response = null;
        response = RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .headers(headers)
                .when()
                .post(url);
        return response;
    }

    /**
     * post 带请求路径
     * @param url 请求路径
     * @return 返回响应结果
     */
    public  Response post(String url){
        Response response = null;
        response =RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .when()
                .post(url);
        return response;
    }






    /**
     * get带路径请求方法，无参数和头部
     * @param urlPath 路径
     * @return 结果响应对象
     */
    public  Response get(String urlPath){
        Response response = null;
        response = RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .when()
                .get(urlPath);
        return  response;
    }



    public  Response get(String urlPath,HashMap<String,Object> body){
        Response response = null;
        response = RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .formParams((Map)body.get("body"))
                .when()
                .get(urlPath);
        return  response;
    }

    /**
     * get 带请求路径以及带请求头部 无参数
     * @param urlPath 路径
     * @param headers 请求头
     * @return 结果响应对象
     */
    public  Response get(String urlPath,Map<String,Object> headers){
        Response response = null;
        response = RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .headers(headers)
                .when()
                .get(urlPath);
        return  response;
    }

    /**
     * get 带请求路径，请求参数，请求头部
     * @param urlPath 请求路径
     * @param body 请求参数
     * @param headers 请求头部
     * @return 返回响应结果
     */
    public  Response get(String urlPath,HashMap<String,Object> body,Map<String,Object> headers){
        Response response = null;
        response = RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .headers(headers)
                .formParams((Map)body.get("body"))
                .when()
                .get(urlPath);
        return  response;
    }



    /**
     * 上传文件
     * @param url
     * @param params
     * @param headers
     * @param multiPart
     * @return
     */
    public Response upload(String url, HashMap<String,Object> params, Map<String,String> headers, HashMap<String,Object> multiPart){


        Response response = null;

        RequestSpecification tls = RestAssured.given().relaxedHTTPSValidation("TLS");

        if (multiPart != null) {
            Set<String> keys = multiPart.keySet();
            tls.headers(headers);
            for (String key : keys) {
                String v = (String) multiPart.get(key);
                tls.multiPart(key,new File(v));
            }
        }
        if (params != null) {
            Set set = params.keySet();
            for (Object o : set) {
                String key = String.valueOf(o);
                Object objV = params.get(key);
                String v = JSON.toJSONString(objV);
                tls.multiPart(key,v,"application/json");

            }
        }


        return tls.when().post(url);
    }

    @Override
    public Response upload(CaseRequestParam caseRequestParam) {
        HashMap<String, Object> multiPart = caseRequestParam.getMultiPart();
        Map<String, String> headers = caseRequestParam.getHeaders();
        HashMap<String, Object> params = caseRequestParam.getParam();
        String url = caseRequestParam.getUrl();
        Response response = null;

        RequestSpecification tls = RestAssured.given().relaxedHTTPSValidation("TLS");

        if (multiPart != null) {
            Set<String> keys = multiPart.keySet();
            tls.headers(headers);
            for (String key : keys) {
                String v = (String) multiPart.get(key);
                tls.multiPart(key,new File(v));
            }
        }
        if (params != null) {
            Set set = params.keySet();
            for (Object o : set) {
                String key = String.valueOf(o);
                Object objV = params.get(key);
                String v = JSON.toJSONString(objV);
                tls.multiPart(key,v,"application/json");

            }
        }

        return tls.when().post(url);
    }


}
