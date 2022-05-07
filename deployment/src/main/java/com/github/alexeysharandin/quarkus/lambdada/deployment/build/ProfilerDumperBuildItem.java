package com.github.alexeysharandin.quarkus.lambdada.deployment.build;

import com.github.alexeysharandin.quarkus.lambdada.runtime.Dumper;
import io.quarkus.builder.item.MultiBuildItem;

public class ProfilerDumperBuildItem extends MultiBuildItem {
    private final Class<? extends Dumper> dumperClass;

    public ProfilerDumperBuildItem(Class<? extends Dumper> dumperClass) {
        this.dumperClass = dumperClass;
    }

    public Class<? extends Dumper> dumperClass() {
        return dumperClass;
    }
}
