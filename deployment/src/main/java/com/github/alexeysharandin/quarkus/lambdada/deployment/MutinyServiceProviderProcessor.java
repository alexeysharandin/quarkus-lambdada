package com.github.alexeysharandin.quarkus.lambdada.deployment;

import com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny.infrastructure.ProfilerCallbackDecorator;
import com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny.infrastructure.ProfilerUniInterceptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;
import io.smallrye.mutiny.infrastructure.CallbackDecorator;
import io.smallrye.mutiny.infrastructure.UniInterceptor;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
@SuppressWarnings("unused")
public class MutinyServiceProviderProcessor {
    private static final Logger LOGGER = Logger.getLogger(MutinyServiceProviderProcessor.class);


    @BuildStep
    void register(BuildProducer<ServiceProviderBuildItem> producer) {
        LOGGER.debug("Register ProfilerCallbackDecorator");
        producer.produce(
                new ServiceProviderBuildItem(
                        CallbackDecorator.class.getName(),
                        ProfilerCallbackDecorator.class.getName()
                )
        );

        LOGGER.debug("Register ProfilerUniInterceptor");
        producer.produce(
                new ServiceProviderBuildItem(
                        UniInterceptor.class.getName(),
                        ProfilerUniInterceptor.class.getName()
                )
        );
    }
}
