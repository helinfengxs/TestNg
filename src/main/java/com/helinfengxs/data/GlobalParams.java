package com.helinfengxs.data;


import lombok.Data;
import java.util.HashMap;


@Data
public class GlobalParams {
    //测试脚本标题
    public static String testTile;
    //测试脚本全局变量参数
    public static HashMap<String,Object> varables;
    //测试脚本公共请求信息，包含请求url，请求头部
    public static HashMap<String,String> request;
    //所有测试用例数据
    public static HashMap<String,Object> data = new HashMap<>();


}
