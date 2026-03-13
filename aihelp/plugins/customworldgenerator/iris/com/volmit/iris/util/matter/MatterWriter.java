package com.volmit.iris.util.matter;

@FunctionalInterface
public interface MatterWriter<W, T> {
   void writeMatter(W w, T d, int x, int y, int z);
}
