package com.volmit.iris.core.edit;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.core.service.WandSVC;
import com.volmit.iris.engine.object.IrisDirection;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawPieceConnector;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.Cuboid;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class JigsawEditor implements Listener {
   public static final KMap<Player, JigsawEditor> editors = new KMap();
   private final Player player;
   private final IrisObject object;
   private final File targetSaveLocation;
   private final IrisJigsawPiece piece;
   private final Location origin;
   private final Cuboid cuboid;
   private final int ticker;
   private final KMap<IrisPosition, Runnable> falling = new KMap();
   private final ChronoLatch cl = new ChronoLatch(100L);
   private Location target;

   public JigsawEditor(Player player, IrisJigsawPiece piece, IrisObject object, File saveLocation) {
      if (var3 == null) {
         throw new RuntimeException("Object is null! " + var2.getObject());
      } else {
         editors.compute(var1, (var1x, var2x) -> {
            if (var2x != null) {
               var2x.exit();
            }

            return this;
         });
         this.object = var3;
         this.player = var1;
         this.origin = var1.getLocation().clone().add(0.0D, 7.0D, 0.0D);
         this.target = this.origin;
         this.targetSaveLocation = var4;
         this.piece = var2 == null ? new IrisJigsawPiece() : var2;
         this.piece.setObject(var3.getLoadKey());
         this.cuboid = new Cuboid(this.origin.clone(), this.origin.clone().add((double)(var3.getW() - 1), (double)(var3.getH() - 1), (double)(var3.getD() - 1)));
         this.ticker = J.sr(this::onTick, 0);
         J.s(() -> {
            var3.placeCenterY(this.origin);
         });
         Iris.instance.registerListener(this);
      }
   }

   @EventHandler
   public void on(PlayerMoveEvent e) {
      if (var1.getPlayer().equals(this.player)) {
         try {
            this.target = this.player.getTargetBlockExact(7).getLocation();
         } catch (Throwable var5) {
            Iris.reportError(var5);
            this.target = this.player.getLocation();
            return;
         }

         if (this.cuboid.contains(this.target)) {
            Iterator var2 = this.falling.k().iterator();

            while(var2.hasNext()) {
               IrisPosition var3 = (IrisPosition)var2.next();
               Location var4 = this.toLocation(var3);
               if (var4.equals(this.target)) {
                  ((Runnable)this.falling.remove(var3)).run();
               }
            }
         }
      }

   }

   public Location toLocation(IrisPosition i) {
      return this.origin.clone().add(new Vector(var1.getX(), var1.getY(), var1.getZ())).add(this.object.getCenter()).getBlock().getLocation();
   }

   public IrisPosition toPosition(Location l) {
      return new IrisPosition(var1.clone().getBlock().getLocation().subtract(this.origin.clone()).subtract(this.object.getCenter()).add(1.0D, 1.0D, 1.0D).toVector());
   }

   @EventHandler
   public void on(PlayerInteractEvent e) {
      if (var1.getAction().equals(Action.RIGHT_CLICK_BLOCK) && var1.getClickedBlock() != null && this.cuboid.contains(var1.getClickedBlock().getLocation()) && var1.getPlayer().equals(this.player)) {
         IrisPosition var2 = this.toPosition(var1.getClickedBlock().getLocation());
         IrisJigsawPieceConnector var3 = null;
         Iterator var4 = this.piece.getConnectors().iterator();

         while(var4.hasNext()) {
            IrisJigsawPieceConnector var5 = (IrisJigsawPieceConnector)var4.next();
            if (var5.getPosition().equals(var2)) {
               var3 = var5;
               break;
            }
         }

         if (!this.player.isSneaking() && var3 == null) {
            var3 = new IrisJigsawPieceConnector();
            var3.setDirection(IrisDirection.getDirection(var1.getBlockFace()));
            var3.setPosition(var2);
            this.piece.getConnectors().add((Object)var3);
            this.player.playSound(var1.getClickedBlock().getLocation(), Sound.ENTITY_ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
         } else if (this.player.isSneaking() && var3 != null) {
            this.piece.getConnectors().remove(var3);
            this.player.playSound(var1.getClickedBlock().getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
         } else if (var3 != null && !this.player.isSneaking()) {
            var3.setDirection(IrisDirection.getDirection(var1.getBlockFace()));
            this.player.playSound(var1.getClickedBlock().getLocation(), Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1.0F, 1.0F);
         }
      }

   }

   private void removeKey(JSONObject o, String... path) {
      if (var2.length == 1) {
         var1.remove(var2[0]);
      } else {
         ArrayList var3 = new ArrayList(List.of(var2));
         var3.remove(0);
         this.removeKey(var1.getJSONObject(var2[0]), (String[])var3.toArray(new String[0]));
      }
   }

   private List<JSONObject> getObjectsInArray(JSONObject a) {
      KList var2 = new KList();

      for(int var3 = 0; var3 < var1.getJSONArray("connectors").length(); ++var3) {
         var2.add((Object)var1.getJSONArray("connectors").getJSONObject(var3));
      }

      return var2;
   }

   public void close() {
      this.exit();

      try {
         JSONObject var1 = new JSONObject((new Gson()).toJson(this.piece));
         this.removeKey(var1, "placementOptions", "translateCenter");
         J.attempt(() -> {
            var1.getJSONObject("placementOptions").remove("translateCenter");
         });
         this.removeKey(var1, "placementOptions");
         var1.remove("placementOptions");
         Iterator var2 = this.getObjectsInArray(var1).iterator();

         while(var2.hasNext()) {
            JSONObject var3 = (JSONObject)var2.next();
            this.removeKey(var3, "rotateConnector");
         }

         IO.writeAll(this.targetSaveLocation, (Object)var1.toString(4));
      } catch (IOException var4) {
         Iris.reportError(var4);
         var4.printStackTrace();
      }

   }

   public void exit() {
      J.car(this.ticker);
      Iris.instance.unregisterListener(this);

      try {
         J.sfut(() -> {
            this.object.unplaceCenterY(this.origin);
            this.falling.v().forEach(Runnable::run);
         }).get();
      } catch (ExecutionException | InterruptedException var2) {
         var2.printStackTrace();
      }

      editors.remove(this.player);
   }

   public void onTick() {
      if (this.cl.flip()) {
         ((WandSVC)Iris.service(WandSVC.class)).draw(this.cuboid, this.player);
         Iterator var1 = this.falling.k().iterator();

         while(true) {
            label50:
            while(var1.hasNext()) {
               IrisPosition var2 = (IrisPosition)var1.next();
               Iterator var3 = this.piece.getConnectors().iterator();

               while(var3.hasNext()) {
                  IrisJigsawPieceConnector var4 = (IrisJigsawPieceConnector)var3.next();
                  if (var4.getPosition().equals(var2)) {
                     continue label50;
                  }
               }

               ((Runnable)this.falling.remove(var2)).run();
            }

            var1 = this.piece.getConnectors().iterator();

            while(var1.hasNext()) {
               IrisJigsawPieceConnector var7 = (IrisJigsawPieceConnector)var1.next();
               IrisPosition var8 = var7.getPosition();
               Location var9 = this.toLocation(var8);
               Vector var5 = var7.getDirection().toVector().clone();

               for(int var6 = 0; var6 < RNG.r.i(1, 3); ++var6) {
                  var9.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, var9.clone().getBlock().getLocation().add(0.25D, 0.25D, 0.25D).add(RNG.r.d(0.5D), RNG.r.d(0.5D), RNG.r.d(0.5D)), 0, var5.getX(), var5.getY(), var5.getZ(), 0.092D + RNG.r.d(-0.03D, 0.08D));
               }

               if (!var9.getBlock().getLocation().equals(this.target) && !this.falling.containsKey(var8)) {
                  if (var9.getBlock().getType().isAir()) {
                     var9.getBlock().setType(Material.STONE);
                  }

                  this.falling.put(var8, BlockSignal.forever(var9.getBlock()));
               }
            }
            break;
         }
      }

   }
}
