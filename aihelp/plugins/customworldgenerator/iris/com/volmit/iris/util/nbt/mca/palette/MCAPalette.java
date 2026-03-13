package com.volmit.iris.util.nbt.mca.palette;

import com.volmit.iris.util.nbt.tag.ListTag;
import java.util.function.Predicate;

public interface MCAPalette<T> {
   int idFor(T paramT);

   boolean maybeHas(Predicate<T> paramPredicate);

   T valueFor(int paramInt);

   int getSize();

   void read(ListTag paramListTag);
}
