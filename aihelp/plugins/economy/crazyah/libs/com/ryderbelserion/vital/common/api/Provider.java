package libs.com.ryderbelserion.vital.common.api;

import libs.com.ryderbelserion.vital.common.VitalAPI;

public final class Provider {
   static VitalAPI api;

   private Provider() {
      throw new AssertionError();
   }

   public static VitalAPI getApi() {
      return api;
   }
}
