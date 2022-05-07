package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny.infrastructure;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.UniInterceptor;
import io.smallrye.mutiny.operators.uni.builders.UniCreateFromKnownItem;
import io.smallrye.mutiny.subscription.UniSubscriber;
import org.jboss.logging.Logger;
/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class ProfilerUniInterceptor implements UniInterceptor {
    private static final Logger LOGGER = Logger.getLogger(ProfilerUniInterceptor.class);

    @Override
    public <T> Uni<T> onUniCreation(Uni<T> uni) {
        if(uni instanceof UniCreateFromKnownItem) {
            LOGGER.warn("Uni created from 'code' instead of lambda. This code will be executed outside of event-loop");
        }
        return UniInterceptor.super.onUniCreation(uni);
    }

    @Override
    public <T> UniSubscriber<? super T> onSubscription(Uni<T> instance, UniSubscriber<? super T> subscriber) {
        LOGGER.debug("UniSubscriber: instance = " + instance + ", subscriber = " + subscriber);
        return UniInterceptor.super.onSubscription(instance, subscriber);
    }

    @Override
    public int ordinal() {
        LOGGER.debug("ordinal: " + UniInterceptor.super.ordinal());
        return UniInterceptor.super.ordinal();
    }
}
