package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;


import com.github.alexeysharandin.quarkus.lambdada.runtime.Tracker;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public interface Trackable {
    void tracker(Tracker tracker);
    Tracker tracker();

    Object delegate();
}
