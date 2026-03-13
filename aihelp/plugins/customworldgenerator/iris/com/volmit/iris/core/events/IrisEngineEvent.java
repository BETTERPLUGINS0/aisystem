package com.volmit.iris.core.events;

import com.volmit.iris.engine.framework.Engine;
import lombok.Generated;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IrisEngineEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private Engine engine;

   public IrisEngineEvent() {
      super(true);
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEngineEvent)) {
         return false;
      } else {
         IrisEngineEvent var2 = (IrisEngineEvent)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            Engine var3 = this.getEngine();
            Engine var4 = var2.getEngine();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEngineEvent;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      Engine var3 = this.getEngine();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var2;
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public void setEngine(final Engine engine) {
      this.engine = var1;
   }

   @Generated
   public String toString() {
      return "IrisEngineEvent(engine=" + String.valueOf(this.getEngine()) + ")";
   }

   @Generated
   public IrisEngineEvent(final Engine engine) {
      this.engine = var1;
   }
}
