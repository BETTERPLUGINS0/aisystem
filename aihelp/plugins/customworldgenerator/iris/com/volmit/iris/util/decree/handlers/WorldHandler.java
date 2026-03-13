package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldHandler implements DecreeParameterHandler<World> {
   public KList<World> getPossibilities() {
      KList var1 = new KList();
      Iterator var2 = Bukkit.getWorlds().iterator();

      while(var2.hasNext()) {
         World var3 = (World)var2.next();
         if (!var3.getName().toLowerCase().startsWith("iris/")) {
            var1.add((Object)var3);
         }
      }

      return var1;
   }

   public String toString(World world) {
      return var1.getName();
   }

   public World parse(String in, boolean force) {
      KList var3 = this.getPossibilities(var1);
      if (var3.isEmpty()) {
         throw new DecreeParsingException("Unable to find World \"" + var1 + "\"");
      } else {
         try {
            return (World)((List)var3.stream().filter((var2x) -> {
               return this.toString(var2x).equalsIgnoreCase(var1);
            }).collect(Collectors.toList())).get(0);
         } catch (Throwable var5) {
            throw new DecreeParsingException("Unable to filter which World \"" + var1 + "\"");
         }
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(World.class);
   }

   public String getRandomDefault() {
      return "world";
   }
}
