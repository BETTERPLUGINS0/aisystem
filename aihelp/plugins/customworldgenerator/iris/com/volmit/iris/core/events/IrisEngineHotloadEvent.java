package com.volmit.iris.core.events;

import com.volmit.iris.engine.framework.Engine;
import org.bukkit.event.HandlerList;

public class IrisEngineHotloadEvent extends IrisEngineEvent {
   private static final HandlerList handlers = new HandlerList();

   public IrisEngineHotloadEvent(Engine engine) {
      super(var1);
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
