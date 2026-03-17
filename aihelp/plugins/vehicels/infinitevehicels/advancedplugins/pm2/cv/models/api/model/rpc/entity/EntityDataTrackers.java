package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.api.utils.scheduling.PlatformScheduler;
import advancedplugins.pm2.cv.models.api.utils.ticker.AbstractLoadBalancer;
import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import advancedplugins.pm2.cv.models.api.utils.ticker.LoadBalancer;
import advancedplugins.pm2.cv.models.api.utils.ticker.PseudoThread;
import advancedplugins.pm2.cv.models.api.utils.ticker.Task;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityDataTrackers extends AbstractLoadBalancer<UUID, EntityDataTrackers.Tracker> {
   private final Map<EntityDataTrackers.Tracker, PseudoThread> threads = new ConcurrentHashMap();
   private final JavaPlugin plugin;
   private final PlatformScheduler scheduler;
   private final int maximumThreads;
   private int trackerCount = 0;
   private boolean started;

   public EntityDataTrackers(JavaPlugin var1, PlatformScheduler var2) {
      super(ConfigProperty.CULLING_THREADS.getInt());
      this.plugin = var1;
      this.scheduler = var2;
      this.maximumThreads = Math.max(ConfigProperty.MAX_CULLING_THREADS.getInt(), this.available.size());
      this.available.forEach(this::setup);
   }

   public EntityDataTrackers.Tracker supply() {
      return new EntityDataTrackers.Tracker();
   }

   public void start() {
      this.started = true;
      Iterator var1 = this.threads.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         ((PseudoThread)var2.getValue()).start();
      }

   }

   public void end() {
      Iterator var1 = this.threads.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         ((PseudoThread)var2.getValue()).end();
      }

   }

   private void setup(EntityDataTrackers.Tracker var1) {
      var1.setId("data_tracker_" + this.trackerCount);
      PseudoThread var2 = new PseudoThread(var1.getId(), this.scheduler, this.plugin, true, 0, 0, true, false);
      var2.registerOverloadCallback((var2x) -> {
         String var3 = var1.getId();
         LogUtil.debug(var3 + " is overloaded with " + var1.getLoad() + " targets.");
         if (this.available.size() < this.maximumThreads) {
            this.growAndBalance(var1, var2x);
         }

      });
      var2.queueTask(new Task((var1x) -> {
         var1.asyncFetchEntityData();
      }, 0, 0, true));
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var1);
      DualTicker.queueRepeatingSyncTask((Runnable)(var1::fetchEntityData), 0, 0);
      this.threads.put(var1, var2);
      if (this.started) {
         var2.start();
      }

      ++this.trackerCount;
   }

   private void growAndBalance(EntityDataTrackers.Tracker var1, int var2) {
      LogUtil.debug(var1.id + " has skipped " + var2 + " ticks. Requested a new server.");
      EntityDataTrackers.Tracker var3 = this.supply();
      int var4 = var1.dataTrackers.size() / 2;

      for(Iterator var5 = var1.dataTrackers.entrySet().iterator(); var5.hasNext(); --var4) {
         Entry var6 = (Entry)var5.next();
         if (var4 <= 0) {
            break;
         }

         var3.putEntityData((UUID)var6.getKey(), (IEntityData)var6.getValue());
         this.reference.put((UUID)var6.getKey(), var3);
      }

      Set var7 = var3.dataTrackers.keySet();
      Map var8 = var1.dataTrackers;
      Objects.requireNonNull(var8);
      Objects.requireNonNull(var8);
      var7.forEach(var8::remove);
      this.available.add(var3);
      this.setup(var3);
      LogUtil.debug("- Created " + var3.id + ".");
   }

   public class Tracker implements LoadBalancer.Server {
      private final Map<UUID, IEntityData> dataTrackers = Maps.newConcurrentMap();
      private String id = "Unknown";
      private int lastSize = 0;
      private long timings;

      public void fetchEntityData() {
         Iterator var1 = this.dataTrackers.entrySet().iterator();

         while(true) {
            while(var1.hasNext()) {
               Entry var2 = (Entry)var1.next();
               IEntityData var3 = (IEntityData)var2.getValue();
               if (var3.isDataValid() && (ModelAPI.isModeledEntity((UUID)var2.getKey()) || ModelAPI.isVisual((UUID)var2.getKey()))) {
                  var3.syncUpdate();
               } else {
                  var3.destroy();
                  this.dataTrackers.remove(var2.getKey());
                  EntityDataTrackers.this.unregister((UUID)var2.getKey());
               }
            }

            return;
         }
      }

      public void asyncFetchEntityData() {
         long var1 = System.currentTimeMillis();
         Iterator var3 = this.dataTrackers.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            IEntityData var5 = (IEntityData)var4.getValue();
            var5.cullUpdate();
         }

         this.timings = System.currentTimeMillis() - var1;
         if (this.lastSize != this.dataTrackers.size()) {
            this.lastSize = this.dataTrackers.size();
            LogUtil.debug(this.id + ": " + this.lastSize + " - " + this.timings);
         }

      }

      public void putEntityData(UUID var1, IEntityData var2) {
         this.dataTrackers.put(var1, var2);
      }

      public IEntityData getEntityData(UUID var1) {
         return (IEntityData)this.dataTrackers.get(var1);
      }

      public IEntityData removeEntityData(UUID var1) {
         EntityDataTrackers.this.unregister(var1);
         return (IEntityData)this.dataTrackers.remove(var1);
      }

      public int getLoad() {
         return this.lastSize;
      }

      public Map<UUID, IEntityData> getDataTrackers() {
         return this.dataTrackers;
      }

      public String getId() {
         return this.id;
      }

      public void setId(String var1) {
         this.id = var1;
      }

      public long getTimings() {
         return this.timings;
      }
   }
}
