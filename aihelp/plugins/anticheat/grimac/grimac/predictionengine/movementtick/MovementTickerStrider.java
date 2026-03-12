package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import java.util.ArrayList;

public class MovementTickerStrider extends MovementTickerRideable {
   private static final WrapperPlayServerUpdateAttributes.PropertyModifier SUFFOCATING_MODIFIER;

   public MovementTickerStrider(GrimPlayer player) {
      super(player);
      this.movementInput = new Vector3dm(0, 0, 1);
   }

   public static void floatStrider(GrimPlayer player) {
      if (player.wasTouchingLava) {
         if (isAbove(player) && player.compensatedWorld.getLavaFluidLevelAt((int)Math.floor(player.x), (int)Math.floor(player.y + 1.0D), (int)Math.floor(player.z)) == 0.0D) {
            player.onGround = true;
         } else {
            player.clientVelocity.multiply(0.5D).add(0.0D, 0.05D, 0.0D);
         }
      }

   }

   public static boolean isAbove(GrimPlayer player) {
      return player.y > Math.floor(player.y) + 0.5D - 9.999999747378752E-6D;
   }

   public void livingEntityAIStep() {
      super.livingEntityAIStep();
      StateType posMaterial = this.player.compensatedWorld.getBlockType(this.player.x, this.player.y, this.player.z);
      StateType belowMaterial = BlockProperties.getOnPos(this.player, this.player.mainSupportingBlockData, new Vector3d(this.player.x, this.player.y, this.player.z));
      PacketEntityStrider strider = (PacketEntityStrider)this.player.compensatedEntities.self.getRiding();
      strider.isShaking = !BlockTags.STRIDER_WARM_BLOCKS.contains(posMaterial) && !BlockTags.STRIDER_WARM_BLOCKS.contains(belowMaterial) && !this.player.wasTouchingLava;
   }

   public float getSteeringSpeed() {
      PacketEntityStrider strider = (PacketEntityStrider)this.player.compensatedEntities.self.getRiding();
      boolean newSpeed = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20);
      float coldSpeed = newSpeed ? 0.35F : 0.23F;
      ValuedAttribute movementSpeedAttr = (ValuedAttribute)strider.getAttribute(Attributes.MOVEMENT_SPEED).orElseThrow();
      float updatedMovementSpeed = (float)movementSpeedAttr.get();
      if (newSpeed) {
         WrapperPlayServerUpdateAttributes.Property lastProperty = (WrapperPlayServerUpdateAttributes.Property)movementSpeedAttr.property().orElse((Object)null);
         if (lastProperty != null && (!strider.isShaking || lastProperty.getModifiers().stream().noneMatch((mod) -> {
            return mod.getName().getKey().equals("suffocating");
         }))) {
            WrapperPlayServerUpdateAttributes.Property newProperty = new WrapperPlayServerUpdateAttributes.Property(lastProperty.getAttribute(), lastProperty.getValue(), new ArrayList(lastProperty.getModifiers()));
            if (!strider.isShaking) {
               newProperty.getModifiers().removeIf((modifier) -> {
                  return modifier.getName().getKey().equals("suffocating");
               });
            } else {
               newProperty.getModifiers().add(SUFFOCATING_MODIFIER);
            }

            movementSpeedAttr.with(newProperty);
            updatedMovementSpeed = (float)movementSpeedAttr.get();
            movementSpeedAttr.with(lastProperty);
         }
      }

      return updatedMovementSpeed * (strider.isShaking ? coldSpeed : 0.55F);
   }

   public boolean canStandOnLava() {
      return true;
   }

   static {
      SUFFOCATING_MODIFIER = new WrapperPlayServerUpdateAttributes.PropertyModifier(ResourceLocation.minecraft("suffocating"), -0.3400000035762787D, WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_BASE);
   }
}
