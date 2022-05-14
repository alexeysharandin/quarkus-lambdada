package com.github.alexeysharandin.quarkus.lambdada.io;


import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;

public interface StackTraceWriter {
    void write(ProfileStackTraceElement profileStackTraceElement);
}
