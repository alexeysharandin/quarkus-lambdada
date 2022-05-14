package com.github.alexeysharandin.quarkus.lambdada.io.sqlite;


import com.github.alexeysharandin.quarkus.lambdada.io.StackTraceWriter;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;

public class EmbeddedStackTraceWriter implements StackTraceWriter {

    private final SqlHelper helper;

    public EmbeddedStackTraceWriter() {
        helper = new SqlHelper();
    }

    @Override
    public void write(ProfileStackTraceElement profileStackTraceElement) {
        System.out.println("\n*************** Dump StackTrace *****************\n");
        dump(profileStackTraceElement, 0, -1);
        System.out.println("\n*************** End Dump StackTrace *****************\n");
        //TODO not implemented
    }

    private void dump(ProfileStackTraceElement profileStackTraceElement, int index, int parentId) {
        System.out.println(build(profileStackTraceElement, index));
        int parent = store(profileStackTraceElement, index, parentId);
        for (ProfileStackTraceElement leaf : profileStackTraceElement.leafs()) {
            dump(leaf, index + 1, parent);
        }
    }

    private int store(ProfileStackTraceElement profileStackTraceElement, int index, int parent) {
        return helper.store(profileStackTraceElement, parent);
    }

    private String build(ProfileStackTraceElement profileStackTraceElement, int index) {
        return offset(index) +
                "Dump class: " + profileStackTraceElement.className() +
                ":" + profileStackTraceElement.methodName() +
                " start: " + profileStackTraceElement.startTime() +
                " executed: " + profileStackTraceElement.result() + "ms" +
                " meta: " + profileStackTraceElement.meta();
    }

    private String offset(int index) {
        return " ".repeat(index * 4);
    }
}
