package com.github.alexeysharandin.quarkus.lambdada.runtime.smallrye.mutiny;


import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public abstract class AbstractTrackable implements Trackable {
    private ProfileStackTraceElement profileStackTraceElement;
    @Override
    public void tracker(ProfileStackTraceElement profileStackTraceElement) {
        this.profileStackTraceElement = profileStackTraceElement;
    }

    @Override
    public ProfileStackTraceElement tracker() {
        return profileStackTraceElement;
    }
}
