package com.volmit.iris.util.matter;

@FunctionalInterface
public interface MatterReader<W, T> {
   T readMatter(W w, int x, int y, int z);
}
