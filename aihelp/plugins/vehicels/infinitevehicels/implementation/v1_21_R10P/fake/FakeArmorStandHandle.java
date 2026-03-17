package implementation.v1_21_R10P.fake;

import advancedplugins.pm2.cv.api.enums.EnumRotableLimb;
import advancedplugins.pm2.cv.api.enums.EnumStandSlot;
import advancedplugins.pm2.cv.enums.EnumStandProperty;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeArmorStandHandle extends FakeEntityHandle<ArmorStand, EnumStandProperty> implements advancedplugins.pm2.cv.fake.armorstand.FakeArmorStandHandle {
   public FakeArmorStandHandle(@NotNull World var1) {
      super(var1);
   }

   protected ArmorStand createHandleInstance(@NotNull World var1) {
      return new ArmorStand(((CraftWorld)var1).getHandle(), 0.0D, 0.0D, 0.0D);
   }

   @NotNull
   protected List<Packet<?>> createShowPackets() {
      List var1 = super.createShowPackets();
      var1.add(this.createEquipmentPacket());
      return var1;
   }

   public void applyProperty(@NotNull EnumStandProperty var1, @NotNull Object var2) {
      switch(var1) {
      case VISIBILITY:
         ((ArmorStand)this.handle).setInvisible(!(Boolean)var2);
         break;
      case GLOWING:
         ((ArmorStand)this.handle).setGlowingTag((Boolean)var2);
         break;
      case GRAVITY:
         ((ArmorStand)this.handle).setNoGravity(!(Boolean)var2);
         break;
      case MARKER:
         ((ArmorStand)this.handle).setMarker((Boolean)var2);
         break;
      case BASE_PLATE:
         ((ArmorStand)this.handle).setNoBasePlate(!(Boolean)var2);
         break;
      case ARMS:
         ((ArmorStand)this.handle).setShowArms((Boolean)var2);
         break;
      case SMALL:
         ((ArmorStand)this.handle).setSmall((Boolean)var2);
         break;
      case SILENT:
         ((ArmorStand)this.handle).setSilent((Boolean)var2);
         break;
      case CUSTOM_NAME:
         String var3 = (String)var2;
         if (var3.length() > 256) {
            var3 = var3.substring(0, 256);
         }

         ((ArmorStand)this.handle).setCustomName(CraftChatMessage.fromStringOrNull(var3));
         break;
      case CUSTOM_NAME_VISIBILITY:
         ((ArmorStand)this.handle).setCustomNameVisible((Boolean)var2);
      }

   }

   public void applyEquipment(@NotNull EnumStandSlot var1, @Nullable ItemStack var2) {
      net.minecraft.world.item.ItemStack var3 = var2 != null ? CraftItemStack.asNMSCopy(var2) : net.minecraft.world.item.ItemStack.EMPTY;
      switch(var1) {
      case HEAD:
         ((ArmorStand)this.handle).setItemSlot(EquipmentSlot.HEAD, var3);
         break;
      case LEFT_HAND:
         ((ArmorStand)this.handle).setItemSlot(EquipmentSlot.OFFHAND, var3);
         break;
      case RIGHT_HAND:
         ((ArmorStand)this.handle).setItemSlot(EquipmentSlot.MAINHAND, var3);
         break;
      default:
         throw new IllegalStateException();
      }

   }

   public void sendEquipment() {
      this.broadcastPacketToViewers(this.createEquipmentPacket());
   }

   public void applyLimbRotation(@NotNull EnumRotableLimb var1, float var2, float var3, float var4) {
      Rotations var5 = new Rotations(var2, var3, var4);

      try {
         switch(var1) {
         case HEAD:
            ((ArmorStand)this.handle).setHeadPose(var5);
            break;
         case LEFT_ARM:
            ((ArmorStand)this.handle).setLeftArmPose(var5);
            break;
         case RIGHT_ARM:
            ((ArmorStand)this.handle).setRightArmPose(var5);
         }
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

   }

   @NotNull
   private ClientboundSetEquipmentPacket createEquipmentPacket() {
      ArrayList var1 = new ArrayList();
      EquipmentSlot[] var2 = EquipmentSlot.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EquipmentSlot var5 = var2[var4];
         net.minecraft.world.item.ItemStack var6 = ((ArmorStand)this.handle).getItemBySlot(var5);
         var1.add(Pair.of(var5, var6 != null ? var6 : net.minecraft.world.item.ItemStack.EMPTY));
      }

      return new ClientboundSetEquipmentPacket(((ArmorStand)this.handle).getId(), var1);
   }
}
