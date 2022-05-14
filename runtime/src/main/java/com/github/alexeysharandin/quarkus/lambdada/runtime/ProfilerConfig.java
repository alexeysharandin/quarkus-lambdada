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

    /**
     * Dev path
     */
    @ConfigItem(defaultValue = "lamdbada")
    public String path;

    /**
     * Max requests queue
     */
    @ConfigItem(name = "max-requests", defaultValue = "100")
    public Integer maxRequests;
}
