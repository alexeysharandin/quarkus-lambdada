package com.github.alexeysharandin.quarkus.lambdada.runtime.context;

import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public interface QuarkusContext {
    static QuarkusContext get(final Context context) {
        if(context != null) {
            return new QuarkusVertxContext() {
                @Override
                protected io.vertx.mutiny.core.Context ctx() {
                    return new io.vertx.mutiny.core.Context(context) ;
                }
            };
        }
        return Vertx.currentContext() == null ?
                QuarkusThreadContext.INSTANCE : QuarkusVertxContext.INSTANCE;
    }

    static QuarkusContext get() {
        return get(null);
    }

    ProfileStackTraceElement value();
    void value(ProfileStackTraceElement profileStackTraceElement);

    void clear();
}
