package com.glu.glutest.guice;

import com.glu.glutest.App;
import com.glu.glutest.dao.Profile;
import com.glu.glutest.dao.ProfileDAO;
import com.glu.glutest.dao.ProfileDAOMorphiaImpl;
import com.glu.glutest.res.PingRes;
import com.google.code.morphia.Morphia;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceFilter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class GlutestModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        bind(String.class).annotatedWith(Names.named("mongo-host")).toInstance(App.config.getString("mongo.host"));
        bind(String.class).annotatedWith(Names.named("mongo-port")).toInstance(App.config.getString("mongo.port"));
        bind(String.class).annotatedWith(Names.named("mongo-dbName")).toInstance(App.config.getString("mongo.dbName"));

        bind(PingRes.class).in(Scopes.SINGLETON);

        bind(ProfileDAO.class).to(ProfileDAOMorphiaImpl.class);

        bind(GuiceContainer.class).in(Scopes.SINGLETON);
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
        serve("/*").with(GuiceContainer.class, params);

        bind(GuiceFilter.class).in(Scopes.SINGLETON);
    }


    @Provides
    @Singleton
    Morphia provideMorphia(Mongo mongo, @Named("mongo-dbName") String dbName) {
        Morphia morphia = new Morphia();
        morphia.map(Profile.class);
        morphia.createDatastore(mongo, dbName).ensureIndexes();
        return morphia;
    }

    @Provides
    @Singleton
    Mongo provideMongo(@Named("mongo-host") String host, @Named("mongo-port") int port) throws UnknownHostException {
        return new MongoClient(host, port);
    }

}
