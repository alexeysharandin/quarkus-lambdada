package com.github.alexeysharandin.quarkus.lambdada.runtime;


public class ConsoleDumber implements Dumper {
    @Override
    public void dump(Tracker tracker) {
        System.out.println("\n*************** Dump StackTrace *****************\n");
        dump(tracker, 0);
        System.out.println("\n*************** End Dump StackTrace *****************\n");
        //TODO not implemented
    }

    private void dump(Tracker tracker, int index) {
        System.out.println(build(tracker, index));
        for (Tracker leaf : tracker.leafs()) {
            dump(leaf, index + 1);
        }
    }

    private String build(Tracker tracker, int index) {
        return offset(index) +
                "Dump class: " + tracker.className() +
                ":" + tracker.methodName() +
                " start: " + tracker.startTime() +
                " executed: " + tracker.result() + "ms";
    }

    private String offset(int index) {
        return " ".repeat(index * 4);
    }
}
