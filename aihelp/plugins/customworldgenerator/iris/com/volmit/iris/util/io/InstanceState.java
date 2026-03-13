package com.volmit.iris.util.io;

import com.volmit.iris.util.math.RNG;
import java.io.File;
import java.io.IOException;

public class InstanceState {
   public static int getInstanceId() {
      try {
         return Integer.parseInt(IO.readAll(instanceFile()).trim());
      } catch (Throwable var1) {
         var1.printStackTrace();
         return -1;
      }
   }

   public static void updateInstanceId() {
      try {
         IO.writeAll(instanceFile(), (Object)RNG.r.imax().makeConcatWithConstants<invokedynamic>(RNG.r.imax()));
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   private static File instanceFile() {
      File var0 = new File("plugins/Iris/cache/instance");
      var0.getParentFile().mkdirs();
      return var0;
   }
}
