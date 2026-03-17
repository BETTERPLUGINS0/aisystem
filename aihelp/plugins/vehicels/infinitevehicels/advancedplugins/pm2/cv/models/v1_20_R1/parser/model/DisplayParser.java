package advancedplugins.pm2.cv.models.v1_20_R1.parser.model;

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
import advancedplugins.pm2.cv.models.v1_20_R1.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_20_R1.entity.EntityUtils;
import advancedplugins.pm2.cv.models.v1_20_R1.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_20_R1.network.utils.Packets;
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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.b;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DisplayParser implements ModelRendererParser<DisplayRenderer> {
   private final Map<String, Set<UUID>> players = new ConcurrentHashMap();
   private final Set<Runnable> cleanupQueue = ConcurrentHashMap.newKeySet();
   private IModelContainer modelContainer;

   public void dispatch(DisplayRenderer renderer) {
      IEntityData var2 = var1.getActiveModel().getModeledEntity().getBase().getData();
      this.modelContainer = var1.getActiveModel().getModeledEntity();
      Iterator var3 = var2.getTracking().entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         switch((CullType)var4.getValue()) {
         case NO_CULL:
            if (var1.pollUpdate((UUID)var4.getKey())) {
               ((Set)this.players.computeIfAbsent("NO_CULL_FORCE", (var0) -> {
                  return new HashSet();
               })).add((UUID)var4.getKey());
            } else {
               ((Set)this.players.computeIfAbsent(((CullType)var4.getValue()).name(), (var0) -> {
                  return new HashSet();
               })).add((UUID)var4.getKey());
            }
            break;
         case MOVEMENT_ONLY:
            var1.pushUpdate((UUID)var4.getKey());
            ((Set)this.players.computeIfAbsent(((CullType)var4.getValue()).name(), (var0) -> {
               return new HashSet();
            })).add((UUID)var4.getKey());
            break;
         case CULLED:
            ((Set)this.players.computeIfAbsent(((CullType)var4.getValue()).name(), (var0) -> {
               return new HashSet();
            })).add((UUID)var4.getKey());
         }
      }

      if (var1.pollFirstSpawn()) {
         HashSet var5 = new HashSet();
         var5.addAll((Collection)this.players.getOrDefault("MOVEMENT_ONLY", ImmutableSet.of()));
         var5.addAll((Collection)this.players.getOrDefault("NO_CULL", ImmutableSet.of()));
         var5.addAll((Collection)this.players.getOrDefault("NO_CULL_FORCE", ImmutableSet.of()));
         this.spawn(var5, var1);
      } else {
         this.spawn(var2.getStartTracking(), var1);
         this.updateRealtime((Set)this.players.getOrDefault("MOVEMENT_ONLY", ImmutableSet.of()), var1, true, false);
         this.updateRealtime((Set)this.players.getOrDefault("NO_CULL", ImmutableSet.of()), var1, false, false);
         this.updateRealtime((Set)this.players.getOrDefault("NO_CULL_FORCE", ImmutableSet.of()), var1, false, true);
         this.updateCulled((Set)this.players.getOrDefault("CULLED", ImmutableSet.of()), var1);
         this.remove(var2.getStopTracking(), var1);
      }

      this.players.forEach((var0, var1x) -> {
         var1x.clear();
      });
      var1.getPivot().clearDirty();
      var1.getHitbox().clearDirty();
      this.cleanupQueue.forEach(Runnable::run);
      this.cleanupQueue.clear();
   }

   public void dispose(DisplayRenderer renderer) {
      IEntityData var2 = var1.getActiveModel().getModeledEntity().getBase().getData();
      HashSet var3 = new HashSet(var2.getStartTracking());
      var3.addAll(var2.getTracking().keySet());
      var3.addAll(var2.getStopTracking());
      this.remove(var3, var1);
   }

   private void spawn(Set<UUID> targets, DisplayRenderer renderer) {
      if (!var1.isEmpty()) {
         Packets var3 = new Packets();
         DisplayRenderer.Pivot var4 = var2.getPivot();
         var3.add(this.pivotSpawn(var4));
         var3.add((Packet)this.pivotData(var4));
         Iterator var5 = var2.getRendered().values().iterator();

         DisplayRenderer.Joint var6;
         Iterator var7;
         DisplayRenderer.JointData var8;
         while(var5.hasNext()) {
            var6 = (DisplayRenderer.Joint)var5.next();
            var7 = var6.getModel().values().iterator();

            while(var7.hasNext()) {
               var8 = (DisplayRenderer.JointData)var7.next();
               var3.add((Packet)this.displaySpawn(var4, var8));
               var3.add(this.displayData(var8, true, false));
            }
         }

         var5 = var2.getSpawnQueue().values().iterator();

         while(var5.hasNext()) {
            var6 = (DisplayRenderer.Joint)var5.next();
            var7 = var6.getModel().values().iterator();

            while(var7.hasNext()) {
               var8 = (DisplayRenderer.JointData)var7.next();
               var3.add((Packet)this.displaySpawn(var4, var8));
               var3.add(this.displayData(var8, true, false));
            }
         }

         var3.add((Packet)this.pivotMount(var4));
         BaseEntity var9 = var2.getActiveModel().getModeledEntity().getBase();
         if (var9 instanceof BukkitEntity) {
            BukkitEntity var10 = (BukkitEntity)var9;
            Entity var11 = var10.getOriginal();
            if (var11 instanceof Player) {
               Player var12 = (Player)var11;
               if (var1.contains(var12.getUniqueId())) {
                  NetworkUtils.sendBundled(var12.getUniqueId(), var3);
               }
            }
         }

         DisplayRenderer.Hitbox var13 = var2.getHitbox();
         var3.add(this.hitboxSpawnPivot(var13));
         var3.add((Packet)this.hitboxDataPivot(var13));
         var3.add((Packet)this.hitboxSpawn(var13));
         var3.add((Packet)this.hitboxData(var13, true));
         var3.add((Packet)this.shadowSpawn(var13));
         var3.add((Packet)this.shadowData(var13, true));
         var3.add((Packet)this.hitboxMount(var13));
         NetworkUtils.sendBundled(var1, var3, (var1x) -> {
            return !var1x.getUniqueId().equals(var9.getUUID());
         });
      }

   }

   private void updateRealtime(Set<UUID> targets, DisplayRenderer renderer, boolean movementOnly, boolean dynamicOnly) {
      if (!var1.isEmpty()) {
         Packets var5 = new Packets();
         HashSet var6 = new HashSet();
         DisplayRenderer.Pivot var7 = var2.getPivot();
         var5.add(this.pivotTeleport(var7));
         Iterator var8;
         DisplayRenderer.Joint var9;
         UpdateScheme var10;
         Iterator var11;
         DisplayRenderer.JointData var12;
         if (!var3) {
            var8 = var2.getRendered().values().iterator();

            while(var8.hasNext()) {
               var9 = (DisplayRenderer.Joint)var8.next();
               var10 = var9.getModelUpdateScheme();
               var11 = var9.getModel().values().iterator();

               while(var11.hasNext()) {
                  var12 = (DisplayRenderer.JointData)var11.next();
                  switch(var10.getUpdateMode(var12)) {
                  case NONE:
                  case UPDATE:
                     var5.add(this.displayData(var12, false, var4));
                  }
               }

               var10.getAdded().forEach((var3x) -> {
                  var5.add((Packet)this.displaySpawn(var7, var3x));
                  var5.add(this.displayData(var3x, true, false));
               });
               var10.getRemoved().forEach((var1x) -> {
                  var6.add(var1x.getId());
               });
            }
         } else {
            var8 = var2.getRendered().values().iterator();

            while(var8.hasNext()) {
               var9 = (DisplayRenderer.Joint)var8.next();
               var10 = var9.getModelUpdateScheme();
               var11 = var9.getModel().values().iterator();

               while(var11.hasNext()) {
                  var12 = (DisplayRenderer.JointData)var11.next();
                  switch(var10.getUpdateMode(var12)) {
                  case NONE:
                  case UPDATE:
                     var5.add((Packet)this.displayVisibleData(var12));
                  }
               }

               var10.getAdded().forEach((var3x) -> {
                  var5.add((Packet)this.displaySpawn(var7, var3x));
                  var5.add(this.displayData(var3x, true, false));
               });
               var10.getRemoved().forEach((var1x) -> {
                  var6.add(var1x.getId());
               });
            }
         }

         var8 = var2.getSpawnQueue().values().iterator();

         while(var8.hasNext()) {
            var9 = (DisplayRenderer.Joint)var8.next();
            Iterator var13 = var9.getModel().values().iterator();

            while(var13.hasNext()) {
               DisplayRenderer.JointData var14 = (DisplayRenderer.JointData)var13.next();
               var5.add((Packet)this.displaySpawn(var7, var14));
               var5.add(this.displayData(var14, true, false));
            }
         }

         var2.getDestroyQueue().forEach((var1x, var2x) -> {
            var2x.getModel().forEach((var1, var2) -> {
               var6.add(var2.getId());
            });
         });
         if (var7.getPassengers().isDirty()) {
            var5.add((Packet)this.pivotMount(var7));
         }

         DisplayRenderer.Hitbox var15 = var2.getHitbox();
         boolean var16 = false;
         var5.add(this.hitboxTeleport(var15));
         if (var15.getHitboxVisible().isDirty()) {
            if (var15.isHitboxVisible()) {
               var5.add((Packet)this.hitboxSpawn(var15));
               var5.add((Packet)this.hitboxData(var15, true));
               var16 = true;
            } else {
               var6.add(var15.getHitboxId());
            }
         } else {
            var5.add((Packet)this.hitboxData(var15, false));
         }

         if (var15.getShadowVisible().isDirty()) {
            if (var15.isShadowVisible()) {
               var5.add((Packet)this.shadowSpawn(var15));
               var5.add((Packet)this.shadowData(var15, true));
               var16 = true;
            } else {
               var6.add(var15.getShadowId());
            }
         } else {
            var5.add((Packet)this.shadowData(var15, false));
         }

         if (var16) {
            var5.add((Packet)this.hitboxMount(var15));
         }

         if (!var6.isEmpty()) {
            var5.add((Packet)(new PacketPlayOutEntityDestroy(new IntArrayList(var6))));
         }

         NetworkUtils.sendBundled(var1, var5);
      }

   }

   private void updateCulled(Set<UUID> targets, DisplayRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = new IntArrayList();
         Map var4 = var2.getDestroyQueue();
         if (!var4.isEmpty()) {
            IntStream var5 = var4.values().stream().mapMultiToInt((var0, var1x) -> {
               var0.getModel().forEach((var1, var2) -> {
                  var1x.accept(var2.getId());
               });
            });
            Objects.requireNonNull(var3);
            Objects.requireNonNull(var3);
            var5.forEach(var3::add);
         }

         if (!var3.isEmpty()) {
            NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(var3));
         }
      }

   }

   private void remove(Set<UUID> targets, DisplayRenderer renderer) {
      if (!var1.isEmpty()) {
         IntArrayList var3 = IntArrayList.toList(var2.getRendered().values().stream().mapMultiToInt((var0, var1x) -> {
            var0.getModel().forEach((var1, var2) -> {
               var1x.accept(var2.getId());
            });
         }));
         var3.add(var2.getPivot().getId());
         var3.add(var2.getHitbox().getPivotId());
         var3.add(var2.getHitbox().getHitboxId());
         var3.add(var2.getHitbox().getShadowId());
         NetworkUtils.send((Set)var1, new PacketPlayOutEntityDestroy(var3));
      }

   }

   private Packets.PacketSupplier pivotSpawn(DisplayRenderer.Pivot pivot) {
      return NetworkUtils.createPivotSpawn(var1.getId(), var1.getUuid(), (Vector3f)var1.getPosition().get());
   }

   private PacketPlayOutEntityMetadata pivotData(DisplayRenderer.Pivot pivot) {
      return new PacketPlayOutEntityMetadata(var1.getId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutMount pivotMount(DisplayRenderer.Pivot pivot) {
      return new PacketPlayOutMount(EntityContainer.of(var1.getId(), (Collection)var1.getPassengers()));
   }

   private Packets.PacketSupplier pivotTeleport(DisplayRenderer.Pivot pivot) {
      if (!var1.getPosition().isDirty()) {
         return null;
      } else {
         this.cleanupQueue.add(() -> {
            var1.getPosition().clearDirty();
         });
         return NetworkUtils.createPivotTeleport(var1.getId(), (Vector3f)var1.getPosition().get());
      }
   }

   private PacketPlayOutSpawnEntity displaySpawn(DisplayRenderer.Pivot pivot, DisplayRenderer.JointData jointData) {
      Vector3f var3 = (Vector3f)var1.getPosition().get();
      return new PacketPlayOutSpawnEntity(var2.getId(), var2.getUuid(), (double)var3.x, (double)var3.y, (double)var3.z, 0.0F, 0.0F, EntityTypes.ae, 0, Vec3D.b, 0.0D);
   }

   private Packets.PacketSupplier displayData(DisplayRenderer.JointData jointData, boolean force, boolean dynamicOnly) {
      DisplayRenderer.Joint var4 = var1.getJoint();
      if (!var2 && !var3 && !var4.isDirty() && !var1.getModel().isDirty()) {
         return null;
      } else {
         Objects.requireNonNull(var4);
         Set var10000 = this.cleanupQueue;
         Objects.requireNonNull(var4);
         var10000.add(var4::clearDirty);
         this.cleanupQueue.add(() -> {
            var1.getModel().clearDirty();
         });
         return !var2 && var4.isSkippable() ? NetworkUtils.lodWrapper(this.modelContainer, (var3x, var4x) -> {
            return this.displayData(var3x, var4x, var1, false, var3);
         }) : (var4x) -> {
            return this.displayData(var4x, (AnimationLODHandler.LODTracker)null, var1, var2, var3);
         };
      }
   }

   private PacketPlayOutEntityMetadata displayData(UUID uuid, @Nullable AnimationLODHandler.LODTracker tracker, DisplayRenderer.JointData jointData, boolean force, boolean dynamicOnly) {
      DisplayRenderer.Joint var6 = var3.getJoint();
      ArrayList var7 = new ArrayList(13);
      if (var4) {
         var7.add(new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
         var7.add(new b(8, DataWatcherRegistry.b, 0));
      } else if (var6.isTransformDirty() || var5) {
         var7.add(new b(8, DataWatcherRegistry.b, 0));
      }

      int var8 = var2 == null ? 1 : (Integer)var2.getTickDuration().get();
      boolean var9 = var2 != null && var2.getTickDuration().isDirty();
      byte var10 = var6.getSnapshotHandler().getUpdate(var1);
      var6.getStep().ifDirty((var2x) -> {
         var7.add(new b(9, DataWatcherRegistry.b, var2x ? 0 : var8));
      }, var4 || var5 || var9);
      var6.getGlowing().ifDirty((var1x) -> {
         var7.add(new b(0, DataWatcherRegistry.a, (byte)(var1x ? 96 : 32)));
      }, var4 || var5);
      var6.getGlowColor().ifDirty((var1x) -> {
         var7.add(new b(22, DataWatcherRegistry.b, var1x));
      }, var4 || var5);
      var6.getBrightness().ifDirty((var1x) -> {
         var7.add(new b(16, DataWatcherRegistry.b, var1x));
      }, var4 || var5);
      var6.getPosition().ifDirty((var1x) -> {
         var7.add(new b(11, DataWatcherRegistry.A, var1x));
      }, var4 || var5 || MathUtils.getBit(var10, 0));
      var6.getScale().ifDirty((var1x) -> {
         var7.add(new b(12, DataWatcherRegistry.A, var1x));
      }, var4 || var5 || MathUtils.getBit(var10, 2));
      var6.getLeftRotation().ifDirty((var1x) -> {
         var7.add(new b(13, DataWatcherRegistry.B, var1x.rotateY(3.1415927F, new Quaternionf())));
      }, var4 || var5 || MathUtils.getBit(var10, 1));
      var6.getRightRotation().ifDirty((var1x) -> {
         var7.add(new b(14, DataWatcherRegistry.B, var1x));
      }, var4 || var5 || MathUtils.getBit(var10, 3));
      var6.getVisibility().ifDirty((var1x) -> {
         var7.add(new b(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
      }, var4 || var5);
      var3.getModel().ifDirty((var1x) -> {
         var7.add(new b(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
      }, var4 || var5);
      var6.getDisplay().ifDirty((var1x) -> {
         var7.add(new b(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
      }, var4 || var5);
      return new PacketPlayOutEntityMetadata(var3.getId(), var7);
   }

   private PacketPlayOutEntityMetadata displayVisibleData(DisplayRenderer.JointData jointData) {
      DisplayRenderer.Joint var2 = var1.getJoint();
      if (!var2.isRenderDirty() && !var1.getModel().isDirty()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(6);
         var2.getGlowing().ifDirty((var1x) -> {
            var3.add(new b(0, DataWatcherRegistry.a, (byte)(var1x ? 96 : 32)));
         });
         var2.getGlowColor().ifDirty((var1x) -> {
            var3.add(new b(22, DataWatcherRegistry.b, var1x));
         });
         var2.getBrightness().ifDirty((var1x) -> {
            var3.add(new b(16, DataWatcherRegistry.b, var1x));
         });
         var2.getVisibility().ifDirty((var1x) -> {
            var3.add(new b(17, DataWatcherRegistry.d, var1x ? 4096.0F : 0.0F));
         });
         var1.getModel().ifDirty((var1x) -> {
            var3.add(new b(23, DataWatcherRegistry.h, CraftItemStack.asNMSCopy(var1x)));
         });
         var2.getDisplay().ifDirty((var1x) -> {
            var3.add(new b(24, DataWatcherRegistry.a, var1x == null ? 0 : (byte)var1x.ordinal()));
         });
         Set var4 = this.cleanupQueue;
         Objects.requireNonNull(var2);
         Objects.requireNonNull(var2);
         var4.add(var2::clearDirty);
         return new PacketPlayOutEntityMetadata(var1.getId(), var3);
      }
   }

   private Packets.PacketSupplier hitboxSpawnPivot(DisplayRenderer.Hitbox hitbox) {
      return var1.isPivotVisible() ? NetworkUtils.createPivotSpawn(var1.getPivotId(), var1.getPivotUuid(), (Vector3f)var1.getPosition().get()) : null;
   }

   private PacketPlayOutEntityMetadata hitboxDataPivot(DisplayRenderer.Hitbox hitbox) {
      return !var1.isPivotVisible() ? null : new PacketPlayOutEntityMetadata(var1.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
   }

   private PacketPlayOutSpawnEntity hitboxSpawn(DisplayRenderer.Hitbox hitbox) {
      if (!var1.isHitboxVisible()) {
         return null;
      } else {
         Vector3f var2 = (Vector3f)var1.getPosition().get();
         return new PacketPlayOutSpawnEntity(var1.getHitboxId(), var1.getHitboxUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ab, 0, Vec3D.b, 0.0D);
      }
   }

   private PacketPlayOutEntityMetadata hitboxData(DisplayRenderer.Hitbox hitbox, boolean spawn) {
      if (var1.isHitboxVisible() && (var1.isHitboxDirty() || var2)) {
         ArrayList var3 = new ArrayList(4);
         if (var2) {
            var3.add(new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
            var3.add(new b(10, DataWatcherRegistry.k, false));
         }

         var1.getWidth().ifDirty((var1x) -> {
            var3.add(new b(8, DataWatcherRegistry.d, var1x));
         }, var2);
         var1.getHeight().ifDirty((var1x) -> {
            var3.add(new b(9, DataWatcherRegistry.d, var1x));
         }, var2);
         return new PacketPlayOutEntityMetadata(var1.getHitboxId(), var3);
      } else {
         return null;
      }
   }

   private PacketPlayOutSpawnEntity shadowSpawn(DisplayRenderer.Hitbox hitbox) {
      if (!var1.isShadowVisible()) {
         return null;
      } else {
         Vector3f var2 = (Vector3f)var1.getPosition().get();
         return new PacketPlayOutSpawnEntity(var1.getShadowId(), var1.getShadowUuid(), (double)var2.x, (double)var2.y, (double)var2.z, 0.0F, 0.0F, EntityTypes.ae, 0, Vec3D.b, 0.0D);
      }
   }

   private PacketPlayOutEntityMetadata shadowData(DisplayRenderer.Hitbox hitbox, boolean spawn) {
      if (var1.isShadowVisible() && (var1.getShadowRadius().isDirty() || var2)) {
         ArrayList var3 = new ArrayList(2);
         if (var2) {
            var3.add(new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
         }

         var1.getShadowRadius().ifDirty((var1x) -> {
            var3.add(new b(18, DataWatcherRegistry.d, var1x));
         }, var2);
         return new PacketPlayOutEntityMetadata(var1.getShadowId(), var3);
      } else {
         return null;
      }
   }

   private Packets.PacketSupplier hitboxTeleport(DisplayRenderer.Hitbox hitbox) {
      if (var1.isPivotVisible() && var1.getPosition().isDirty()) {
         this.cleanupQueue.add(() -> {
            var1.getPosition().clearDirty();
         });
         return NetworkUtils.createPivotTeleport(var1.getPivotId(), (Vector3f)var1.getPosition().get());
      } else {
         return null;
      }
   }

   private PacketPlayOutMount hitboxMount(DisplayRenderer.Hitbox hitbox) {
      if (!var1.isPivotVisible()) {
         return null;
      } else {
         boolean var2 = var1.isHitboxVisible();
         boolean var3 = var1.isShadowVisible();
         ArrayList var4 = new ArrayList(2);
         if (var2) {
            var4.add(var1.getHitboxId());
         }

         if (var3) {
            var4.add(var1.getShadowId());
         }

         return new PacketPlayOutMount(EntityContainer.of(var1.getPivotId(), (Collection)var4));
      }
   }
}
