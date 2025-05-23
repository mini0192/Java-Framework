package com.tomcat.framework.core;

import com.tomcat.framework.annotation.method.DeleteMapping;
import com.tomcat.framework.annotation.method.GetMapping;
import com.tomcat.framework.annotation.method.PutMapping;
import com.tomcat.framework.annotation.method.PostMapping;
import com.tomcat.framework.annotation.type.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DispatcherServlet extends HttpServlet {

    private final Map<String, HandlerAdapter> getMappings = new HashMap<>();
    private final Map<String, HandlerAdapter> postMappings = new HashMap<>();
    private final Map<String, HandlerAdapter> putMappings = new HashMap<>();
    private final Map<String, HandlerAdapter> deleteMappings = new HashMap<>();

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
                    getMappings.put(mappingPath + path, new HandlerAdapter(instance, method));
                    continue;
                }

                if(method.isAnnotationPresent(PostMapping.class)) {
                    String path = method.getAnnotation(PostMapping.class).value();
                    postMappings.put(mappingPath + path, new HandlerAdapter(instance, method));
                    continue;
                }

                if(method.isAnnotationPresent(PutMapping.class)) {
                    String path = method.getAnnotation(PutMapping.class).value();
                    putMappings.put(mappingPath + path, new HandlerAdapter(instance, method));
                    continue;
                }

                if(method.isAnnotationPresent(DeleteMapping.class)) {
                    String path = method.getAnnotation(DeleteMapping.class).value();
                    deleteMappings.put(mappingPath + path, new HandlerAdapter(instance, method));
                    continue;
                }
            }
        }
    }

    private void doInvoke(HandlerAdapter handlerAdapter, Map<String, String> pathVariables, HttpServletRequest req, HttpServletResponse res) throws Exception {
        if(handlerAdapter == null) {
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }
        try {
            String view = handlerAdapter.invoke(req, pathVariables);
            if (view.startsWith("redirect:")) {
                String redirectUrl = view.substring("redirect:".length());
                res.sendRedirect(redirectUrl);
            } else {
                req.getRequestDispatcher("/" + view + ".jsp").forward(req, res);
            }
        } catch(Exception e) {
            e.printStackTrace();
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "실행 오류");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        String uri = req.getRequestURI();
        System.out.println("[DispatcherServlet] GET 요청: " + uri);

        Map<String, String> pathVariables = new HashMap<>();
        HandlerAdapter handlerAdapter = findHandlerMethod(uri, getMappings, pathVariables);
        try {
            doInvoke(handlerAdapter, pathVariables, req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        String uri = req.getRequestURI();
        System.out.println("[DispatcherServlet] POST 요청: " + uri);

        Map<String, String> pathVariables = new HashMap<>();
        HandlerAdapter handlerAdapter = findHandlerMethod(uri, postMappings, pathVariables);
        try {
            doInvoke(handlerAdapter, pathVariables, req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) {
        String uri = req.getRequestURI();
        System.out.println("[DispatcherServlet] PUT 요청: " + uri);

        Map<String, String> pathVariables = new HashMap<>();
        HandlerAdapter handlerAdapter = findHandlerMethod(uri, putMappings, pathVariables);
        try {
            doInvoke(handlerAdapter, pathVariables, req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        String uri = req.getRequestURI();
        System.out.println("[DispatcherServlet] DELETE 요청: " + uri);

        Map<String, String> pathVariables = new HashMap<>();
        HandlerAdapter handlerAdapter = findHandlerMethod(uri, deleteMappings, pathVariables);
        try {
            doInvoke(handlerAdapter, pathVariables, req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HandlerAdapter findHandlerMethod(String uri, Map<String, HandlerAdapter> mappings, Map<String, String> pathVariables) {
        String key = uri;
        HandlerAdapter handlerAdapter = mappings.get(key);
        if(handlerAdapter == null) {
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
                    handlerAdapter = mappings.get(mapping);
                }
            }
        }
        if(handlerAdapter == null) {
            System.out.println("[DispatcherServlet] MisMapping: " + key);
            return null;
        }
        System.out.println("[DispatcherServlet] Mapping: " + key);
        return handlerAdapter;
    }
}
