package com.tomcat.framework;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, Object> model;

    public Model() {
        model = new HashMap<>();
    }

    public Map<String, Object> getModel() {
        return model;
    }
    public void append(String key, Object value) {
        model.put(key, value);
    }
}
