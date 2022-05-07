package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import io.smallrye.context.impl.wrappers.SlowContextualFunction;

import java.lang.reflect.Field;
import java.util.function.Function;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class TrackableFunction<I, O> extends AbstractTrackable implements Function<I, O> {
    private final Function<I, O> delegate;
    private final Object source;

    public TrackableFunction(Function<I, O> delegate) {
        this.delegate = delegate;

        if (delegate instanceof SlowContextualFunction) {
            source = unwrap((SlowContextualFunction<I, O>) delegate);
        } else {
            source = delegate;
        }
    }

    @Override
    public O apply(I i) {
        LambdaTracker.trackBefore(this);
        try {
            return delegate.apply(i);
        } finally {
            LambdaTracker.trackAfter(this);
        }
    }

    public Object delegate() {
        return source;
    }

    private static <I, O> Object unwrap(SlowContextualFunction<I, O> function) {
        Object result = function;
        try {
            Field f = function.getClass().getDeclaredField("function");
            f.setAccessible(true);
            result = f.get(function);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
