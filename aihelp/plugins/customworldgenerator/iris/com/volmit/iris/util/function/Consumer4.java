package com.volmit.iris.util.function;

@FunctionalInterface
public interface Consumer4<A, B, C, D> {
   void accept(A a, B b, C c, D d);
}
