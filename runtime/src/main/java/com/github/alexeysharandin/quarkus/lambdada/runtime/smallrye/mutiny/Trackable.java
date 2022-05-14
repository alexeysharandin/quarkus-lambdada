package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;


import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public interface Trackable {
    void tracker(ProfileStackTraceElement profileStackTraceElement);
    ProfileStackTraceElement tracker();

    Object delegate();
}
