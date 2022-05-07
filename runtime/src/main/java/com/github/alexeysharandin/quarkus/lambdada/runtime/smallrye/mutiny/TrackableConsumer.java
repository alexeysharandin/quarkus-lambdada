package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import io.smallrye.context.impl.wrappers.SlowContextualConsumer;

import java.lang.reflect.Field;
import java.util.function.Consumer;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class TrackableConsumer<T> extends AbstractTrackable implements Consumer<T> {

    private final Consumer<T> delegate;
    private final Object source;

    public TrackableConsumer(Consumer<T> delegate) {
        this.delegate = delegate;

        if(delegate instanceof SlowContextualConsumer) {
            source = unwrap((SlowContextualConsumer<T>)delegate);
        } else {
            source = delegate;
        }
    }

    @Override
    public void accept(T t) {
        LambdaTracker.trackBefore(this);
        try {
            delegate.accept(t);
        } finally {
            LambdaTracker.trackAfter(this);
        }
    }

    @Override
    public Object delegate() {
        return source;
    }

    private static <T> Object unwrap(SlowContextualConsumer<T> delegate) {
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
