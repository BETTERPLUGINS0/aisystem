package com.volmit.iris.engine;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedComponent;
import com.volmit.iris.engine.framework.EngineEffects;
import com.volmit.iris.engine.framework.EnginePlayer;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import org.bukkit.entity.Player;

public class IrisEngineEffects extends EngineAssignedComponent implements EngineEffects {
   private final KMap<UUID, EnginePlayer> players = new KMap();
   private final Semaphore limit = new Semaphore(1);

   public IrisEngineEffects(Engine engine) {
      super(var1, "FX");
   }

   public void updatePlayerMap() {
      List var1 = this.getEngine().getWorld().getPlayers();
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Player var3 = (Player)var2.next();
            boolean var4 = this.players.containsKey(var3.getUniqueId());
            if (!var4) {
               this.players.put(var3.getUniqueId(), new EnginePlayer(this.getEngine(), var3));
            }
         }

         var2 = this.players.k().iterator();

         while(var2.hasNext()) {
            UUID var5 = (UUID)var2.next();
            if (!var1.contains(((EnginePlayer)this.players.get(var5)).getPlayer())) {
               this.players.remove(var5);
            }
         }

      }
   }

   public void tickRandomPlayer() {
      if (this.limit.tryAcquire()) {
         if (M.r(0.02D)) {
            this.updatePlayerMap();
            this.limit.release();
            return;
         }

         if (this.players.isEmpty()) {
            this.limit.release();
            return;
         }

         double var1 = 1.5D;
         int var3 = this.players.size();
         PrecisionStopwatch var4 = new PrecisionStopwatch();

         while(var3-- > 0 && (double)M.ms() - var4.getMilliseconds() < var1) {
            ((EnginePlayer)this.players.v().getRandom()).tick();
         }

         this.limit.release();
      }

   }
}
