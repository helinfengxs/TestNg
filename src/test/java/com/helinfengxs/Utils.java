package com.helinfengxs;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.*;

public class Utils {
    @Test
    public void getYaml(){
        String name = "";
        String base_url = "";

        List<Entity> list = new ArrayList<>();

        Response response = null;
        response = RestAssured.given()
                .relaxedHTTPSValidation("TLS")
                .when()
//                .get("http://localhost:10086/v2/api-docs");
                .get("https://api-dev.deepeleph.com/food-safety/v2/api-docs");

        HashMap<String, Object> param = param(response);
        Map<Object, Object> paths = response.jsonPath().getMap("paths");
        Set<Object> keys = paths.keySet();
        for (Object key : keys) {
            Entity entity = new Entity();
            String path = String.valueOf(key);
            entity.setPath(path);

            Map typeMap = (Map) paths.get(key);
            Set typeKeys = typeMap.keySet();
            for (Object typeKey : typeKeys) {
                String method = String.valueOf(typeKey);
                entity.setMethod(method);
                Map infoMap = (Map) typeMap.get(typeKey);
                String interfaceName = String.valueOf(infoMap.get("operationId"));
                String[] usings = interfaceName.split("Using");
                interfaceName = usings[0];
                List consumes = (List)infoMap.get("consumes");
                String header = String.valueOf(consumes.get(0));
                Object parameters = infoMap.get("parameters");
                String schema = findSchema(parameters);
                if(schema!=null){
                    Object o = finadParam(param, schema);
                    entity.setParam(o);
                }


                entity.setInterfaceName(interfaceName);
                entity.setHeader(header);
            }

        list.add(entity);
        }
        for (Entity entity : list) {
            System.out.println(entity);
        }


    }
    public HashMap<String,Object> param(Response response){
        Map<Object, Object> definitions = response.jsonPath().getMap("definitions");
        HashMap<String,Object> defMap = new HashMap<>();
        Set<Object> defKeys = definitions.keySet();
        for (Object defKey : defKeys) {
            String k = String.valueOf(defKey);
            Map properties = (Map)definitions.get(defKey);
            Map proMap = (Map)properties.get("properties");
            Set proMapSet= null;
            if(proMap != null){
                proMapSet  = proMap.keySet();
                HashMap<String,Object> hashMap = new HashMap<>();
                for (Object proMapKey : proMapSet) {
                    Map v = (Map)proMap.get(proMapKey);
                    Set vKeys = v.keySet();
                    for (Object vKey : vKeys) {
                        Object o = v.get(vKey);
                        hashMap.put(String.valueOf(proMapKey),o);
                    }
                }
                defMap.put(k,hashMap);
            }
        }
        return defMap;
    }
    public String findSchema(Object parameters){
        Object value = null;
        String finalStr = null;
        if (parameters instanceof List){
            List paramList = (List) parameters;
            for (Object index : paramList) {
                if(index instanceof Map){
                    Map m = (Map)index;
                    Set set = m.keySet();
                    for (Object o : set) {
                        String k  = String.valueOf(o);
                        if("schema".equals(k)){
                            Map v = (Map) m.get(o);
                            value = v.get("$ref");
                            if(value!=null){
                                String value1 = String.valueOf(value);
                                int num = value1.lastIndexOf("/");
                                finalStr = value1.substring(num+1);

                            }


                            break;
                        }
                    }
                }
            }
        }
        return finalStr;
    }
    public Object finadParam(HashMap<String,Object> hashMap,String str){
        Object finalObj = null;
        Set<String> keys = hashMap.keySet();
        for (String key : keys) {
            if(str.equals(key)){
                finalObj = hashMap.get(key);
            }
        }
        return finalObj;
    }
}
