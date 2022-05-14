package com.github.alexeysharandin.quarkus.lambdada.runtime;

import com.github.alexeysharandin.quarkus.lambdada.io.StackTraceWriter;
import com.github.alexeysharandin.quarkus.lambdada.runtime.context.QuarkusContext;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.vertx.core.Context;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class ProfilerRuntimeRecorder {
    private static final Logger LOGGER = Logger.getLogger(ProfilerRuntimeRecorder.class);

    private final List<StackTraceWriter> stackTraceWriters;
    private final List<ProfilerPlugin> profilerPlugins;

    public ProfilerRuntimeRecorder(List<StackTraceWriter> stackTraceWriters, List<ProfilerPlugin> profilerPlugins) {
        this.stackTraceWriters = stackTraceWriters;
        this.profilerPlugins = profilerPlugins;
    }

    public static <I, O> ProfileStackTraceElement enter(EntryMetadata metadata, Object source) {
        return enter(null, metadata, source);
    }

    public static ProfileStackTraceElement enter(Context context, EntryMetadata metadata, Object source) {
        return recorder().enterMethod(context, metadata, source);
    }

    private ProfileStackTraceElement enterMethod(Context context, EntryMetadata metadata, Object source) {
        QuarkusContext holder = QuarkusContext.get(context);
        LOGGER.debug("enterMethod:ctx = " + holder.toString() + " className = " + metadata.className() + ", methodName = " + metadata.methodName() + ", source = " + source);

        ProfileStackTraceElement current = holder.value();
        ProfileStackTraceElement result = new ProfileStackTraceElement(metadata);
        if (current == null) {
            UUID uuid = result.newUUID();
            LOGGER.debug("Parent node is not exist. Generate new UUID: " + uuid);
        } else {
            LOGGER.debug("Parent node exist. Use UUID: " + current.uuid());
            result.uuid(current.uuid());
            result.parent(current);
        }
        holder.value(result);
        applyPlugins(result, source, true);
        //TODO apply plugin
        return result;
    }

    private void exitMethod(ProfileStackTraceElement profileStackTraceElement, Object source) {
        LOGGER.debug("exitMethod UUID: " + profileStackTraceElement.uuid() + " tracker = " + profileStackTraceElement.className() + ":" + profileStackTraceElement.methodName() + ", source = " + source);
        profileStackTraceElement.stop();
        QuarkusContext holder = QuarkusContext.get();
        ProfileStackTraceElement parent = profileStackTraceElement.parent();
        applyPlugins(profileStackTraceElement, source, false);

        if (parent == null) {
            LOGGER.debug("Parent is null for '"+ profileStackTraceElement +"'. Dump");
            holder.clear();
            applyDumpers(profileStackTraceElement);
        } else {
            holder.value(parent);
        }
    }

    private void applyDumpers(ProfileStackTraceElement profileStackTraceElement) {
        LOGGER.debug("Trace finished. Apply dumpers.");
        for (StackTraceWriter d : stackTraceWriters) {
            LOGGER.debug("Apply dumper: " + d.getClass().getSimpleName());
            d.write(profileStackTraceElement);
        }
    }

    private void applyPlugins(ProfileStackTraceElement profileStackTraceElement, Object source, boolean start) {
        LOGGER.debug("apply " + (start ? "start" : "end") + " plugins for " + profileStackTraceElement.uuid());
        List<ProfilerPlugin> plugins = findPlugins(profileStackTraceElement);
        for (ProfilerPlugin p : plugins) {
            if (start) {
                p.applyStart(profileStackTraceElement, source);
            } else {
                p.applyEnd(profileStackTraceElement, source);
            }
        }
    }

    private List<ProfilerPlugin> findPlugins(ProfileStackTraceElement profileStackTraceElement) {
        //TODO not implemented
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    public static void exit(ProfileStackTraceElement profileStackTraceElement, Object source) {
        recorder().exitMethod(profileStackTraceElement, source);
    }

    private static ProfilerRuntimeRecorder recorder() {
        try(InstanceHandle<ProfilerRuntimeRecorder> instance = Arc.container().instance(ProfilerRuntimeRecorder.class)) {
            ProfilerRuntimeRecorder rec = instance.get();
            if (rec == null) {
                throw new RuntimeException("Unable to find ProfilerRuntimeRecorder bean");
            }
            return rec;
        }
    }
}
