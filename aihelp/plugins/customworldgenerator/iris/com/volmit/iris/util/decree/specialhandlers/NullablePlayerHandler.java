package com.volmit.iris.util.decree.specialhandlers;

import com.volmit.iris.util.decree.handlers.PlayerHandler;
import org.bukkit.entity.Player;

public class NullablePlayerHandler extends PlayerHandler {
   public Player parse(String in, boolean force) {
      return (Player)this.getPossibilities(var1).stream().filter((var2x) -> {
         return this.toString(var2x).equalsIgnoreCase(var1);
      }).findFirst().orElse((Object)null);
   }
}
