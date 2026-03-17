package implementation.v1_21_R10P.fake;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.handler.PlayerWrapperHandler;
import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.service.PacketService;
import advancedplugins.pm2.cv.util.Constants;
import advancedplugins.pm2.cv.util.PacketUtil;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.THashSet;
import implementation.v1_21_R10P.util.PacketWritingUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Pos;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public abstract class FakeEntityHandle<T extends Entity, P extends Enum<P>> implements advancedplugins.pm2.cv.fake.FakeEntityHandle<P> {
   protected final PacketService packetService;
   protected final PlayerWrapperHandler playerWrapperHandler;
   protected final T handle;
   protected final Set<PlayerWrapper> viewers = new THashSet();
   @Nullable
   protected advancedplugins.pm2.cv.fake.FakeEntityHandle<?> riding;
   protected final Set<advancedplugins.pm2.cv.fake.FakeEntityHandle<?>> passengers = new THashSet();
   protected Vec3 lastLocationSent = null;
   protected long lastTeleportPacketTimestamp;
   protected boolean rotationDirty = false;

   public FakeEntityHandle(@NotNull World var1) {
      this.handle = this.createHandleInstance(var1);
      this.packetService = (PacketService)InfiniteVehicles.getService(PacketService.class);
      this.playerWrapperHandler = InfiniteVehicles.getPlayerWrapperHandler();
   }

   protected abstract T createHandleInstance(@NotNull World var1);

   public int getId() {
      return this.handle.getId();
   }

   public boolean isShownTo(@NotNull Player var1) {
      return this.viewers.contains(this.playerWrapperHandler.getWrapper(var1));
   }

   public void show(@NotNull Collection<? extends Player> var1) {
      if (var1.size() != 0) {
         List var2 = this.createShowPackets();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Player var4 = (Player)var3.next();
            this.viewers.add(this.playerWrapperHandler.getWrapper(var4));
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               Packet var6 = (Packet)var5.next();
               if (var6 != null) {
                  this.packetService.sendPacket((Player)var4, var6);
               }
            }
         }

         if (this.riding != null) {
            this.riding.sendPassengers();
         }

      }
   }

   @NotNull
   protected List<Packet<?>> createShowPackets() {
      ArrayList var1 = new ArrayList();
      Entity var2 = this.handle;
      var1.add(new ClientboundAddEntityPacket(var2.getId(), var2.getUUID(), var2.getX(), var2.getY(), var2.getZ(), var2.getXRot(), var2.getYRot(), var2.getType(), 0, var2.getDeltaMovement(), (double)var2.getYHeadRot()));
      var1.add(new ClientboundRotateHeadPacket(this.handle, PacketUtil.serializeRotationAngle(this.handle.getYHeadRot())));
      ClientboundSetEntityDataPacket var3 = null;
      List var4 = this.handle.getEntityData().getNonDefaultValues();
      if (var4 != null && var4.size() > 0) {
         var3 = new ClientboundSetEntityDataPacket(this.handle.getId(), var4);
      }

      if (var3 != null) {
         var1.add(var3);
      }

      ClientboundSetPassengersPacket var5 = this.passengers.size() > 0 ? this.createPassengersPacket() : null;
      if (var5 != null) {
         var1.add(var5);
      }

      return var1;
   }

   public void hide(@NotNull Collection<? extends Player> var1, boolean var2) {
      ClientboundRemoveEntitiesPacket var3 = new ClientboundRemoveEntitiesPacket(new int[]{this.handle.getId()});
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         this.viewers.remove(this.playerWrapperHandler.getWrapper(var5));
         if (!var2) {
            this.packetService.sendPacket((Player)var5, var3);
         }
      }

   }

   public void hide() {
      ClientboundRemoveEntitiesPacket var1 = new ClientboundRemoveEntitiesPacket(new int[]{this.handle.getId()});
      Iterator var2 = this.viewers.iterator();

      while(var2.hasNext()) {
         PlayerWrapper var3 = (PlayerWrapper)var2.next();
         this.packetService.sendPacket((PlayerWrapper)var3, var1);
      }

      this.viewers.clear();
   }

   public void sendMetadata(boolean var1) {
      this.broadcastMetadataPacket(var1);
   }

   public void applyLocationRotation(double var1, double var3, double var5, float var7, float var8) {
      this.handle.setPosRaw(var1, var3, var5);
      this.applyRotation(var7, var8);
   }

   public void applyLocation(double var1, double var3, double var5) {
      this.handle.setPosRaw(var1, var3, var5);
   }

   public void applyRotation(float var1, float var2) {
      this.handle.setYRot(var1);
      this.handle.setXRot(var2);
      this.rotationDirty = true;
   }

   public void applyHeadRotation(float var1) {
      this.handle.setYHeadRot(var1);
   }

   public void sendLocationRotation(boolean var1) {
      this.sendLocation(var1, true);
   }

   public void sendLocation(boolean var1) {
      this.sendLocation(var1, false);
   }

   protected void sendLocation(boolean var1, boolean var2) {
      this.broadcastPacketToViewers(this.prepareLocationPacket(var1, var2), true);
   }

   public void writeShowPackets(@NotNull PlayerWrapper var1, boolean var2) {
      if (var2) {
         this.viewers.add(var1);
      }

      ChannelPipeline var3 = var1.getPipeline();
      if (var3 != null) {
         Iterator var4 = this.createShowPackets().iterator();

         while(var4.hasNext()) {
            Packet var5 = (Packet)var4.next();
            PacketWritingUtil.compressAndWriteToPipeline(var5, var3);
         }
      }

   }

   public void writeHidePackets(@NotNull PlayerWrapper var1, boolean var2) {
      if (var2) {
         this.viewers.remove(var1);
      }

      ChannelPipeline var3 = var1.getPipeline();
      if (var3 != null) {
         PacketWritingUtil.compressAndWriteToPipeline(new ClientboundRemoveEntitiesPacket(new int[]{this.handle.getId()}), var3);
      }

   }

   @NotNull
   public Collection<ChannelPipeline> writeMetadata(boolean var1) {
      return this.compressAndWriteToPipelines(() -> {
         return this.prepareMetadataPacket(var1);
      });
   }

   @NotNull
   public Collection<ChannelPipeline> writeLocationRotation(boolean var1) {
      return this.compressAndWriteToPipelines(this.prepareLocationPacket(var1, true));
   }

   @NotNull
   public Collection<ChannelPipeline> writeLocation(boolean var1) {
      return this.compressAndWriteToPipelines(this.prepareLocationPacket(var1, false));
   }

   @NotNull
   public Collection<ChannelPipeline> writeRotation() {
      return this.compressAndWriteToPipelines((Packet)this.prepareRotationPacket());
   }

   @NotNull
   protected Collection<ChannelPipeline> compressAndWriteToPipelines(Packet<?> var1) {
      return this.compressAndWriteToPipelines(() -> {
         return var1;
      });
   }

   @NotNull
   protected Collection<ChannelPipeline> compressAndWriteToPipelines(Supplier<Packet<?>> var1) {
      Set var2 = this.preparePipelines();
      if (var2.size() > 0) {
         Packet var3 = (Packet)var1.get();
         if (var3 != null) {
            PacketWritingUtil.compressAndWriteToPipelines(var3, var2);
         } else {
            var2.clear();
         }
      }

      return var2;
   }

   protected Set<ChannelPipeline> preparePipelines() {
      THashSet var1 = new THashSet();
      Iterator var2 = this.viewers.iterator();

      while(var2.hasNext()) {
         PlayerWrapper var3 = (PlayerWrapper)var2.next();
         if (!var3.isOffline()) {
            ChannelPipeline var4 = var3.getPipeline();
            if (var4 != null) {
               var1.add(var4);
            }
         }
      }

      return var1;
   }

   protected Packet<?> prepareLocationPacket(boolean var1, boolean var2) {
      Vec3 var4 = this.handle.position();
      if (System.currentTimeMillis() - this.lastTeleportPacketTimestamp >= Constants.FORCE_TELEPORT_PERIOD) {
         var1 = true;
      }

      Object var3;
      if (!var1 && this.lastLocationSent != null && !(this.lastLocationSent.distanceTo(var4) > 8.0D)) {
         if (var2) {
            var3 = this.createRelativePosRotPacket(var4, this.lastLocationSent);
         } else {
            var3 = this.createRelativePosRotPacket(var4, this.lastLocationSent);
         }
      } else {
         var3 = new ClientboundEntityPositionSyncPacket(this.handle.getId(), PositionMoveRotation.of(this.handle), this.handle.onGround());
         this.lastTeleportPacketTimestamp = System.currentTimeMillis();
      }

      this.lastLocationSent = var4;
      return (Packet)var3;
   }

   protected Pos createRelativePosPacket(Vec3 var1, Vec3 var2) {
      short var3 = (short)((int)PacketUtil.serializeDeltaLocation(var1.x, var2.x));
      short var4 = (short)((int)PacketUtil.serializeDeltaLocation(var1.y, var2.y));
      short var5 = (short)((int)PacketUtil.serializeDeltaLocation(var1.z, var2.z));
      return new Pos(this.handle.getId(), var3, var4, var5, this.handle.onGround);
   }

   protected PosRot createRelativePosRotPacket(Vec3 var1, Vec3 var2) {
      short var3 = (short)((int)PacketUtil.serializeDeltaLocation(var1.x, var2.x));
      short var4 = (short)((int)PacketUtil.serializeDeltaLocation(var1.y, var2.y));
      short var5 = (short)((int)PacketUtil.serializeDeltaLocation(var1.z, var2.z));
      return new PosRot(this.handle.getId(), var3, var4, var5, PacketUtil.serializeRotationAngle(this.handle.getYRot()), PacketUtil.serializeRotationAngle(this.handle.getXRot()), this.handle.onGround);
   }

   public void sendRotation() {
      this.broadcastPacketToViewers(this.prepareRotationPacket());
   }

   protected ClientboundMoveEntityPacket prepareRotationPacket() {
      return new Rot(this.handle.getId(), PacketUtil.serializeRotationAngle(this.handle.getYRot()), PacketUtil.serializeRotationAngle(this.handle.getXRot()), this.handle.onGround);
   }

   public void sendHeadRotation() {
      this.broadcastPacketToViewers(new ClientboundRotateHeadPacket(this.handle, PacketUtil.serializeRotationAngle(this.handle.getYHeadRot())));
   }

   @Nullable
   public advancedplugins.pm2.cv.fake.FakeEntityHandle<?> getRiding() {
      return this.riding;
   }

   public void setRiding(@Nullable advancedplugins.pm2.cv.fake.FakeEntityHandle<?> var1) {
      this.riding = var1;
   }

   public void setPassengers(@Nullable Collection<advancedplugins.pm2.cv.fake.FakeEntityHandle<?>> var1) {
      Iterator var2 = this.passengers.iterator();

      advancedplugins.pm2.cv.fake.FakeEntityHandle var3;
      while(var2.hasNext()) {
         var3 = (advancedplugins.pm2.cv.fake.FakeEntityHandle)var2.next();
         if (Objects.equals(var3.getRiding(), this)) {
            var3.setRiding((advancedplugins.pm2.cv.fake.FakeEntityHandle)null);
         }
      }

      this.passengers.clear();
      if (var1 != null) {
         var2 = var1.iterator();

         while(var2.hasNext()) {
            var3 = (advancedplugins.pm2.cv.fake.FakeEntityHandle)var2.next();
            this.passengers.add(var3);
            var3.setRiding(this);
         }
      }

   }

   public void sendPassengersTo(@NotNull Player var1) {
      this.packetService.sendPacket((Player)var1, this.createPassengersPacket(), false);
   }

   public void sendPassengers() {
      this.broadcastPacketToViewers(this.createPassengersPacket());
   }

   @NotNull
   protected ClientboundSetPassengersPacket createPassengersPacket() {
      FriendlyByteBuf var1 = new FriendlyByteBuf(Unpooled.buffer());
      TIntArrayList var2 = new TIntArrayList();
      Iterator var3 = this.passengers.iterator();

      while(var3.hasNext()) {
         advancedplugins.pm2.cv.fake.FakeEntityHandle var4 = (advancedplugins.pm2.cv.fake.FakeEntityHandle)var3.next();
         var2.add(var4.getId());
      }

      var1.writeVarInt(this.getId());
      var1.writeVarIntArray(var2.toArray());

      try {
         Constructor var6 = ClientboundSetPassengersPacket.class.getDeclaredConstructor(FriendlyByteBuf.class);
         var6.setAccessible(true);
         return (ClientboundSetPassengersPacket)var6.newInstance(var1);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var5) {
         throw new RuntimeException(var5);
      }
   }

   protected void broadcastMetadataPacket(boolean var1) {
      ClientboundSetEntityDataPacket var2 = this.prepareMetadataPacket(var1);
      if (var2 != null) {
         this.broadcastPacketToViewers(var2);
      }

   }

   @Nullable
   protected ClientboundSetEntityDataPacket prepareMetadataPacket(boolean var1) {
      ClientboundSetEntityDataPacket var2 = null;
      SynchedEntityData var3 = this.handle.getEntityData();
      Object var4;
      if (var1) {
         var4 = this.handle.getEntityData().getNonDefaultValues();
      } else {
         var4 = var3.packDirty();
      }

      if (this.rotationDirty && var4 == null) {
         var4 = new ArrayList();
      }

      if (var4 != null && ((List)var4).isEmpty() && this.rotationDirty || var4 != null && !((List)var4).isEmpty()) {
         this.modifyRot((List)var4);
      }

      if (var4 != null && ((List)var4).size() > 0) {
         var2 = new ClientboundSetEntityDataPacket(this.handle.getId(), (List)var4);
      }

      return var2;
   }

   private void modifyRot(List<DataValue<?>> var1) {
      Entity var3 = this.handle;
      if (var3 instanceof Display) {
         Display var2 = (Display)var3;

         EntityDataAccessor var4;
         EntityDataAccessor var5;
         EntityDataAccessor var6;
         EntityDataAccessor var15;
         try {
            Field var7 = Display.class.getDeclaredField("DATA_LEFT_ROTATION_ID");
            var7.setAccessible(true);
            var15 = (EntityDataAccessor)var7.get((Object)null);
            var7 = Display.class.getDeclaredField("DATA_TRANSLATION_ID");
            var7.setAccessible(true);
            var4 = (EntityDataAccessor)var7.get((Object)null);
            var7 = Display.class.getDeclaredField("DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID");
            var7.setAccessible(true);
            var5 = (EntityDataAccessor)var7.get((Object)null);
            var7 = Display.class.getDeclaredField("DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID");
            var7.setAccessible(true);
            var6 = (EntityDataAccessor)var7.get((Object)null);
         } catch (Exception var14) {
            var14.printStackTrace();
            return;
         }

         float var16 = this.handle.getBukkitYaw() * 0.017453292F;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;

         for(int var11 = 0; var11 < var1.size(); ++var11) {
            DataValue var12 = (DataValue)var1.get(var11);
            if (var12.id() == var5.id()) {
               var10 = true;
            } else if (var12.id() == var4.id()) {
               Vector3f var13 = new Vector3f((Vector3f)var12.value());
               var13.rotateY(-var16);
               var1.set(var11, new DataValue(var12.id(), var12.serializer(), var13));
               var8 = true;
            } else if (var12.id() == var15.id()) {
               Quaternionf var19 = new Quaternionf((Quaternionf)var12.value());
               var19 = (new Quaternionf()).rotateY(-var16).mul(var19).normalize();
               var1.set(var11, new DataValue(var12.id(), var12.serializer(), var19));
               var9 = true;
            }
         }

         if (!var10) {
            var1.add(new DataValue(var5.id(), var5.serializer(), 3));
            var1.add(new DataValue(var6.id(), var6.serializer(), 0));
         }

         if (!var8) {
            Vector3f var17 = new Vector3f((Vector3fc)var2.getEntityData().get(var4));
            var17.rotateY(-var16);
            var1.add(new DataValue(var4.id(), var4.serializer(), var17));
         }

         if (!var9) {
            Quaternionf var18 = new Quaternionf((Quaternionfc)var2.getEntityData().get(var15));
            var18 = (new Quaternionf()).rotateY(-var16).mul(var18);
            var1.add(new DataValue(var15.id(), var15.serializer(), var18));
         }

      }
   }

   protected void broadcastPacketToViewers(Object var1, boolean var2) {
      Iterator var3 = this.viewers.iterator();

      while(var3.hasNext()) {
         PlayerWrapper var4 = (PlayerWrapper)var3.next();
         this.packetService.sendPacket(var4, var1, var2);
      }

   }

   protected void broadcastPacketToViewers(Object var1) {
      this.broadcastPacketToViewers(var1, false);
   }
}
