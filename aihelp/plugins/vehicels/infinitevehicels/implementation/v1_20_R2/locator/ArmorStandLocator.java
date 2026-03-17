package implementation.v1_20_R2.locator;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.handler.PlayerWrapperHandler;
import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.util.PacketUtil;
import gnu.trove.set.hash.THashSet;
import implementation.v1_20_R2.util.PacketWritingUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorStandLocator extends advancedplugins.pm2.cv.locator.ArmorStandLocator {
   public void setLocation(@NotNull ArmorStand var1, double var2, double var4, double var6, float var8, float var9, int var10) {
      EntityArmorStand var11 = this.getHandle(var1);
      this.process(var11, false, var2, var4, var6, var8, var9, var10);
   }

   public void setLocation(@NotNull ArmorStand var1, double var2, double var4, double var6, int var8) {
      EntityArmorStand var9 = this.getHandle(var1);
      this.process(var9, false, var2, var4, var6, (Float)null, (Float)null, var8);
   }

   @NotNull
   public Set<ChannelPipeline> writeLocation(@NotNull ArmorStand var1, double var2, double var4, double var6, float var8, float var9, int var10) {
      EntityArmorStand var11 = this.getHandle(var1);
      Set var12 = this.process(var11, true, var2, var4, var6, var8, var9, var10);
      return (Set)(var12 != null ? var12 : new THashSet());
   }

   @NotNull
   public Set<ChannelPipeline> writeLocation(@NotNull ArmorStand var1, double var2, double var4, double var6, int var8) {
      EntityArmorStand var9 = this.getHandle(var1);
      Set var10 = this.process(var9, true, var2, var4, var6, (Float)null, (Float)null, var8);
      return (Set)(var10 != null ? var10 : new THashSet());
   }

   @Nullable
   private Set<ChannelPipeline> process(EntityArmorStand var1, boolean var2, double var3, double var5, double var7, @Nullable Float var9, @Nullable Float var10, int var11) {
      advancedplugins.pm2.cv.locator.ArmorStandLocator.Entry var12 = this.getEntry(var1.ah());
      if (!var12.shouldSet(var3, var5, var7)) {
         return null;
      } else {
         var12.setLastXSet(var3);
         var12.setLastYSet(var5);
         var12.setLastZSet(var7);
         var12.setFirstTimeSet(false);
         boolean var13 = this.isFlagPresent(var11, 2);
         if (!this.reducedCalls || var13 || var12.shouldUpdateServer(var3, var5, var7)) {
            var12.setLastServerX(var3);
            var12.setLastServerY(var5);
            var12.setLastServerZ(var7);
            var12.setFirstServerUpdate(false);
            Run.sync(() -> {
               var1.e(var3, var5, var7);
               if (var9 != null && var10 != null) {
                  var1.r(var9);
                  var1.s(var10);
               }

            });
         }

         if (var2) {
            Set var18 = this.preparePipelines(var1.getBukkitEntity().getWorld());
            if (var18.size() > 0) {
               Packet var19 = this.buildLocationPacket(var1, var3, var5, var7, var9, var10, var11);
               Iterator var20 = var18.iterator();

               while(var20.hasNext()) {
                  ChannelPipeline var17 = (ChannelPipeline)var20.next();
                  PacketWritingUtil.compressAndWriteToPipeline(var19, var17);
               }
            }

            return var18;
         } else {
            Packet var14 = this.buildLocationPacket(var1, var3, var5, var7, var9, var10, var11);
            Iterator var15 = var1.getBukkitEntity().getWorld().getPlayers().iterator();

            while(var15.hasNext()) {
               Player var16 = (Player)var15.next();
               this.packetService.sendPacket((Player)var16, var14, true);
            }

            return null;
         }
      }
   }

   private Set<ChannelPipeline> preparePipelines(@NotNull World var1) {
      PlayerWrapperHandler var2 = InfiniteVehicles.getPlayerWrapperHandler();
      THashSet var3 = new THashSet();
      Iterator var4 = var1.getPlayers().iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         if (var5.isOnline()) {
            PlayerWrapper var6 = var2.getWrapper(var5);
            ChannelPipeline var7 = var6.getPipeline();
            if (var7 != null) {
               var3.add(var7);
            }
         }
      }

      return var3;
   }

   private Packet<?> buildLocationPacket(@NotNull EntityArmorStand var1, double var2, double var4, double var6, @Nullable Float var8, @Nullable Float var9, int var10) {
      advancedplugins.pm2.cv.locator.ArmorStandLocator.Entry var11 = this.getEntry(var1.ah());
      Object var12;
      if (!var11.shouldTeleport() && !this.isFlagPresent(var10, 8)) {
         if (var8 != null && var9 != null) {
            var12 = new PacketPlayOutRelEntityMoveLook(var1.ah(), (short)((int)PacketUtil.serializeDeltaLocation(var2, var11.getLastXSent())), (short)((int)PacketUtil.serializeDeltaLocation(var4, var11.getLastYSent())), (short)((int)PacketUtil.serializeDeltaLocation(var6, var11.getLastZSent())), PacketUtil.serializeRotationAngle(var8), PacketUtil.serializeRotationAngle(var9), var1.aJ);
         } else {
            var12 = new PacketPlayOutRelEntityMove(var1.ah(), (short)((int)PacketUtil.serializeDeltaLocation(var2, var11.getLastXSent())), (short)((int)PacketUtil.serializeDeltaLocation(var4, var11.getLastYSent())), (short)((int)PacketUtil.serializeDeltaLocation(var6, var11.getLastZSent())), var1.aJ);
         }
      } else {
         var11.teleported();
         PacketDataSerializer var13 = new PacketDataSerializer(Unpooled.buffer());
         var13.c(var1.ah());
         var13.a(var2);
         var13.a(var4);
         var13.a(var6);
         var13.k(PacketUtil.serializeRotationAngle(var1.dB()));
         var13.k(PacketUtil.serializeRotationAngle(var1.dD()));
         var13.a(var1.aJ);
         var12 = new PacketPlayOutEntityTeleport(var13);
      }

      var11.setLastXSent(var2);
      var11.setLastYSent(var4);
      var11.setLastZSent(var6);
      var11.setFirstTimeSent(false);
      return (Packet)var12;
   }

   private EntityArmorStand getHandle(ArmorStand var1) {
      return ((CraftArmorStand)var1).getHandle();
   }
}
