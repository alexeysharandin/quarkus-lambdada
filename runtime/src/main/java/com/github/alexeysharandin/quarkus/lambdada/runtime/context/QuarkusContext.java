package com.github.alexeysharandin.quarkus.lambdada.runtime.context;

import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public interface QuarkusContext {
    static QuarkusContext get(Context context) {
        if(context == null) {
            context = Vertx.currentContext();
        }
        return context == null ? QuarkusThreadContext.INSTANCE : QuarkusVertxContext.INSTANCE;
    }

    static QuarkusContext get() {
        return get(null);
    }

    Tracker value();
    void value(Tracker tracker);

    void clear();
}
