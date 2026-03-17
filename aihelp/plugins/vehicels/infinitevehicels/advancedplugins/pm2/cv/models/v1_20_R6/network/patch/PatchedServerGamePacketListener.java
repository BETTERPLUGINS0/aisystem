package advancedplugins.pm2.cv.models.v1_20_R6.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.events.BaseEntityInteractEvent;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_20_R6.NMSMethods;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriterionTriggers;
import net.minecraft.network.protocol.PlayerConnectionUtils;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketPlayInBlockPlace;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity.c;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.RayTrace.BlockCollisionOption;
import net.minecraft.world.level.RayTrace.FluidCollisionOption;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.MovingObjectPosition.EnumMovingObjectType;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class PatchedServerGamePacketListener {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static void handleInteract(ServerboundInteractPacketWrapper interactPacket, PacketListenerPlayIn listener) {
      if (var1 instanceof PlayerConnection) {
         final PlayerConnection var2 = (PlayerConnection)var1;
         PlayerConnectionUtils.a(var0, var2, var2.o().z());
         final EntityPlayer var3 = var2.o();
         if (!var3.fg()) {
            final CraftPlayer var4 = var3.getBukkitEntity();
            final WorldServer var5 = var3.z();
            final CraftServer var6 = var5.o().server;
            final Entity var7 = var0.getTarget(var5);
            var0.dispatch(new c() {
               public void a(EnumHand hand) {
                  if (var0.isFakeInteraction()) {
                     EntityHandler var2 = ModelAPI.getNMSHandler().getEntityHandler();
                     var2.forceUseItem(var4, EquipmentSlot.HAND);
                     var2.forceUseItem(var4, EquipmentSlot.OFF_HAND);
                  }

               }

               public void a(EnumHand hand, Vec3D pos) {
               }

               public void a() {
               }
            });
            if (var7 != var3 || var3.N_()) {
               var3.G();
               var3.g(var0.isUsingSecondaryAction());
               final IVisualModel var8 = ModelAPI.getInteractionTracker().getModelRelay(var0.getOriginalId());
               if (var8 != null) {
                  final BaseEntity var9 = var8.getModeledEntity().getBase();
                  var0.dispatch(new c() {
                     public void a(EnumHand hand) {
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var9, var8, BaseEntityInteractEvent.Action.INTERACT, EquipmentSlot.HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.HAND), (Vector)null));
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var9, var8, BaseEntityInteractEvent.Action.INTERACT, EquipmentSlot.OFF_HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.OFF_HAND), (Vector)null));
                     }

                     public void a(EnumHand hand, Vec3D pos) {
                        Vector var3 = new Vector(var2.c, var2.d, var2.e);
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var9, var8, BaseEntityInteractEvent.Action.INTERACT_ON, EquipmentSlot.HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.HAND), var3));
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var9, var8, BaseEntityInteractEvent.Action.INTERACT_ON, EquipmentSlot.OFF_HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.OFF_HAND), var3));
                     }

                     public void a() {
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var9, var8, BaseEntityInteractEvent.Action.ATTACK, EquipmentSlot.HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.HAND), (Vector)null));
                     }
                  });
               }

               if (var7 == null) {
                  if (ServerInfo.IS_PAPER) {
                     var0.dispatch(new c() {
                        public void a(EnumHand hand) {
                           PatchedServerGamePacketListener.callPlayerUseUnknownEntityEvent(var6, var4, var0, var1, (Vec3D)null);
                        }

                        public void a(EnumHand hand, Vec3D pos) {
                           PatchedServerGamePacketListener.callPlayerUseUnknownEntityEvent(var6, var4, var0, var1, var2);
                        }

                        public void a() {
                           PatchedServerGamePacketListener.callPlayerUseUnknownEntityEvent(var6, var4, var0, EnumHand.a, (Vec3D)null);
                        }
                     });
                  }
               } else if (var5.C_().a(var7.dp())) {
                  AxisAlignedBB var10 = var7.cK();
                  if (!(var10.e(var3.bx()) >= 9.0D)) {
                     var0.dispatch(new c() {
                        private void performInteraction(EnumHand enumhand, PatchedServerGamePacketListener.EntityInteraction entityInteraction, PlayerInteractEntityEvent event) {
                           ItemStack var4 = var3.b(var1);
                           if (var4.a(var5.J())) {
                              ItemStack var5x = var4.s();
                              ItemStack var6x = var3.b(var1);
                              boolean var7x = var6x.g() == Items.uK && var7 instanceof EntityInsentient;
                              Item var8 = var3.gc().f().g();
                              var6.getPluginManager().callEvent(var3x);
                              if (var7 instanceof Bucketable && var7 instanceof EntityLiving && var8.r() == Items.qz && (var3x.isCancelled() || var3.gc().f().g() != var8)) {
                                 if (var7.tracker != null && var3.getBukkitEntity().canSee(var7.getBukkitEntity())) {
                                    EntityTrackerEntry var9 = var7.tracker.b;
                                    ArrayList var10 = new ArrayList();
                                    EntityPlayer var11 = var3;
                                    Objects.requireNonNull(var10);
                                    Objects.requireNonNull(var10);
                                    var9.a(var11, var10::add);
                                    var3.c.b(new ClientboundBundlePacket(var10));
                                 }

                                 var3.cb.b();
                              }

                              if (var7x && (var3x.isCancelled() || var3.gc().f().g() != var8)) {
                                 var2.b(new PacketPlayOutAttachEntity(var7, ((EntityInsentient)var7).gf()));
                              }

                              if (var3x.isCancelled() || var3.gc().f().g() != var8) {
                                 List var12 = var7.ap().b();
                                 if (var12 != null && var3.getBukkitEntity().canSee(var7.getBukkitEntity())) {
                                    var3.c.b(new PacketPlayOutEntityMetadata(var7.al(), var12));
                                 }

                                 if (var7 instanceof Allay) {
                                    Allay var14 = (Allay)var7;
                                    List var15 = (List)Arrays.stream(EnumItemSlot.values()).map((var1x) -> {
                                       return Pair.of(var1x, var14.a(var1x));
                                    }).collect(Collectors.toList());
                                    var2.b(new PacketPlayOutEntityEquipment(var7.al(), var15));
                                    var3.cb.b();
                                 }
                              }

                              if (var3x.isCancelled()) {
                                 var3.cb.b();
                                 return;
                              }

                              EnumInteractionResult var13 = var2x.run(var3, var7, var1);
                              if (!var6x.e() && var6x.I() <= -1) {
                                 var3.cb.b();
                              }

                              if (var13.a()) {
                                 CriterionTriggers.T.a(var3, var5x, var7);
                                 if (var13.b()) {
                                    var3.a(var1, true);
                                 }
                              }
                           }

                        }

                        public void a(@NotNull EnumHand hand) {
                           this.performInteraction(var1, EntityHuman::a, new PlayerInteractEntityEvent(var2.getCraftPlayer(), var7.getBukkitEntity(), var1 == EnumHand.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
                        }

                        public void a(@NotNull EnumHand hand, @NotNull Vec3D pos) {
                           this.performInteraction(var1, (var1x, var2xx, var3x) -> {
                              return var2xx.a(var1x, var2x, var3x);
                           }, new PlayerInteractAtEntityEvent(var2.getCraftPlayer(), var7.getBukkitEntity(), new Vector(var2x.c, var2x.d, var2x.e), var1 == EnumHand.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
                        }

                        public void a() {
                           if (!(var7 instanceof EntityItem) && !(var7 instanceof EntityExperienceOrb) && !(var7 instanceof EntityArrow) && (var7 != var3 || var3.N_())) {
                              ItemStack var1 = var3.b(EnumHand.a);
                              if (var1.a(var5.J())) {
                                 var3.e(var7);
                                 if (!var1.e() && var1.I() <= -1) {
                                    var3.cb.b();
                                 }
                              }
                           }

                        }
                     });
                  }
               }
            }
         }
      }

   }

   public static void handleUseItem(PacketPlayInBlockPlace packet, PacketListenerPlayIn listener, Consumer<EnumInteractionResult> afterUse) {
      if (var1 instanceof PlayerConnection) {
         PlayerConnection var3 = (PlayerConnection)var1;
         EntityPlayer var4 = var3.o();
         PlayerConnectionUtils.a(var0, var3, var4.z());
         if (!var4.fg()) {
            boolean var5 = Boolean.TRUE.equals(ReflectionUtils.call(var3, NMSMethods.SERVER_GAME_PACKET_LISTENER_IMPL_checkLimit, var0.timestamp));
            if (var5) {
               var3.a(var0.e());
               WorldServer var6 = var4.z();
               EnumHand var7 = var0.b();
               ItemStack var8 = var4.b(var7);
               var4.G();
               if (!var8.e() && var8.a(var6.J())) {
                  float var9 = var4.dH();
                  float var10 = var4.dF();
                  double var11 = var4.du();
                  double var13 = var4.dw() + (double)var4.cL();
                  double var15 = var4.dA();
                  Vec3D var17 = new Vec3D(var11, var13, var15);
                  float var18 = MathHelper.b(-var10 * 0.017453292F - 3.1415927F);
                  float var19 = MathHelper.a(-var10 * 0.017453292F - 3.1415927F);
                  float var20 = -MathHelper.b(-var9 * 0.017453292F);
                  float var21 = MathHelper.a(-var9 * 0.017453292F);
                  float var22 = var19 * var20;
                  float var23 = var18 * var20;
                  double var24 = var4.e.b() == EnumGamemode.b ? 5.0D : 4.5D;
                  Vec3D var26 = var17.b((double)var22 * var24, (double)var21 * var24, (double)var23 * var24);
                  MovingObjectPositionBlock var27 = var4.dP().a(new RayTrace(var17, var26, BlockCollisionOption.b, FluidCollisionOption.a, var4));
                  boolean var28;
                  if (var27 != null && var27.c() == EnumMovingObjectType.b) {
                     MovingObjectPositionBlock var31 = (MovingObjectPositionBlock)var27;
                     if (var4.e.firedInteract && var4.e.interactPosition.equals(var31.a()) && var4.e.interactHand == var7 && ItemStack.c(var4.e.interactItemStack, var8)) {
                        var28 = var4.e.interactResult;
                     } else {
                        PlayerInteractEvent var30 = CraftEventFactory.callPlayerInteractEvent(var4, Action.RIGHT_CLICK_BLOCK, var31.a(), var31.b(), var8, true, var7, var31.e());
                        var28 = var30.useItemInHand() == Result.DENY;
                     }

                     var4.e.firedInteract = false;
                  } else {
                     PlayerInteractEvent var29 = CraftEventFactory.callPlayerInteractEvent(var4, Action.RIGHT_CLICK_AIR, var8, var7);
                     var28 = var29.useItemInHand() == Result.DENY;
                  }

                  if (var28) {
                     var4.resyncUsingItem(var4);
                     var4.getBukkitEntity().updateInventory();
                     return;
                  }

                  var8 = var4.b(var7);
                  if (var8.e()) {
                     return;
                  }

                  EnumInteractionResult var32 = var4.e.a(var4, var6, var8, var7);
                  var2.accept(var32);
                  if (var32.b()) {
                     var4.a(var7, true);
                  }
               }
            }
         }
      }

   }

   private static void callPlayerUseUnknownEntityEvent(CraftServer cserver, CraftPlayer craftPlayer, ServerboundInteractPacketWrapper packet, EnumHand hand, @Nullable Vec3D vector) {
      var0.getPluginManager().callEvent(new PlayerUseUnknownEntityEvent(var1, var2.getEntityId(), var2.isAttack(), var3 == EnumHand.a ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND, var4 != null ? new Vector(var4.c, var4.d, var4.e) : null));
   }

   @FunctionalInterface
   private interface EntityInteraction {
      EnumInteractionResult run(EntityPlayer var1, Entity var2, EnumHand var3);
   }
}
