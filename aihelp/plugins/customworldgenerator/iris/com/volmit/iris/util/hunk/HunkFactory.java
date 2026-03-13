package com.volmit.iris.util.hunk;

@FunctionalInterface
public interface HunkFactory {
   <T> Hunk<T> create(int w, int h, int d);
}
