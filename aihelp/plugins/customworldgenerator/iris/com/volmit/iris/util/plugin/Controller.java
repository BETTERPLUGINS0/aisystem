package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;

public abstract class Controller implements IController {
   private final String name;
   private int tickRate;

   public Controller() {
      String var10001 = this.getClass().getSimpleName();
      this.name = var10001.replaceAll("Controller", "") + " Controller";
      this.tickRate = -1;
   }

   protected void setTickRate(int rate) {
      this.tickRate = var1;
   }

   protected void disableTicking() {
      this.setTickRate(-1);
   }

   public void l(Object l) {
      Iris.info("[" + this.getName() + "]: " + String.valueOf(var1));
   }

   public void w(Object l) {
      Iris.warn("[" + this.getName() + "]: " + String.valueOf(var1));
   }

   public void f(Object l) {
      Iris.error("[" + this.getName() + "]: " + String.valueOf(var1));
   }

   public void v(Object l) {
      String var10000 = this.getName();
      Iris.verbose("[" + var10000 + "]: " + String.valueOf(var1));
   }

   public String getName() {
      return this.name;
   }

   public abstract void start();

   public abstract void stop();

   public abstract void tick();

   public int getTickInterval() {
      return this.tickRate;
   }
}
