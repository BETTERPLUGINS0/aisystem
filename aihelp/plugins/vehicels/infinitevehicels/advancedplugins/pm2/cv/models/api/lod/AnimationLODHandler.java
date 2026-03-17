package advancedplugins.pm2.cv.models.api.lod;

import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.utils.callback.ExecutionCallback;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.Box;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class AnimationLODHandler {
   private static final Set<UUID> ACTIVE = new ObjectOpenHashSet();
   private static final ExecutionCallback<Consumer<Boolean>> TOGGLE_EXECUTION_CALLBACK = new ExecutionCallback((var0) -> {
      return (var1) -> {
         var0.forEach((var1x) -> {
            var1x.accept(var1);
         });
      };
   });
   private static final ExecutionCallback<BiConsumer<UUID, Boolean>> PLAYER_EXECUTION_CALLBACK = new ExecutionCallback((var0) -> {
      return (var1, var2) -> {
         var0.forEach((var2x) -> {
            var2x.accept(var1, var2);
         });
      };
   });
   private static boolean ENABLED;
   private static boolean DEFAULT;
   private static double FALLOFF_LENGTH;
   private static double FALLOFF_MULTIPLIER;
   private static double IGNORE_DISTANCE;
   private static double IGNORE_DISTANCE_SQR;
   private static double IGNORE_MULTIPLIER;
   private static double MAX_RATE_MULTIPLIER;
   private static double MIN_RATE_MULTIPLIER;
   private final IModelContainer modelContainer;
   private final UUID toggleCallback;
   private final UUID playerCallback;
   private final Object2ObjectMap<UUID, AnimationLODHandler.LODTracker> trackers = new Object2ObjectOpenHashMap();
   private final AnimationLODHandler.LODTracker fullUpdate;
   private Boolean enabled;

   public AnimationLODHandler(IModelContainer var1) {
      this.modelContainer = var1;
      this.fullUpdate = new AnimationLODHandler.LODTracker(UUID.randomUUID(), var1);
      this.fullUpdate.canSkip = false;
      this.toggleCallback = TOGGLE_EXECUTION_CALLBACK.subscribe((var1x) -> {
         if (!var1x) {
            this.trackers.clear();
         }

      });
      this.playerCallback = PLAYER_EXECUTION_CALLBACK.subscribe(this::updatePlayer);
   }

   public static void updateConfig() {
      ENABLED = ConfigProperty.ANIMATION_LOD_ENABLED.getBoolean();
      DEFAULT = ConfigProperty.ANIMATION_LOD_DEFAULT.getBoolean();
      FALLOFF_LENGTH = ConfigProperty.ANIMATION_LOD_FALLOFF_LENGTH.getDouble();
      FALLOFF_MULTIPLIER = ConfigProperty.ANIMATION_LOD_FALLOFF_MULTIPLIER.getDouble();
      IGNORE_DISTANCE = ConfigProperty.ANIMATION_LOD_IGNORE_DISTANCE.getDouble();
      IGNORE_DISTANCE_SQR = IGNORE_DISTANCE * IGNORE_DISTANCE;
      IGNORE_MULTIPLIER = ConfigProperty.ANIMATION_LOD_IGNORE_MULTIPLIER.getDouble();
      MAX_RATE_MULTIPLIER = ConfigProperty.ANIMATION_LOD_MAX_RATE_MULTIPLIER.getDouble();
      MIN_RATE_MULTIPLIER = ConfigProperty.ANIMATION_LOD_MIN_RATE_MULTIPLIER.getDouble();
   }

   public static boolean isGlobalEnabled() {
      return ENABLED;
   }

   public static void setGlobalEnabled(boolean var0) {
      if (ENABLED != var0) {
         ENABLED = var0;
         ((Consumer)TOGGLE_EXECUTION_CALLBACK.invoker()).accept(var0);
      }

   }

   public static void registerPlayer(UUID var0) {
      setPlayerActive(var0, DEFAULT);
   }

   public static void setPlayerActive(UUID var0, boolean var1) {
      if (var1 && !ACTIVE.contains(var0)) {
         ACTIVE.add(var0);
         ((BiConsumer)PLAYER_EXECUTION_CALLBACK.invoker()).accept(var0, true);
      } else if (!var1) {
         ACTIVE.remove(var0);
         ((BiConsumer)PLAYER_EXECUTION_CALLBACK.invoker()).accept(var0, false);
      }

   }

   public AnimationLODHandler.LODTracker tick(UUID var1) {
      if (this.enabled() && ACTIVE.contains(var1)) {
         AnimationLODHandler.LODTracker var2 = (AnimationLODHandler.LODTracker)this.trackers.computeIfAbsent(var1, (var2x) -> {
            return new AnimationLODHandler.LODTracker(var1, this.modelContainer);
         });
         if (var2.tick(ServerInfo.getCurrentTick())) {
            this.trackers.remove(var1);
            var2.canSkip = true;
         }

         return var2;
      } else {
         return this.fullUpdate;
      }
   }

   public boolean enabled() {
      return this.enabled == null ? ENABLED : this.enabled;
   }

   public void destroy() {
      TOGGLE_EXECUTION_CALLBACK.unsubscribe(this.toggleCallback);
      PLAYER_EXECUTION_CALLBACK.unsubscribe(this.playerCallback);
   }

   private void updatePlayer(UUID var1, boolean var2) {
      if (var2) {
         this.trackers.computeIfAbsent(var1, (var2x) -> {
            return new AnimationLODHandler.LODTracker(var1, this.modelContainer);
         });
      } else {
         this.trackers.remove(var1);
      }

   }

   public void setEnabled(Boolean var1) {
      this.enabled = var1;
      if (!this.enabled()) {
         this.trackers.clear();
      }

   }

   @Generated
   public IModelContainer getModelContainer() {
      return this.modelContainer;
   }

   @Generated
   public UUID getToggleCallback() {
      return this.toggleCallback;
   }

   @Generated
   public UUID getPlayerCallback() {
      return this.playerCallback;
   }

   @Generated
   public Object2ObjectMap<UUID, AnimationLODHandler.LODTracker> getTrackers() {
      return this.trackers;
   }

   @Generated
   public AnimationLODHandler.LODTracker getFullUpdate() {
      return this.fullUpdate;
   }

   @Generated
   public Boolean getEnabled() {
      return this.enabled;
   }

   public class LODTracker {
      private final UUID uuid;
      private final IModelContainer modelContainer;
      private final DataTracker<Integer> tickDuration = new DataTracker(1);
      private int lastCheckedTick = ServerInfo.getCurrentTick();
      private double distanceSqr = -1.0D;
      private double tickRate = 1.0D;
      private double cumulatedTick = 0.0D;
      private int lastSuccessfulTick = 0;
      private boolean canSkip;

      public boolean tick(int var1) {
         if (this.canRemoveSelf()) {
            this.canSkip = true;
            return true;
         } else if (this.lastCheckedTick == var1) {
            return false;
         } else {
            this.lastCheckedTick = var1;
            this.calculateTickRate();
            this.cumulatedTick += this.tickRate;
            int var2 = MathUtils.floor(this.cumulatedTick);
            this.canSkip = this.lastSuccessfulTick == var2;
            this.lastSuccessfulTick = var2;
            if (!this.canSkip) {
               this.tickDuration.clearDirty();
               this.tickDuration.set(Math.max(MathUtils.ceil((Math.ceil(this.cumulatedTick) - this.cumulatedTick) / this.tickRate), 1));
            }

            return false;
         }
      }

      public boolean canRemoveSelf() {
         return !AnimationLODHandler.this.enabled() || !AnimationLODHandler.ACTIVE.contains(this.uuid) || Bukkit.getPlayer(this.uuid) == null;
      }

      public void calculateTickRate() {
         if (AnimationLODHandler.this.enabled() && AnimationLODHandler.ACTIVE.contains(this.uuid)) {
            Player var1 = Bukkit.getPlayer(this.uuid);
            if (var1 != null) {
               BaseEntity var2 = this.modelContainer.getBase();
               Location var3 = var2.getLocation();
               if (var3.getWorld() == var1.getWorld()) {
                  double var4 = var3.distanceSquared(var1.getLocation());
                  if (!MathUtils.isSimilar(var4, this.distanceSqr)) {
                     this.distanceSqr = var4;
                     if (AnimationLODHandler.IGNORE_DISTANCE_SQR >= var4) {
                        this.tickRate = 1.0D;
                     } else {
                        Box var6 = var2.getData().getCullHitbox();
                        BoundingBox var7 = var6 == null ? var2.getBoundingBox() : var6.createBoundingBox(var3.toVector());
                        double var8 = (var7.getWidthX() + var7.getHeight() + var7.getWidthZ()) / 3.0D * AnimationLODHandler.IGNORE_MULTIPLIER;
                        float var10 = (float)Math.max(0.0D, Math.sqrt(var4) - AnimationLODHandler.IGNORE_DISTANCE * var8);
                        if (MathUtils.isSimilar(var10, 0.0F)) {
                           this.tickRate = 1.0D;
                        } else {
                           this.tickRate = Math.pow(AnimationLODHandler.FALLOFF_MULTIPLIER, (double)var10 / AnimationLODHandler.FALLOFF_LENGTH);
                        }
                     }

                     this.tickRate = MathUtils.clamp(this.tickRate, AnimationLODHandler.MIN_RATE_MULTIPLIER, AnimationLODHandler.MAX_RATE_MULTIPLIER);
                  }
               }
            }
         }

      }

      @Generated
      public LODTracker(final UUID param2, final IModelContainer param3) {
         this.uuid = var2;
         this.modelContainer = var3;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public IModelContainer getModelContainer() {
         return this.modelContainer;
      }

      @Generated
      public DataTracker<Integer> getTickDuration() {
         return this.tickDuration;
      }

      @Generated
      public int getLastCheckedTick() {
         return this.lastCheckedTick;
      }

      @Generated
      public double getDistanceSqr() {
         return this.distanceSqr;
      }

      @Generated
      public double getTickRate() {
         return this.tickRate;
      }

      @Generated
      public double getCumulatedTick() {
         return this.cumulatedTick;
      }

      @Generated
      public int getLastSuccessfulTick() {
         return this.lastSuccessfulTick;
      }

      @Generated
      public boolean isCanSkip() {
         return this.canSkip;
      }

      @Generated
      public void setLastCheckedTick(int var1) {
         this.lastCheckedTick = var1;
      }

      @Generated
      public void setDistanceSqr(double var1) {
         this.distanceSqr = var1;
      }

      @Generated
      public void setTickRate(double var1) {
         this.tickRate = var1;
      }

      @Generated
      public void setCumulatedTick(double var1) {
         this.cumulatedTick = var1;
      }

      @Generated
      public void setLastSuccessfulTick(int var1) {
         this.lastSuccessfulTick = var1;
      }

      @Generated
      public void setCanSkip(boolean var1) {
         this.canSkip = var1;
      }
   }
}
