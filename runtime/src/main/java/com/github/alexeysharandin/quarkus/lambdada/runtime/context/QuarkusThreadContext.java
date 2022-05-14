package com.github.alexeysharandin.quarkus.lambdada.runtime.context;


import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;

public class QuarkusThreadContext implements QuarkusContext {
    private static final ThreadLocal<ProfileStackTraceElement> local = new ThreadLocal<>();
    static final QuarkusContext INSTANCE = new QuarkusThreadContext();

    @Override
    public ProfileStackTraceElement value() {
        return local.get();
    }

    @Override
    public void value(ProfileStackTraceElement profileStackTraceElement) {
        local.set(profileStackTraceElement);
    }

    @Override
    public void clear() {
        local.set(null);
    }

    @Override
    public String toString() {
        return local.toString();
    }
}
