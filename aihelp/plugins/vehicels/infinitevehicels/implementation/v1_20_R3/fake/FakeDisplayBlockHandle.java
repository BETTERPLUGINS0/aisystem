package implementation.v1_20_R3.fake;

import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import advancedplugins.pm2.cv.util.PacketUtil;
import implementation.v1_20_R3.util.PacketWritingUtil;
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
import net.minecraft.world.level.block.Blocks;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeDisplayBlockHandle extends FakeDisplayHandle<BlockDisplay> implements advancedplugins.pm2.cv.fake.display.FakeDisplayBlockHandle {
   public FakeDisplayBlockHandle(@NotNull World var1) {
      super(var1);
   }

   protected BlockDisplay createHandleInstance(@NotNull World var1) {
      return (BlockDisplay)EntityTypes.j.a(((CraftWorld)var1).getHandle());
   }

   public void applyProperty(@NotNull EnumDisplayProperty var1, @NotNull Object var2) {
      super.applyProperty(var1, var2);
      switch(var1) {
      case BLOCK_DATA:
         ((BlockDisplay)this.handle).c(((CraftBlockData)var2).getState());
      default:
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
               if (Objects.equals(var8.b(), DataWatcherRegistry.i)) {
                  var3 = var8;
                  break;
               }
            }
         }

         if (var3 != null) {
            b var10;
            if (var1) {
               var10 = b.a(var3, Blocks.a.o());
            } else {
               var10 = b.a(var3, ((BlockDisplay)this.handle).u());
            }

            PacketDataSerializer var11 = new PacketDataSerializer(Unpooled.buffer());
            var11.c(((BlockDisplay)this.handle).aj());
            this.packData(Collections.singletonList(var10), var11);
            return new PacketPlayOutEntityMetadata(var11);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return null;
   }
}
