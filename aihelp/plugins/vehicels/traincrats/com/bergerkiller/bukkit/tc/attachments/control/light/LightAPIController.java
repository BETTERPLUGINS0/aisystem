package com.bergerkiller.bukkit.tc.attachments.control.light;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.tc.TrainCarts;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public abstract class LightAPIController {
   private static final Map<World, LightAPIController> _blockLightControllers = new HashMap();
   private static final Map<World, LightAPIController> _skyLightControllers = new HashMap();
   private static LightAPIController.SyncTask _task;
   private boolean syncPending = false;

   protected LightAPIController() {
   }

   protected void schedule() {
      if (!this.syncPending) {
         this.syncPending = true;
         if (_task == null) {
            _task = new LightAPIController.SyncTask();
            if (_task.getPlugin().isEnabled()) {
               _task.start(1L, 1L);
            }
         }
      }

   }

   public static LightAPIController get(World world, boolean skyLight) {
      Map<World, LightAPIController> map = skyLight ? _skyLightControllers : _blockLightControllers;
      LightAPIController controller = (LightAPIController)map.get(world);
      if (controller == null) {
         boolean isLightAPIV5Installed = false;

         try {
            Class<?> typeLightAPI = Class.forName("ru.beykerykt.minecraft.lightapi.common.LightAPI");
            Class<?> typeEditPolicy = Class.forName("ru.beykerykt.minecraft.lightapi.common.api.engine.EditPolicy");
            Class<?> typeSendPolicy = Class.forName("ru.beykerykt.minecraft.lightapi.common.api.engine.SendPolicy");
            Class<?> typeICallBack = Class.forName("ru.beykerykt.minecraft.lightapi.common.api.engine.sched.ICallback");
            typeLightAPI.getMethod("setLightLevel", String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, typeEditPolicy, typeSendPolicy, typeICallBack);
            isLightAPIV5Installed = true;
         } catch (NoSuchMethodException | SecurityException | ClassNotFoundException var9) {
         }

         Plugin plugin;
         if (isLightAPIV5Installed) {
            try {
               controller = skyLight ? LightAPIControllerV5Impl.forSkyLight(world) : LightAPIControllerV5Impl.forBlockLight(world);
            } catch (Throwable var11) {
               plugin = Bukkit.getPluginManager().getPlugin("LightAPI");
               if (plugin == null) {
                  TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to initialize LightAPI handler: LightAPI plugin is not enabled!");
               } else {
                  TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to initialize LightAPI handler", var11);
               }

               controller = LightAPIControllerUnavailable.INSTANCE;
            }
         } else {
            try {
               controller = skyLight ? LightAPIControllerForkImpl.forSkyLight(world) : LightAPIControllerForkImpl.forBlockLight(world);
            } catch (Throwable var10) {
               plugin = Bukkit.getPluginManager().getPlugin("LightAPI");
               if (plugin == null) {
                  TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to initialize LightAPI-Fork handler: LightAPI-Fork plugin is not enabled!");
               } else if (plugin.getDescription().getMain().equals("ru.beykerykt.minecraft.lightapi.bukkit.impl.BukkitPlugin")) {
                  TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to initialize LightAPI-Fork handler: LightAPI is installed, but you need LightAPI-Fork instead!");
               } else {
                  TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to initialize LightAPI-Fork handler", var10);
               }

               controller = LightAPIControllerUnavailable.INSTANCE;
            }
         }

         map.put(world, controller);
      }

      return (LightAPIController)controller;
   }

   public static void disableWorld(World world) {
      _blockLightControllers.remove(world);
      _skyLightControllers.remove(world);
   }

   public static void disable() {
      _blockLightControllers.clear();
      _skyLightControllers.clear();
      Task.stop(_task);
      _task = null;
   }

   public abstract void add(IntVector3 var1, int var2);

   public abstract void remove(IntVector3 var1, int var2);

   public abstract void move(IntVector3 var1, IntVector3 var2, int var3);

   public abstract void update(IntVector3 var1, int var2, int var3);

   protected abstract boolean onSync();

   public final boolean sync() {
      this.syncPending = false;
      return this.onSync();
   }

   private static class SyncTask extends Task {
      private int ticksIdle = 0;

      public SyncTask() {
         super(TrainCarts.plugin);
      }

      public void run() {
         boolean busy = false;

         Iterator var2;
         LightAPIController controller;
         for(var2 = LightAPIController._blockLightControllers.values().iterator(); var2.hasNext(); busy |= controller.sync()) {
            controller = (LightAPIController)var2.next();
         }

         for(var2 = LightAPIController._skyLightControllers.values().iterator(); var2.hasNext(); busy |= controller.sync()) {
            controller = (LightAPIController)var2.next();
         }

         if (busy) {
            this.ticksIdle = 0;
         } else if (++this.ticksIdle > 100) {
            this.stop();
            LightAPIController._task = null;
         }

      }
   }
}
