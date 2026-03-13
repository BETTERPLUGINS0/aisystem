package com.volmit.iris.engine.framework;

import com.volmit.iris.util.data.KCache;

public interface MeteredCache {
   long getSize();

   KCache<?, ?> getRawCache();

   long getMaxSize();

   default double getUsage() {
      return (double)this.getSize() / (double)this.getMaxSize();
   }

   boolean isClosed();
}
