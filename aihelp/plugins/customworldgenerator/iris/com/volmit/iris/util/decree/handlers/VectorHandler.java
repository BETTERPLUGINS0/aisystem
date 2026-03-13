package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeContext;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.DecreeSystem;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;
import com.volmit.iris.util.format.Form;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class VectorHandler implements DecreeParameterHandler<Vector> {
   private static final KList<String> randoms = new KList(new String[]{"here", "0,0,0", "0,0", "look", "player:<name>"});

   public KList<Vector> getPossibilities() {
      return null;
   }

   public String toString(Vector v) {
      String var10000;
      if (var1.getY() == 0.0D) {
         var10000 = Form.f(var1.getX(), 2);
         return var10000 + "," + Form.f(var1.getZ(), 2);
      } else {
         var10000 = Form.f(var1.getX(), 2);
         return var10000 + "," + Form.f(var1.getY(), 2) + "," + Form.f(var1.getZ(), 2);
      }
   }

   public Vector parse(String in, boolean force) {
      try {
         if (var1.contains(",")) {
            String[] var6 = var1.split("\\Q,\\E");
            if (var6.length == 2) {
               return new BlockVector(Double.parseDouble(var6[0].trim()), 0.0D, Double.parseDouble(var6[1].trim()));
            } else if (var6.length == 3) {
               return new BlockVector(Double.parseDouble(var6[0].trim()), Double.parseDouble(var6[1].trim()), Double.parseDouble(var6[2].trim()));
            } else {
               throw new DecreeParsingException("Could not parse components for vector. You have " + var6.length + " components. Expected 2 or 3.");
            }
         } else if (!var1.equalsIgnoreCase("here") && !var1.equalsIgnoreCase("me") && !var1.equalsIgnoreCase("self")) {
            if (!var1.equalsIgnoreCase("look") && !var1.equalsIgnoreCase("cursor") && !var1.equalsIgnoreCase("crosshair")) {
               if (var1.trim().toLowerCase().startsWith("player:")) {
                  String var3 = var1.trim().split("\\Q:\\E")[1];
                  KList var4 = DecreeSystem.getHandler(Player.class).getPossibilities(var3);
                  if (var4 != null && var4.isNotEmpty()) {
                     return ((Player)var4.get(0)).getLocation().toVector();
                  }

                  if (var4 == null || var4.isEmpty()) {
                     throw new DecreeParsingException("Cannot find player: " + var3);
                  }
               }

               return null;
            } else if (!DecreeContext.get().isPlayer()) {
               throw new DecreeParsingException("You cannot specify look,cursor,crosshair as a console.");
            } else {
               return DecreeContext.get().player().getTargetBlockExact(256, FluidCollisionMode.NEVER).getLocation().toVector();
            }
         } else if (!DecreeContext.get().isPlayer()) {
            throw new DecreeParsingException("You cannot specify me,self,here as a console.");
         } else {
            return DecreeContext.get().player().getLocation().toVector();
         }
      } catch (Throwable var5) {
         throw new DecreeParsingException("Unable to get Vector for \"" + var1 + "\" because of an uncaught exception: " + String.valueOf(var5));
      }
   }

   public boolean supports(Class<?> type) {
      return var1.equals(Vector.class);
   }

   public String getRandomDefault() {
      return (String)randoms.getRandom();
   }
}
