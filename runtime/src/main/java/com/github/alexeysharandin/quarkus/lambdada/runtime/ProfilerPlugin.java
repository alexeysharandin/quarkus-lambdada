package com.github.alexeysharandin.quarkus.lambdada.runtime;

public interface ProfilerPlugin {
    void applyStart(ProfileStackTraceElement profileStackTraceElement, Object object);
    void applyEnd(ProfileStackTraceElement profileStackTraceElement, Object object);
}
