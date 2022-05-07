package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import io.smallrye.context.impl.wrappers.SlowContextualBiConsumer;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class TrackableBiConsumer<T, U> extends AbstractTrackable implements BiConsumer<T, U> {
    private final BiConsumer<T, U> delegate;
    private final Object source;

    public TrackableBiConsumer(BiConsumer<T, U> delegate) {
        this.delegate = delegate;

        if (delegate instanceof SlowContextualBiConsumer) {
            source = unwrap((SlowContextualBiConsumer<T, U>) delegate);
        } else {
            source = delegate;
        }
    }

    @Override
    public void accept(T t, U u) {
        LambdaTracker.trackBefore(this);
        try {
            delegate.accept(t, u);
        } finally {
            LambdaTracker.trackAfter(this);
        }
    }

    @Override
    public Object delegate() {
        return source;
    }

    private static <T, U> Object unwrap(SlowContextualBiConsumer<T, U> delegate) {
        Object result = delegate;
        try {
            Field f = delegate.getClass().getDeclaredField("consumer");
            f.setAccessible(true);
            result = f.get(delegate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
