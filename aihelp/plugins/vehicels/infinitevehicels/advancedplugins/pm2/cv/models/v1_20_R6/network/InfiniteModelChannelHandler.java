package advancedplugins.pm2.cv.models.v1_20_R6.network;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelUpdaters;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.DynamicHitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.network.ClientDesyncMonitor;
import advancedplugins.pm2.cv.models.api.nms.network.PipelineWrapper;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.v1_20_R6.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_20_R6.network.patch.ServerboundInteractPacketWrapper;
import advancedplugins.pm2.cv.models.v1_20_R6.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_20_R6.network.utils.PacketInterceptor;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayInFlying;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEffect;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutRemoveEntityEffect;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInPosition;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfiniteModelChannelHandler extends ChannelDuplexHandler {
   private final Player player;
   private final EntityPlayer serverPlayer;
   private final ModelUpdaters updaters;
   private final EntityHandler entityHandler;
   private final ClientDesyncMonitor desyncMonitor;
   private final PacketInterceptor<PacketListenerPlayOut> writeInterceptors;
   private final PacketInterceptor<PacketListenerPlayIn> readInterceptors;

   public InfiniteModelChannelHandler(Player player, PipelineWrapper pipeline) {
      this.player = var1;
      this.serverPlayer = ((CraftPlayer)var1).getHandle();
      this.updaters = ModelAPI.getAPI().getModelUpdaters();
      this.entityHandler = ModelAPI.getEntityHandler();
      this.desyncMonitor = var2.getDesyncMonitor();
      this.writeInterceptors = new PacketInterceptor();
      this.writeInterceptors.register(PacketPlayOutSpawnEntity.class, this::handleAddEntity).register(PacketPlayOutEntityDestroy.class, this::handleRemoveEntities).register(PacketPlayOutRelEntityMove.class, this::handleEntityId).register(PacketPlayOutEntityLook.class, this::handleEntityId).register(PacketPlayOutRelEntityMoveLook.class, this::handleEntityId).register(PacketPlayOutEntityHeadRotation.class, this::handleEntityId).register(PacketPlayOutEntityStatus.class, this::handleEntityId).register(PacketPlayOutEntityVelocity.class, this::handleEntityMotion).register(PacketPlayOutEntityTeleport.class, this::handleTeleportEntity).register(PacketPlayOutAnimation.class, this::handleAnimate).register(PacketPlayOutEntityMetadata.class, this::handleEntityData).register(PacketPlayOutEntityEquipment.class, this::handleSetEquipment).register(PacketPlayOutRemoveEntityEffect.class, this::handleRemoveMobEffect).register(PacketPlayOutEntityEffect.class, this::handleUpdateMobEffect).register(ClientboundKeepAlivePacket.class, this::handleKeepAlive).registerPost(PacketPlayOutSpawnEntity.class, this::handleAddEntityPost);
      this.readInterceptors = new PacketInterceptor();
      this.readInterceptors.register(PacketPlayInUseEntity.class, this::handleInteract).register(PacketPlayInSteerVehicle.class, this::handlePlayerInput).register(ServerboundPongPacket.class, this::handlePong).register(PacketPlayInPosition.class, this::handlePlayerMove).register(PacketPlayInPositionLook.class, this::handlePlayerMove);
   }

   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
      if (!(var2 instanceof Packet)) {
         super.write(var1, var2, var3);
      } else {
         try {
            Packet var5 = (Packet)var2;
            final ArrayList var4;
            ClientboundBundlePacket var11;
            if (var5 instanceof ClientboundBundlePacket) {
               ClientboundBundlePacket var6 = (ClientboundBundlePacket)var5;
               var4 = new ArrayList();
               Iterator var7 = var6.b().iterator();

               while(var7.hasNext()) {
                  Packet var8 = (Packet)var7.next();
                  Packet var9 = this.writeInterceptors.accept(var8);
                  if (var9 != null) {
                     var4.add(var9);
                     var4.addAll(this.writeInterceptors.acceptPost(var9));
                  }
               }

               if (!var4.isEmpty()) {
                  var11 = new ClientboundBundlePacket(new Iterable<Packet<? super PacketListenerPlayOut>>(this) {
                     @NotNull
                     public Iterator<Packet<? super PacketListenerPlayOut>> iterator() {
                        return var4.iterator();
                     }
                  });
                  super.write(var1, var11, var3);
               }
            } else {
               var5 = this.writeInterceptors.accept(var5);
               if (var5 == null) {
                  return;
               }

               var4 = new ArrayList();
               var4.add(var5);
               var4.addAll(this.writeInterceptors.acceptPost(var5));
               if (var4.size() == 1) {
                  super.write(var1, var5, var3);
               } else {
                  var11 = new ClientboundBundlePacket(var4);
                  super.write(var1, var11, var3);
               }
            }
         } catch (Throwable var10) {
            var10.printStackTrace();
         }
      }

   }

   public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
      if (!(var2 instanceof Packet)) {
         super.channelRead(var1, var2);
      } else {
         Packet var3 = this.readInterceptors.accept((Packet)var2);
         if (var3 != null) {
            super.channelRead(var1, var3);
            this.readInterceptors.acceptPost(var3);
         }
      }

   }

   private PacketPlayOutSpawnEntity handleAddEntity(PacketPlayOutSpawnEntity packet) {
      return this.shouldShow(var1.b()) ? var1 : null;
   }

   private List<Packet<? super PacketListenerPlayOut>> handleAddEntityPost(PacketPlayOutSpawnEntity packet) {
      return this.handleMount(var1.e());
   }

   private List<Packet<? super PacketListenerPlayOut>> handleMount(UUID uuid) {
      Pair var2 = ModelAPI.getMountPairManager().get(var1);
      if (var2 == null) {
         return null;
      } else {
         IVisualModel var3 = (IVisualModel)var2.left();
         ArrayList var4 = new ArrayList();
         var3.getBehaviorRenderer(JointBehaviorTypes.MOUNT).ifPresent((var2x) -> {
            if (var2x instanceof MountRenderer) {
               MountRenderer var3 = (MountRenderer)var2x;
               MountController var4x = (MountController)var2.right();
               Mount var5 = var4x.getMount();
               if (var5 instanceof JointAction) {
                  JointAction var6 = (JointAction)var5;
                  MountRenderer.Mount var7 = (MountRenderer.Mount)var3.getRendered().get(var6.getJoint().getJointId());
                  CollectionDataTracker var8 = var7.getPassengers();
                  var4.add(new PacketPlayOutMount(EntityContainer.of(var7.getMountId(), (Collection)var8)));
               }
            }

         });
         return var4;
      }
   }

   private PacketPlayOutEntityDestroy handleRemoveEntities(PacketPlayOutEntityDestroy packet) {
      int[] var2 = var1.b().intStream().filter(this::shouldShow).toArray();
      return var2.length == var1.b().size() ? var1 : new PacketPlayOutEntityDestroy(IntArrayList.wrap(var2));
   }

   private <T extends Packet<? super PacketListenerPlayOut>> T handleEntityId(T packet) {
      PacketDataSerializer var2 = NetworkUtils.readClientbound(var1);
      int var3 = var2.l();
      return this.shouldShow(var3) ? var1 : null;
   }

   private PacketPlayOutEntityVelocity handleEntityMotion(PacketPlayOutEntityVelocity packet) {
      return this.shouldShow(var1.b()) ? var1 : null;
   }

   private PacketPlayOutEntityTeleport handleTeleportEntity(PacketPlayOutEntityTeleport packet) {
      return this.shouldShow(var1.b()) ? var1 : null;
   }

   private PacketPlayOutAnimation handleAnimate(PacketPlayOutAnimation packet) {
      return this.shouldShow(var1.b()) ? var1 : null;
   }

   private PacketPlayOutEntityMetadata handleEntityData(PacketPlayOutEntityMetadata packet) {
      if (!this.shouldShow(var1.b())) {
         return null;
      } else if (var1.b() != this.player.getEntityId()) {
         return var1;
      } else {
         if (this.entityHandler.isForcedInvisible(this.player)) {
            ArrayList var2 = new ArrayList();
            PacketDataSerializer var3 = NetworkUtils.createByteBuf();
            var3.c(var1.b());
            Iterator var4 = var1.e().iterator();

            while(var4.hasNext()) {
               c var5 = (c)var4.next();
               if (var5.a() == 0) {
                  byte var6 = (Byte)var5.c();
                  var6 = MathUtils.setBit(var6, 5, true);
                  var2.add(new c(0, DataWatcherRegistry.a, var6));
               } else {
                  var2.add(var5);
               }
            }

            var3.k(255);
            var1 = new PacketPlayOutEntityMetadata(var1.b(), var2);
         }

         return var1;
      }
   }

   private PacketPlayOutEntityEquipment handleSetEquipment(PacketPlayOutEntityEquipment packet) {
      return this.shouldShow(var1.b()) ? var1 : null;
   }

   private PacketPlayOutRemoveEntityEffect handleRemoveMobEffect(PacketPlayOutRemoveEntityEffect packet) {
      int var2;
      try {
         Field var3 = var1.getClass().getField("entityId");
         var3.setAccessible(true);
         var2 = var3.getInt(var1);
      } catch (NoSuchFieldException | IllegalAccessException var4) {
         throw new RuntimeException(var4);
      }

      return this.shouldShow(var2) ? var1 : null;
   }

   private PacketPlayOutEntityEffect handleUpdateMobEffect(PacketPlayOutEntityEffect packet) {
      return this.shouldShow(var1.b()) ? var1 : null;
   }

   private boolean shouldShow(int id) {
      if (ModelAPI.isRenderCanceled(var1)) {
         return false;
      } else if (this.player.getEntityId() == var1) {
         return true;
      } else {
         IModelContainer var2 = this.updaters.getModeledEntity(var1);
         return var2 == null || var2.isBaseEntityVisible();
      }
   }

   private ClientboundKeepAlivePacket handleKeepAlive(ClientboundKeepAlivePacket packet) {
      if (this.desyncMonitor.clientTickShifted() || this.desyncMonitor.shouldRetest()) {
         this.desyncMonitor.startTest();
      }

      return var1;
   }

   private Packet<? super PacketListenerPlayIn> handleInteract(PacketPlayInUseEntity packet) {
      PacketDataSerializer var2 = NetworkUtils.readServerbound(var1);
      int var3 = var2.l();
      int var4 = var2.l();
      if (var3 == DynamicHitbox.getHitboxId()) {
         DynamicHitbox var5 = ModelAPI.getInteractionTracker().getDynamicHitbox(this.player.getUniqueId());
         if (var5 != null) {
            return new ServerboundInteractPacketWrapper(var3, var5.getTarget(), var4, var1);
         }
      }

      IVisualModel var8 = ModelAPI.getInteractionTracker().getModelRelay(var3);
      if (var8 != null) {
         IModelContainer var9 = var8.getModeledEntity();
         if (var9 == null) {
            return var1;
         } else {
            int var7 = var9.getBase().getEntityId();
            return new ServerboundInteractPacketWrapper(var3, var7, var4, var1);
         }
      } else {
         Integer var6 = ModelAPI.getInteractionTracker().getEntityRelay(var3);
         return (Packet)(var6 != null ? new ServerboundInteractPacketWrapper(var3, var6, var4, var1) : var1);
      }
   }

   private PacketPlayInSteerVehicle handlePlayerInput(PacketPlayInSteerVehicle inputPacket) {
      MountController var2 = ModelAPI.getMountPairManager().getController(this.player.getUniqueId());
      if (var2 != null) {
         MountController.MountInput var3 = var2.getInput();
         if (var3 == null) {
            var2.setInput(new MountController.MountInput(var1.b(), var1.e(), var1.f(), var1.g()));
         } else {
            var3.setSide(var1.b());
            var3.setFront(var1.e());
            var3.setJump(var1.f());
            var3.setSneak(var1.g());
         }
      }

      return var1;
   }

   private ServerboundPongPacket handlePong(ServerboundPongPacket packet) {
      this.desyncMonitor.recordPongTime(System.currentTimeMillis());
      return null;
   }

   private PacketPlayInFlying handlePlayerMove(PacketPlayInFlying packet) {
      this.desyncMonitor.recordClientSyncTime(System.currentTimeMillis());
      return var1;
   }
}
