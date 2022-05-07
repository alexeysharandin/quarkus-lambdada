package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import io.smallrye.context.impl.wrappers.SlowContextualRunnable;

import java.lang.reflect.Field;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class TrackableRunnable extends AbstractTrackable implements Runnable {
    private final Runnable delegate;
    private final Object source;

    public TrackableRunnable(Runnable delegate) {
        this.delegate = delegate;

        if(delegate instanceof SlowContextualRunnable) {
            source = unwrap((SlowContextualRunnable) delegate);
        } else {
            source = delegate;
        }
    }

    @Override
    public void run() {
        try {
            LambdaTracker.trackBefore(this);
            delegate.run();
        } finally {
            LambdaTracker.trackAfter(this);
        }
    }

    @Override
    public Object delegate() {
        return source;
    }

    private static Object unwrap(SlowContextualRunnable delegate) {
        Object result = delegate;
        try {
            Field f = delegate.getClass().getDeclaredField("runnable");
            f.setAccessible(true);
            result = f.get(delegate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
