package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import com.github.alexeysharandin.quarkus.lambdada.runtime.EntryMetadata;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfilerRuntimeRecorder;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;
import org.jboss.logging.Logger;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class LambdaTracker {
    private static final Logger LOGGER = Logger.getLogger(LambdaTracker.class);

    public static void trackBefore(Trackable supplier) {
        Object delegate = supplier.delegate();

        SerializedLambda lambda = getSerializedLambda(delegate);
        EntryMetadata metadata;

        if (lambda == null) {
            metadata = EntryMetadata.from(delegate);
        } else {
            metadata = EntryMetadata.from(lambda);
        }

        ProfileStackTraceElement profileStackTraceElement = ProfilerRuntimeRecorder.enter(metadata, delegate);
        supplier.tracker(profileStackTraceElement);
    }
    public static <T> void trackAfter(Trackable supplier) {
        ProfilerRuntimeRecorder.exit(supplier.tracker(), supplier.delegate());
    }

    public static SerializedLambda getSerializedLambda(Object function) {
        if (!(function instanceof Serializable)) {
            LOGGER.info("Object is not Serializable. Return");
            return null;
        }
        LOGGER.info("Object Serializable. Continue");
        for (Class<?> clazz = function.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                LOGGER.info("Process class: " + clazz);
                Method replaceMethod = clazz.getDeclaredMethod("writeReplace");
                replaceMethod.setAccessible(true);
                Object serializedForm = replaceMethod.invoke(function);

                if (serializedForm instanceof SerializedLambda) {
                    return (SerializedLambda) serializedForm;
                }
            } catch (NoSuchMethodError e) {
                // fall through the loop and try the next class
                LOGGER.info("writeReplace not found");
            } catch (Throwable t) {
                LOGGER.info("Shit happens", t);
                return null;
            }
        }
        return null;
    }
}
