package com.volmit.iris.util.matter;

@FunctionalInterface
public interface MatterFilter<T> {
   T update(int x, int y, int z, T t);
}
