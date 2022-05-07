package com.github.alexeysharandin.quarkus.lambdada.deployment.build;

import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfilerPlugin;
import io.quarkus.builder.item.MultiBuildItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfilerPluginBuildItem extends MultiBuildItem {
    private final List<Class<? extends ProfilerPlugin>> plugins;

    public ProfilerPluginBuildItem(Class<? extends ProfilerPlugin> plugins) {
        this.plugins = new ArrayList<>();
        this.plugins.add(plugins);
    }

    public void addPlugin(Class<? extends ProfilerPlugin> plugin) {
        this.plugins.add(plugin);
    }

    public List<Class<? extends ProfilerPlugin>> plugins() {
        return Collections.unmodifiableList(plugins);
    }
}
