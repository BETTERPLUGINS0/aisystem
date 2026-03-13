package com.volmit.iris.util.nbt.mca.palette;

public interface MCAIdMap<T> extends Iterable<T> {
   int getId(T paramT);

   T byId(int paramInt);
}
