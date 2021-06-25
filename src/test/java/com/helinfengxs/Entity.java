package com.helinfengxs;

import lombok.Data;

@Data
public class Entity {

    private String path;
    private String method;
    private String description;
    private String interfaceName;
    private String header;
    private Object param;
}
