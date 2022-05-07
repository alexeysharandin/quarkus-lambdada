package com.github.alexeysharandin.quarkus.lambdada.runtime.context;

import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;
import io.vertx.mutiny.core.Vertx;

public class QuarkusVertxContext implements QuarkusContext {
    public final static String KEY = "quarkus$profiler$key";

    static final QuarkusContext INSTANCE = new QuarkusVertxContext();

    @Override
    public Tracker value() {
        return Vertx.currentContext().getLocal(KEY);
    }

    @Override
    public void value(Tracker tracker) {
        Vertx.currentContext().putLocal(KEY, tracker);
    }

    @Override
    public void clear() {
        Vertx.currentContext().removeLocal(KEY);
    }

    @Override
    public String toString() {
        return Vertx.currentContext().toString();
    }
}
