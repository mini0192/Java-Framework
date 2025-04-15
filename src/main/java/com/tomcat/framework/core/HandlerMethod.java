package com.tomcat.framework.core;

import com.tomcat.framework.Model;
import com.tomcat.framework.annotation.param.ModelAttribute;
import com.tomcat.framework.annotation.param.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class HandlerMethod {
    private final Object controllerInstance;
    private final Method method;

    public HandlerMethod(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public String invoke(HttpServletRequest req, Map<String, String> pathVariables) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        Model model = new Model();

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if(parameter.getType() == HttpServletRequest.class) {
                args[i] = req;
                continue;
            }

            if(parameter.getType() == Model.class) {
                args[i] = model;
                continue;
            }

            if(parameter.isAnnotationPresent(ModelAttribute.class)) {
                Class<?> paramType = parameter.getType();
                Object dto = paramType.getDeclaredConstructor().newInstance();

                for (Field field : paramType.getDeclaredFields()) {
                    field.setAccessible(true);
                    String value = req.getParameter(field.getName());
                    if (value != null) {
                        field.set(dto, value);
                    }
                }
                args[i] = dto;
                continue;
            }

            if(!pathVariables.isEmpty()) {
                if (parameter.isAnnotationPresent(PathVariable.class)) {
                    PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                    String pathVariableName = pathVariable.value();

                    if(!pathVariables.containsKey(pathVariableName)) {
                        throw new IllegalArgumentException("Path variable not found: " + pathVariableName);
                    }

                    args[i] = convert(pathVariables.get(pathVariableName), parameter.getType());
                }
                continue;
            }
        }

        String view = (String) method.invoke(controllerInstance, args);

        for (Map.Entry<String, Object> entry : model.getModel().entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }

        return view;
    }


    private Object convert(String value, Class<?> targetType) {
        if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
        if (targetType == long.class || targetType == Long.class) return Long.parseLong(value);
        if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);
        return value;
    }
}
