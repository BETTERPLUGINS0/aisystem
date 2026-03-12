package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.checks.impl.sprint.SprintD;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.inventory.EnchantmentHelper;
import ac.grim.grimac.utils.math.GrimMath;
import java.util.ArrayList;

public class PacketEntitySelf extends PacketEntity {
   private final GrimPlayer player;
   public int opLevel;

   public PacketEntitySelf(GrimPlayer player) {
      super(player, EntityTypes.PLAYER);
      this.player = player;
   }

   public PacketEntitySelf(GrimPlayer player, PacketEntitySelf old) {
      super(player, EntityTypes.PLAYER);
      this.player = player;
      this.opLevel = old.opLevel;
      this.attributeMap.putAll(old.attributeMap);
   }

   protected void initAttributes(GrimPlayer player) {
      super.initAttributes(player);
      if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
         this.setAttribute(Attributes.STEP_HEIGHT, 0.5D);
      }

      ((ValuedAttribute)this.getAttribute(Attributes.SCALE).orElseThrow()).withSetRewriter((oldValue, newValue) -> {
         if (!player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_20_5) && !newValue.equals(oldValue)) {
            player.possibleEyeHeights[2][0] = 0.4D * newValue;
            player.possibleEyeHeights[2][1] = 1.62D * newValue;
            player.possibleEyeHeights[2][2] = 1.27D * newValue;
            player.possibleEyeHeights[1][0] = 1.27D * newValue;
            player.possibleEyeHeights[1][1] = 1.62D * newValue;
            player.possibleEyeHeights[1][2] = 0.4D * newValue;
            player.possibleEyeHeights[0][0] = 1.62D * newValue;
            player.possibleEyeHeights[0][1] = 1.27D * newValue;
            player.possibleEyeHeights[0][2] = 0.4D * newValue;
            return newValue;
         } else {
            return oldValue;
         }
      });
      ValuedAttribute movementSpeed = ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.10000000149011612D, 0.0D, 1024.0D);
      movementSpeed.with(new WrapperPlayServerUpdateAttributes.Property(Attributes.MOVEMENT_SPEED, 0.10000000149011612D, new ArrayList()));
      this.trackAttribute(movementSpeed);
      this.trackAttribute(ValuedAttribute.ranged(Attributes.ATTACK_DAMAGE, 2.0D, 0.0D, 2048.0D));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.ATTACK_SPEED, 4.0D, 0.0D, 1024.0D).requiredVersion(player, ClientVersion.V_1_9));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.JUMP_STRENGTH, 0.41999998688697815D, 0.0D, 32.0D).requiredVersion(player, ClientVersion.V_1_20_5));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.BLOCK_BREAK_SPEED, 1.0D, 0.0D, 1024.0D).requiredVersion(player, ClientVersion.V_1_20_5));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.MINING_EFFICIENCY, 0.0D, 0.0D, 1024.0D).requiredVersion(player, ClientVersion.V_1_21));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.SUBMERGED_MINING_SPEED, 0.2D, 0.0D, 20.0D).requiredVersion(player, ClientVersion.V_1_21));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.ENTITY_INTERACTION_RANGE, 3.0D, 0.0D, 64.0D).requiredVersion(player, ClientVersion.V_1_20_5));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.BLOCK_INTERACTION_RANGE, 4.5D, 0.0D, 64.0D).withGetRewriter((value) -> {
         return player.gamemode != GameMode.CREATIVE || !PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_20_5) && !player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5) ? value : 5.0D;
      }).requiredVersion(player, ClientVersion.V_1_20_5));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.0D, 0.0D, 1.0D).withGetRewriter((value) -> {
         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            return 0.0D;
         } else {
            double depthStrider = (double)EnchantmentHelper.getMaximumEnchantLevel(player.inventory, EnchantmentTypes.DEPTH_STRIDER);
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21)) {
               return depthStrider;
            } else {
               return PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21) ? depthStrider / 3.0D : value;
            }
         }
      }).requiredVersion(player, ClientVersion.V_1_21));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_EFFICIENCY, 0.0D, 0.0D, 1.0D).requiredVersion(player, ClientVersion.V_1_21));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.SNEAKING_SPEED, 0.3D, 0.0D, 1.0D).withGetRewriter((value) -> {
         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_19)) {
            return 0.30000001192092896D;
         } else {
            int swiftSneak = player.inventory.getLeggings().getEnchantmentLevel(EnchantmentTypes.SWIFT_SNEAK);
            double clamped = (double)GrimMath.clamp(0.3F + (float)swiftSneak * 0.15F, 0.0F, 1.0F);
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21)) {
               return clamped;
            } else {
               return PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21) ? clamped : value;
            }
         }
      }).requiredVersion(player, ClientVersion.V_1_21));
   }

   public boolean inVehicle() {
      return this.getRiding() != null;
   }

   public void addPotionEffect(PotionType effect, int amplifier) {
      if (effect == PotionTypes.BLINDNESS && !this.hasPotionEffect(PotionTypes.BLINDNESS)) {
         ((SprintD)this.player.checkManager.getPostPredictionCheck(SprintD.class)).startedSprintingBeforeBlind = this.player.isSprinting;
      }

      this.player.pointThreeEstimator.updatePlayerPotions(effect, amplifier);
      super.addPotionEffect(effect, amplifier);
   }

   public void removePotionEffect(PotionType effect) {
      this.player.pointThreeEstimator.updatePlayerPotions(effect, (Integer)null);
      super.removePotionEffect(effect);
   }

   public void onFirstTransaction(boolean relative, boolean hasPos, double relX, double relY, double relZ, GrimPlayer player) {
   }

   public void onSecondTransaction() {
   }

   public SimpleCollisionBox getPossibleCollisionBoxes() {
      return this.player.boundingBox.copy();
   }
}
