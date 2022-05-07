package com.github.alexeysharandin.quarkus.lambdada.runtime.vertx;

import io.vertx.core.VertxOptions;
import io.vertx.core.tracing.TracingOptions;
import org.jboss.logging.Logger;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class VertxOptionsConsumer implements Consumer<VertxOptions> {
    private static final Logger LOGGER = Logger.getLogger(VertxOptionsConsumer.class);

    @Override
    public void accept(VertxOptions vertxOptions) {
        applyTracingOptions(vertxOptions);
    }

    private static void applyTracingOptions(VertxOptions vertxOptions) {
        TracingOptions tracingOptions = vertxOptions.getTracingOptions();
        if (tracingOptions != null && tracingOptions.getFactory() != null) {
            LOGGER.warn("Tracing options and factory exist: " + tracingOptions);
            LOGGER.warn("Need to do something with it.");
        } else {
            LOGGER.debug("Tracing options not exist. Creating new");
            tracingOptions = new TracingOptions();
            tracingOptions.setFactory(options -> new VertxConsoleTracer());
        }
        tracingOptions.setFactory(options -> new VertxConsoleTracer());
        vertxOptions.setTracingOptions(tracingOptions);
    }
}
