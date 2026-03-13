package com.volmit.iris.util.matter.slices.container;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.loader.ResourceLoader;

public abstract class RegistrantContainer<T extends IrisRegistrant> {
   private final Class<T> type;
   private final String loadKey;

   public RegistrantContainer(Class<T> type, String loadKey) {
      this.type = var1;
      this.loadKey = var2;
   }

   public T load(IrisData data) {
      return ((ResourceLoader)var1.getLoaders().get(this.type)).load(this.loadKey);
   }

   public String getLoadKey() {
      return this.loadKey;
   }
}
