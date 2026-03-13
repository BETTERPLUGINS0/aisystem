package com.volmit.iris.util.function;

public interface Supplier3R<T, TT, TTT, TTTT> {
   TTTT get(T t, TT tt, TTT ttt);
}
