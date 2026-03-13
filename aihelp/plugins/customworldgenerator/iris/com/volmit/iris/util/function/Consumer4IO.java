package com.volmit.iris.util.function;

import java.io.IOException;

@FunctionalInterface
public interface Consumer4IO<A, B, C, D> {
   void accept(A a, B b, C c, D d) throws IOException;
}
