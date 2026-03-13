package com.volmit.iris.util.collection;

import java.util.Iterator;
import java.util.List;

public abstract class GListAdapter<FROM, TO> {
   public List<TO> adapt(List<FROM> from) {
      KList var2 = new KList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Object var5 = this.onAdapt(var4);
         if (var5 != null) {
            var2.add(this.onAdapt(var4));
         }
      }

      return var2;
   }

   public abstract TO onAdapt(FROM from);
}
