package com.github.alexeysharandin.quarkus.lambdada.runtime;

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

    private final List<Dumper> dumpers;
    private final List<ProfilerPlugin> profilerPlugins;

    public ProfilerRuntimeRecorder(List<Dumper> dumpers, List<ProfilerPlugin> profilerPlugins) {
        this.dumpers = dumpers;
        this.profilerPlugins = profilerPlugins;
    }

    public static <I, O> Tracker enter(EntryMetadata metadata, Object source) {
        return enter(null, metadata, source);
    }

    public static Tracker enter(Context context, EntryMetadata metadata, Object source) {
        return recorder().enterMethod(context, metadata, source);
    }

    private Tracker enterMethod(Context context, EntryMetadata metadata, Object source) {
        QuarkusContext holder = QuarkusContext.get(context);
        LOGGER.debug("enterMethod:ctx = " + holder.toString() + " className = " + metadata.className() + ", methodName = " + metadata.methodName() + ", source = " + source);

        Tracker current = holder.value();
        Tracker result = new Tracker(metadata);
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

    private void exitMethod(Tracker tracker, Object source) {
        LOGGER.debug("exitMethod UUID: " + tracker.uuid() + " tracker = " + tracker.className() + ":" + tracker.methodName() + ", source = " + source);
        tracker.stop();
        QuarkusContext holder = QuarkusContext.get();
        Tracker parent = tracker.parent();
        applyPlugins(tracker, source, false);

        if (parent == null) {
            LOGGER.debug("Parent is null for '"+tracker+"'. Dump");
            holder.clear();
            applyDumpers(tracker);
        } else {
            holder.value(parent);
        }
    }

    private void applyDumpers(Tracker tracker) {
        LOGGER.debug("Trace finished. Apply dumpers.");
        for (Dumper d : dumpers) {
            LOGGER.debug("Apply dumper: " + d.getClass().getSimpleName());
            d.dump(tracker);
        }
    }

    private void applyPlugins(Tracker tracker, Object source, boolean start) {
        LOGGER.debug("apply " + (start ? "start" : "end") + " plugins for " + tracker.uuid());
        List<ProfilerPlugin> plugins = findPlugins(tracker);
        for (ProfilerPlugin p : plugins) {
            if (start) {
                p.applyStart(tracker, source);
            } else {
                p.applyEnd(tracker, source);
            }
        }
    }

    private List<ProfilerPlugin> findPlugins(Tracker tracker) {
        //TODO not implemented
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    public static void exit(Tracker tracker, Object source) {
        recorder().exitMethod(tracker, source);
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
