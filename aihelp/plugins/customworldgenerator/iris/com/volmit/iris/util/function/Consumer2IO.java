package com.volmit.iris.util.function;

import java.io.IOException;

@FunctionalInterface
public interface Consumer2IO<A, B> {
   void accept(A a, B b) throws IOException;
}
