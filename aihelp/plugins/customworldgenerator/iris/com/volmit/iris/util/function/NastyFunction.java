package com.volmit.iris.util.function;

@FunctionalInterface
public interface NastyFunction<T, R> {
   R run(T t) throws Throwable;
}
