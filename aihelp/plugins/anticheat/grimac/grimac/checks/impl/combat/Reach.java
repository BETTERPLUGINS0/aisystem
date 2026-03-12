package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttackRange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectMap;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySizeable;
import ac.grim.grimac.utils.data.packetentity.dragon.PacketEntityEnderDragonPart;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "Reach",
   setback = 10.0D
)
public class Reach extends Check implements PacketCheck {
   private static final List<EntityType> blacklisted;
   private static final Reach.CheckResult NONE;
   private final Int2ObjectMap<Reach.InteractionData> playerAttackQueue = new Int2ObjectOpenHashMap();
   private boolean cancelImpossibleHits;
   private double threshold;
   private double cancelBuffer;
   private static final boolean ATTACK_RANGE_COMPONENT_EXISTS;

   public Reach(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.disableGrim && event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(event);
         if (this.player.getSetbackTeleportUtil().shouldBlockMovement()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
            return;
         }

         PacketEntity entity = (PacketEntity)this.player.compensatedEntities.entityMap.get(action.getEntityId());
         if (entity == null || entity instanceof PacketEntityEnderDragonPart) {
            if (this.shouldModifyPackets() && this.player.compensatedEntities.serverPositionsMap.containsKey(action.getEntityId())) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }

            return;
         }

         if (entity.isDead) {
            return;
         }

         if (entity.type == EntityTypes.ARMOR_STAND && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            return;
         }

