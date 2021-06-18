package com.helinfengxs.service;

import com.helinfengxs.data.CaseRequestParam;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public interface RequestFunctionInterFace {
    Response post(String url, HashMap<String,Object> body, Map<String,Object> headers);
    Response post(String url, HashMap<String,Object> body);
    Response post(String url,Map<String,Object> headers);
    Response post(String url);
    Response get(String url, HashMap<String,Object> body, Map<String,Object> headers);
    Response get(String url, HashMap<String,Object> body);
    Response get(String url,Map<String,Object> headers);
    Response get(String url);
    Response post(CaseRequestParam caseRequestParam);
    Response get(CaseRequestParam caseRequestParam);
    Response upload(String url, HashMap<String,Object> params, Map<String,String> headers, HashMap<String,Object> multiPart);
    Response upload(CaseRequestParam caseRequestParam);
}
