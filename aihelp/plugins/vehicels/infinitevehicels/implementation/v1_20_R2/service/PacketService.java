package implementation.v1_20_R2.service;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.EntityActionWrapper;
import advancedplugins.pm2.cv.packet.incoming.InteractPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.PlayerActionPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.PlayerInputPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.SetPlayerPositionRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.SetPlayerRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.SwingArmPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.UseItemPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.TeleportEntityPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityPositionPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityPositionRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityRotationPacketWrapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.util.Objects;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.EnumProtocol;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInArmAnimation;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import net.minecraft.network.protocol.game.PacketPlayInBlockPlace;
import net.minecraft.network.protocol.game.PacketPlayInEntityAction;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.network.protocol.game.PacketPlayInEntityAction.EnumPlayerAction;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInLook;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.EnumHand;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketService implements advancedplugins.pm2.cv.service.PacketService {
   @VersionSensible
   private NetworkManager getNetworkManager(@NotNull PlayerConnection var1) {
      try {
         Field var2 = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
         var2.setAccessible(true);
         return (NetworkManager)var2.get(var1);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public ChannelPipeline getChannelPipeline(@NotNull Player var1) {
      return this.getNetworkManager(((CraftPlayer)var1).getHandle().c).n.pipeline();
   }

   public void sendPacket(@NotNull Player var1, @NotNull Object var2, boolean var3) {
      if (var3) {
         Packet var4 = (Packet)var2;
         ChannelPipeline var5 = this.getChannelPipeline(var1);

         try {
            int var6 = EnumProtocol.b.b(EnumProtocolDirection.b).a(var4);
            PacketDataSerializer var7 = new PacketDataSerializer(Unpooled.buffer());
            var7.k(var6);
            var4.a(var7);
            var5.write(var7);
            var5.flush();
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      } else {
         this.getNetworkManager(((CraftPlayer)var1).getHandle().c).a((Packet)var2);
      }

   }

   @Nullable
   public PacketWrapper read(@NotNull Object var1) {
      if (var1 instanceof PacketPlayInSteerVehicle) {
         PacketPlayInSteerVehicle var22 = (PacketPlayInSteerVehicle)var1;
         return new PlayerInputPacketWrapper(var22.a(), var22.d(), var22.e(), var22.f());
      } else if (var1 instanceof PacketPlayInEntityAction) {
         PacketPlayInEntityAction var21 = (PacketPlayInEntityAction)var1;
         EnumPlayerAction var19 = var21.d();
         EntityActionWrapper.Action var28 = null;
         switch(var19) {
         case a:
            var28 = EntityActionWrapper.Action.START_SNEAKING;
            break;
         case b:
            var28 = EntityActionWrapper.Action.STOP_SNEAKING;
            break;
         case d:
            var28 = EntityActionWrapper.Action.START_SPRINTING;
            break;
         case e:
            var28 = EntityActionWrapper.Action.STOP_SPRINTING;
            break;
         case c:
            var28 = EntityActionWrapper.Action.LEAVE_BED;
            break;
         case f:
            var28 = EntityActionWrapper.Action.START_JUMP_HORSE;
            break;
         case g:
            var28 = EntityActionWrapper.Action.STOP_JUMP_HORSE;
            break;
         case h:
            var28 = EntityActionWrapper.Action.OPEN_VEHICLE_INVENTORY;
            break;
         case i:
            var28 = EntityActionWrapper.Action.START_FLYING_ELYTRA;
         }

         return new EntityActionWrapper(var21.a(), var28);
      } else {
         int var4;
         if (var1 instanceof PacketPlayInUseEntity) {
            PacketDataSerializer var2 = new PacketDataSerializer(Unpooled.buffer());
            ((Packet)var1).a(var2);
            int var3 = var2.m();
            var4 = var2.m();
            if (var4 == 1) {
               boolean var25 = var2.readBoolean();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.ATTACK, (Double)null, (Double)null, (Double)null, (InteractPacketWrapper.Hand)null, var25);
            }

            if (var4 == 2) {
               float var23 = var2.readFloat();
               float var27 = var2.readFloat();
               float var7 = var2.readFloat();
               int var8 = var2.m();
               boolean var9 = var2.readBoolean();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT_AT, (double)var23, (double)var27, (double)var7, var8 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var9);
            }

            if (var4 == 0) {
               int var5 = var2.m();
               boolean var6 = var2.readBoolean();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT, (Double)null, (Double)null, (Double)null, var5 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var6);
            }
         } else {
            if (var1 instanceof PacketPlayInArmAnimation) {
               return new SwingArmPacketWrapper(((PacketPlayInArmAnimation)var1).a() == EnumHand.a ? SwingArmPacketWrapper.Hand.MAIN_HAND : SwingArmPacketWrapper.Hand.OFF_HAND);
            }

            if (var1 instanceof PacketPlayInBlockPlace) {
               PacketPlayInBlockPlace var20 = (PacketPlayInBlockPlace)var1;
               return new UseItemPacketWrapper(var20.a() == EnumHand.a ? UseItemPacketWrapper.Hand.MAIN_HAND : UseItemPacketWrapper.Hand.OFF_HAND, var20.d());
            }

            if (var1 instanceof PacketPlayInLook) {
               PacketPlayInLook var17 = (PacketPlayInLook)var1;
               return new SetPlayerRotationPacketWrapper(var17.d, var17.e, var17.a());
            }

            if (var1 instanceof PacketPlayInPositionLook) {
               PacketPlayInPositionLook var16 = (PacketPlayInPositionLook)var1;
               return new SetPlayerPositionRotationPacketWrapper(var16.a, var16.b, var16.c, var16.d, var16.e, var16.a());
            }

            if (var1 instanceof PacketPlayInBlockDig) {
               PacketPlayInBlockDig var15 = (PacketPlayInBlockDig)var1;
               BlockPosition var18 = var15.a();
               PlayerActionPacketWrapper.Direction var24;
               switch(var15.d()) {
               case a:
                  var24 = PlayerActionPacketWrapper.Direction.DOWN;
                  break;
               case b:
                  var24 = PlayerActionPacketWrapper.Direction.UP;
                  break;
               case c:
                  var24 = PlayerActionPacketWrapper.Direction.NORTH;
                  break;
               case d:
                  var24 = PlayerActionPacketWrapper.Direction.SOUTH;
                  break;
               case e:
                  var24 = PlayerActionPacketWrapper.Direction.WEST;
                  break;
               case f:
                  var24 = PlayerActionPacketWrapper.Direction.EAST;
                  break;
               default:
                  throw new IllegalStateException();
               }

               PlayerActionPacketWrapper.Action var26;
               switch(var15.e()) {
               case a:
                  var26 = PlayerActionPacketWrapper.Action.START_DESTROY_BLOCK;
                  break;
               case b:
                  var26 = PlayerActionPacketWrapper.Action.ABORT_DESTROY_BLOCK;
                  break;
               case c:
                  var26 = PlayerActionPacketWrapper.Action.STOP_DESTROY_BLOCK;
                  break;
               case d:
                  var26 = PlayerActionPacketWrapper.Action.DROP_ALL_ITEMS;
                  break;
               case e:
                  var26 = PlayerActionPacketWrapper.Action.DROP_ITEM;
                  break;
               case f:
                  var26 = PlayerActionPacketWrapper.Action.RELEASE_USE_ITEM;
                  break;
               case g:
                  var26 = PlayerActionPacketWrapper.Action.SWAP_ITEM_WITH_OFFHAND;
                  break;
               default:
                  throw new IllegalStateException();
               }

               return new PlayerActionPacketWrapper(var18.u(), var18.v(), var18.w(), var24, var26, var15.f());
            }
         }

         if (var1 instanceof PacketPlayOutEntityTeleport) {
            PacketPlayOutEntityTeleport var13 = (PacketPlayOutEntityTeleport)var1;
            return new TeleportEntityPacketWrapper(var13.a(), var13.d(), var13.e(), var13.f(), var13.g(), var13.h(), var13.i());
         } else {
            PacketDataSerializer var14;
            if (var1 instanceof PacketPlayOutRelEntityMove) {
               PacketPlayOutRelEntityMove var12 = (PacketPlayOutRelEntityMove)var1;
               var14 = new PacketDataSerializer(Unpooled.buffer());
               var12.a(var14);
               var4 = var14.m();
               return new UpdateEntityPositionPacketWrapper(var4, var12.a(), var12.d(), var12.e(), var12.j());
            } else if (var1 instanceof PacketPlayOutEntityLook) {
               PacketPlayOutEntityLook var11 = (PacketPlayOutEntityLook)var1;
               var14 = new PacketDataSerializer(Unpooled.buffer());
               var11.a(var14);
               var4 = var14.m();
               return new UpdateEntityRotationPacketWrapper(var4, var11.f(), var11.g(), var11.j());
            } else if (var1 instanceof PacketPlayOutRelEntityMoveLook) {
               PacketPlayOutRelEntityMoveLook var10 = (PacketPlayOutRelEntityMoveLook)var1;
               var14 = new PacketDataSerializer(Unpooled.buffer());
               var10.a(var14);
               var4 = var14.m();
               return new UpdateEntityPositionRotationPacketWrapper(var4, var10.a(), var10.d(), var10.e(), var10.f(), var10.g(), var10.j());
            } else {
               return null;
            }
         }
      }
   }

   @NotNull
   public Object createInstance(@NotNull PacketWrapper var1) {
      if (var1 instanceof PlayerInputPacketWrapper) {
         PlayerInputPacketWrapper var15 = (PlayerInputPacketWrapper)var1;
         return new PacketPlayInSteerVehicle(var15.sideways, var15.forward, var15.jump, var15.unmount);
      } else {
         PacketDataSerializer var3;
         if (var1 instanceof InteractPacketWrapper) {
            InteractPacketWrapper var14 = (InteractPacketWrapper)var1;
            var3 = new PacketDataSerializer(Unpooled.buffer());
            var3.c(var14.entityId);
            switch(var14.action) {
            case INTERACT:
               var3.c(0);
               var3.c(var14.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
               break;
            case ATTACK:
               var3.c(1);
               break;
            case INTERACT_AT:
               var3.c(2);
               var3.a(((Double)Objects.requireNonNull(var14.targetX)).floatValue());
               var3.a(((Double)Objects.requireNonNull(var14.targetY)).floatValue());
               var3.a(((Double)Objects.requireNonNull(var14.targetZ)).floatValue());
               var3.c(var14.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
            }

            var3.a(var14.sneaking);
            return new PacketPlayInUseEntity(var3);
         } else if (var1 instanceof SwingArmPacketWrapper) {
            return new PacketPlayInArmAnimation(((SwingArmPacketWrapper)var1).hand == SwingArmPacketWrapper.Hand.MAIN_HAND ? EnumHand.a : EnumHand.b);
         } else if (var1 instanceof UseItemPacketWrapper) {
            UseItemPacketWrapper var13 = (UseItemPacketWrapper)var1;
            return new PacketPlayInBlockPlace(var13.hand == UseItemPacketWrapper.Hand.MAIN_HAND ? EnumHand.a : EnumHand.b, var13.sequence);
         } else if (var1 instanceof SetPlayerRotationPacketWrapper) {
            SetPlayerRotationPacketWrapper var12 = (SetPlayerRotationPacketWrapper)var1;
            return new PacketPlayInLook(var12.yRot, var12.xRot, var12.onGround);
         } else if (var1 instanceof SetPlayerPositionRotationPacketWrapper) {
            SetPlayerPositionRotationPacketWrapper var11 = (SetPlayerPositionRotationPacketWrapper)var1;
            return new PacketPlayInPositionLook(var11.x, var11.y, var11.z, var11.yRot, var11.xRot, var11.onGround);
         } else if (var1 instanceof PlayerActionPacketWrapper) {
            PlayerActionPacketWrapper var9 = (PlayerActionPacketWrapper)var1;
            BlockPosition var10 = new BlockPosition(var9.positionX, var9.positionY, var9.positionZ);
            EnumDirection var4;
            switch(var9.direction) {
            case DOWN:
               var4 = EnumDirection.a;
               break;
            case UP:
               var4 = EnumDirection.b;
               break;
            case NORTH:
               var4 = EnumDirection.c;
               break;
            case SOUTH:
               var4 = EnumDirection.d;
               break;
            case WEST:
               var4 = EnumDirection.e;
               break;
            case EAST:
               var4 = EnumDirection.f;
               break;
            default:
               throw new IllegalStateException();
            }

            EnumPlayerDigType var5;
            switch(var9.action) {
            case START_DESTROY_BLOCK:
               var5 = EnumPlayerDigType.a;
               break;
            case ABORT_DESTROY_BLOCK:
               var5 = EnumPlayerDigType.b;
               break;
            case STOP_DESTROY_BLOCK:
               var5 = EnumPlayerDigType.c;
               break;
            case DROP_ALL_ITEMS:
               var5 = EnumPlayerDigType.d;
               break;
            case DROP_ITEM:
               var5 = EnumPlayerDigType.e;
               break;
            case RELEASE_USE_ITEM:
               var5 = EnumPlayerDigType.f;
               break;
            case SWAP_ITEM_WITH_OFFHAND:
               var5 = EnumPlayerDigType.g;
               break;
            default:
               throw new IllegalStateException();
            }

            return new PacketPlayInBlockDig(var5, var10, var4, var9.sequence);
         } else if (var1 instanceof TeleportEntityPacketWrapper) {
            TeleportEntityPacketWrapper var8 = (TeleportEntityPacketWrapper)var1;
            var3 = new PacketDataSerializer(Unpooled.buffer());
            var3.c(var8.entityId);
            var3.a(var8.x);
            var3.a(var8.y);
            var3.a(var8.z);
            var3.k((byte)((int)((float)var8.yRot * 256.0F / 360.0F)));
            var3.k((byte)((int)((float)var8.xRot * 256.0F / 360.0F)));
            var3.a(var8.onGround);
            return new PacketPlayOutEntityTeleport(var3);
         } else if (var1 instanceof UpdateEntityPositionPacketWrapper) {
            UpdateEntityPositionPacketWrapper var7 = (UpdateEntityPositionPacketWrapper)var1;
            return new PacketPlayOutRelEntityMove(var7.entityId, var7.deltaX, var7.deltaY, var7.deltaZ, var7.onGround);
         } else if (var1 instanceof UpdateEntityRotationPacketWrapper) {
            UpdateEntityRotationPacketWrapper var6 = (UpdateEntityRotationPacketWrapper)var1;
            return new PacketPlayOutEntityLook(var6.entityId, var6.yRot, var6.xRot, var6.onGround);
         } else if (var1 instanceof UpdateEntityPositionRotationPacketWrapper) {
            UpdateEntityPositionRotationPacketWrapper var2 = (UpdateEntityPositionRotationPacketWrapper)var1;
            return new PacketPlayOutRelEntityMoveLook(var2.entityId, var2.deltaX, var2.deltaY, var2.deltaZ, var2.yRot, var2.xRot, var2.onGround);
         } else {
            throw new IllegalArgumentException();
         }
      }
   }
}
