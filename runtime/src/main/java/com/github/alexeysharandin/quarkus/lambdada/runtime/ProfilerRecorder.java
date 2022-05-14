package com.github.alexeysharandin.quarkus.lambdada.runtime;

import com.github.alexeysharandin.quarkus.lambdada.io.StackTraceWriter;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.List;

@SuppressWarnings("unused")
@Recorder
public class ProfilerRecorder {
    public RuntimeValue<ProfilerRuntimeRecorder> create(List<StackTraceWriter> stackTraceWriters, List<ProfilerPlugin> profilerPlugins) {
        return new RuntimeValue<>(new ProfilerRuntimeRecorder(stackTraceWriters, profilerPlugins));
    }
}