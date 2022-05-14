package com.github.alexeysharandin.quarkus.lambdada.runtime.context;

import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;
import io.vertx.mutiny.core.Context;
import io.vertx.mutiny.core.Vertx;

public class QuarkusVertxContext implements QuarkusContext {
    public final static String KEY = "quarkus$profiler$key";

    static final QuarkusContext INSTANCE = new QuarkusVertxContext();

    @Override
    public ProfileStackTraceElement value() {
        return ctx().getLocal(KEY);
    }

    protected Context ctx() {
        return Vertx.currentContext();
    }

    @Override
    public void value(ProfileStackTraceElement profileStackTraceElement) {
        ctx().putLocal(KEY, profileStackTraceElement);
    }

    @Override
    public void clear() {
        ctx().removeLocal(KEY);
    }

    @Override
    public String toString() {
        return String.valueOf(ctx());
    }
}
