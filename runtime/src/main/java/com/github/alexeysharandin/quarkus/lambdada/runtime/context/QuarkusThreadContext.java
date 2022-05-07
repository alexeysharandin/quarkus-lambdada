package com.github.alexeysharandin.quarkus.lambdada.runtime.context;


import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;

public class QuarkusThreadContext implements QuarkusContext {
    private static final ThreadLocal<Tracker> local = new ThreadLocal<>();
    static final QuarkusContext INSTANCE = new QuarkusThreadContext();

    @Override
    public Tracker value() {
        return local.get();
    }

    @Override
    public void value(Tracker tracker) {
        local.set(tracker);
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
