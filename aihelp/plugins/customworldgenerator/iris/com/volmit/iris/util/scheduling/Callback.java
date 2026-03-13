package com.volmit.iris.util.scheduling;

@FunctionalInterface
public interface Callback<T> {
   void run(T t);
}
