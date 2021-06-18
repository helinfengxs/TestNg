package com.helinfengxs.util;

import com.helinfengxs.data.CaseRequestParam;
import com.helinfengxs.data.GlobalParams;
import com.helinfengxs.service.RequestFunctionInterFace;
import com.helinfengxs.service.imp.RequestFunction;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.lang.reflect.Proxy;
import java.util.*;

public class Util {
    static {
        readYaml("D:\\iderproject\\kitchen_api\\xml\\perm.yaml");
    }
    public static final Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * 解析测试数据yaml文件
     * @param yamlPath yaml路径
     */
    public static void readYaml(String yamlPath){
        Yaml yaml = new Yaml();

        try {
            FileInputStream fileInputStream = new FileInputStream(yamlPath);
            Object load = yaml.load(fileInputStream);
            List loadList = null;
            if(load instanceof List){
                loadList = (List)load;

                Map config =  (Map)loadList.get(0);
                Object configObject = config.get("config");
                if(configObject != null){
                    Map configInfo = (Map)config.get("config");
                    GlobalParams.testTile = (String)configInfo.get("name");
                    GlobalParams.varables = (HashMap<String,Object>)configInfo.get("variables");
                    GlobalParams.request = (HashMap<String,String>)configInfo.get("request");
                    for (int i = 1; i < loadList.size(); i++) {
                        Map loadMap = (Map)loadList.get(i);
                        Set set = loadMap.keySet();
                        for (Object key : set) {
                            GlobalParams.data.put(String.valueOf(key),loadMap.get(key));
                        }
                    }
                }else {
                    for (int i = 0; i < loadList.size(); i++) {
                        Map loadMap = (Map)loadList.get(i);
                        Set set = loadMap.keySet();
                        for (Object key : set) {
                            GlobalParams.data.put(String.valueOf(key),loadMap.get(key));
                        }
                    }
                }
            }else {
                throw new Exception("yaml为空或未找到yaml文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取模块数据
     * @param str 模块名称
     * @return 返回该模块下所有数据
     */
    public  static HashMap<String,Object> analyseData(String str){
        List dataList =  (List)GlobalParams.data.get(str);
        HashMap<String,Object> hashMap = new HashMap<>();
        if (dataList != null) {
            for (Object o : dataList) {
                Map dataMap = (Map) o;
                Set set = dataMap.keySet();
                for (Object s : set) {
                    hashMap.put(String.valueOf(s),dataMap.get(s));
                }
            }
        }

        return hashMap;
    }

    /**
     * 解析单个用例数据
     * @param loginData 模块用例数据集
     * @param caseName 用例方法名称
     * @return 返回单个用例信息实体类
     */
    public static CaseRequestParam getCaseData(HashMap<String,Object> loginData,String caseName){
        CaseRequestParam params = new CaseRequestParam();

        Map loginCorrect = (Map)loginData.get(caseName);
        Map request = (Map)loginCorrect.get("request");
        Object nameObject = loginCorrect.get("name");

        if(nameObject != null){
            String name = String.valueOf(nameObject);
            if(checkData(name)){
                Object result = queryVarables(name);
                params.setCaseName(String.valueOf(result));
            }
            else {
                params.setCaseName(name);

            }
        }
        Object urlObject = request.get("url");
        if(urlObject != null){
            String url = String.valueOf(urlObject);
            if(checkData(url)){
                Object result = queryVarables(url);
                params.setUrl(String.valueOf(result));
            }
            else {
                params.setUrl(url);
            }
        }
        Object methodObject = request.get("method");
        if(methodObject != null){
            String method = String.valueOf(methodObject).toLowerCase();


            if(checkData(method)){
                Object result = queryVarables(method);
                params.setMethod(String.valueOf(result));
            }
            else{
                params.setMethod(method);

            }
        }



       Object headerObject = request.get("header");

       if(headerObject !=null){
           Map<String,String> headerMap = new HashMap<>();
           Map headers = (Map)headerObject;
           Set headersKey = headers.keySet();
           for (Object key : headersKey) {
               String result = String.valueOf(headers.get(key));
               if(checkData(result)){
                   Object o = queryVarables(result);
                   result = String.valueOf(o);
               }
               headerMap.put(String.valueOf(key),result);
           }
           params.setHeaders(headerMap);
       }




        if(request.get("json") != null && !"{}".equals(request.get("json"))){
            HashMap<String,Object> paramMap = new HashMap<>();
            Map jsonMap = (Map)request.get("json");
            Set jsonMapKeys = jsonMap.keySet();
            for (Object key : jsonMapKeys) {
                String result = String.valueOf(jsonMap.get(key));
                if(checkData(result)){
                    Object o = queryVarables(result);
                    jsonMap.put(key,o);
                };
            }
            paramMap.put("json",jsonMap);
            params.setParam(paramMap);
        }else if(request.get("body") != null && !"{}".equals(request.get("body"))){
            HashMap<String,Object> paramMap = new HashMap<>();
            Map bodyMap = (Map)request.get("body");
            Set bodyMapkeys = bodyMap.keySet();
            for (Object key : bodyMapkeys) {
                String result = String.valueOf(bodyMap.get(key));
                if(checkData(result)){
                    Object o = queryVarables(result);
                    bodyMap.put(key,o);
                };
            }
            paramMap.put("body",bodyMap);
            params.setParam(paramMap);
        }
        else {
            HashMap<String,Object> paramMap = new HashMap<>();
            Map otherMap = (Map)request.get("other");
            Set otherMapkeys = otherMap.keySet();
            for (Object otherMapkey : otherMapkeys) {
               String key = String.valueOf(otherMapkey);
               Map obj = (Map) otherMap.get(key);
               Set objKeys = obj.keySet();
               for (Object objKey : objKeys) {
                   String k = String.valueOf(objKey);
                   String v = String.valueOf(obj.get(k));
                   if(checkData(v)){
                       Object o = queryVarables(v);
                       obj.put(k,o);
                   }
               }
               paramMap.put(key,obj);

            }
            params.setParam(paramMap);
        }



        Object multiPartObj = request.get("multiPart");

        if (multiPartObj != null) {
            HashMap<String,Object> map = new HashMap<>();

            Map multiPart = (Map) multiPartObj;
            Set keys = multiPart.keySet();
            for (Object key : keys) {
                String mapKey =String.valueOf(key);
                String value = String.valueOf(multiPart.get(mapKey));
                if(checkData(value)){
                    value = String.valueOf(queryVarables(value));
                }
                map.put(mapKey,value);
            }
            params.setMultiPart(map);
        }

        Object extractObject = request.get("extract");
        if(extractObject != null){
            ArrayList<String> extractList = new ArrayList<>();
            List extract = (List) extractObject;
            for (Object ex : extract) {
                extractList.add(String.valueOf(ex));
            }
            params.setExtract(extractList);
        }




        Object validateObject = request.get("validate");
        if (validateObject != null){
            List<HashMap<String,List<Object>>> validateList = new ArrayList<>();
            List  validate = (List) validateObject;
            for (Object val : validate) {
                if (val != null){
                    Map valMap = (Map)val;
                    Set valMapKey = valMap.keySet();
                    HashMap<String,List<Object>> hashMap = new HashMap<>();
                    for (Object key : valMapKey) {
                        hashMap.put(String.valueOf(key),(List<Object>) valMap.get(key));
                    }
                    validateList.add(hashMap);
                }



            }
            params.setValidate(validateList);
        }



        Object setup_hooksObject = request.get("setup_hooks");
        if(setup_hooksObject != null){
            List<String> setup_hooksList = new ArrayList<>();
            List setup_hooks =(List) setup_hooksObject;

            for (Object setup_hook : setup_hooks) {
                setup_hooksList.add(String.valueOf(setup_hook));
            }
            params.setSetup_hooks(setup_hooksList);
        }



        Object teardown_hooksObject = request.get("teardown_hooks");
        if(teardown_hooksObject != null){
            List<String> teardown_hooksList = new ArrayList<>();
            List teardown_hooks =(List) request.get("teardown_hooks");

            for (Object teardown_hook : teardown_hooks) {
                teardown_hooksList.add(String.valueOf(teardown_hook));
            }
            params.setTeardown_hooks(teardown_hooksList);
        }



        return params;
    }

    /**
     * 解析单个用例数据，封装成用例数据对象
     * @param obj 待解析的Object 用例
     * @return CaseRequestParam
     */
    public static CaseRequestParam getCaseData(Object obj){
        CaseRequestParam params = new CaseRequestParam();

        Map loginCorrect = (Map)obj;
        Map request = (Map)loginCorrect.get("request");
        Object nameObject = loginCorrect.get("name");

        if(nameObject != null){
            String name = String.valueOf(nameObject);
            if(checkData(name)){
                Object result = queryVarables(name);
                params.setCaseName(String.valueOf(result));
            }
            else {
                params.setCaseName(name);

            }
        }
        Object urlObject = request.get("url");
        if(urlObject != null){
            String url = String.valueOf(urlObject);
            if(checkData(url)){
                Object result = queryVarables(url);
                params.setUrl(String.valueOf(result));
            }
            else {
                params.setUrl(url);
            }
        }
        Object methodObject = request.get("method");
        if(methodObject != null){
            String method = String.valueOf(methodObject).toLowerCase();


            if(checkData(method)){
                Object result = queryVarables(method);
                params.setMethod(String.valueOf(result));
            }
            else{
                params.setMethod(method);

            }
        }



        Object headerObject = request.get("header");

        if(headerObject !=null){
            Map<String,String> headerMap = new HashMap<>();
            Map headers = (Map)headerObject;
            Set headersKey = headers.keySet();
            for (Object key : headersKey) {
                String result = String.valueOf(headers.get(key));
                if(checkData(result)){
                    Object o = queryVarables(result);
                    result = String.valueOf(o);
                }
                headerMap.put(String.valueOf(key),result);
            }
            params.setHeaders(headerMap);
        }




        if(request.get("json") != null && !"{}".equals(request.get("json"))){
            HashMap<String,Object> paramMap = new HashMap<>();
            Map jsonMap = (Map)request.get("json");
            Set jsonMapKeys = jsonMap.keySet();
            for (Object key : jsonMapKeys) {
                String result = String.valueOf(jsonMap.get(key));
                if(checkData(result)){
                    Object o = queryVarables(result);
                    jsonMap.put(key,o);
                };
            }
            paramMap.put("json",jsonMap);
            params.setParam(paramMap);
        }else if(request.get("body") != null && !"{}".equals(request.get("body"))){
            HashMap<String,Object> paramMap = new HashMap<>();
            Map bodyMap = (Map)request.get("body");
            Set bodyMapkeys = bodyMap.keySet();
            for (Object key : bodyMapkeys) {
                String result = String.valueOf(bodyMap.get(key));
                if(checkData(result)){
                    Object o = queryVarables(result);
                    bodyMap.put(key,o);
                };
            }
            paramMap.put("body",bodyMap);
            params.setParam(paramMap);
        }else {
            HashMap<String,Object> paramMap = new HashMap<>();
            Map otherMap = (Map)request.get("other");
            Set otherMapkeys = otherMap.keySet();
            for (Object otherMapkey : otherMapkeys) {
                String key = String.valueOf(otherMapkey);
                Map objMap = (Map) otherMap.get(key);
                Set objKeys = objMap.keySet();
                for (Object objKey : objKeys) {
                    String k = String.valueOf(objKey);
                    String v = String.valueOf(objMap.get(k));
                    if(checkData(v)){
                        Object o = queryVarables(v);
                        objMap.put(k,o);
                    }
                }
                paramMap.put(key,obj);

            }
            params.setParam(paramMap);
        }
        Object multiPartObj = request.get("multiPart");

        if (multiPartObj != null) {
            HashMap<String,Object> map = new HashMap<>();

            Map multiPart = (Map) multiPartObj;
            Set keys = multiPart.keySet();
            for (Object key : keys) {
                String mapKey =String.valueOf(key);
                String value = String.valueOf(multiPart.get(mapKey));
                if(checkData(value)){
                    value = String.valueOf(queryVarables(value));
                }
                map.put(mapKey,value);
            }
            params.setMultiPart(map);
        }
        Object extractObject = request.get("extract");
        if(extractObject != null){
            ArrayList<String> extractList = new ArrayList<>();
            List extract = (List) extractObject;
            for (Object ex : extract) {
                extractList.add(String.valueOf(ex));
            }
            params.setExtract(extractList);
        }




        Object validateObject = request.get("validate");
        if (validateObject != null){
            List<HashMap<String,List<Object>>> validateList = new ArrayList<>();
            List  validate = (List) validateObject;
            for (Object val : validate) {
                if (val != null){
                    Map valMap = (Map)val;
                    Set valMapKey = valMap.keySet();
                    HashMap<String,List<Object>> hashMap = new HashMap<>();
                    for (Object key : valMapKey) {
                        hashMap.put(String.valueOf(key),(List<Object>) valMap.get(key));
                    }
                    validateList.add(hashMap);
                }



            }
            params.setValidate(validateList);
        }



        Object setup_hooksObject = request.get("setup_hooks");
        if(setup_hooksObject != null){
            List<String> setup_hooksList = new ArrayList<>();
            List setup_hooks =(List) setup_hooksObject;

            for (Object setup_hook : setup_hooks) {
                setup_hooksList.add(String.valueOf(setup_hook));
            }
            params.setSetup_hooks(setup_hooksList);
        }



        Object teardown_hooksObject = request.get("teardown_hooks");
        if(teardown_hooksObject != null){
            List<String> teardown_hooksList = new ArrayList<>();
            List teardown_hooks =(List) request.get("teardown_hooks");

            for (Object teardown_hook : teardown_hooks) {
                teardown_hooksList.add(String.valueOf(teardown_hook));
            }
            params.setTeardown_hooks(teardown_hooksList);
        }



        return params;
    }

    /**
     * 检测字符串是否包含$符号
     * @param str 字符串
     * @return 返回true & false
     */
    public static Boolean checkData(String str){
        if("".equals(str) || str == null){
            return false;
        }
        return str.charAt(0)=='$';
    }

    /**
     * 全局列表参数查询
     * @param str 需要查询的字符
     * @return 查询后的结果
     */
    public static Object queryVarables(String str){
        Object finalData = null;
        Set<String> varablesKey = GlobalParams.varables.keySet();
        for (String key : varablesKey) {
            if (str.contains(key)){
                finalData = GlobalParams.varables.get(key);
            }
        }
        return finalData;
    }

    /**
     * 发送http请求
     * @param caseRequestParam 单个用例实体类对象
     * @return 返回发送请求后响应结果
     */
    public static Response sendRequest(CaseRequestParam caseRequestParam){

        Response response = null;
        RequestFunctionInterFace re = new RequestFunction();
        ProxyHandler proxyHandler = new ProxyHandler(re);
        RequestFunctionInterFace proxy = (RequestFunctionInterFace) Proxy.newProxyInstance(re.getClass().getClassLoader(), re.getClass().getInterfaces(),proxyHandler);

        switch (caseRequestParam.getMethod()){
            case "post":
                response = proxy.post(caseRequestParam);
                break;
            case "get":
                response = proxy.get(caseRequestParam);
                break;
            case "upload":
                response = proxy.upload(caseRequestParam);
                break;
        }


        /**
         *         Class<RequestFunction> requestFunctionClass = RequestFunction.class;
         *         Method declaredMethod = null;
         *         Object invoke = null;
         *         try {
         *             RequestFunction requestFunction = requestFunctionClass.newInstance();
         *
         *             if( caseRequestParam.getUrl()!=null ){
         *                 if(caseRequestParam.getHeaders() != null  ){
         *                     if(caseRequestParam.getParam() != null){
         *                         if(caseRequestParam.getMultiPart() != null){
         *                             declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),String.class,HashMap.class,Map.class,HashMap.class);
         *                             invoke = declaredMethod.invoke(requestFunction, caseRequestParam.getUrl(),caseRequestParam.getParam(),caseRequestParam.getHeaders(),caseRequestParam.getMultiPart());
         *                         }else {
         *                             //请求包含 路径，参数，头部
         *                             declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),String.class,HashMap.class,Map.class);
         *                             invoke = declaredMethod.invoke(requestFunction, caseRequestParam.getUrl(),caseRequestParam.getParam(),caseRequestParam.getHeaders());
         *                         }
         *
         *
         *
         *                     }else {
         *                         //请求包含 路径,头部，无参数
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),String.class, Map.class);
         *                         invoke = declaredMethod.invoke(requestFunction, caseRequestParam.getUrl(),caseRequestParam.getHeaders());
         *                     }
         *                 }else {
         *                     if(caseRequestParam.getParam() != null){
         *                         //请求包含路径 参数，无头部
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),String.class,HashMap.class);
         *                         invoke = declaredMethod.invoke(requestFunction, caseRequestParam.getUrl(),caseRequestParam.getParam());
         *                     }else {
         *                         //请求包含路径
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),String.class);
         *                         invoke = declaredMethod.invoke(requestFunction, caseRequestParam.getUrl());
         *
         *                     }
         *                 }
         *             }else {
         *                 if(caseRequestParam.getHeaders() != null  ){
         *                     if(caseRequestParam.getParam() != null){
         *                         //无请求路径，带参数和头部
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),HashMap.class,Map.class);
         *                         invoke = declaredMethod.invoke(requestFunction,caseRequestParam.getParam(),caseRequestParam.getHeaders());
         *
         *                     }else {
         *                         //无请求路径和参数，带请求头部
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),Map.class);
         *                         invoke = declaredMethod.invoke(requestFunction,caseRequestParam.getHeaders());
         *
         *                     }
         *                 }else {
         *                     if(caseRequestParam.getParam() != null){
         *                         //无请求路径和头部，带参数
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod(),HashMap.class);
         *                         invoke = declaredMethod.invoke(requestFunction, caseRequestParam.getParam());
         *
         *                     }else {
         *                         //无请求路径、头部和参数
         *                         declaredMethod = requestFunctionClass.getDeclaredMethod(caseRequestParam.getMethod());
         *                         invoke = declaredMethod.invoke(requestFunction);
         *                     }
         *                 }
         *             }
         *         }catch (Exception e){
         *             e.printStackTrace();
         *         }
         *         if(invoke != null){
         *             response = (Response) invoke;
         *
         *         }else {
         *             throw  new RuntimeException("发送请求失败");
         *         }
         */

        return response;
    }

    /**
     * 响应结果断言
     * @param response 响应对象
     * @param caseRequestParam 单个用例实体类
     */
    public static void testNgAssert(Response response,CaseRequestParam caseRequestParam){
        int failCount = 0;
        String result = "请求地址:【"+caseRequestParam.getUrl()+"】" +
                " "+"  请求方式："+caseRequestParam.getMethod()+" 参数:"+caseRequestParam.getParam()+
                "  期望结果："+caseRequestParam.getValidate()+
                "  实际结果："+response.asString()+" 测试结果：";
        SoftAssert sa = new SoftAssert();
        if(response.statusCode() != 200){
            logger.warn(result+"失败");
        }
        Assert.assertEquals(response.statusCode(), 200);

        for (HashMap<String, List<Object>> stringListHashMap :caseRequestParam.getValidate()) {
            Set<String> stringListHashMapKeys = stringListHashMap.keySet();
            for (String stringListHashMapKey : stringListHashMapKeys) {
                if("eq".equals(stringListHashMapKey)){
                    Object key = stringListHashMap.get(stringListHashMapKey).get(0);
                    String value = String.valueOf(stringListHashMap.get(stringListHashMapKey).get(1));
                    String expect = response.getBody().jsonPath().getString(String.valueOf(key));
                    sa.assertEquals(expect,value);
                    if(!value.equals(expect)){
                        failCount++;
                    }
                }else {
                    Object key = stringListHashMap.get(stringListHashMapKey).get(0);
                    String value = String.valueOf(stringListHashMap.get(stringListHashMapKey).get(1));
                    String expect = response.getBody().jsonPath().getString(String.valueOf(key));

                    sa.assertNotEquals(expect,value);
                    if(value.equals(expect)){
                        failCount++;
                    }
                }
            }
        }
    if(failCount>0){
        logger.warn(result+"失败");
        TestStep.assertReult("用例测试失败");
    }else {
        TestStep.assertReult("用例测试通过");
        logger.info(result+"通过");
    }
    sa.assertAll();
    }

    /**
     * 提取用例执行返回的参数，存放再全局列表
     * @param response 响应结果对象
     * @param extractList 待提取参数列表
     */
    public static void getExtract(Response response,ArrayList<String> extractList){
        if(extractList != null){
            for (String key : extractList) {
                Object value = response.getBody().jsonPath().get(key);
                GlobalParams.varables.put(key,value);
            }
        }

    }

    /**
     * 用于构造二维数组
     * @param str 模块用例数据
     * @return 二维数组
     */
    public static Object[][] getArray(List str) {


        Object[][] arry = new Object[str.size()][];
        for (int i = 0; i < str.size(); i++) {
            arry[i] = new Object[]{str.get(i)};
        }

        return arry;
    }

}