         if (entity.type == EntityTypes.HAPPY_GHAST && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_6)) {
            return;
         }

         if (this.player.gamemode == GameMode.CREATIVE || this.player.gamemode == GameMode.SPECTATOR) {
            return;
         }

         if (this.player.inVehicle()) {
            return;
         }

         if (entity.riding != null) {
            return;
         }

         InteractionHand hand = action.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK ? InteractionHand.MAIN_HAND : action.getHand();
         ItemStack currentStack = this.player.inventory.getItemInHand(hand);
         ItemStack startStack = this.player.inventory.getStartOfTickStack();
         boolean hasRange = false;
         float maxReach = 0.0F;
         float hitboxMargin = 0.0F;
         if (ATTACK_RANGE_COMPONENT_EXISTS) {
            ItemAttackRange startRange = (ItemAttackRange)startStack.getComponentOr(ComponentTypes.ATTACK_RANGE, (Object)null);
            if (startRange != null) {
               ItemAttackRange currentRange = (ItemAttackRange)currentStack.getComponentOr(ComponentTypes.ATTACK_RANGE, (Object)null);
               if (currentRange == null) {
                  hasRange = true;
                  maxReach = startRange.getMaxRange();
                  hitboxMargin = startRange.getHitboxMargin();
               } else {
                  hasRange = true;
                  maxReach = Math.min(startRange.getMaxRange(), currentRange.getMaxRange());
                  hitboxMargin = Math.min(startRange.getHitboxMargin(), currentRange.getHitboxMargin());
               }
            }
         }

         boolean tooManyAttacks = this.playerAttackQueue.size() > 10;
         if (!tooManyAttacks) {
            this.playerAttackQueue.put(action.getEntityId(), new Reach.InteractionData(this.player.x, this.player.y, this.player.z, hasRange, maxReach, hitboxMargin));
         }

         boolean knownInvalid = this.isKnownInvalid(entity, hasRange, maxReach, hitboxMargin);
         if (this.shouldModifyPackets() && this.cancelImpossibleHits && knownInvalid || tooManyAttacks) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }
      }

      if (this.isUpdate(event.getPacketType())) {
         this.tickBetterReachCheckWithAngle();
      }

   }

   private boolean isKnownInvalid(PacketEntity reachEntity, boolean hasAttackRange, float itemMaxReach, float itemHitboxMargin) {
      if ((blacklisted.contains(reachEntity.type) || !reachEntity.isLivingEntity) && reachEntity.type != EntityTypes.END_CRYSTAL) {
         return false;
      } else if (this.player.gamemode != GameMode.CREATIVE && this.player.gamemode != GameMode.SPECTATOR) {
         if (this.player.inVehicle()) {
            return false;
         } else if (this.cancelBuffer != 0.0D) {
            Reach.CheckResult result = this.checkReach(reachEntity, this.player.x, this.player.y, this.player.z, hasAttackRange, itemMaxReach, itemHitboxMargin, true);
            return result.isFlag();
         } else {
            SimpleCollisionBox targetBox = this.getTargetBox(reachEntity);
            double maxReach = this.applyReachModifiers(targetBox, hasAttackRange, itemMaxReach, itemHitboxMargin, !this.player.packetStateData.didLastMovementIncludePosition);
            return ReachUtils.getMinReachToBox(this.player, targetBox) > maxReach;
         }
      } else {
         return false;
      }
   }

   private void tickBetterReachCheckWithAngle() {
      ObjectIterator var1 = this.playerAttackQueue.int2ObjectEntrySet().iterator();

      while(var1.hasNext()) {
         Int2ObjectMap.Entry<Reach.InteractionData> attack = (Int2ObjectMap.Entry)var1.next();
         PacketEntity reachEntity = (PacketEntity)this.player.compensatedEntities.entityMap.get(attack.getIntKey());
         if (reachEntity != null) {
            Reach.InteractionData interactionData = (Reach.InteractionData)attack.getValue();
            Reach.CheckResult result = this.checkReach(reachEntity, interactionData.x, interactionData.y, interactionData.z, interactionData.hasAttackRange, interactionData.maxReach, interactionData.hitboxMargin, false);
            String var10001;
            String added;
            PacketEntitySizeable sizeable;
            switch(result.type().ordinal()) {
            case 0:
               added = ", type=" + reachEntity.type.getName().getKey();
               if (reachEntity instanceof PacketEntitySizeable) {
                  sizeable = (PacketEntitySizeable)reachEntity;
                  added = added + ", size=" + sizeable.size;
               }

               var10001 = result.verbose();
               this.flagAndAlert(var10001 + added);
               break;
            case 1:
               added = "type=" + reachEntity.type.getName().getKey();
               if (reachEntity instanceof PacketEntitySizeable) {
                  sizeable = (PacketEntitySizeable)reachEntity;
                  added = added + ", size=" + sizeable.size;
               }

               Hitboxes var10000 = (Hitboxes)this.player.checkManager.getCheck(Hitboxes.class);
               var10001 = result.verbose();
               var10000.flagAndAlert(var10001 + added);
            }
         }
      }

      this.playerAttackQueue.clear();
   }

   @NotNull
   private Reach.CheckResult checkReach(PacketEntity reachEntity, double x, double y, double z, boolean hasAttackRange, float itemMaxReach, float itemHitboxMargin, boolean isPrediction) {
      SimpleCollisionBox targetBox = this.getTargetBox(reachEntity);
      double maxReach = this.applyReachModifiers(targetBox, hasAttackRange, itemMaxReach, itemHitboxMargin, !this.player.packetStateData.didLastLastMovementIncludePosition);
      double minDistance = Double.MAX_VALUE;
      List<Vector3dm> possibleLookDirs = new ArrayList(Collections.singletonList(ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch)));
      if (!isPrediction) {
         ((List)possibleLookDirs).add(ReachUtils.getLook(this.player, this.player.lastYaw, this.player.pitch));
         if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            ((List)possibleLookDirs).add(ReachUtils.getLook(this.player, this.player.lastYaw, this.player.lastPitch));
         }

         if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            possibleLookDirs = Collections.singletonList(ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch));
         }
      }

      double distance = maxReach + 3.0D;
      double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
      Vector3dm eyePos = new Vector3dm(x, 0.0D, z);
      Iterator var22 = ((List)possibleLookDirs).iterator();

      while(true) {
         while(var22.hasNext()) {
            Vector3dm lookVec = (Vector3dm)var22.next();
            double[] var24 = possibleEyeHeights;
            int var25 = possibleEyeHeights.length;

            for(int var26 = 0; var26 < var25; ++var26) {
               double eye = var24[var26];
               eyePos.setY(y + eye);
               Vector3dm endReachPos = eyePos.clone().add(lookVec.getX() * distance, lookVec.getY() * distance, lookVec.getZ() * distance);
               Vector3dm intercept = (Vector3dm)ReachUtils.calculateIntercept(targetBox, eyePos, endReachPos).first();
               if (ReachUtils.isVecInside(targetBox, eyePos)) {
                  minDistance = 0.0D;
                  break;
               }

               if (intercept != null) {
                  minDistance = Math.min(eyePos.distance(intercept), minDistance);
               }
            }
         }

         if (!blacklisted.contains(reachEntity.type) && reachEntity.isLivingEntity || reachEntity.type == EntityTypes.END_CRYSTAL) {
            if (minDistance == Double.MAX_VALUE) {
               this.cancelBuffer = 1.0D;
               return new Reach.CheckResult(Reach.ResultType.HITBOX, "");
            }

            if (minDistance > maxReach) {
               this.cancelBuffer = 1.0D;
               Reach.ResultType var10002 = Reach.ResultType.REACH;
               Object[] var10004 = new Object[]{minDistance};
               return new Reach.CheckResult(var10002, String.format("%.5f", var10004) + " blocks");
            }

            this.cancelBuffer = Math.max(0.0D, this.cancelBuffer - 0.25D);
         }

         return NONE;
      }
   }

   private SimpleCollisionBox getTargetBox(PacketEntity reachEntity) {
      return reachEntity.type == EntityTypes.END_CRYSTAL ? new SimpleCollisionBox(reachEntity.trackedServerPosition.getPos().subtract(1.0D, 0.0D, 1.0D), reachEntity.trackedServerPosition.getPos().add(1.0D, 2.0D, 1.0D)) : reachEntity.getPossibleCollisionBoxes();
   }

   private double applyReachModifiers(SimpleCollisionBox targetBox, boolean hasAttackRange, float itemMaxReach, float itemHitboxMargin, boolean giveMovementThreshold) {
      double hitboxMargin = this.threshold;
      double maxReach;
      if (hasAttackRange) {
         maxReach = (double)itemMaxReach;
         hitboxMargin += (double)itemHitboxMargin;
      } else {
         maxReach = this.player.compensatedEntities.self.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
         if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
            hitboxMargin += 0.10000000149011612D;
         }
      }

      if (giveMovementThreshold || this.player.canSkipTicks()) {
         hitboxMargin += this.player.getMovementThreshold();
      }

      targetBox.expand(hitboxMargin);
      return maxReach;
   }

   public void onReload(ConfigManager config) {
      this.cancelImpossibleHits = config.getBooleanElse("Reach.block-impossible-hits", true);
      this.threshold = config.getDoubleElse("Reach.threshold", 5.0E-4D);
   }

   static {
      blacklisted = Arrays.asList(EntityTypes.BOAT, EntityTypes.CHEST_BOAT, EntityTypes.SHULKER);
      NONE = new Reach.CheckResult(Reach.ResultType.NONE, "");
      ATTACK_RANGE_COMPONENT_EXISTS = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);
   }

   private static record InteractionData(double x, double y, double z, boolean hasAttackRange, float maxReach, float hitboxMargin) {
      private InteractionData(double x, double y, double z, boolean hasAttackRange, float maxReach, float hitboxMargin) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.hasAttackRange = hasAttackRange;
         this.maxReach = maxReach;
         this.hitboxMargin = hitboxMargin;
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }

      public boolean hasAttackRange() {
         return this.hasAttackRange;
      }

      public float maxReach() {
         return this.maxReach;
      }

      public float hitboxMargin() {
         return this.hitboxMargin;
      }
   }

   private static record CheckResult(Reach.ResultType type, String verbose) {
      private CheckResult(Reach.ResultType type, String verbose) {
         this.type = type;
         this.verbose = verbose;
      }

      public boolean isFlag() {
         return this.type != Reach.ResultType.NONE;
      }

      public Reach.ResultType type() {
         return this.type;
      }

      public String verbose() {
         return this.verbose;
      }
   }

   private static enum ResultType {
      REACH,
      HITBOX,
      NONE;

      // $FF: synthetic method
      private static Reach.ResultType[] $values() {
         return new Reach.ResultType[]{REACH, HITBOX, NONE};
      }
   }
}
