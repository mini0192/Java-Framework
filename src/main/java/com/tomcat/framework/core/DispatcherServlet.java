package com.tomcat.framework.core;

import com.tomcat.framework.annotation.method.GetMapping;
import com.tomcat.framework.annotation.method.PostMapping;
import com.tomcat.framework.annotation.type.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DispatcherServlet extends HttpServlet {
    private final Map<String, HandlerMethod> getMappings = new HashMap<>();
    private final Map<String, HandlerMethod> postMappings = new HashMap<>();

    public DispatcherServlet(Set<Class<?>> controller) throws Exception {
        for(Class<?> controllerClass : controller) {
            String mappingPath = "";

            if(controllerClass.isAnnotationPresent(RequestMapping.class)) {
                mappingPath = controllerClass.getAnnotation(RequestMapping.class).value();
            }

            Object instance = controllerClass.getDeclaredConstructor().newInstance();
            for(Method method : controllerClass.getMethods()) {
                if(method.isAnnotationPresent(GetMapping.class)) {
                    String path = method.getAnnotation(GetMapping.class).value();
                    getMappings.put(mappingPath + path, new HandlerMethod(instance, method));
                    continue;
                }

                if(method.isAnnotationPresent(PostMapping.class)) {
                    String path = method.getAnnotation(PostMapping.class).value();
                    postMappings.put(mappingPath + path, new HandlerMethod(instance, method));
                    continue;
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uri = req.getRequestURI();
        System.out.println("[DispatcherServlet] GET 요청: " + uri);

        Map<String, String> pathVariables = new HashMap<>();
        HandlerMethod handlerMethod = findHandlerMethod(uri, getMappings, pathVariables);
        if (handlerMethod == null) {
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        try {
            String view = handlerMethod.getInvoke(req, pathVariables);
            if (view.startsWith("redirect:")) {
                String redirectUrl = view.substring("redirect:".length());
                res.sendRedirect(redirectUrl);
            } else {
                req.getRequestDispatcher("/" + view + ".jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET 처리 오류");
            }
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uri = req.getRequestURI();
        System.out.println("[DispatcherServlet] POST 요청: " + uri);

        Map<String, String> pathVariables = new HashMap<>();
        HandlerMethod handlerMethod = findHandlerMethod(uri, postMappings, pathVariables);
        if(handlerMethod == null) {
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
            return;
        }

        try {
            String view = handlerMethod.postInvoke(req, pathVariables);
            if (view.startsWith("redirect:")) {
                String redirectUrl = view.substring("redirect:".length());
                res.sendRedirect(redirectUrl);
            } else {
                req.getRequestDispatcher("/" + view + ".jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "POST 처리 오류");
            }
        }
    }

    private HandlerMethod findHandlerMethod(String uri, Map<String, HandlerMethod> mappings, Map<String, String> pathVariables) {
        String key = uri;
        HandlerMethod handlerMethod = mappings.get(key);
        if(handlerMethod == null) {
            for (String mapping : mappings.keySet()) {
                String[] pathParts = mapping.split("/");
                String[] uriParts = uri.split("/");

                if (pathParts.length != uriParts.length) continue;

                Map<String, String> pathVariablesMethod = new HashMap<>();
                boolean match = true;

                for (int i = 0; i < pathParts.length; i++) {
                    if (pathParts[i].startsWith("{") && pathParts[i].endsWith("}")) {
                        String paramName = pathParts[i].substring(1, pathParts[i].length() - 1);
                        String paramValue = uriParts[i];
                        pathVariablesMethod.put(paramName, paramValue);
                        continue;
                    }

                    if (!pathParts[i].equals(uriParts[i])) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    key = mapping;
                    if(!pathVariablesMethod.isEmpty()) {
                        pathVariables.putAll(pathVariablesMethod);
                    }
                    handlerMethod = mappings.get(mapping);
                }
            }
        }
        if(handlerMethod == null) {
            System.out.println("[DispatcherServlet] MisMapping: " + key);
            return null;
        }
        System.out.println("[DispatcherServlet] Mapping: " + key);
        return handlerMethod;
    }
}
