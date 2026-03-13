package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.edit.BlockSignal;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.matter.MatterMarker;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

@Decree(
   name = "what",
   origin = DecreeOrigin.PLAYER,
   studio = true,
   description = "Iris What?"
)
public class CommandWhat implements DecreeExecutor {
   @Decree(
      description = "What is in my hand?",
      origin = DecreeOrigin.PLAYER
   )
   public void hand() {
      VolmitSender var10000;
      String var10001;
      try {
         BlockData var1 = this.player().getInventory().getItemInMainHand().getType().createBlockData();
         if (!var1.getMaterial().equals(Material.AIR)) {
            var10000 = this.sender();
            var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage("Material: " + var10001 + var1.getMaterial().name());
            var10000 = this.sender();
            var10001 = String.valueOf(C.WHITE);
            var10000.sendMessage("Full: " + var10001 + var1.getAsString(true));
         } else {
            this.sender().sendMessage("Please hold a block/item");
         }
      } catch (Throwable var3) {
         Iris.reportError(var3);
         Material var2 = this.player().getInventory().getItemInMainHand().getType();
         if (!var2.equals(Material.AIR)) {
            var10000 = this.sender();
            var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage("Material: " + var10001 + var2.name());
         } else {
            this.sender().sendMessage("Please hold a block/item");
         }
      }

   }

   @Decree(
      description = "What biome am i in?",
      origin = DecreeOrigin.PLAYER
   )
   public void biome() {
      VolmitSender var10000;
      String var10001;
      try {
         IrisBiome var1 = this.engine().getBiome(this.player().getLocation().getBlockX(), this.player().getLocation().getBlockY() - this.player().getWorld().getMinHeight(), this.player().getLocation().getBlockZ());
         var10000 = this.sender();
         var10001 = var1.getLoadKey();
         var10000.sendMessage("IBiome: " + var10001 + " (" + var1.getDerivative().name() + ")");
      } catch (Throwable var4) {
         Iris.reportError(var4);
         this.sender().sendMessage("Non-Iris Biome: " + this.player().getLocation().getBlock().getBiome().name());
         if (this.player().getLocation().getBlock().getBiome().equals(Biome.CUSTOM)) {
            try {
               var10000 = this.sender();
               var10001 = INMS.get().getTrueBiomeBaseKey(this.player().getLocation());
               var10000.sendMessage("Data Pack Biome: " + var10001 + " (ID: " + INMS.get().getTrueBiomeBaseId(INMS.get().getTrueBiomeBase(this.player().getLocation())) + ")");
            } catch (Throwable var3) {
               Iris.reportError(var3);
            }
         }
      }

   }

   @Decree(
      description = "What region am i in?",
      origin = DecreeOrigin.PLAYER
   )
   public void region() {
      try {
         Chunk var1 = this.world().getChunkAt(this.player().getLocation().getBlockZ() / 16, this.player().getLocation().getBlockZ() / 16);
         IrisRegion var2 = this.engine().getRegion(var1);
         VolmitSender var10000 = this.sender();
         String var10001 = var2.getLoadKey();
         var10000.sendMessage("IRegion: " + var10001 + " (" + var2.getName() + ")");
      } catch (Throwable var3) {
         Iris.reportError(var3);
         this.sender().sendMessage(String.valueOf(C.IRIS) + "Iris worlds only.");
      }

   }

   @Decree(
      description = "What block am i looking at?",
      origin = DecreeOrigin.PLAYER
   )
   public void block() {
      BlockData var1;
      try {
         var1 = this.player().getTargetBlockExact(128, FluidCollisionMode.NEVER).getBlockData();
      } catch (NullPointerException var3) {
         Iris.reportError(var3);
         this.sender().sendMessage("Please look at any block, not at the sky");
         var1 = null;
      }

      if (var1 != null) {
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage("Material: " + var10001 + var1.getMaterial().name());
         var10000 = this.sender();
         var10001 = String.valueOf(C.WHITE);
         var10000.sendMessage("Full: " + var10001 + var1.getAsString(true));
         if (B.isStorage(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Storage Block (Loot Capable)");
         }

         if (B.isLit(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Lit Block (Light Capable)");
         }

         if (B.isFoliage(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Foliage Block");
         }

         if (B.isDecorant(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Decorant Block");
         }

         if (B.isFluid(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Fluid Block");
         }

         if (B.isFoliagePlantable(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Plantable Foliage Block");
         }

         if (B.isSolid(var1)) {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "* Solid Block");
         }
      }

   }

   @Decree(
      description = "Show markers in chunk",
      origin = DecreeOrigin.PLAYER
   )
   public void markers(@Param(description = "Marker name such as cave_floor or cave_ceiling") String marker) {
      Chunk var2 = this.player().getLocation().getChunk();
      if (IrisToolbelt.isIrisWorld(var2.getWorld())) {
         boolean var3 = true;
         AtomicInteger var4 = new AtomicInteger(0);

         for(int var5 = var2.getX() - 4; var5 <= var2.getX() + 4; ++var5) {
            for(int var6 = var2.getZ() - 4; var6 <= var2.getZ() + 4; ++var6) {
               IrisToolbelt.access(var2.getWorld()).getEngine().getMantle().findMarkers(var5, var6, new MatterMarker(var1)).convert((var1x) -> {
                  return var1x.toLocation(var2.getWorld());
               }).forEach((var1x) -> {
                  J.s(() -> {
                     BlockSignal.of(var1x.getBlock(), 100);
                  });
                  var4.incrementAndGet();
               });
            }
         }

         VolmitSender var10000 = this.sender();
         int var10001 = var4.get();
         var10000.sendMessage("Found " + var10001 + " Nearby Markers (" + var1 + ")");
      } else {
         this.sender().sendMessage(String.valueOf(C.IRIS) + "Iris worlds only.");
      }

   }
}
