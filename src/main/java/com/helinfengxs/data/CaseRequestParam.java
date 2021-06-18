package com.helinfengxs.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CaseRequestParam {
    private String caseName;
    private String url;
    private String method;
    private Map<String,String> headers;
    private HashMap<String,Object> param;
    private HashMap<String,Object> multiPart;
    private ArrayList<String> extract;
    private List<HashMap<String,List<Object>>> validate ;
    private List<String> setup_hooks;
    private List<String> teardown_hooks ;

}
