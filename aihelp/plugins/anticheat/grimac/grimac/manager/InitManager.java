package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.manager.init.load.LoadableInitable;
import ac.grim.grimac.manager.init.load.PacketEventsInit;
import ac.grim.grimac.manager.init.start.CommandRegister;
import ac.grim.grimac.manager.init.start.JavaVersion;
import ac.grim.grimac.manager.init.start.PacketLimiter;
import ac.grim.grimac.manager.init.start.PacketManager;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.manager.init.start.TAB;
import ac.grim.grimac.manager.init.start.TickRunner;
import ac.grim.grimac.manager.init.start.UpdateChecker;
import ac.grim.grimac.manager.init.start.ViaBackwardsManager;
import ac.grim.grimac.manager.init.start.ViaVersion;
import ac.grim.grimac.manager.init.stop.StoppableInitable;
import ac.grim.grimac.manager.init.stop.TerminatePacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.utils.anticheat.LogUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import lombok.Generated;

public class InitManager {
   private final ImmutableList<LoadableInitable> initializersOnLoad;
   private final ImmutableList<StartableInitable> initializersOnStart;
   private final ImmutableList<StoppableInitable> initializersOnStop;
   private boolean loaded = false;
   private boolean started = false;
   private boolean stopped = false;

   public InitManager(PacketEventsAPI<?> packetEventsAPI, Initable... platformSpecificInitables) {
      ArrayList<LoadableInitable> extraLoadableInitables = new ArrayList();
      ArrayList<StartableInitable> extraStartableInitables = new ArrayList();
      ArrayList<StoppableInitable> extraStoppableInitables = new ArrayList();
      Initable[] var6 = platformSpecificInitables;
      int var7 = platformSpecificInitables.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Initable initable = var6[var8];
         if (initable instanceof LoadableInitable) {
            extraLoadableInitables.add((LoadableInitable)initable);
         }

         if (initable instanceof StartableInitable) {
            extraStartableInitables.add((StartableInitable)initable);
         }

         if (initable instanceof StoppableInitable) {
            extraStoppableInitables.add((StoppableInitable)initable);
         }
      }

      this.initializersOnLoad = ImmutableList.builder().add(new PacketEventsInit(packetEventsAPI)).add(() -> {
         GrimAPI.INSTANCE.getExternalAPI().load();
      }).addAll(extraLoadableInitables).build();
      this.initializersOnStart = ImmutableList.builder().add(GrimAPI.INSTANCE.getExternalAPI()).add(new PacketManager()).add(new ViaBackwardsManager()).add(new TickRunner()).add(new CommandRegister(GrimAPI.INSTANCE.getCommandService())).add(new UpdateChecker()).add(new PacketLimiter()).add(GrimAPI.INSTANCE.getAlertManager()).add(GrimAPI.INSTANCE.getDiscordManager()).add(GrimAPI.INSTANCE.getSpectateManager()).add(GrimAPI.INSTANCE.getViolationDatabaseManager()).add(new JavaVersion()).add(new ViaVersion()).add(new TAB()).addAll(extraStartableInitables).build();
      this.initializersOnStop = ImmutableList.builder().add(new TerminatePacketEvents()).addAll(extraStoppableInitables).build();
   }

   public void load() {
      UnmodifiableIterator var1 = this.initializersOnLoad.iterator();

      while(var1.hasNext()) {
         LoadableInitable initable = (LoadableInitable)var1.next();

         try {
            initable.load();
         } catch (Exception var4) {
            LogUtil.error("Failed to load " + initable.getClass().getSimpleName(), var4);
         }
      }

      this.loaded = true;
   }

   public void start() {
      UnmodifiableIterator var1 = this.initializersOnStart.iterator();

      while(var1.hasNext()) {
         StartableInitable initable = (StartableInitable)var1.next();

         try {
            initable.start();
         } catch (Exception var4) {
            LogUtil.error("Failed to start " + initable.getClass().getSimpleName(), var4);
         }
      }

      this.started = true;
   }

   public void stop() {
      UnmodifiableIterator var1 = this.initializersOnStop.iterator();

      while(var1.hasNext()) {
         StoppableInitable initable = (StoppableInitable)var1.next();

         try {
            initable.stop();
         } catch (Exception var4) {
            LogUtil.error("Failed to stop " + initable.getClass().getSimpleName(), var4);
         }
      }

      this.stopped = true;
   }

   @Generated
   public boolean isLoaded() {
      return this.loaded;
   }

   @Generated
   public boolean isStarted() {
      return this.started;
   }

   @Generated
   public boolean isStopped() {
      return this.stopped;
   }
}
