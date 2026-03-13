package com.volmit.iris.util.scheduling;

import com.volmit.iris.util.collection.KList;

public interface Queue<T> {
   static <T> Queue<T> create(KList<T> t) {
      return (new ShurikenQueue()).queue(t);
   }

   static <T> Queue<T> create(T... t) {
      return (new ShurikenQueue()).queue((new KList()).add(t));
   }

   Queue<T> queue(T t);

   Queue<T> queue(KList<T> t);

   boolean hasNext(int amt);

   boolean hasNext();

   T next();

   KList<T> next(int amt);

   Queue<T> clear();

   int size();

   boolean contains(T p);
}
