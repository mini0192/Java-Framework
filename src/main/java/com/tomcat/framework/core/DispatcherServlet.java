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
        System.out.println("[DispatcherServlet] GET 요청 URI: " + uri);

        HandlerMethod handlerMethod = getMappings.get(uri);
        if (handlerMethod == null) {
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        try {
            String view = handlerMethod.getInvoke(req);
            req.getRequestDispatcher("/" + view + ".jsp").forward(req, res);
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
        System.out.println("[DispatcherServlet] POST 요청 URI: " + uri);
        HandlerMethod handlerMethod = postMappings.get(uri);
        if(handlerMethod == null) {
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
            return;
        }

        try {
            String view = handlerMethod.postInvoke(req);
            req.getRequestDispatcher("/" + view + ".jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            if (!res.isCommitted()) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "POST 처리 오류");
            }
        }
    }
}
