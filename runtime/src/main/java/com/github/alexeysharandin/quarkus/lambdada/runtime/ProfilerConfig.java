package com.github.alexeysharandin.quarkus.lambdada.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(
        name = "profiler"
)
public class ProfilerConfig {
    /**
     * Enable or disable profiler.
     * Default: false
     * Auto enable in Dev/Test modes
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;
}
