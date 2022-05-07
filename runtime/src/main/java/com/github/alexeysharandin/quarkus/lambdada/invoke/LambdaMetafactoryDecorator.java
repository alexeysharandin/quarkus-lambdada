package com.github.alexeysharandin.quarkus.lambdada.invoke;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.logging.Logger;

import java.lang.invoke.*;

@SuppressWarnings("unused")
@RegisterForReflection
public class LambdaMetafactoryDecorator {

    private static final Logger LOGGER = Logger.getLogger(LambdaMetafactoryDecorator.class);

    public static CallSite metafactory(MethodHandles.Lookup caller,
                                       String interfaceMethodName,
                                       MethodType factoryType,
                                       MethodType interfaceMethodType,
                                       MethodHandle implementation,
                                       MethodType dynamicMethodType)
            throws LambdaConversionException {
        LOGGER.debug("Decorate: caller = " + caller + ", interfaceMethodName = " + interfaceMethodName + ", factoryType = " + factoryType + ", interfaceMethodType = " + interfaceMethodType + ", implementation = " + implementation + ", dynamicMethodType = " + dynamicMethodType);

        return LambdaMetafactory.altMetafactory(
                caller,
                interfaceMethodName,
                factoryType,
                interfaceMethodType,
                implementation,
                dynamicMethodType,
                LambdaMetafactory.FLAG_SERIALIZABLE | LambdaMetafactory.FLAG_MARKERS,
                1,
                SerializableLambdaMarker.class
        );
    }
}
