package com.volmit.iris.util.function;

public interface NastySupplier<T> {
   T get() throws Throwable;
}
