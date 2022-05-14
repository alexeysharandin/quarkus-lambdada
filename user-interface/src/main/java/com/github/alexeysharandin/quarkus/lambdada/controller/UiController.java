package com.github.alexeysharandin.quarkus.lambdada.controller;

import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.function.Consumer;

@ApplicationScoped
public class UiController {

    @ConfigProperty(name = "quarkus.http.root-path")
    String root;
    @ConfigProperty(name = "quarkus.http.non-application-root-path")
    String qPath;

    void init(@Observes Router router) {

        String path = root + qPath + "/" + "lamdbada";
        System.out.println("Building router: " + path);
        router.route(path).handler(handle());
    }

    private Consumer<RoutingContext> handle() {
        return rc -> rc.response().endAndForget("Hello, World!");
    }
}
