package com.volmit.iris.util.function;

@FunctionalInterface
public interface Consumer3<A, B, C> {
   void accept(A a, B b, C c);
}
