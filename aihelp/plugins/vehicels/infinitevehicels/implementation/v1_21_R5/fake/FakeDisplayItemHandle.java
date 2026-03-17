package implementation.v1_21_R5.fake;

import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.enums.EnumItemDisplaySlot;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import advancedplugins.pm2.cv.util.PacketUtil;
import implementation.v1_21_R5.util.PacketWritingUtil;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Objects;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemDisplayContext;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeDisplayItemHandle extends FakeDisplayHandle<ItemDisplay> implements advancedplugins.pm2.cv.fake.display.FakeDisplayItemHandle {
   public FakeDisplayItemHandle(@NotNull World var1) {
      super(var1);
   }

   protected ItemDisplay createHandleInstance(@NotNull World var1) {
      return (ItemDisplay)EntityType.ITEM_DISPLAY.create(((CraftWorld)var1).getHandle(), EntitySpawnReason.COMMAND);
   }

   public void applyProperty(@NotNull EnumDisplayProperty var1, @NotNull Object var2) {
      super.applyProperty(var1, var2);
      switch(var1) {
      case ITEM_STACK:
         ((ItemDisplay)this.handle).setItemStack(CraftItemStack.asNMSCopy((ItemStack)var2));
         break;
      case ITEM_DISPLAY_SLOT:
         ((ItemDisplay)this.handle).setItemTransform(this.toDisplayContext((EnumItemDisplaySlot)var2));
      }

   }

   private ItemDisplayContext toDisplayContext(EnumItemDisplaySlot var1) {
      ItemDisplayContext var10000;
      switch(var1) {
      case NONE:
         var10000 = ItemDisplayContext.NONE;
         break;
      case THIRD_PERSON_LEFT_HAND:
         var10000 = ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
         break;
      case THIRD_PERSON_RIGHT_HAND:
         var10000 = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
         break;
      case FIRST_PERSON_LEFT_HAND:
         var10000 = ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
         break;
      case FIRST_PERSON_RIGHT_HAND:
         var10000 = ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
         break;
      case HEAD:
         var10000 = ItemDisplayContext.HEAD;
         break;
      case GUI:
         var10000 = ItemDisplayContext.GUI;
         break;
      case GROUND:
         var10000 = ItemDisplayContext.GROUND;
         break;
      case FIXED:
         var10000 = ItemDisplayContext.FIXED;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public void trickySetInvisibleTo(@NotNull Player var1, boolean var2, @Nullable FakeEntityLinker.Generic var3) {
      Packet var4 = this.createTrickyInvisibilityPacket(var2);
      if (var4 != null) {
         if (var3 == null) {
            this.packetService.sendPacket((Player)var1, var4);
         } else {
            ChannelPipeline var5 = PacketUtil.getPipeline(var1);
            if (var5 != null) {
               PacketWritingUtil.compressAndWriteToPipeline(var4, var5);
               var3.track(var5);
            }
         }
      }

   }

   @Nullable
   private Packet<?> createTrickyInvisibilityPacket(boolean var1) {
      Class var2 = BlockDisplay.class;

      try {
         EntityDataAccessor var3 = null;
         Field[] var4 = var2.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field var7 = var4[var6];
            var7.setAccessible(true);
            if (EntityDataAccessor.class.isAssignableFrom(var7.getType())) {
               EntityDataAccessor var8 = (EntityDataAccessor)var7.get(this.handle);
               if (Objects.equals(var8.serializer(), EntityDataSerializers.ITEM_STACK)) {
                  var3 = var8;
                  break;
               }
            }
         }

         if (var3 != null) {
            DataValue var10;
            if (var1) {
               var10 = DataValue.create(var3, net.minecraft.world.item.ItemStack.EMPTY);
            } else {
               var10 = DataValue.create(var3, ((ItemDisplay)this.handle).getItemStack());
            }

            return new ClientboundSetEntityDataPacket(((ItemDisplay)this.handle).getId(), Collections.singletonList(var10));
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return null;
   }
}
