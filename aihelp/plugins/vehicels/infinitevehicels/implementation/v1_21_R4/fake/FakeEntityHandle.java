package implementation.v1_21_R4.fake;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.handler.PlayerWrapperHandler;
import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.service.PacketService;
import advancedplugins.pm2.cv.util.Constants;
import advancedplugins.pm2.cv.util.PacketUtil;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.THashSet;
import implementation.v1_21_R4.util.PacketWritingUtil;
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
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3D;
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
   protected Vec3D lastLocationSent = null;
   protected long lastTeleportPacketTimestamp;
   protected boolean rotationDirty = false;

   public FakeEntityHandle(@NotNull World var1) {
      this.handle = this.createHandleInstance(var1);
      this.packetService = (PacketService)InfiniteVehicles.getService(PacketService.class);
      this.playerWrapperHandler = InfiniteVehicles.getPlayerWrapperHandler();
   }

   protected abstract T createHandleInstance(@NotNull World var1);

   public int getId() {
      return this.handle.ao();
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
      var1.add(new PacketPlayOutSpawnEntity(var2.ao(), var2.cG(), var2.dA(), var2.dC(), var2.dG(), var2.dN(), var2.dL(), var2.an(), 0, var2.dy(), (double)var2.cA()));
      var1.add(new PacketPlayOutEntityHeadRotation(this.handle, PacketUtil.serializeRotationAngle(this.handle.cA())));
      PacketPlayOutEntityMetadata var3 = null;
      List var4 = this.handle.ar().c();
      if (var4 != null && var4.size() > 0) {
         var3 = new PacketPlayOutEntityMetadata(this.handle.ao(), var4);
      }

      if (var3 != null) {
         var1.add(var3);
      }

      PacketPlayOutMount var5 = this.passengers.size() > 0 ? this.createPassengersPacket() : null;
      if (var5 != null) {
         var1.add(var5);
      }

      return var1;
   }

   public void hide(@NotNull Collection<? extends Player> var1, boolean var2) {
      PacketPlayOutEntityDestroy var3 = new PacketPlayOutEntityDestroy(new int[]{this.handle.ao()});
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
      PacketPlayOutEntityDestroy var1 = new PacketPlayOutEntityDestroy(new int[]{this.handle.ao()});
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
      this.handle.o(var1, var3, var5);
      this.applyRotation(var7, var8);
   }

   public void applyLocation(double var1, double var3, double var5) {
      this.handle.o(var1, var3, var5);
   }

   public void applyRotation(float var1, float var2) {
      this.handle.w(var1);
      this.handle.x(var2);
      this.rotationDirty = true;
   }

   public void applyHeadRotation(float var1) {
      this.handle.r(var1);
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
         PacketWritingUtil.compressAndWriteToPipeline(new PacketPlayOutEntityDestroy(new int[]{this.handle.ao()}), var3);
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
      Vec3D var4 = this.handle.dt();
      if (System.currentTimeMillis() - this.lastTeleportPacketTimestamp >= Constants.FORCE_TELEPORT_PERIOD) {
         var1 = true;
      }

      Object var3;
      if (!var1 && this.lastLocationSent != null && !(this.lastLocationSent.f(var4) > 8.0D)) {
         if (var2) {
            var3 = this.createRelativePosRotPacket(var4, this.lastLocationSent);
         } else {
            var3 = this.createRelativePosRotPacket(var4, this.lastLocationSent);
         }
      } else {
         var3 = new ClientboundEntityPositionSyncPacket(this.handle.ao(), PositionMoveRotation.a(this.handle), this.handle.aH);
         this.lastTeleportPacketTimestamp = System.currentTimeMillis();
      }

      this.lastLocationSent = var4;
      return (Packet)var3;
   }

   protected PacketPlayOutRelEntityMove createRelativePosPacket(Vec3D var1, Vec3D var2) {
      short var3 = (short)((int)PacketUtil.serializeDeltaLocation(var1.d, var2.d));
      short var4 = (short)((int)PacketUtil.serializeDeltaLocation(var1.e, var2.e));
      short var5 = (short)((int)PacketUtil.serializeDeltaLocation(var1.f, var2.f));
      return new PacketPlayOutRelEntityMove(this.handle.ao(), var3, var4, var5, this.handle.aH);
   }

   protected PacketPlayOutRelEntityMoveLook createRelativePosRotPacket(Vec3D var1, Vec3D var2) {
      short var3 = (short)((int)PacketUtil.serializeDeltaLocation(var1.d, var2.d));
      short var4 = (short)((int)PacketUtil.serializeDeltaLocation(var1.e, var2.e));
      short var5 = (short)((int)PacketUtil.serializeDeltaLocation(var1.f, var2.f));
      return new PacketPlayOutRelEntityMoveLook(this.handle.ao(), var3, var4, var5, PacketUtil.serializeRotationAngle(this.handle.dL()), PacketUtil.serializeRotationAngle(this.handle.dN()), this.handle.aH);
   }

   public void sendRotation() {
      this.broadcastPacketToViewers(this.prepareRotationPacket());
   }

   protected PacketPlayOutEntity prepareRotationPacket() {
      return new PacketPlayOutEntityLook(this.handle.ao(), PacketUtil.serializeRotationAngle(this.handle.dL()), PacketUtil.serializeRotationAngle(this.handle.dN()), this.handle.aH);
   }

   public void sendHeadRotation() {
      this.broadcastPacketToViewers(new PacketPlayOutEntityHeadRotation(this.handle, PacketUtil.serializeRotationAngle(this.handle.cA())));
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
   protected PacketPlayOutMount createPassengersPacket() {
      PacketDataSerializer var1 = new PacketDataSerializer(Unpooled.buffer());
      TIntArrayList var2 = new TIntArrayList();
      Iterator var3 = this.passengers.iterator();

      while(var3.hasNext()) {
         advancedplugins.pm2.cv.fake.FakeEntityHandle var4 = (advancedplugins.pm2.cv.fake.FakeEntityHandle)var3.next();
         var2.add(var4.getId());
      }

      var1.c(this.getId());
      var1.a(var2.toArray());

      try {
         Constructor var6 = PacketPlayOutMount.class.getDeclaredConstructor(PacketDataSerializer.class);
         var6.setAccessible(true);
         return (PacketPlayOutMount)var6.newInstance(var1);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var5) {
         throw new RuntimeException(var5);
      }
   }

   protected void broadcastMetadataPacket(boolean var1) {
      PacketPlayOutEntityMetadata var2 = this.prepareMetadataPacket(var1);
      if (var2 != null) {
         this.broadcastPacketToViewers(var2);
      }

   }

   @Nullable
   protected PacketPlayOutEntityMetadata prepareMetadataPacket(boolean var1) {
      PacketPlayOutEntityMetadata var2 = null;
      DataWatcher var3 = this.handle.ar();
      Object var4;
      if (var1) {
         var4 = this.handle.ar().c();
      } else {
         var4 = var3.b();
      }

      if (this.rotationDirty && var4 == null) {
         var4 = new ArrayList();
      }

      if (var4 != null && ((List)var4).isEmpty() && this.rotationDirty || var4 != null && !((List)var4).isEmpty()) {
         this.modifyRot((List)var4);
      }

      if (var4 != null && ((List)var4).size() > 0) {
         var2 = new PacketPlayOutEntityMetadata(this.handle.ao(), (List)var4);
      }

      return var2;
   }

   private void modifyRot(List<c<?>> var1) {
      Entity var3 = this.handle;
      if (var3 instanceof Display) {
         Display var2 = (Display)var3;

         DataWatcherObject var4;
         DataWatcherObject var5;
         DataWatcherObject var6;
         DataWatcherObject var15;
         try {
            Field var7 = Display.class.getDeclaredField("DATA_LEFT_ROTATION_ID");
            var7.setAccessible(true);
            var15 = (DataWatcherObject)var7.get((Object)null);
            var7 = Display.class.getDeclaredField("DATA_TRANSLATION_ID");
            var7.setAccessible(true);
            var4 = (DataWatcherObject)var7.get((Object)null);
            var7 = Display.class.getDeclaredField("DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID");
            var7.setAccessible(true);
            var5 = (DataWatcherObject)var7.get((Object)null);
            var7 = Display.class.getDeclaredField("DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID");
            var7.setAccessible(true);
            var6 = (DataWatcherObject)var7.get((Object)null);
         } catch (Exception var14) {
            var14.printStackTrace();
            return;
         }

         float var16 = this.handle.getBukkitYaw() * 0.017453292F;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;

         for(int var11 = 0; var11 < var1.size(); ++var11) {
            c var12 = (c)var1.get(var11);
            if (var12.a() == var5.a()) {
               var10 = true;
            } else if (var12.a() == var4.a()) {
               Vector3f var13 = new Vector3f((Vector3f)var12.c());
               var13.rotateY(-var16);
               var1.set(var11, new c(var12.a(), var12.b(), var13));
               var8 = true;
            } else if (var12.a() == var15.a()) {
               Quaternionf var19 = new Quaternionf((Quaternionf)var12.c());
               var19 = (new Quaternionf()).rotateY(-var16).mul(var19);
               var1.set(var11, new c(var12.a(), var12.b(), var19));
               var9 = true;
            }
         }

         if (!var10) {
            var1.add(new c(var5.a(), var5.b(), 3));
            var1.add(new c(var6.a(), var6.b(), 0));
         }

         if (!var8) {
            Vector3f var17 = new Vector3f((Vector3fc)var2.ar().a(var4));
            var17.rotateY(-var16);
            var1.add(new c(var4.a(), var4.b(), var17));
         }

         if (!var9) {
            Quaternionf var18 = new Quaternionf((Quaternionfc)var2.ar().a(var15));
            var18 = (new Quaternionf()).rotateY(-var16).mul(var18);
            var1.add(new c(var15.a(), var15.b(), var18));
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
