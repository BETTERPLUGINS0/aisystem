package com.volmit.iris.util.scheduling;

@FunctionalInterface
public interface Observer<T> {
   void onChanged(T from, T to);
}
