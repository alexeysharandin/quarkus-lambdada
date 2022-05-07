package com.github.alexeysharandin.quarkus.lambdada.deployment.vertx;

import com.github.alexeysharandin.quarkus.lambdada.runtime.vertx.VertxOptionsConsumer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.vertx.core.deployment.VertxOptionsConsumerBuildItem;

import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
@SuppressWarnings("unused")
public class QuarkusVertxOptionsCustomizer {
    private static final Logger LOGGER = Logger.getLogger(QuarkusVertxOptionsCustomizer.class);
    private final static int PRIORITY = 1000;

    @BuildStep
    VertxOptionsConsumerBuildItem configureVertx() {
        LOGGER.debug("Add VertxOptionsConsumerBuildItem to Quarkus Build");
        return new VertxOptionsConsumerBuildItem(new VertxOptionsConsumer(), PRIORITY);
    }

}