package com.ryandw11.structure.bottomfill;

import java.util.ArrayList;
import java.util.List;

public final class BottomFillProvider {
   private static final List<BottomFillImpl> providers = new ArrayList();

   /** @deprecated */
   @Deprecated
   public static void addProvider(BottomFillImpl bottomFill) {
      providers.add(bottomFill);
   }

   public static void addImplementation(BottomFillImpl bottomFill) {
      providers.add(bottomFill);
   }

   public static BottomFillImpl provide() {
      return (BottomFillImpl)(!providers.isEmpty() ? (BottomFillImpl)providers.get(0) : new DefaultBottomFill());
   }

   public static BottomFillImpl provide(int impl) {
      return (BottomFillImpl)(impl < providers.size() ? (BottomFillImpl)providers.get(impl) : new DefaultBottomFill());
   }
}
