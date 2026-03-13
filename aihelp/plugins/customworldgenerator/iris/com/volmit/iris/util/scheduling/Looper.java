package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;
import com.volmit.iris.core.service.PreservationSVC;

public abstract class Looper extends Thread {
   public void run() {
      ((PreservationSVC)Iris.service(PreservationSVC.class)).register((Thread)this);

      while(!interrupted()) {
         try {
            long var1 = this.loop();
            if (var1 < 0L) {
               break;
            }

            Thread.sleep(var1);
         } catch (InterruptedException var3) {
            break;
         } catch (Throwable var4) {
            Iris.reportError(var4);
            var4.printStackTrace();
         }
      }

      Iris.debug("Iris Thread " + this.getName() + " Shutdown.");
   }

   protected abstract long loop();
}
