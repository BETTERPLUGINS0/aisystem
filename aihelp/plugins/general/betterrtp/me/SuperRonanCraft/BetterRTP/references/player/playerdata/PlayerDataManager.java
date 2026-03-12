package me.SuperRonanCraft.BetterRTP.references.player.playerdata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerDataManager {
   private final HashMap<Player, PlayerData> playerData = new HashMap();

   public PlayerData getData(@NonNull Player p) {
      if (p == null) {
         throw new NullPointerException("p is marked non-null but is null");
      } else {
         if (!this.playerData.containsKey(p)) {
            this.playerData.put(p, new PlayerData(p));
         }

         return (PlayerData)this.playerData.get(p);
      }
   }

   @Nullable
   public PlayerData getData(UUID id) {
      Iterator var2 = this.playerData.keySet().iterator();

      Player p;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         p = (Player)var2.next();
      } while(!p.getUniqueId().equals(id));

      return (PlayerData)this.playerData.get(p);
   }

   public void clear() {
      this.playerData.clear();
   }

   public void clear(Player p) {
      this.playerData.remove(p);
   }
}
