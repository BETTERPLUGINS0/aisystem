package com.volmit.iris.util.board;

import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Predicate;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BoardUpdateTask extends BukkitRunnable {
   private static final Predicate<UUID> PLAYER_IS_ONLINE = (var0) -> {
      return Bukkit.getPlayer(var0) != null;
   };
   private final BoardManager boardManager;

   public void run() {
      Iterator var1 = this.boardManager.getScoreboards().entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         if (PLAYER_IS_ONLINE.test((UUID)var2.getKey())) {
            ((Board)var2.getValue()).update();
         }
      }

   }

   @Generated
   public BoardUpdateTask(final BoardManager boardManager) {
      this.boardManager = var1;
   }
}
