package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;


import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public abstract class AbstractTrackable implements Trackable {
    private Tracker tracker;
    @Override
    public void tracker(Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public Tracker tracker() {
        return tracker;
    }
}
