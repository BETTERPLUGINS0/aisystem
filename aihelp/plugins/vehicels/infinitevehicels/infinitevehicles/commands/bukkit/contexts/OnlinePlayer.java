package me.PM2.infinitevehicles.commands.bukkit.contexts;

import java.util.Objects;
import org.bukkit.entity.Player;

public class OnlinePlayer {
   public final Player player;

   public OnlinePlayer(Player player) {
      this.player = var1;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         OnlinePlayer var2 = (OnlinePlayer)var1;
         return Objects.equals(this.player, var2.player);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.player});
   }

   public String toString() {
      return "OnlinePlayer{player=" + this.player + '}';
   }
}
