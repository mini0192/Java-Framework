package com.tomcat;

import com.tomcat.framework.core.TomcatHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        TomcatHandler tomcatHandler = new TomcatHandler(8080, Main.class.getPackage().getName());
        tomcatHandler.run();
    }
}
