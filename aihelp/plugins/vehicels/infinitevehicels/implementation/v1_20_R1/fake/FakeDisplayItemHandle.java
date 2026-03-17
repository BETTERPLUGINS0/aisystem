package implementation.v1_20_R1.fake;

import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.enums.EnumItemDisplaySlot;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import advancedplugins.pm2.cv.util.PacketUtil;
import implementation.v1_20_R1.util.PacketWritingUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Objects;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.b;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemDisplayContext;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeDisplayItemHandle extends FakeDisplayHandle<ItemDisplay> implements advancedplugins.pm2.cv.fake.display.FakeDisplayItemHandle {
   public FakeDisplayItemHandle(@NotNull World var1) {
      super(var1);
   }

   protected ItemDisplay createHandleInstance(@NotNull World var1) {
      return (ItemDisplay)EntityTypes.ae.a(((CraftWorld)var1).getHandle());
   }

   public void applyProperty(@NotNull EnumDisplayProperty var1, @NotNull Object var2) {
      super.applyProperty(var1, var2);
      switch(var1) {
      case ITEM_STACK:
         ((ItemDisplay)this.handle).a(CraftItemStack.asNMSCopy((ItemStack)var2));
         break;
      case ITEM_DISPLAY_SLOT:
         ((ItemDisplay)this.handle).a(this.toDisplayContext((EnumItemDisplaySlot)var2));
      }

   }

   private ItemDisplayContext toDisplayContext(EnumItemDisplaySlot var1) {
      switch(var1) {
      case NONE:
         return ItemDisplayContext.a;
      case THIRD_PERSON_LEFT_HAND:
         return ItemDisplayContext.b;
      case THIRD_PERSON_RIGHT_HAND:
         return ItemDisplayContext.c;
      case FIRST_PERSON_LEFT_HAND:
         return ItemDisplayContext.d;
      case FIRST_PERSON_RIGHT_HAND:
         return ItemDisplayContext.e;
      case HEAD:
         return ItemDisplayContext.f;
      case GUI:
         return ItemDisplayContext.g;
      case GROUND:
         return ItemDisplayContext.h;
      case FIXED:
         return ItemDisplayContext.i;
      default:
         throw new IllegalStateException();
      }
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
         DataWatcherObject var3 = null;
         Field[] var4 = var2.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field var7 = var4[var6];
            var7.setAccessible(true);
            if (DataWatcherObject.class.isAssignableFrom(var7.getType())) {
               DataWatcherObject var8 = (DataWatcherObject)var7.get(this.handle);
               if (Objects.equals(var8.b(), DataWatcherRegistry.h)) {
                  var3 = var8;
                  break;
               }
            }
         }

         if (var3 != null) {
            b var10;
            if (var1) {
               var10 = b.a(var3, net.minecraft.world.item.ItemStack.b);
            } else {
               var10 = b.a(var3, ((ItemDisplay)this.handle).p());
            }

            PacketDataSerializer var11 = new PacketDataSerializer(Unpooled.buffer());
            var11.d(((ItemDisplay)this.handle).af());
            this.packData(Collections.singletonList(var10), var11);
            return new PacketPlayOutEntityMetadata(var11);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return null;
   }
}
