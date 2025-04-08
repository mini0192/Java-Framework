package com.tomcat.framework.core;

import com.tomcat.framework.annotation.type.Controller;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.reflections.Reflections;

import java.io.File;
import java.util.Set;

public class TomcatHandler {

    private Tomcat tomcat;
    private Set<Class<?>> controllers;
    private Context ctx;

    public TomcatHandler(int port, String packageName) throws Exception {
        this.tomcat = new Tomcat();
        tomcat.setPort(port);

        Connector connector = tomcat.getConnector();
        connector.setURIEncoding("UTF-8");
        ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

        setControllers(packageName);
        setDispatcherServlet();
    }

    private void setControllers(String packageName) {
        Reflections reflections = new Reflections(packageName);
        this.controllers = reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void setDispatcherServlet() throws Exception {
        Tomcat.addServlet(ctx, "dispatcher", new DispatcherServlet(controllers));
        ctx.addServletMappingDecoded("/app/*", "dispatcher");
    }

    public void run() throws LifecycleException {
        this.tomcat.start();
        System.out.println("Tomcat started.");
        this.tomcat.getServer().await();
    }
}
