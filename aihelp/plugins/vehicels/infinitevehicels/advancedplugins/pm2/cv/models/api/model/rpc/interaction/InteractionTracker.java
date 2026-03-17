package advancedplugins.pm2.cv.models.api.model.rpc.interaction;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

public class InteractionTracker {
   private final Map<UUID, Integer> hitboxesUUID = Maps.newConcurrentMap();
   private final Map<Integer, HitboxEntity> hitboxes = Maps.newConcurrentMap();
   private final Map<UUID, DynamicHitbox> playerRelay = Maps.newConcurrentMap();
   private final Map<Integer, IVisualModel> modelRelay = Maps.newConcurrentMap();
   private final Map<Integer, Integer> entityRelay = Maps.newConcurrentMap();

   public void raytraceHitboxes() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(true) {
         while(var1.hasNext()) {
            Player var2 = (Player)var1.next();
            if (var2.getGameMode() == GameMode.SPECTATOR) {
               this.playerRelay.computeIfPresent(var2.getUniqueId(), (var0, var1x) -> {
                  var1x.destroy();
                  return null;
               });
            } else {
               Vector var3 = var2.getEyeLocation().toVector();
               Vector3f var4 = var3.toVector3f();
               Vector var5 = var2.getEyeLocation().getDirection();
               Vector3f var6 = var5.toVector3f();
               double var7 = var2.getGameMode() == GameMode.CREATIVE ? 5.0D : 3.0D;
               double var9 = Double.MAX_VALUE;
               HitboxEntity var11 = null;
               RayTraceResult var12 = null;
               Iterator var13 = this.hitboxes.values().iterator();

               while(var13.hasNext()) {
                  HitboxEntity var14 = (HitboxEntity)var13.next();
                  if (var14.getLocation().getWorld() == var2.getWorld() && (Integer)this.entityRelay.getOrDefault(var14.getEntityId(), var2.getEntityId() + 1) != var2.getEntityId() && var14.getJoint().getVisualModel().getModeledEntity().getBase().getEntityId() != var2.getEntityId()) {
                     OrientedBoundingBox var15 = var14.getOrientedBoundingBox();
                     if (var15 != null) {
                        if (var15.contains(var4)) {
                           var11 = var14;
                           var12 = null;
                           break;
                        }

                        RayTraceResult var16 = var15.rayTrace(var4, var6, var7, (Consumer)null);
                        if (var16 != null) {
                           double var17 = var16.getHitPosition().distanceSquared(var3);
                           if (!(var9 < var17)) {
                              var11 = var14;
                              var12 = var16;
                           }
                        }
                     }
                  }
               }

               if (var11 == null) {
                  this.playerRelay.computeIfPresent(var2.getUniqueId(), (var0, var1x) -> {
                     var1x.destroy();
                     return null;
                  });
               } else {
                  Vector var19 = var12 == null ? var3.add(var5.multiply(var7)) : var12.getHitPosition();
                  DynamicHitbox var20 = (DynamicHitbox)this.playerRelay.computeIfAbsent(var2.getUniqueId(), (var2x) -> {
                     return new DynamicHitbox(var2, var19);
                  });
                  var20.setTarget(var11.getEntityId());
                  var20.update(var19);
               }
            }
         }

         return;
      }
   }

   public void addHitbox(HitboxEntity var1) {
      this.hitboxesUUID.put(var1.getUniqueId(), var1.getEntityId());
      this.hitboxes.put(var1.getEntityId(), var1);
   }

   public void removeHitbox(UUID var1) {
      Integer var2 = (Integer)this.hitboxesUUID.remove(var1);
      if (var2 != null) {
         this.hitboxes.remove(var2);
      }

   }

   public void removeHitbox(int var1) {
      HitboxEntity var2 = (HitboxEntity)this.hitboxes.remove(var1);
      if (var2 != null) {
         this.hitboxesUUID.remove(var2.getUniqueId());
      }

   }

   public HitboxEntity getHitbox(UUID var1) {
      Integer var2 = (Integer)this.hitboxesUUID.get(var1);
      return var2 == null ? null : this.getHitbox(var2);
   }

   public HitboxEntity getHitbox(int var1) {
      return (HitboxEntity)this.hitboxes.get(var1);
   }

   public void removeDynamicHitbox(UUID var1) {
      this.playerRelay.remove(var1);
   }

   public DynamicHitbox getDynamicHitbox(UUID var1) {
      return (DynamicHitbox)this.playerRelay.get(var1);
   }

   public void setModelRelay(int var1, IVisualModel var2) {
      this.modelRelay.put(var1, var2);
   }

   public void removeModelRelay(int var1) {
      IVisualModel var10000 = (IVisualModel)this.modelRelay.remove(var1);
   }

   public IVisualModel getModelRelay(int var1) {
      return (IVisualModel)this.modelRelay.get(var1);
   }

   public void setEntityRelay(int var1, Integer var2) {
      this.entityRelay.put(var1, var2);
   }

   public void removeEntityRelay(int var1) {
      this.entityRelay.remove(var1);
   }

   public Integer getEntityRelay(int var1) {
      return (Integer)this.entityRelay.get(var1);
   }
}
