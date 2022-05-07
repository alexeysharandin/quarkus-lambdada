package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import io.smallrye.context.impl.wrappers.SlowContextualSupplier;

import java.lang.reflect.Field;
import java.util.function.Supplier;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class TrackableSupplier<T> extends AbstractTrackable implements Supplier<T> {
    private final Supplier<T> delegate;
    private final Object source;

    public TrackableSupplier(Supplier<T> delegate) {
        this.delegate = delegate;

        if(delegate instanceof SlowContextualSupplier) {
            source = unwrap((SlowContextualSupplier<T>)delegate);
        } else {
            source = delegate;
        }
    }

    @Override
    public T get() {
        LambdaTracker.trackBefore(this);
        try {
            return delegate.get();
        } finally {
            LambdaTracker.trackAfter(this);
        }
    }

    public Object delegate() {
        return source;
    }

    private static <T> Object unwrap(SlowContextualSupplier<T> delegate) {
        Object result = delegate;
        try {
            Field f = delegate.getClass().getDeclaredField("supplier");
            f.setAccessible(true);
            result = f.get(delegate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
