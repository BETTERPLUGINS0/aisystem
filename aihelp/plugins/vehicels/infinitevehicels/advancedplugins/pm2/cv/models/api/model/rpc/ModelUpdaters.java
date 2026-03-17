package advancedplugins.pm2.cv.models.api.model.rpc;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.nms.network.NetworkHandler;
import advancedplugins.pm2.cv.models.api.utils.Profiler;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.config.DebugToggle;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class ModelUpdaters {
   private final ExecutorService executors;
   private final Object2ObjectMap<UUID, IModelContainer> uuidLookup;
   private final Int2ObjectMap<UUID> idToUUID;
   private final RenderParsers parsers;
   private final NetworkHandler networkHandler;
   private final ObjectSet<UUID> desyncMonitored;
   private final AtomicReference<CompletableFuture<Void>> lastTickFuture;
   private final Profiler profiler;

   public ModelUpdaters() {
      this.executors = Executors.newWorkStealingPool(ConfigProperty.MAX_ENGINE_THREADS.getInt());
      this.uuidLookup = new Object2ObjectOpenHashMap();
      this.idToUUID = new Int2ObjectOpenHashMap();
      this.desyncMonitored = new ObjectOpenHashSet();
      this.lastTickFuture = new AtomicReference(CompletableFuture.completedFuture((Object)null));
      this.profiler = new Profiler(200);
      this.parsers = ModelAPI.getNMSHandler().createParsers();
      this.networkHandler = ModelAPI.getNetworkHandler();
   }

   public void start() {
      DualTicker.queueRepeatingSyncTask((Runnable)(this::tick), 0, 0);
   }

   private void log(String var1) {
      if (DebugToggle.isDebugging(DebugToggle.OOM_TEST)) {
         LogUtil.log(var1);
      }

   }

   public void tick() {
      this.log("Executor Tick");
      synchronized(this.executors) {
         this.log("Synchronized Executor Tick");
         CompletableFuture var2 = (CompletableFuture)this.lastTickFuture.get();
         CompletableFuture var3 = var2.thenComposeAsync((var1) -> {
            this.log("Starting Profiling");
            this.profiler.startProfiling();
            this.log(" - Started Profiling");
            ArrayList var2 = new ArrayList();
            this.log(" - Queuing " + this.uuidLookup.size() + " updates");
            ObjectIterator var3 = this.uuidLookup.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               IModelContainer var5 = (IModelContainer)var4.getValue();
               BaseEntity var6 = var5.getBase();
               CompletableFuture var7 = CompletableFuture.runAsync(() -> {
                  try {
                     var5.runTickTasks(IModelContainer.Phase.PRE_DATA_SYNC);
                     var6.getData().asyncUpdate();
                     var5.runTickTasks(IModelContainer.Phase.PRE_MODEL_TICK);
                     if (!var5.tick()) {
                        this.forceRemoveModeledEntity(var5);
                     } else {
                        var5.runTickTasks(IModelContainer.Phase.PRE_MODEL_RENDER);
                        this.forRenderers(var5, (var1) -> {
                           var1.dispatch(this.parsers);
                        });
                        var5.runTickTasks(IModelContainer.Phase.POST_MODEL_RENDER);
                     }

                  } catch (Exception var4) {
                     throw new RuntimeException("Failed to load entity, UUID " + String.valueOf(var6.getUUID()) + " with an entity ID of " + var6.getEntityId() + ":", var4);
                  }
               }, this.executors).handle((var0, var1x) -> {
                  if (var1x != null) {
                     var1x.printStackTrace();
                  }

                  return null;
               });
               var2.add(var7);
            }

            this.log(" - Updates Queued");
            return CompletableFuture.allOf((CompletableFuture[])var2.toArray(new CompletableFuture[0])).handle((var1x, var2x) -> {
               this.log("Stopping Profiling");
               this.profiler.stopProfiling();
               this.log(" - Stopped Profiling");
               if (var2x != null) {
                  var2x.printStackTrace();
               }

               return null;
            });
         }, this.executors).handle((var0, var1) -> {
            if (var1 != null) {
               var1.printStackTrace();
            }

            return null;
         });
         this.lastTickFuture.set(var3);
         ObjectIterator var4 = this.desyncMonitored.iterator();

         while(var4.hasNext()) {
            UUID var5 = (UUID)var4.next();
            this.networkHandler.ping(var5);
         }

      }
   }

   public void startDesyncMonitor(UUID var1) {
      synchronized(this.executors) {
         this.desyncMonitored.add(var1);
      }
   }

   public void stopDesyncMonitor(UUID var1) {
      synchronized(this.executors) {
         this.desyncMonitored.remove(var1);
      }
   }

   private void forRenderers(IModelContainer var1, Consumer<ModelRenderer> var2) {
      IEntityData var3 = var1.getBase().getData();
      Map var4 = var1.getModels();
      if (!var4.isEmpty()) {
         ArrayList var5 = new ArrayList();
         Iterator var6 = var4.values().iterator();

         IVisualModel var7;
         while(var6.hasNext()) {
            var7 = (IVisualModel)var6.next();
            if (!var7.getModelRenderer().isReady()) {
               var5.add(var7);
            }
         }

         if (!var5.isEmpty()) {
            return;
         }

         var6 = var4.values().iterator();

         while(var6.hasNext()) {
            var7 = (IVisualModel)var6.next();
            var2.accept(var7.getModelRenderer());
         }

         var3.cleanup();
      }

   }

   public void end() {
      this.executors.shutdown();

      try {
         if (!this.executors.awaitTermination(10L, TimeUnit.SECONDS)) {
            this.executors.shutdownNow();
         }
      } catch (InterruptedException var2) {
         this.executors.shutdownNow();
      }

   }

   public Optional<IModelContainer> registerModeledEntity(BaseEntity<?> var1, IModelContainer var2) {
      synchronized(this.executors) {
         this.idToUUID.put(var1.getEntityId(), var1.getUUID());
         return Optional.ofNullable((IModelContainer)this.uuidLookup.put(var1.getUUID(), var2));
      }
   }

   public IModelContainer getModeledEntity(int var1) {
      return this.getModeledEntity((UUID)this.idToUUID.get(var1));
   }

   public IModelContainer getModeledEntity(UUID var1) {
      return var1 == null ? null : (IModelContainer)this.uuidLookup.get(var1);
   }

   public IModelContainer removeModeledEntity(int var1) {
      return this.removeModeledEntity((UUID)this.idToUUID.get(var1));
   }

   public IModelContainer removeModeledEntity(UUID var1) {
      if (var1 == null) {
         return null;
      } else {
         IModelContainer var2 = (IModelContainer)this.uuidLookup.get(var1);
         if (var2 != null) {
            var2.markRemoved();
         }

         return var2;
      }
   }

   public void forceRemoveModeledEntity(IModelContainer var1) {
      synchronized(this.executors) {
         this.uuidLookup.remove(var1.getBase().getUUID());
         this.idToUUID.remove(var1.getBase().getEntityId());
         var1.getBase().setForcedAlive(false);
         var1.destroy();
      }
   }

   public void saveAllModels() {
      ObjectIterator var1 = this.uuidLookup.values().iterator();

      while(var1.hasNext()) {
         IModelContainer var2 = (IModelContainer)var1.next();
         Object var3 = var2.getBase().getOriginal();
         if (var3 instanceof Entity) {
            Entity var4 = (Entity)var3;
            if (!(var4 instanceof Player)) {
               var2.save().ifPresent((var1x) -> {
                  var4.getPersistentDataContainer().set(SavedData.DATA_KEY, PersistentDataType.STRING, var1x.toString());
               });
            }
         }
      }

   }

   @Generated
   public ExecutorService getExecutors() {
      return this.executors;
   }

   @Generated
   public Object2ObjectMap<UUID, IModelContainer> getUuidLookup() {
      return this.uuidLookup;
   }

   @Generated
   public Int2ObjectMap<UUID> getIdToUUID() {
      return this.idToUUID;
   }

   @Generated
   public RenderParsers getParsers() {
      return this.parsers;
   }

   @Generated
   public NetworkHandler getNetworkHandler() {
      return this.networkHandler;
   }

   @Generated
   public ObjectSet<UUID> getDesyncMonitored() {
      return this.desyncMonitored;
   }

   @Generated
   public AtomicReference<CompletableFuture<Void>> getLastTickFuture() {
      return this.lastTickFuture;
   }

   @Generated
   public Profiler getProfiler() {
      return this.profiler;
   }
}
