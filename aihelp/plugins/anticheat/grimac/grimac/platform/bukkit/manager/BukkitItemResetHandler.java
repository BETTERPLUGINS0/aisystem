package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.bukkit.utils.reflection.PaperUtils;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class BukkitItemResetHandler implements ItemResetHandler {
   private static final Consumer<Player> resetItemUsage;
   private static final Predicate<Player> isUsingItem;
   private static final Function<Player, InteractionHand> getItemUsageHand;

   public void resetItemUsage(@Nullable PlatformPlayer player) {
      if (player != null) {
         resetItemUsage.accept((Player)player.getNative());
      }

   }

   @Nullable
   public InteractionHand getItemUsageHand(@Nullable PlatformPlayer player) {
      return player == null ? null : (InteractionHand)getItemUsageHand.apply((Player)player.getNative());
   }

   public boolean isUsingItem(@Nullable PlatformPlayer player) {
      return player != null && isUsingItem.test((Player)player.getNative());
   }

   static {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      boolean legacy = version.isOlderThanOrEquals(ServerVersion.V_1_8_8);

      try {
         Method getHandle_ = null;
         if (PaperUtils.PAPER && version.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            Class<?> cls = ReflectionUtils.getClass("org.bukkit.craftbukkit.entity.CraftLivingEntity");
            if (cls != null) {
               getHandle_ = cls.getMethod("getHandle");
            }
         }

         boolean obfuscated = getHandle_ == null;
         String nmsPackage = obfuscated ? Bukkit.getServer().getClass().getPackageName().split("\\.")[3] : null;
         if (obfuscated) {
            String clazzName = legacy ? "CraftHumanEntity" : "CraftLivingEntity";
            getHandle_ = Class.forName("org.bukkit.craftbukkit." + nmsPackage + ".entity." + clazzName).getMethod("getHandle");
         }

         Class<?> clazz = getHandle_.getReturnType();
         Method setLivingEntityFlag;
         String name;
         byte var9;
         String var10001;
         if (version.isNewerThanOrEquals(ServerVersion.V_1_10)) {
            isUsingItem = HumanEntity::isHandRaised;
         } else {
            name = (String)Objects.requireNonNull(nmsPackage);
            var9 = -1;
            switch(name.hashCode()) {
            case -1156422964:
               if (name.equals("v1_8_R3")) {
                  var9 = 0;
               }
               break;
            case -1156393175:
               if (name.equals("v1_9_R1")) {
                  var9 = 1;
               }
               break;
            case -1156393174:
               if (name.equals("v1_9_R2")) {
                  var9 = 2;
               }
            }

            switch(var9) {
            case 0:
               var10001 = "bS";
               break;
            case 1:
               var10001 = "cs";
               break;
            case 2:
               var10001 = "ct";
               break;
            default:
               throw new IllegalStateException("You are using an unsupported server version! (" + version.getReleaseName() + ")");
            }

            setLivingEntityFlag = clazz.getMethod(var10001);
            isUsingItem = (player) -> {
               try {
                  return (Boolean)setLivingEntityFlag.invoke(getHandle_.invoke(player));
               } catch (InvocationTargetException | IllegalAccessException var4) {
                  throw new RuntimeException(var4);
               }
            };
         }

         if (legacy) {
            getItemUsageHand = (player) -> {
               return isUsingItem.test(player) ? InteractionHand.MAIN_HAND : null;
            };
         } else if (PaperUtils.PAPER && version.isNewerThanOrEquals(ServerVersion.V_1_16_5)) {
            getItemUsageHand = (player) -> {
               return player.isHandRaised() ? (player.getHandRaised() == EquipmentSlot.OFF_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND) : null;
            };
         } else {
            name = (String)Objects.requireNonNull(nmsPackage);
            var9 = -1;
            switch(name.hashCode()) {
            case -1497224837:
               if (name.equals("v1_10_R1")) {
                  var9 = 2;
               }
               break;
            case -1497195046:
               if (name.equals("v1_11_R1")) {
                  var9 = 3;
               }
               break;
            case -1497165255:
               if (name.equals("v1_12_R1")) {
                  var9 = 4;
               }
               break;
            case -1497135464:
               if (name.equals("v1_13_R1")) {
                  var9 = 5;
               }
               break;
            case -1497135463:
               if (name.equals("v1_13_R2")) {
                  var9 = 6;
               }
               break;
            case -1497105673:
               if (name.equals("v1_14_R1")) {
                  var9 = 7;
               }
               break;
            case -1497075882:
               if (name.equals("v1_15_R1")) {
                  var9 = 8;
               }
               break;
            case -1497046091:
               if (name.equals("v1_16_R1")) {
                  var9 = 9;
               }
               break;
            case -1497046090:
               if (name.equals("v1_16_R2")) {
                  var9 = 10;
               }
               break;
            case -1497046089:
               if (name.equals("v1_16_R3")) {
                  var9 = 11;
               }
               break;
            case -1497016300:
               if (name.equals("v1_17_R1")) {
                  var9 = 12;
               }
               break;
            case -1496986509:
               if (name.equals("v1_18_R1")) {
                  var9 = 13;
               }
               break;
            case -1496986508:
               if (name.equals("v1_18_R2")) {
                  var9 = 14;
               }
               break;
            case -1496956718:
               if (name.equals("v1_19_R1")) {
                  var9 = 15;
               }
               break;
            case -1496956717:
               if (name.equals("v1_19_R2")) {
                  var9 = 16;
               }
               break;
            case -1496956716:
               if (name.equals("v1_19_R3")) {
                  var9 = 17;
               }
               break;
            case -1496301316:
               if (name.equals("v1_20_R1")) {
                  var9 = 18;
               }
               break;
            case -1496301315:
               if (name.equals("v1_20_R2")) {
                  var9 = 19;
               }
               break;
            case -1496301314:
               if (name.equals("v1_20_R3")) {
                  var9 = 20;
               }
               break;
            case -1496301313:
               if (name.equals("v1_20_R4")) {
                  var9 = 21;
               }
               break;
            case -1496271525:
               if (name.equals("v1_21_R1")) {
                  var9 = 22;
               }
               break;
            case -1496271524:
               if (name.equals("v1_21_R2")) {
                  var9 = 23;
               }
               break;
            case -1496271523:
               if (name.equals("v1_21_R3")) {
                  var9 = 24;
               }
               break;
            case -1496271522:
               if (name.equals("v1_21_R4")) {
                  var9 = 25;
               }
               break;
            case -1496271521:
               if (name.equals("v1_21_R5")) {
                  var9 = 26;
               }
               break;
            case -1496271520:
               if (name.equals("v1_21_R6")) {
                  var9 = 27;
               }
               break;
            case -1156393175:
               if (name.equals("v1_9_R1")) {
                  var9 = 0;
               }
               break;
            case -1156393174:
               if (name.equals("v1_9_R2")) {
                  var9 = 1;
               }
            }

            switch(var9) {
            case 0:
               var10001 = "ct";
               break;
            case 1:
               var10001 = "cu";
               break;
            case 2:
               var10001 = "cy";
               break;
            case 3:
               var10001 = "cz";
               break;
            case 4:
               var10001 = "cH";
               break;
            case 5:
            case 6:
            case 7:
               var10001 = "cU";
               break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
               var10001 = "getRaisedHand";
               break;
            case 13:
               var10001 = "eM";
               break;
            case 14:
               var10001 = "eN";
               break;
            case 15:
               var10001 = "eU";
               break;
            case 16:
               var10001 = "fa";
               break;
            case 17:
               var10001 = "ff";
               break;
            case 18:
               var10001 = "fj";
               break;
            case 19:
               var10001 = "fn";
               break;
            case 20:
               var10001 = "fo";
               break;
            case 21:
               var10001 = "fw";
               break;
            case 22:
               var10001 = "fs";
               break;
            case 23:
            case 24:
            case 25:
               var10001 = "fA";
               break;
            case 26:
               var10001 = "fH";
               break;
            case 27:
               var10001 = "fP";
               break;
            default:
               throw new IllegalStateException("You are using an unsupported server version! (" + version.getReleaseName() + ")");
            }

            setLivingEntityFlag = clazz.getMethod(var10001);
            getItemUsageHand = (player) -> {
               try {
                  return isUsingItem.test(player) ? (((Enum)setLivingEntityFlag.invoke(getHandle_.invoke(player))).ordinal() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND) : null;
               } catch (InvocationTargetException | IllegalAccessException var4) {
                  throw new RuntimeException(var4);
               }
            };
         }

         if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            name = obfuscated ? "c" : "setLivingEntityFlag";
            setLivingEntityFlag = clazz.getDeclaredMethod(name, Integer.TYPE, Boolean.TYPE);
            setLivingEntityFlag.setAccessible(true);
         } else {
            setLivingEntityFlag = null;
         }

         if (PaperUtils.PAPER && version.isNewerThan(ServerVersion.V_1_17)) {
            resetItemUsage = setLivingEntityFlag == null ? LivingEntity::clearActiveItem : (player) -> {
               try {
                  setLivingEntityFlag.invoke(getHandle_.invoke(player), 1, false);
               } catch (InvocationTargetException | IllegalAccessException var4) {
                  throw new RuntimeException(var4);
               }

               player.clearActiveItem();
            };
         } else {
            String var15 = (String)Objects.requireNonNull(nmsPackage);
            byte var10 = -1;
            switch(var15.hashCode()) {
            case -1497224837:
               if (var15.equals("v1_10_R1")) {
                  var10 = 3;
               }
               break;
            case -1497195046:
               if (var15.equals("v1_11_R1")) {
                  var10 = 4;
               }
               break;
            case -1497165255:
               if (var15.equals("v1_12_R1")) {
                  var10 = 5;
               }
               break;
            case -1497135464:
               if (var15.equals("v1_13_R1")) {
                  var10 = 6;
               }
               break;
            case -1497135463:
               if (var15.equals("v1_13_R2")) {
                  var10 = 7;
               }
               break;
            case -1497105673:
               if (var15.equals("v1_14_R1")) {
                  var10 = 8;
               }
               break;
            case -1497075882:
               if (var15.equals("v1_15_R1")) {
                  var10 = 9;
               }
               break;
            case -1497046091:
               if (var15.equals("v1_16_R1")) {
                  var10 = 10;
               }
               break;
            case -1497046090:
               if (var15.equals("v1_16_R2")) {
                  var10 = 11;
               }
               break;
            case -1497046089:
               if (var15.equals("v1_16_R3")) {
                  var10 = 12;
               }
               break;
            case -1497016300:
               if (var15.equals("v1_17_R1")) {
                  var10 = 13;
               }
               break;
            case -1496986509:
               if (var15.equals("v1_18_R1")) {
                  var10 = 14;
               }
               break;
            case -1496986508:
               if (var15.equals("v1_18_R2")) {
                  var10 = 15;
               }
               break;
            case -1496956718:
               if (var15.equals("v1_19_R1")) {
                  var10 = 16;
               }
               break;
            case -1496956717:
               if (var15.equals("v1_19_R2")) {
                  var10 = 17;
               }
               break;
            case -1496956716:
               if (var15.equals("v1_19_R3")) {
                  var10 = 18;
               }
               break;
            case -1496301316:
               if (var15.equals("v1_20_R1")) {
                  var10 = 19;
               }
               break;
            case -1496301315:
               if (var15.equals("v1_20_R2")) {
                  var10 = 20;
               }
               break;
            case -1496301314:
               if (var15.equals("v1_20_R3")) {
                  var10 = 21;
               }
               break;
            case -1496301313:
               if (var15.equals("v1_20_R4")) {
                  var10 = 22;
               }
               break;
            case -1496271525:
               if (var15.equals("v1_21_R1")) {
                  var10 = 23;
               }
               break;
            case -1496271524:
               if (var15.equals("v1_21_R2")) {
                  var10 = 24;
               }
               break;
            case -1496271523:
               if (var15.equals("v1_21_R3")) {
                  var10 = 25;
               }
               break;
            case -1496271522:
               if (var15.equals("v1_21_R4")) {
                  var10 = 26;
               }
               break;
            case -1496271521:
               if (var15.equals("v1_21_R5")) {
                  var10 = 27;
               }
               break;
            case -1496271520:
               if (var15.equals("v1_21_R6")) {
                  var10 = 28;
               }
               break;
            case -1156422964:
               if (var15.equals("v1_8_R3")) {
                  var10 = 0;
               }
               break;
            case -1156393175:
               if (var15.equals("v1_9_R1")) {
                  var10 = 1;
               }
               break;
            case -1156393174:
               if (var15.equals("v1_9_R2")) {
                  var10 = 2;
               }
            }

            switch(var10) {
            case 0:
               var10001 = "bV";
               break;
            case 1:
               var10001 = "cz";
               break;
            case 2:
               var10001 = "cA";
               break;
            case 3:
               var10001 = "cE";
               break;
            case 4:
               var10001 = "cF";
               break;
            case 5:
               var10001 = "cN";
               break;
            case 6:
            case 7:
               var10001 = "da";
               break;
            case 8:
               var10001 = "dp";
               break;
            case 9:
               var10001 = "dH";
               break;
            case 10:
            case 11:
            case 12:
            case 13:
               var10001 = "clearActiveItem";
               break;
            case 14:
               var10001 = "eR";
               break;
            case 15:
               var10001 = "eS";
               break;
            case 16:
               var10001 = "eZ";
               break;
            case 17:
               var10001 = "ff";
               break;
            case 18:
               var10001 = "fk";
               break;
            case 19:
               var10001 = "fo";
               break;
            case 20:
               var10001 = "fs";
               break;
            case 21:
               var10001 = "ft";
               break;
            case 22:
               var10001 = "fB";
               break;
            case 23:
               var10001 = "fx";
               break;
            case 24:
            case 25:
            case 26:
               var10001 = "fF";
               break;
            case 27:
               var10001 = "fM";
               break;
            case 28:
               var10001 = "fU";
               break;
            default:
               throw new IllegalStateException("You are using an unsupported server version! (" + version.getReleaseName() + ")");
            }

            Method method = clazz.getMethod(var10001);
            if (legacy) {
               resetItemUsage = (player) -> {
                  try {
                     method.invoke(getHandle_.invoke(player));
                     if (isUsingItem.test(player)) {
                        player.updateInventory();
                     }

                  } catch (InvocationTargetException | IllegalAccessException var4) {
                     throw new RuntimeException(var4);
                  }
               };
            } else if (setLivingEntityFlag == null) {
               resetItemUsage = (player) -> {
                  try {
                     method.invoke(getHandle_.invoke(player));
                  } catch (InvocationTargetException | IllegalAccessException var4) {
                     throw new RuntimeException(var4);
                  }
               };
            } else {
               resetItemUsage = (player) -> {
                  try {
                     Object handle = getHandle_.invoke(player);
                     setLivingEntityFlag.invoke(handle, 1, false);
                     method.invoke(handle);
                  } catch (InvocationTargetException | IllegalAccessException var5) {
                     throw new RuntimeException(var5);
                  }
               };
            }
         }

      } catch (Throwable var11) {
         RuntimeException var10000;
         if (var11 instanceof RuntimeException) {
            RuntimeException e = (RuntimeException)var11;
            var10000 = e;
         } else {
            var10000 = new RuntimeException(var11);
         }

         throw var10000;
      }
   }
}
