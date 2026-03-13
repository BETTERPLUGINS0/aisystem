package tntrun.datahandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import tntrun.arena.Arena;
import tntrun.utils.Utils;

public class ArenasManager {
   private Arena bungeeArena;
   private HashMap<String, Arena> arenanames = new HashMap();

   public void registerArena(Arena arena) {
      this.arenanames.put(arena.getArenaName(), arena);
   }

   public void unregisterArena(Arena arena) {
      this.arenanames.remove(arena.getArenaName());
   }

   public Collection<Arena> getArenas() {
      return this.arenanames.values();
   }

   public Set<String> getArenasNames() {
      return this.arenanames.keySet();
   }

   public Arena getArenaByName(String name) {
      return (Arena)this.arenanames.get(name);
   }

   public Arena getPlayerArena(String name) {
      Iterator var3 = this.arenanames.values().iterator();

      while(var3.hasNext()) {
         Arena arena = (Arena)var3.next();
         if (arena.getPlayersManager().isInArena(name)) {
            return arena;
         }
      }

      return null;
   }

   public Set<Arena> getPvpArenas() {
      return (Set)this.arenanames.entrySet().stream().filter((e) -> {
         return !"no".equalsIgnoreCase(((Arena)e.getValue()).getStructureManager().getDamageEnabled().toString());
      }).map((e) -> {
         return (Arena)e.getValue();
      }).collect(Collectors.toSet());
   }

   public Set<Arena> getNonPvpArenas() {
      return (Set)this.arenanames.entrySet().stream().filter((e) -> {
         return "no".equalsIgnoreCase(((Arena)e.getValue()).getStructureManager().getDamageEnabled().toString());
      }).map((e) -> {
         return (Arena)e.getValue();
      }).collect(Collectors.toSet());
   }

   public Arena getBungeeArena() {
      return this.bungeeArena;
   }

   public void setBungeeArena() {
      List<Arena> arenaList = new ArrayList(this.getArenas());
      if (!arenaList.isEmpty()) {
         Collections.shuffle(arenaList);
         if (Utils.debug()) {
            Bukkit.getLogger().info("[TNTRun_reloaded] Bungee arena set to " + ((Arena)arenaList.get(0)).getArenaName());
         }

         this.bungeeArena = (Arena)arenaList.get(0);
      }
   }
}
