package com.volmit.iris.util.data.palette;

import java.util.List;

public interface Palette<T> {
   int idFor(T paramT);

   T valueFor(int paramInt);

   int getSize();

   void read(List<T> fromList);

   void write(List<T> toList);
}
