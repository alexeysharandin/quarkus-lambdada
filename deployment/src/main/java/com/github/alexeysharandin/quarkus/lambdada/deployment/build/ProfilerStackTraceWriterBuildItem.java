package com.github.alexeysharandin.quarkus.lambdada.deployment.build;

import com.github.alexeysharandin.quarkus.lambdada.io.StackTraceWriter;
import io.quarkus.builder.item.MultiBuildItem;

public class ProfilerStackTraceWriterBuildItem extends MultiBuildItem {
    private final Class<? extends StackTraceWriter> dumperClass;

    public ProfilerStackTraceWriterBuildItem(Class<? extends StackTraceWriter> dumperClass) {
        this.dumperClass = dumperClass;
    }

    public Class<? extends StackTraceWriter> dumperClass() {
        return dumperClass;
    }
}
