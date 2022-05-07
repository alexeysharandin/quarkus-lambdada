package com.github.alexeysharandin.quarkus.lambdada.runtime;

import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;

public interface ProfilerPlugin {
    void applyStart(Tracker tracker, Object object);
    void applyEnd(Tracker tracker, Object object);
}
