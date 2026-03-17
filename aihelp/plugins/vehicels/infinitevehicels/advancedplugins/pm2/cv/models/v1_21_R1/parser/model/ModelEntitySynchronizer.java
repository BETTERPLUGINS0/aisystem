package advancedplugins.pm2.cv.models.v1_21_R1.parser.model;

import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.CullType;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRendererParser;
import advancedplugins.pm2.cv.models.api.utils.data.UpdateScheme;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.PacketTransmissionUtility;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelEntitySynchronizer implements ModelRendererParser<DisplayRenderer> {
   private final Map<String, Set<UUID>> viewerCategories = new ConcurrentHashMap();
   private final Set<Runnable> postProcessingTasks = ConcurrentHashMap.newKeySet();
   private IModelContainer activeContainer;

   public void dispatch(DisplayRenderer renderer) {
      IEntityData var2 = var1.getActiveModel().getModeledEntity().getBase().getData();
      this.activeContainer = var1.getActiveModel().getModeledEntity();
      this.categorizeViewers(var2, var1);
      if (var1.pollFirstSpawn()) {
         HashSet var3 = new HashSet();
         var3.addAll((Collection)this.viewerCategories.getOrDefault("MOVEMENT_ONLY", ImmutableSet.of()));
         var3.addAll((Collection)this.viewerCategories.getOrDefault("NO_CULL", ImmutableSet.of()));
         var3.addAll((Collection)this.viewerCategories.getOrDefault("NO_CULL_FORCE", ImmutableSet.of()));
         this.initializeForViewers(var3, var1);
      } else {
         this.initializeForViewers(var2.getStartTracking(), var1);
         this.synchronizeMovementViewers((Set)this.viewerCategories.getOrDefault("MOVEMENT_ONLY", ImmutableSet.of()), var1, true, false);
         this.synchronizeStandardViewers((Set)this.viewerCategories.getOrDefault("NO_CULL", ImmutableSet.of()), var1, false, false);
         this.synchronizeForcedViewers((Set)this.viewerCategories.getOrDefault("NO_CULL_FORCE", ImmutableSet.of()), var1, false, true);
         this.processCulledViewers((Set)this.viewerCategories.getOrDefault("CULLED", ImmutableSet.of()), var1);
         this.cleanupDisconnectedViewers(var2.getStopTracking(), var1);
      }

      this.clearViewerCategories();
      var1.getPivot().clearDirty();
      var1.getHitbox().clearDirty();
      this.executePostProcessing();
   }

   public void dispose(DisplayRenderer renderer) {
      IEntityData var2 = var1.getActiveModel().getModeledEntity().getBase().getData();
      HashSet var3 = this.consolidateAllViewers(var2);
      this.cleanupDisconnectedViewers(var3, var1);
   }

   private void categorizeViewers(IEntityData data, DisplayRenderer renderer) {
      Iterator var3 = var1.getTracking().entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         UUID var5 = (UUID)var4.getKey();
         CullType var6 = (CullType)var4.getValue();
         switch(var6) {
         case NO_CULL:
            String var7 = var2.pollUpdate(var5) ? "NO_CULL_FORCE" : var6.name();
            ((Set)this.viewerCategories.computeIfAbsent(var7, (var0) -> {
               return new HashSet();
            })).add(var5);
            break;
         case MOVEMENT_ONLY:
            var2.pushUpdate(var5);
            ((Set)this.viewerCategories.computeIfAbsent(var6.name(), (var0) -> {
               return new HashSet();
            })).add(var5);
            break;
         case CULLED:
            ((Set)this.viewerCategories.computeIfAbsent(var6.name(), (var0) -> {
               return new HashSet();
            })).add(var5);
         }
      }

   }

   private HashSet<UUID> consolidateAllViewers(IEntityData data) {
      HashSet var2 = new HashSet(var1.getStartTracking());
      var2.addAll(var1.getTracking().keySet());
      var2.addAll(var1.getStopTracking());
      return var2;
   }

   private void clearViewerCategories() {
      this.viewerCategories.forEach((var0, var1) -> {
         var1.clear();
      });
   }

   private void executePostProcessing() {
      this.postProcessingTasks.forEach(Runnable::run);
      this.postProcessingTasks.clear();
   }

   private void initializeForViewers(Set<UUID> viewers, DisplayRenderer renderer) {
      if (!var1.isEmpty()) {
         PacketBundleProvider var3 = this.createInitializationBundle(var2);
         BaseEntity var4 = var2.getActiveModel().getModeledEntity().getBase();
         if (var4 instanceof BukkitEntity) {
            BukkitEntity var5 = (BukkitEntity)var4;
            Entity var6 = var5.getOriginal();
            if (var6 instanceof Player) {
               Player var7 = (Player)var6;
               if (var1.contains(var7.getUniqueId())) {
                  PacketTransmissionUtility.deliverPacketBundle(var7.getUniqueId(), var3);
               }
            }
         }

         PacketBundleProvider var8 = this.addHitboxPackets(var3, var2);
         PacketTransmissionUtility.deliverBundleConditionally(var1, var8, (var1x) -> {
            return !var1x.getUniqueId().equals(var4.getUUID());
         });
      }
   }

   private PacketBundleProvider createInitializationBundle(DisplayRenderer renderer) {
      PacketBundleProvider var2 = new PacketBundleProvider();
      DisplayRenderer.Pivot var3 = var1.getPivot();
      var2.add(PacketTransmissionUtility.generateAnchorSpawn(var3.getId(), var3.getUuid(), (Vector3f)var3.getPosition().get()));
      var2.addStaticPacket(this.createPivotConfiguration(var3));
      this.addJointPackets(var2, var3, var1.getRendered().values(), true);
      this.addJointPackets(var2, var3, var1.getSpawnQueue().values(), true);
      var2.addStaticPacket(this.assemblePivotHierarchy(var3));
      return var2;
   }

   private void addJointPackets(PacketBundleProvider bundle, DisplayRenderer.Pivot pivot, Collection<DisplayRenderer.Joint> joints, boolean initial) {
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         DisplayRenderer.Joint var6 = (DisplayRenderer.Joint)var5.next();
         Iterator var7 = var6.getModel().values().iterator();

         while(var7.hasNext()) {
            DisplayRenderer.JointData var8 = (DisplayRenderer.JointData)var7.next();
            var1.addStaticPacket(this.createJointEntity(var2, var8));
            PacketBundleProvider.PacketFactory var9 = this.createJointUpdateFactory(var8, var4, false);
            if (var9 != null) {
               var1.add(var9);
            }
         }
      }

   }

   private PacketBundleProvider addHitboxPackets(PacketBundleProvider existing, DisplayRenderer renderer) {
      PacketBundleProvider var3 = new PacketBundleProvider(var1);
      DisplayRenderer.Hitbox var4 = var2.getHitbox();
      if (var4.isPivotVisible()) {
         var3.add(PacketTransmissionUtility.generateAnchorSpawn(var4.getPivotId(), var4.getPivotUuid(), (Vector3f)var4.getPosition().get()));
         var3.addStaticPacket(this.createHitboxPivotConfiguration(var4));
      }

      if (var4.isHitboxVisible()) {
         var3.addStaticPacket(this.createInteractionEntity(var4));
         var3.addStaticPacket(this.configureInteractionData(var4, true));
      }

      if (var4.isShadowVisible()) {
         var3.addStaticPacket(this.createShadowEntity(var4));
         var3.addStaticPacket(this.configureShadowData(var4, true));
      }

      if (var4.isPivotVisible()) {
         var3.addStaticPacket(this.assembleHitboxHierarchy(var4));
      }

      return var3;
   }

   private void synchronizeMovementViewers(Set<UUID> viewers, DisplayRenderer renderer, boolean movementOnly, boolean dynamicOnly) {
      if (!var1.isEmpty()) {
         this.synchronizeViewerGroup(var1, var2, var3, var4);
      }
   }

   private void synchronizeStandardViewers(Set<UUID> viewers, DisplayRenderer renderer, boolean movementOnly, boolean dynamicOnly) {
      if (!var1.isEmpty()) {
         this.synchronizeViewerGroup(var1, var2, var3, var4);
      }
   }

   private void synchronizeForcedViewers(Set<UUID> viewers, DisplayRenderer renderer, boolean movementOnly, boolean dynamicOnly) {
      if (!var1.isEmpty()) {
         this.synchronizeViewerGroup(var1, var2, var3, var4);
      }
   }

   private void synchronizeViewerGroup(Set<UUID> viewers, DisplayRenderer renderer, boolean movementOnly, boolean dynamicOnly) {
      PacketBundleProvider var5 = new PacketBundleProvider();
      HashSet var6 = new HashSet();
      DisplayRenderer.Pivot var7 = var2.getPivot();
      if (var7.getPosition().isDirty()) {
         var5.add(PacketTransmissionUtility.generateAnchorRelocation(var7.getId(), (Vector3f)var7.getPosition().get()));
         this.postProcessingTasks.add(() -> {
            var7.getPosition().clearDirty();
         });
      }

      this.processJointUpdates(var5, var6, var7, var2, var3, var4);
      this.processNewJoints(var5, var7, var2);
      this.processRemovedJoints(var6, var2);
      if (var7.getPassengers().isDirty()) {
         var5.addStaticPacket(this.assemblePivotHierarchy(var7));
      }

      this.processHitboxUpdates(var5, var6, var2);
      if (!var6.isEmpty()) {
         var5.addStaticPacket(new PacketPlayOutEntityDestroy(new IntArrayList(var6)));
      }

      PacketTransmissionUtility.deliverBundleToMultiple(var1, var5);
   }

   private void processJointUpdates(PacketBundleProvider updates, HashSet<Integer> removalQueue, DisplayRenderer.Pivot pivot, DisplayRenderer renderer, boolean movementOnly, boolean dynamicOnly) {
      Iterator var7 = var4.getRendered().values().iterator();

      while(var7.hasNext()) {
         DisplayRenderer.Joint var8 = (DisplayRenderer.Joint)var7.next();
         UpdateScheme var9 = var8.getModelUpdateScheme();
         Iterator var10 = var8.getModel().values().iterator();

         while(var10.hasNext()) {
            DisplayRenderer.JointData var11 = (DisplayRenderer.JointData)var10.next();
            switch(var9.getUpdateMode(var11)) {
            case NONE:
            case UPDATE:
               if (var5) {
                  PacketPlayOutEntityMetadata var12 = this.createVisibilityUpdate(var11);
                  if (var12 != null) {
                     var1.addStaticPacket(var12);
                  }
               } else {
                  PacketBundleProvider.PacketFactory var13 = this.createJointUpdateFactory(var11, false, var6);
                  if (var13 != null) {
                     var1.add(var13);
                  }
               }
            }
         }

         var9.getAdded().forEach((var3x) -> {
            var1.addStaticPacket(this.createJointEntity(var3, var3x));
            PacketBundleProvider.PacketFactory var4 = this.createJointUpdateFactory(var3x, true, false);
            if (var4 != null) {
               var1.add(var4);
            }

         });
         var9.getRemoved().forEach((var1x) -> {
            var2.add(var1x.getId());
         });
      }

   }

   private void processNewJoints(PacketBundleProvider updates, DisplayRenderer.Pivot pivot, DisplayRenderer renderer) {
      Iterator var4 = var3.getSpawnQueue().values().iterator();

      while(var4.hasNext()) {
         DisplayRenderer.Joint var5 = (DisplayRenderer.Joint)var4.next();
         Iterator var6 = var5.getModel().values().iterator();

         while(var6.hasNext()) {
            DisplayRenderer.JointData var7 = (DisplayRenderer.JointData)var6.next();
            var1.addStaticPacket(this.createJointEntity(var2, var7));
            PacketBundleProvider.PacketFactory var8 = this.createJointUpdateFactory(var7, true, false);
            if (var8 != null) {
               var1.add(var8);
            }
         }
      }

   }

   private void processRemovedJoints(HashSet<Integer> removalQueue, DisplayRenderer renderer) {
      var2.getDestroyQueue().forEach((var1x, var2x) -> {
         var2x.getModel().forEach((var1xx, var2) -> {
            var1.add(var2.getId());
         });
      });
   }

   private void processHitboxUpdates(PacketBundleProvider updates, HashSet<Integer> removalQueue, DisplayRenderer renderer) {
      DisplayRenderer.Hitbox var4 = var3.getHitbox();
      boolean var5 = false;
      if (var4.getPosition().isDirty() && var4.isPivotVisible()) {
         var1.add(PacketTransmissionUtility.generateAnchorRelocation(var4.getPivotId(), (Vector3f)var4.getPosition().get()));
         this.postProcessingTasks.add(() -> {
            var4.getPosition().clearDirty();
         });
      }

      PacketPlayOutEntityMetadata var6;
      if (var4.getHitboxVisible().isDirty()) {
         if (var4.isHitboxVisible()) {
            var1.addStaticPacket(this.createInteractionEntity(var4));
            var1.addStaticPacket(this.configureInteractionData(var4, true));
            var5 = true;
         } else {
            var2.add(var4.getHitboxId());
         }
      } else if (var4.isHitboxVisible()) {
         var6 = this.configureInteractionData(var4, false);
         if (var6 != null) {
            var1.addStaticPacket(var6);
         }
      }

      if (var4.getShadowVisible().isDirty()) {
         if (var4.isShadowVisible()) {
            var1.addStaticPacket(this.createShadowEntity(var4));
            var1.addStaticPacket(this.configureShadowData(var4, true));
            var5 = true;
         } else {
            var2.add(var4.getShadowId());
         }
      } else if (var4.isShadowVisible()) {
         var6 = this.configureShadowData(var4, false);
         if (var6 != null) {
            var1.addStaticPacket(var6);
         }
      }

      if (var5 && var4.isPivotVisible()) {
         var1.addStaticPacket(this.assembleHitboxHierarchy(var4));
      }

   }

   private void processCulledViewers(Set<UUID> viewers, DisplayRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = new IntArrayList();
         Map var4 = var2.getDestroyQueue();
         if (!var4.isEmpty()) {
            IntStream var10000 = var4.values().stream().mapMultiToInt((var0, var1x) -> {
               var0.getModel().forEach((var1, var2) -> {
                  var1x.accept(var2.getId());
               });
            });
            Objects.requireNonNull(var3);
            var10000.forEach(var3::add);
         }

         if (!var3.isEmpty()) {
            PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
         }

      }
   }

   private void cleanupDisconnectedViewers(Set<UUID> viewers, DisplayRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = this.collectAllEntityIds(var2, var1);
         PacketTransmissionUtility.broadcastToPlayers(var1, new PacketPlayOutEntityDestroy(var3));
      }
   }

   private IntArrayList collectAllEntityIds(DisplayRenderer renderer, Set<UUID> viewers) {
      IntArrayList var3 = new IntArrayList();
      var1.getRendered().forEach((var2x, var3x) -> {
         var2.forEach((var1) -> {
            var3x.getSnapshotHandler().remove(var1);
         });
         var3x.getModel().forEach((var1, var2xx) -> {
            var3.add(var2xx.getId());
         });
      });
      var3.add(var1.getPivot().getId());
      var3.add(var1.getHitbox().getPivotId());
      var3.add(var1.getHitbox().getHitboxId());
      var3.add(var1.getHitbox().getShadowId());
      return var3;
   }

   private PacketPlayOutEntityMetadata createPivotConfiguration(DisplayRenderer.Pivot pivot) {
      return new PacketPlayOutEntityMetadata(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutMount assemblePivotHierarchy(DisplayRenderer.Pivot pivot) {
      return new PacketPlayOutMount(EntityRelationship.of(var1.getId(), (Collection)var1.getPassengers()));
   }

   private PacketPlayOutSpawnEntity createJointEntity(DisplayRenderer.Pivot pivot, DisplayRenderer.JointData data) {
      Vector3f var3 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), (double)var3.x, (double)var3.y, (double)var3.z, 0.0F, 0.0F, EntityTypes.ah, 0, Vec3D.b, 0.0D);
   }

   private PacketBundleProvider.PacketFactory createJointUpdateFactory(DisplayRenderer.JointData data, boolean force, boolean dynamicOnly) {
      DisplayRenderer.Joint var4 = var1.getJoint();
      if (!var2 && !var3 && !var4.isDirty() && !var1.getModel().isDirty()) {
         return null;
      } else {
         Set var10000 = this.postProcessingTasks;
         Objects.requireNonNull(var4);
         var10000.add(var4::clearDirty);
         this.postProcessingTasks.add(() -> {
            var1.getModel().clearDirty();
         });
         return !var2 && var4.isSkippable() ? PacketTransmissionUtility.wrapWithLevelOfDetail(this.activeContainer, (var3x, var4x) -> {
            return this.buildJointDataPacket(var3x, var4x, var1, false, var3);
         }) : (var4x) -> {
            return this.buildJointDataPacket(var4x, (AnimationLODHandler.LODTracker)null, var1, var2, var3);
         };
      }
   }

   private PacketPlayOutEntityMetadata buildJointDataPacket(UUID viewerId, @Nullable AnimationLODHandler.LODTracker tracker, DisplayRenderer.JointData data, boolean force, boolean dynamicOnly) {
      DisplayRenderer.Joint var6 = var3.getJoint();
      ArrayList var7 = new ArrayList(13);
      if (var4) {
         var7.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
         var7.add(new c(8, DataWatcherRegistry.b, 0));
      } else if (var6.isTransformDirty() || var5) {
         var7.add(new c(8, DataWatcherRegistry.b, 0));
      }

      int var8 = var2 == null ? 1 : (Integer)var2.getTickDuration().get();
      boolean var9 = var2 != null && var2.getTickDuration().isDirty();
      byte var10 = var6.getSnapshotHandler().getUpdate(var1);
      this.addJointProperties(var7, var6, var3, var4, var5, var9, var10, var8);
      return new PacketPlayOutEntityMetadata(var3.getId(), var7);
   }

   private void addJointProperties(ArrayList<c<?>> properties, DisplayRenderer.Joint joint, DisplayRenderer.JointData data, boolean force, boolean dynamicOnly, boolean lodChanged, byte updateFlags, int lodDuration) {
      var2.getStep().ifDirty((var2x) -> {
         var1.add(new c(9, DataWatcherRegistry.b, var2x ? 0 : var8));
      }, var4 || var5 || var6);
      var2.getGlowing().ifDirty((var1x) -> {
         var1.add(new c(0, DataWatcherRegistry.a, (byte)(var1x ? 96 : 32)));
      }, var4 || var5);
      var2.getGlowColor().ifDirty((var1x) -> {
         var1.add(new c(22, DataWatcherRegistry.b, var1x));
      }, var4 || var5);
      var2.getBrightness().ifDirty((var1x) -> {
         var1.add(new c(16, DataWatcherRegistry.b, var1x));
      }, var4 || var5);
      var2.getPosition().ifDirty((var1x) -> {
         var1.add(new c(11, DataWatcherRegistry.D, var1x));
      }, var4 || var5 || MathUtils.getBit(var7, 0));
      var2.getScale().ifDirty((var1x) -> {
         var1.add(new c(12, DataWatcherRegistry.D, var1x));
      }, var4 || var5 || MathUtils.getBit(var7, 2));
      var2.getLeftRotation().ifDirty((var1x) -> {
         var1.add(new c(13, DataWatcherRegistry.E, var1x.rotateY(3.1415927F, new Quaternionf())));
      }, var4 || var5 || MathUtils.getBit(var7, 1));
      var2.getRightRotation().ifDirty((var1x) -> {
         var1.add(new c(14, DataWatcherRegistry.E, var1x));
      }, var4 || var5 || MathUtils.getBit(var7, 3));
      var2.getVisibility().ifDirty((var1x) -> {
         var1.add(new c(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
      }, var4 || var5);
      var3.getModel().ifDirty((var1x) -> {
         var1.add(new c(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
      }, var4 || var5);
      var2.getDisplay().ifDirty((var1x) -> {
         var1.add(new c(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
      }, var4 || var5);
   }

   private PacketPlayOutEntityMetadata createVisibilityUpdate(DisplayRenderer.JointData data) {
      DisplayRenderer.Joint var2 = var1.getJoint();
      if (!var2.isRenderDirty() && !var1.getModel().isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(6);
         var2.getGlowing().ifDirty((var1x) -> {
            var3.add(new c(0, DataWatcherRegistry.a, (byte)(var1x ? 96 : 32)));
         });
         var2.getGlowColor().ifDirty((var1x) -> {
            var3.add(new c(22, DataWatcherRegistry.b, var1x));
         });
         var2.getBrightness().ifDirty((var1x) -> {
            var3.add(new c(16, DataWatcherRegistry.b, var1x));
         });
         var2.getVisibility().ifDirty((var1x) -> {
            var3.add(new c(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
         });
         var1.getModel().ifDirty((var1x) -> {
            var3.add(new c(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
         });
         var2.getDisplay().ifDirty((var1x) -> {
            var3.add(new c(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
         });
         Set var10000 = this.postProcessingTasks;
         Objects.requireNonNull(var2);
         var10000.add(var2::clearDirty);
         return new PacketPlayOutEntityMetadata(var1.getId(), var3);
      }
   }

   private PacketPlayOutEntityMetadata createHitboxPivotConfiguration(DisplayRenderer.Hitbox hitbox) {
      return new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity createInteractionEntity(DisplayRenderer.Hitbox hitbox) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getHitboxId(), var1.getHitboxUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ae, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureInteractionData(DisplayRenderer.Hitbox hitbox, boolean spawn) {
      if (var1.isHitboxVisible() && (var2 || var1.isHitboxDirty())) {
         ArrayList var3 = new ArrayList(4);
         if (var2) {
            var3.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
            var3.add(new c(10, DataWatcherRegistry.k, false));
         }

         var1.getWidth().ifDirty((var1x) -> {
            var3.add(new c(8, DataWatcherRegistry.d, var1x));
         }, var2);
         var1.getHeight().ifDirty((var1x) -> {
            var3.add(new c(9, DataWatcherRegistry.d, var1x));
         }, var2);
         return new PacketPlayOutEntityMetadata(var1.getHitboxId(), var3);
      } else {
         return null;
      }
   }

   private PacketPlayOutSpawnEntity createShadowEntity(DisplayRenderer.Hitbox hitbox) {
      Vector3f var2 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var1.getShadowId(), var1.getShadowUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ah, 0, Vec3D.b, 0.0D);
   }

   private PacketPlayOutEntityMetadata configureShadowData(DisplayRenderer.Hitbox hitbox, boolean spawn) {
      if (var1.isShadowVisible() && (var2 || var1.getShadowRadius().isDirty())) {
         ArrayList var3 = new ArrayList(2);
         if (var2) {
            var3.add(new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
         }

         var1.getShadowRadius().ifDirty((var1x) -> {
            var3.add(new c(18, DataWatcherRegistry.d, var1x));
         }, var2);
         return new PacketPlayOutEntityMetadata(var1.getShadowId(), var3);
      } else {
         return null;
      }
   }

   private PacketPlayOutMount assembleHitboxHierarchy(DisplayRenderer.Hitbox hitbox) {
      ArrayList var2 = new ArrayList(2);
      if (var1.isHitboxVisible()) {
         var2.add(var1.getHitboxId());
      }

      if (var1.isShadowVisible()) {
         var2.add(var1.getShadowId());
      }

      return new PacketPlayOutMount(EntityRelationship.of(var1.getPivotId(), (Collection)var2));
   }
}
