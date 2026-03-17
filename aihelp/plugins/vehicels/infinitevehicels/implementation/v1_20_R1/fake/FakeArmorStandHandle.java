package implementation.v1_20_R1.fake;

import advancedplugins.pm2.cv.api.enums.EnumRotableLimb;
import advancedplugins.pm2.cv.api.enums.EnumStandSlot;
import advancedplugins.pm2.cv.enums.EnumStandProperty;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Vector3f;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeArmorStandHandle extends FakeEntityHandle<EntityArmorStand, EnumStandProperty> implements advancedplugins.pm2.cv.fake.armorstand.FakeArmorStandHandle {
   public FakeArmorStandHandle(@NotNull World var1) {
      super(var1);
   }

   protected EntityArmorStand createHandleInstance(@NotNull World var1) {
      return new EntityArmorStand(((CraftWorld)var1).getHandle(), 0.0D, 0.0D, 0.0D);
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
         ((EntityArmorStand)this.handle).j(!(Boolean)var2);
         break;
      case GLOWING:
         ((EntityArmorStand)this.handle).i((Boolean)var2);
         break;
      case GRAVITY:
         ((EntityArmorStand)this.handle).e(!(Boolean)var2);
         break;
      case MARKER:
         ((EntityArmorStand)this.handle).u((Boolean)var2);
         break;
      case BASE_PLATE:
         ((EntityArmorStand)this.handle).s(!(Boolean)var2);
         break;
      case ARMS:
         ((EntityArmorStand)this.handle).a((Boolean)var2);
         break;
      case SMALL:
         ((EntityArmorStand)this.handle).t((Boolean)var2);
         break;
      case SILENT:
         ((EntityArmorStand)this.handle).d((Boolean)var2);
         break;
      case CUSTOM_NAME:
         String var3 = (String)var2;
         if (var3.length() > 256) {
            var3 = var3.substring(0, 256);
         }

         ((EntityArmorStand)this.handle).b(CraftChatMessage.fromStringOrNull(var3));
         break;
      case CUSTOM_NAME_VISIBILITY:
         ((EntityArmorStand)this.handle).n((Boolean)var2);
      }

   }

   public void applyEquipment(@NotNull EnumStandSlot var1, @Nullable ItemStack var2) {
      net.minecraft.world.item.ItemStack var3 = var2 != null ? CraftItemStack.asNMSCopy(var2) : net.minecraft.world.item.ItemStack.b;
      switch(var1) {
      case HEAD:
         ((EntityArmorStand)this.handle).a(EnumItemSlot.f, var3);
         break;
      case LEFT_HAND:
         ((EntityArmorStand)this.handle).a(EnumItemSlot.b, var3);
         break;
      case RIGHT_HAND:
         ((EntityArmorStand)this.handle).a(EnumItemSlot.a, var3);
         break;
      default:
         throw new IllegalStateException();
      }

   }

   public void sendEquipment() {
      this.broadcastPacketToViewers(this.createEquipmentPacket());
   }

   public void applyLimbRotation(@NotNull EnumRotableLimb var1, float var2, float var3, float var4) {
      Vector3f var5 = new Vector3f(var2, var3, var4);

      try {
         switch(var1) {
         case HEAD:
            ((EntityArmorStand)this.handle).a(var5);
            break;
         case LEFT_ARM:
            ((EntityArmorStand)this.handle).c(var5);
            break;
         case RIGHT_ARM:
            ((EntityArmorStand)this.handle).d(var5);
         }
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

   }

   @NotNull
   private PacketPlayOutEntityEquipment createEquipmentPacket() {
      ArrayList var1 = new ArrayList();
      EnumItemSlot[] var2 = EnumItemSlot.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumItemSlot var5 = var2[var4];
         net.minecraft.world.item.ItemStack var6 = ((EntityArmorStand)this.handle).c(var5);
         var1.add(Pair.of(var5, var6 != null ? var6 : net.minecraft.world.item.ItemStack.b));
      }

      return new PacketPlayOutEntityEquipment(((EntityArmorStand)this.handle).af(), var1);
   }
}
