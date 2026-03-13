package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeContext;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.DecreeSystem;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

public class BlockVectorHandler implements DecreeParameterHandler<BlockVector> {
   public KList<BlockVector> getPossibilities() {
      KList var1 = new KList();
      VolmitSender var2 = DecreeContext.get();
      if (var2.isPlayer()) {
         var1.add((Object)var2.player().getLocation().toVector().toBlockVector());
      }

      return var1;
   }

   public String toString(BlockVector v) {
      String var10000;
      if (var1.getY() == 0.0D) {
         var10000 = Form.f((float)var1.getBlockX(), 2);
         return var10000 + "," + Form.f((float)var1.getBlockZ(), 2);
      } else {
         var10000 = Form.f((float)var1.getBlockX(), 2);
         return var10000 + "," + Form.f((float)var1.getBlockY(), 2) + "," + Form.f((float)var1.getBlockZ(), 2);
      }
   }

   public BlockVector parse(String in, boolean force) {
      try {
         if (var1.contains(",")) {
            String[] var6 = var1.split("\\Q,\\E");
            if (var6.length == 2) {
               return new BlockVector(Integer.parseInt(var6[0].trim()), 0, Integer.parseInt(var6[1].trim()));
            } else if (var6.length == 3) {
               return new BlockVector(Integer.parseInt(var6[0].trim()), Integer.parseInt(var6[1].trim()), Integer.parseInt(var6[2].trim()));
            } else {
               throw new DecreeParsingException("Could not parse components for vector. You have " + var6.length + " components. Expected 2 or 3.");
            }
         } else if (!var1.equalsIgnoreCase("here") && !var1.equalsIgnoreCase("me") && !var1.equalsIgnoreCase("self")) {
            if (!var1.equalsIgnoreCase("look") && !var1.equalsIgnoreCase("cursor") && !var1.equalsIgnoreCase("crosshair")) {
               if (var1.trim().toLowerCase().startsWith("player:")) {
                  String var3 = var1.trim().split("\\Q:\\E")[1];
                  KList var4 = DecreeSystem.getHandler(Player.class).getPossibilities(var3);
                  if (var4 != null && var4.isNotEmpty()) {
                     return ((Player)var4.get(0)).getLocation().toVector().toBlockVector();
                  }

                  if (var4 == null || var4.isEmpty()) {
                     throw new DecreeParsingException("Cannot find player: " + var3);
                  }
               }

               return null;
            } else if (!DecreeContext.get().isPlayer()) {
               throw new DecreeParsingException("You cannot specify look,cursor,crosshair as a console.");
            } else {
               return DecreeContext.get().player().getTargetBlockExact(256, FluidCollisionMode.NEVER).getLocation().toVector().toBlockVector();
            }
         } else if (!DecreeContext.get().isPlayer()) {
            throw new DecreeParsingException("You cannot specify me,self,here as a console.");
         } else {
            return DecreeContext.get().player().getLocation().toVector().toBlockVector();
         }
      } catch (Throwable var5) {
         throw new DecreeParsingException("Unable to get Vector for \"" + var1 + "\" because of an uncaught exception: " + String.valueOf(var5));
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(BlockVector.class);
   }

   public String getRandomDefault() {
      return M.r(0.5D) ? "0,0" : "0,0,0";
   }
}
