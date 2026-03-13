package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerHandler implements DecreeParameterHandler<Player> {
   public KList<Player> getPossibilities() {
      return new KList(new ArrayList(Bukkit.getOnlinePlayers()));
   }

   public String toString(Player player) {
      return var1.getName();
   }

   public Player parse(String in, boolean force) {
      KList var3 = this.getPossibilities(var1);
      if (var3.isEmpty()) {
         throw new DecreeParsingException("Unable to find Player \"" + var1 + "\"");
      } else {
         try {
            return (Player)((List)var3.stream().filter((var2x) -> {
               return this.toString(var2x).equalsIgnoreCase(var1);
            }).collect(Collectors.toList())).get(0);
         } catch (Throwable var5) {
            throw new DecreeParsingException("Unable to filter which Player \"" + var1 + "\"");
         }
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(Player.class);
   }

   public String getRandomDefault() {
      return "playername";
   }
}
