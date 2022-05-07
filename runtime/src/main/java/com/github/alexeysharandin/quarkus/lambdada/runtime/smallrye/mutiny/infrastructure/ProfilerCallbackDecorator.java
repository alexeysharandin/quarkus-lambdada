package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny.infrastructure;

import com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny.*;
import io.smallrye.mutiny.infrastructure.CallbackDecorator;

import java.util.function.*;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class ProfilerCallbackDecorator implements CallbackDecorator {
    @Override
    public <T> Supplier<T> decorate(Supplier<T> supplier) {
        return new TrackableSupplier<T>(supplier);
    }

    @Override
    public <T> Consumer<T> decorate(Consumer<T> consumer) {
        return new TrackableConsumer<>(consumer);
    }

    @Override
    public LongConsumer decorate(LongConsumer consumer) {
        return new TrackableLongConsumer(consumer);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        return new TrackableRunnable(runnable);
    }

    @Override
    public <T1, T2> BiConsumer<T1, T2> decorate(BiConsumer<T1, T2> consumer) {
        return new TrackableBiConsumer<>(consumer);
    }

    @Override
    public <I, O> Function<I, O> decorate(Function<I, O> function) {
        return new TrackableFunction<>(function);
    }

}
