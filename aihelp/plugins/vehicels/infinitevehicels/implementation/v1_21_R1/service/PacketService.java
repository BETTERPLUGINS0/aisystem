package implementation.v1_21_R1.service;

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
import implementation.v1_21_R1.util.PacketWritingUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInArmAnimation;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import net.minecraft.network.protocol.game.PacketPlayInBlockPlace;
import net.minecraft.network.protocol.game.PacketPlayInEntityAction;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
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
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketService implements advancedplugins.pm2.cv.service.PacketService {
   @VersionSensible
   private NetworkManager getNetworkManager(@NotNull PlayerConnection var1) {
      try {
         Field var2 = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
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
            PacketWritingUtil.compressAndWriteToPipeline(var4, var5);
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      } else {
         this.getNetworkManager(((CraftPlayer)var1).getHandle().c).a((Packet)var2);
      }

   }

   private Object reflective(Object var1) {
      try {
         Field var2 = PacketPlayInUseEntity.class.getDeclaredField("b");
         var2.setAccessible(true);
         return var2.get(var1);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         throw new RuntimeException(var3);
      }
   }

   private Object object(Packet<?> var1, String var2, String var3, String var4) {
      try {
         Class var5 = Class.forName(var2);
         Field var6 = var1.getClass().getDeclaredField(var3);
         var6.setAccessible(true);
         Object var7 = var6.get(var1);
         Field var8 = var5.getDeclaredField(var4);
         var8.setAccessible(true);
         return var8.get(var7);
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   @Nullable
   public PacketWrapper read(@NotNull Object var1) {
      if (var1 instanceof PacketPlayInSteerVehicle) {
         PacketPlayInSteerVehicle var30 = (PacketPlayInSteerVehicle)var1;
         return new PlayerInputPacketWrapper(var30.b(), var30.e(), var30.f(), var30.g());
      } else if (var1 instanceof PacketPlayInEntityAction) {
         PacketPlayInEntityAction var29 = (PacketPlayInEntityAction)var1;
         EnumPlayerAction var34 = var29.e();
         EntityActionWrapper.Action var41 = null;
         switch(var34) {
         case a:
            var41 = EntityActionWrapper.Action.START_SNEAKING;
            break;
         case b:
            var41 = EntityActionWrapper.Action.STOP_SNEAKING;
            break;
         case d:
            var41 = EntityActionWrapper.Action.START_SPRINTING;
            break;
         case e:
            var41 = EntityActionWrapper.Action.STOP_SPRINTING;
            break;
         case c:
            var41 = EntityActionWrapper.Action.LEAVE_BED;
            break;
         case f:
            var41 = EntityActionWrapper.Action.START_JUMP_HORSE;
            break;
         case g:
            var41 = EntityActionWrapper.Action.STOP_JUMP_HORSE;
            break;
         case h:
            var41 = EntityActionWrapper.Action.OPEN_VEHICLE_INVENTORY;
            break;
         case i:
            var41 = EntityActionWrapper.Action.START_FLYING_ELYTRA;
         }

         return new EntityActionWrapper(var29.b(), var41);
      } else {
         int var3;
         if (var1 instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity var2 = (PacketPlayInUseEntity)var1;
            var3 = (Integer)this.reflective(var2);

            String var4;
            try {
               Class var5 = Class.forName("net.minecraft.network.protocol.game.PacketPlayInUseEntity");
               Field var6 = var5.getDeclaredField("c");
               var6.setAccessible(true);
               Object var7 = var6.get(var2);
               Method var8 = var7.getClass().getDeclaredMethod("a");
               var8.setAccessible(true);
               var4 = String.valueOf(var8.invoke(var7));
            } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException var18) {
               throw new RuntimeException(var18);
            }

            if (var4.equalsIgnoreCase("ATTACK")) {
               boolean var36 = var2.b();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.ATTACK, (Double)null, (Double)null, (Double)null, (InteractPacketWrapper.Hand)null, var36);
            }

            if (var4.equalsIgnoreCase("INTERACT_AT")) {
               Vec3D var35 = (Vec3D)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e", "c", "b");
               EnumHand var39 = (EnumHand)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e", "c", "a");

               assert var35 != null;

               double var42 = var35.c;
               double var9 = var35.d;
               double var11 = var35.e;

               assert var39 != null;

               int var13 = var39.ordinal();
               boolean var14 = var2.b();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT_AT, var42, var9, var11, var13 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var14);
            }

            if (var4.equalsIgnoreCase("INTERACT")) {
               EnumHand var31 = (EnumHand)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$d", "c", "a");

               assert var31 != null;

               int var37 = var31.ordinal();
               boolean var40 = var2.b();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT, (Double)null, (Double)null, (Double)null, var37 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var40);
            }
         } else {
            if (var1 instanceof PacketPlayInArmAnimation) {
               return new SwingArmPacketWrapper(((PacketPlayInArmAnimation)var1).b() == EnumHand.a ? SwingArmPacketWrapper.Hand.MAIN_HAND : SwingArmPacketWrapper.Hand.OFF_HAND);
            }

            if (var1 instanceof PacketPlayInBlockPlace) {
               PacketPlayInBlockPlace var28 = (PacketPlayInBlockPlace)var1;
               return new UseItemPacketWrapper(var28.b() == EnumHand.a ? UseItemPacketWrapper.Hand.MAIN_HAND : UseItemPacketWrapper.Hand.OFF_HAND, var28.e());
            }

            if (var1 instanceof PacketPlayInLook) {
               PacketPlayInLook var26 = (PacketPlayInLook)var1;
               return new SetPlayerRotationPacketWrapper(var26.d, var26.e, var26.b());
            }

            if (var1 instanceof PacketPlayInPositionLook) {
               PacketPlayInPositionLook var25 = (PacketPlayInPositionLook)var1;
               return new SetPlayerPositionRotationPacketWrapper(var25.a, var25.b, var25.c, var25.d, var25.e, var25.b());
            }

            if (var1 instanceof PacketPlayInBlockDig) {
               PacketPlayInBlockDig var24 = (PacketPlayInBlockDig)var1;
               BlockPosition var32 = var24.b();
               PlayerActionPacketWrapper.Direction var33;
               switch(var24.e()) {
               case a:
                  var33 = PlayerActionPacketWrapper.Direction.DOWN;
                  break;
               case b:
                  var33 = PlayerActionPacketWrapper.Direction.UP;
                  break;
               case c:
                  var33 = PlayerActionPacketWrapper.Direction.NORTH;
                  break;
               case d:
                  var33 = PlayerActionPacketWrapper.Direction.SOUTH;
                  break;
               case e:
                  var33 = PlayerActionPacketWrapper.Direction.WEST;
                  break;
               case f:
                  var33 = PlayerActionPacketWrapper.Direction.EAST;
                  break;
               default:
                  throw new IllegalStateException();
               }

               PlayerActionPacketWrapper.Action var38;
               switch(var24.f()) {
               case a:
                  var38 = PlayerActionPacketWrapper.Action.START_DESTROY_BLOCK;
                  break;
               case b:
                  var38 = PlayerActionPacketWrapper.Action.ABORT_DESTROY_BLOCK;
                  break;
               case c:
                  var38 = PlayerActionPacketWrapper.Action.STOP_DESTROY_BLOCK;
                  break;
               case d:
                  var38 = PlayerActionPacketWrapper.Action.DROP_ALL_ITEMS;
                  break;
               case e:
                  var38 = PlayerActionPacketWrapper.Action.DROP_ITEM;
                  break;
               case f:
                  var38 = PlayerActionPacketWrapper.Action.RELEASE_USE_ITEM;
                  break;
               case g:
                  var38 = PlayerActionPacketWrapper.Action.SWAP_ITEM_WITH_OFFHAND;
                  break;
               default:
                  throw new IllegalStateException();
               }

               return new PlayerActionPacketWrapper(var32.u(), var32.v(), var32.w(), var33, var38, var24.g());
            }
         }

         if (var1 instanceof PacketPlayOutEntityTeleport) {
            PacketPlayOutEntityTeleport var22 = (PacketPlayOutEntityTeleport)var1;
            return new TeleportEntityPacketWrapper(var22.b(), var22.e(), var22.f(), var22.g(), var22.h(), var22.i(), var22.j());
         } else {
            boolean var23;
            Field var27;
            if (var1 instanceof PacketPlayOutRelEntityMove) {
               PacketPlayOutRelEntityMove var21 = (PacketPlayOutRelEntityMove)var1;
               var23 = false;

               try {
                  var27 = PacketPlayOutEntity.class.getDeclaredField("a");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var15) {
                  throw new RuntimeException(var15);
               }

               return new UpdateEntityPositionPacketWrapper(var3, var21.b(), var21.e(), var21.f(), var21.k());
            } else if (var1 instanceof PacketPlayOutEntityLook) {
               PacketPlayOutEntityLook var20 = (PacketPlayOutEntityLook)var1;
               var23 = false;

               try {
                  var27 = PacketPlayOutEntity.class.getDeclaredField("a");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var16) {
                  throw new RuntimeException(var16);
               }

               return new UpdateEntityRotationPacketWrapper(var3, var20.g(), var20.h(), var20.k());
            } else if (var1 instanceof PacketPlayOutRelEntityMoveLook) {
               PacketPlayOutRelEntityMoveLook var19 = (PacketPlayOutRelEntityMoveLook)var1;
               var23 = false;

               try {
                  var27 = PacketPlayOutEntity.class.getDeclaredField("a");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var17) {
                  throw new RuntimeException(var17);
               }

               return new UpdateEntityPositionRotationPacketWrapper(var3, var19.b(), var19.e(), var19.f(), var19.g(), var19.h(), var19.k());
            } else {
               return null;
            }
         }
      }
   }

   @NotNull
   public Object createInstance(@NotNull PacketWrapper var1) {
      if (var1 instanceof PlayerInputPacketWrapper) {
         PlayerInputPacketWrapper var18 = (PlayerInputPacketWrapper)var1;
         return new PacketPlayInSteerVehicle(var18.sideways, var18.forward, var18.jump, var18.unmount);
      } else {
         PacketDataSerializer var3;
         Constructor var4;
         if (var1 instanceof InteractPacketWrapper) {
            InteractPacketWrapper var17 = (InteractPacketWrapper)var1;
            var3 = new PacketDataSerializer(Unpooled.buffer());
            var3.c(var17.entityId);
            switch(var17.action) {
            case INTERACT:
               var3.c(0);
               var3.c(var17.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
               break;
            case ATTACK:
               var3.c(1);
               break;
            case INTERACT_AT:
               var3.c(2);
               var3.a(((Double)Objects.requireNonNull(var17.targetX)).floatValue());
               var3.a(((Double)Objects.requireNonNull(var17.targetY)).floatValue());
               var3.a(((Double)Objects.requireNonNull(var17.targetZ)).floatValue());
               var3.c(var17.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
            }

            var3.a(var17.sneaking);

            try {
               var4 = PacketPlayInUseEntity.class.getDeclaredConstructor(PacketDataSerializer.class);
               var4.setAccessible(true);
               return var4.newInstance(var3);
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException var6) {
               throw new RuntimeException(var6);
            }
         } else if (var1 instanceof SwingArmPacketWrapper) {
            return new PacketPlayInArmAnimation(((SwingArmPacketWrapper)var1).hand == SwingArmPacketWrapper.Hand.MAIN_HAND ? EnumHand.a : EnumHand.b);
         } else if (var1 instanceof UseItemPacketWrapper) {
            UseItemPacketWrapper var15 = (UseItemPacketWrapper)var1;
            return new PacketPlayInBlockPlace(var15.hand == UseItemPacketWrapper.Hand.MAIN_HAND ? EnumHand.a : EnumHand.b, var15.sequence, 0.0F, 0.0F);
         } else if (var1 instanceof SetPlayerRotationPacketWrapper) {
            SetPlayerRotationPacketWrapper var14 = (SetPlayerRotationPacketWrapper)var1;
            return new PacketPlayInLook(var14.yRot, var14.xRot, var14.onGround);
         } else if (var1 instanceof SetPlayerPositionRotationPacketWrapper) {
            SetPlayerPositionRotationPacketWrapper var13 = (SetPlayerPositionRotationPacketWrapper)var1;
            return new PacketPlayInPositionLook(var13.x, var13.y, var13.z, var13.yRot, var13.xRot, var13.onGround);
         } else if (var1 instanceof PlayerActionPacketWrapper) {
            PlayerActionPacketWrapper var11 = (PlayerActionPacketWrapper)var1;
            BlockPosition var12 = new BlockPosition(var11.positionX, var11.positionY, var11.positionZ);
            EnumDirection var16;
            switch(var11.direction) {
            case DOWN:
               var16 = EnumDirection.a;
               break;
            case UP:
               var16 = EnumDirection.b;
               break;
            case NORTH:
               var16 = EnumDirection.c;
               break;
            case SOUTH:
               var16 = EnumDirection.d;
               break;
            case WEST:
               var16 = EnumDirection.e;
               break;
            case EAST:
               var16 = EnumDirection.f;
               break;
            default:
               throw new IllegalStateException();
            }

            EnumPlayerDigType var5;
            switch(var11.action) {
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

            return new PacketPlayInBlockDig(var5, var12, var16, var11.sequence);
         } else if (var1 instanceof TeleportEntityPacketWrapper) {
            TeleportEntityPacketWrapper var10 = (TeleportEntityPacketWrapper)var1;
            var3 = new PacketDataSerializer(Unpooled.buffer());
            var3.c(var10.entityId);
            var3.a(var10.x);
            var3.a(var10.y);
            var3.a(var10.z);
            var3.k((byte)((int)((float)var10.yRot * 256.0F / 360.0F)));
            var3.k((byte)((int)((float)var10.xRot * 256.0F / 360.0F)));
            var3.a(var10.onGround);

            try {
               var4 = PacketPlayOutEntityTeleport.class.getDeclaredConstructor(PacketDataSerializer.class);
               var4.setAccessible(true);
               return var4.newInstance(var3);
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException var7) {
               throw new RuntimeException(var7);
            }
         } else if (var1 instanceof UpdateEntityPositionPacketWrapper) {
            UpdateEntityPositionPacketWrapper var9 = (UpdateEntityPositionPacketWrapper)var1;
            return new PacketPlayOutRelEntityMove(var9.entityId, var9.deltaX, var9.deltaY, var9.deltaZ, var9.onGround);
         } else if (var1 instanceof UpdateEntityRotationPacketWrapper) {
            UpdateEntityRotationPacketWrapper var8 = (UpdateEntityRotationPacketWrapper)var1;
            return new PacketPlayOutEntityLook(var8.entityId, var8.yRot, var8.xRot, var8.onGround);
         } else if (var1 instanceof UpdateEntityPositionRotationPacketWrapper) {
            UpdateEntityPositionRotationPacketWrapper var2 = (UpdateEntityPositionRotationPacketWrapper)var1;
            return new PacketPlayOutRelEntityMoveLook(var2.entityId, var2.deltaX, var2.deltaY, var2.deltaZ, var2.yRot, var2.xRot, var2.onGround);
         } else {
            throw new IllegalArgumentException();
         }
      }
   }
}
