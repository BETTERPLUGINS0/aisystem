package com.volmit.iris.engine.framework;

import com.volmit.iris.util.collection.KList;

public interface Staged {
   KList<EngineStage> getStages();

   void registerStage(EngineStage stage);

   default void dump() {
      this.getStages().forEach(EngineStage::close);
      this.getStages().clear();
   }
}
