package com.volmit.iris.util.stream;

import com.volmit.iris.util.collection.KList;

public interface Significance<T> {
   KList<T> getFactorTypes();

   double getSignificance(T t);

   T getMostSignificantType();
}
