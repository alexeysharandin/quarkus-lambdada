package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;

import java.util.function.LongConsumer;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class TrackableLongConsumer extends AbstractTrackable implements LongConsumer {
    private final LongConsumer delegate;

    public TrackableLongConsumer(LongConsumer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accept(long value) {
        LambdaTracker.trackBefore(this);
        try {
            delegate.accept(value);
        } finally {
            LambdaTracker.trackAfter(this);
        }
    }

    @Override
    public Object delegate() {
        return delegate;
    }
}
