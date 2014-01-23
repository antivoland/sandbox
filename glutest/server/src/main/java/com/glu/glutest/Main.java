package com.glu.glutest;

import com.glu.glutest.guice.GlutestModule;
import com.google.inject.Guice;
import com.google.inject.servlet.GuiceFilter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Main {
    public static void main(String[] args) throws Exception {
        App.config = new PropertiesConfiguration("conf/server.properties");
        App.injector = Guice.createInjector(new GlutestModule());

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        Server server = new Server(8181);
        server.setHandler(handler);
        server.start();
        server.join();
    }
}
