package com.github.alexeysharandin.quarkus.lambdada.runtime;

import java.lang.invoke.SerializedLambda;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class EntryMetadata {
    private final String className;
    private final String methodName;

    private final Map<String, Object> meta;

    EntryMetadata(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        meta = new LinkedHashMap<>();
    }

    public static EntryMetadata from(SerializedLambda lambda) {
        //System.out.println("lambda = " + lambda);
        return from(lambda.getCapturingClass(), lambda.getImplMethodName());
    }

    public static EntryMetadata from(String className, String methodName) {
        return new EntryMetadata(className, methodName);
    }

    public static <I, O> EntryMetadata from(Object object) {
        System.out.println("object = " + object + ":" + Arrays.toString(object.getClass().getInterfaces()));
        // ru.LambdaTest$$Lambda$15/0x0000000800c01c00@7ba4f24f
        String source = object.toString();
        int index1 = source.indexOf("$$");
        if (index1 == -1) {
            index1 = source.indexOf("$");
        }

        int index2 = source.indexOf("@");
        if (index1 == -1 || index2 == -1) {
            System.out.println("Something happen with '" + source + "' class: " + object);
        }
        String className = source.substring(0, index1);
        String methodName = source.substring(index1 + 2, index2);
        return from(className, methodName);
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    public void addMetaData(String key, String value) {
        meta.put(key, value);
    }

    public void addMetaData(Map<String, String> map) {
        meta.putAll(map);
    }

    public Map<String, Object> metadata() {
        return meta;
    }

    @Override
    public String toString() {
        return "EntryMetadata{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", meta=" + meta +
                '}';
    }
}
