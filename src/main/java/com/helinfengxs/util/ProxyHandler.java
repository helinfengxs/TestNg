package com.helinfengxs.util;

import com.alibaba.fastjson.JSON;
import com.helinfengxs.data.CaseRequestParam;
import io.restassured.response.Response;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

@Data
public class ProxyHandler  implements InvocationHandler {
    private Object object;

    public ProxyHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object arg = args[0];
        if(arg instanceof CaseRequestParam){
            CaseRequestParam caseInfo = (CaseRequestParam) arg;
            String url = caseInfo.getUrl();
            String meth = caseInfo.getMethod();
            List<HashMap<String, List<Object>>> validate = caseInfo.getValidate();
            StringBuffer validateStr = new StringBuffer();
            for (HashMap<String, List<Object>> stringListHashMap : validate) {
                validateStr.append(stringListHashMap.toString());
            }
            HashMap<String, Object> param = caseInfo.getParam();
            Map<String, String> headers = caseInfo.getHeaders();
            Set<String> keys = param.keySet();
            String paramStr = null;
            for (String key : keys) {
                Map map = (Map) param.get(key);
                paramStr = JSON.toJSONString(map);
            }

            String headerStr = JSON.toJSONString(headers);
            TestStep.requestBody(url,meth,headerStr,paramStr,validateStr);
        }
        Object invoke = method.invoke(object, args);
        if(invoke instanceof Response){
            Response response = (Response)invoke;
            TestStep.responseResutl(response.asString());
        }
        return method.invoke(object, args);
    }
}
