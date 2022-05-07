package com.github.alexeysharandin.quarkus.lambdada.runtime.vertx;

import com.github.alexeysharandin.quarkus.lambdada.runtime.EntryMetadata;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfilerRuntimeRecorder;
import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;
import com.github.alexeysharandin.quarkus.lambdada.runtime.context.QuarkusVertxContext;
import io.vertx.core.Context;
import io.vertx.core.spi.tracing.SpanKind;
import io.vertx.core.spi.tracing.TagExtractor;
import io.vertx.core.spi.tracing.VertxTracer;
import io.vertx.core.tracing.TracingPolicy;
import org.jboss.logging.Logger;

import java.util.Map;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class VertxConsoleTracer implements VertxTracer<Tracker, Object> {
    private static final Logger LOGGER = Logger.getLogger(VertxConsoleTracer.class);

    @Override
    public <R> Tracker receiveRequest(Context context,
                                      SpanKind kind,
                                      TracingPolicy policy,
                                      R request,
                                      String operation,
                                      Iterable<Map.Entry<String, String>> headers,
                                      TagExtractor<R> tagExtractor) {
        LOGGER.debug("Enter request");
        Tracker enter = ProfilerRuntimeRecorder
                .enter(
                        context,
                        EntryMetadata.from("VertxConsoleTracer", "receiveRequest"),
                        this
                );
        return enter;
    }

    @Override
    public <R> void sendResponse(Context context, R response, Tracker payload, Throwable failure, TagExtractor<R> tagExtractor) {
        LOGGER.debug("Exit request");
        ProfilerRuntimeRecorder.exit(payload, this);
    }
}