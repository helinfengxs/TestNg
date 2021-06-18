package com.helinfengxs.api;

import com.helinfengxs.util.Util;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;


public class TestNgIsOk {

    public static void main(String[] args) {
        Util.readYaml("D:/iderproject/kitchen_api/test.yaml");

        TestNG testNG = new TestNG();
        List<String> suties = new ArrayList<>();
        suties.add("test.xml");
        testNG.setTestSuites(suties);
        testNG.run();
        /**
         *         Util.readYaml(arg[0]);
         *
         *         TestNG testNG = new TestNG();
         *         List<String> suties = new ArrayList<>();
         *         suties.add(arg[1]);
         *         testNG.setTestSuites(suties);
         *         testNG.run();
         *
         *
         *
         */

    }

}
