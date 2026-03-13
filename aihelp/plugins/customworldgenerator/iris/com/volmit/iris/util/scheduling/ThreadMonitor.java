package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.RollingSequence;
import java.lang.Thread.State;

public class ThreadMonitor extends Thread {
   private final Thread monitor;
   private final ChronoLatch cl;
   private final RollingSequence sq = new RollingSequence(3);
   int cycles = 0;
   private boolean running = true;
   private State lastState;
   private PrecisionStopwatch st = PrecisionStopwatch.start();

   private ThreadMonitor(Thread monitor) {
      this.monitor = var1;
      this.lastState = State.NEW;
      this.cl = new ChronoLatch(1000L);
      this.start();
   }

   public static ThreadMonitor bind(Thread monitor) {
      return new ThreadMonitor(var0);
   }

   public void run() {
      while(true) {
         if (this.running) {
            try {
               Thread.sleep(0L);
               State var1 = this.monitor.getState();
               if (this.lastState != var1) {
                  ++this.cycles;
                  this.pushState(var1);
               }

               this.lastState = var1;
               if (this.cl.flip()) {
                  String var10000 = Form.f(this.cycles);
                  Iris.info("Cycles: " + var10000 + " (" + Form.duration(this.sq.getAverage(), 2) + ")");
               }
               continue;
            } catch (Throwable var2) {
               Iris.reportError(var2);
               this.running = false;
            }
         }

         return;
      }
   }

   public void pushState(State s) {
      if (var1 != State.RUNNABLE) {
         if (this.st != null) {
            this.sq.put(this.st.getMilliseconds());
         }
      } else {
         this.st = PrecisionStopwatch.start();
      }

   }

   public void unbind() {
      this.running = false;
   }
}
