package com.github.alexeysharandin.quarkus.lambdada.runtime.vertx;

import com.github.alexeysharandin.quarkus.lambdada.runtime.EntryMetadata;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfilerRuntimeRecorder;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;
import io.vertx.core.Context;
import io.vertx.core.spi.tracing.SpanKind;
import io.vertx.core.spi.tracing.TagExtractor;
import io.vertx.core.spi.tracing.VertxTracer;
import io.vertx.core.tracing.TracingPolicy;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import java.util.Map;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class VertxConsoleTracer implements VertxTracer<ProfileStackTraceElement, Object> {
    private static final Logger LOGGER = Logger.getLogger(VertxConsoleTracer.class);



    String path;

    @Override
    public <R> ProfileStackTraceElement receiveRequest(Context context,
                                                       SpanKind kind,
                                                       TracingPolicy policy,
                                                       R request,
                                                       String operation,
                                                       Iterable<Map.Entry<String, String>> headers,
                                                       TagExtractor<R> tagExtractor) {
        LOGGER.info("Enter request: " + context);
        /*if(request instanceof HttpServerRequest) {
            String uri = ((HttpServerRequest)request).uri();
            if(isConsole(uri)) {
                return null;
            }
        }*/
        EntryMetadata from = EntryMetadata.from("VertxConsoleTracer", "receiveRequest");
        fillMeta(tagExtractor.extract(request), from);
        ProfileStackTraceElement enter = ProfilerRuntimeRecorder
                .enter(
                        context,
                        from,
                        this
                );
        return enter;
    }

    private boolean isConsole(String uri) {
        if(path==null) {
            Config config = ConfigProvider.getConfig();
            path = config.getValue("quarkus.http.root-path", String.class) + config.getValue("quarkus.http.non-application-root-path",String.class);
        }
        return uri != null && uri.startsWith(path);
    }

    @Override
    public <R> void sendResponse(Context context, R response, ProfileStackTraceElement payload, Throwable failure, TagExtractor<R> tagExtractor) {
        if(payload != null) {
            LOGGER.debug("Exit request");
            ProfilerRuntimeRecorder.exit(payload, this);
        }
    }

    private void fillMeta(Map<String, String> map, EntryMetadata meta) {
        meta.addMetaData(map);
    }
}