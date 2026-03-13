package com.volmit.iris.engine.framework;

import com.volmit.iris.util.collection.KList;

public abstract class IrisEngineMode implements EngineMode {
   private final Engine engine;
   private final KList<EngineStage> stages;
   private boolean closed;

   public IrisEngineMode(Engine engine) {
      this.engine = var1;
      this.stages = new KList();
      this.closed = false;
   }

   public synchronized void close() {
      if (!this.closed) {
         this.closed = true;
         this.dump();
      }
   }

   public Engine getEngine() {
      return this.engine;
   }

   public KList<EngineStage> getStages() {
      return this.stages;
   }

   public void registerStage(EngineStage stage) {
      this.stages.add((Object)var1);
   }
}
