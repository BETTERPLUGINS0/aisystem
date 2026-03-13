package com.volmit.iris.util.data.palette;

public interface IdMap<T> extends Iterable<T> {
   int getId(T paramT);

   T byId(int paramInt);
}
