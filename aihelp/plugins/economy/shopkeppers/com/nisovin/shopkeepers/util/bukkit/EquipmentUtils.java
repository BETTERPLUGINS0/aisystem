package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EquipmentSlot;

public class EquipmentUtils {
   public static final Optional<EquipmentSlot> EQUIPMENT_SLOT_BODY;
   public static final Optional<EquipmentSlot> EQUIPMENT_SLOT_SADDLE;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_HANDS_AND_ARMOR;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_HANDS_AND_HEAD;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_HANDS_HEAD_SADDLE;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_MAINHAND_AND_HEAD;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_HANDS;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_MAINHAND;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_HEAD;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_BODY;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_SADDLE;
   public static final List<? extends EquipmentSlot> EQUIPMENT_SLOTS_BODY_AND_SADDLE;

   public static boolean supportsEquipment(EntityType entityType) {
      if (entityType != EntityType.ARMOR_STAND && entityType != EntityType.PLAYER) {
         Class<?> entityClass = entityType.getEntityClass();
         return entityClass == null ? false : Mob.class.isAssignableFrom(entityClass);
      } else {
         return true;
      }
   }

   public static List<? extends EquipmentSlot> getSupportedEquipmentSlots(EntityType entityType) {
      String var1 = entityType.name();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -2125864634:
         if (var1.equals("VILLAGER")) {
            var2 = 28;
         }
         break;
      case -1940664743:
         if (var1.equals("VINDICATOR")) {
            var2 = 30;
         }
         break;
      case -1935027645:
         if (var1.equals("PIGLIN")) {
            var2 = 24;
         }
         break;
      case -1932423455:
         if (var1.equals("PLAYER")) {
            var2 = 11;
         }
         break;
      case -1739913794:
         if (var1.equals("DOLPHIN")) {
            var2 = 34;
         }
         break;
      case -1670492626:
         if (var1.equals("CAMEL_HUSK")) {
            var2 = 7;
         }
         break;
      case -1651041609:
         if (var1.equals("DROWNED")) {
            var2 = 16;
         }
         break;
      case -1643025882:
         if (var1.equals("ZOMBIE")) {
            var2 = 14;
         }
         break;
      case -1611381190:
         if (var1.equals("MANNEQUIN")) {
            var2 = 13;
         }
         break;
      case -1504101618:
         if (var1.equals("ILLUSIONER")) {
            var2 = 37;
         }
         break;
      case -1484593075:
         if (var1.equals("SKELETON")) {
            var2 = 19;
         }
         break;
      case -1428246125:
         if (var1.equals("NAUTILUS")) {
            var2 = 9;
         }
         break;
      case -1288904373:
         if (var1.equals("SKELETON_HORSE")) {
            var2 = 2;
         }
         break;
      case -1163786087:
         if (var1.equals("STRIDER")) {
            var2 = 1;
         }
         break;
      case -1125519454:
         if (var1.equals("HAPPY_GHAST")) {
            var2 = 41;
         }
         break;
      case -1023865567:
         if (var1.equals("ZOMBIFIED_PIGLIN")) {
            var2 = 26;
         }
         break;
      case -679759041:
         if (var1.equals("ZOMBIE_VILLAGER")) {
            var2 = 15;
         }
         break;
      case -499196828:
         if (var1.equals("ZOMBIE_HORSE")) {
            var2 = 3;
         }
         break;
      case -75458619:
         if (var1.equals("PARCHED")) {
            var2 = 22;
         }
         break;
      case 69807:
         if (var1.equals("FOX")) {
            var2 = 33;
         }
         break;
      case 79214:
         if (var1.equals("PIG")) {
            var2 = 0;
         }
         break;
      case 84873:
         if (var1.equals("VEX")) {
            var2 = 31;
         }
         break;
      case 2229285:
         if (var1.equals("HUSK")) {
            var2 = 17;
         }
         break;
      case 2378017:
         if (var1.equals("MULE")) {
            var2 = 4;
         }
         break;
      case 2670162:
         if (var1.equals("WOLF")) {
            var2 = 40;
         }
         break;
      case 17859468:
         if (var1.equals("ZOMBIE_NAUTILUS")) {
            var2 = 10;
         }
         break;
      case 62368121:
         if (var1.equals("ALLAY")) {
            var2 = 32;
         }
         break;
      case 63888534:
         if (var1.equals("CAMEL")) {
            var2 = 6;
         }
         break;
      case 67809701:
         if (var1.equals("GIANT")) {
            var2 = 18;
         }
         break;
      case 68928445:
         if (var1.equals("HORSE")) {
            var2 = 8;
         }
         break;
      case 72516629:
         if (var1.equals("LLAMA")) {
            var2 = 38;
         }
         break;
      case 78304826:
         if (var1.equals("PIGLIN_BRUTE")) {
            var2 = 25;
         }
         break;
      case 79235593:
         if (var1.equals("STRAY")) {
            var2 = 21;
         }
         break;
      case 82603943:
         if (var1.equals("WITCH")) {
            var2 = 35;
         }
         break;
      case 302175244:
         if (var1.equals("PILLAGER")) {
            var2 = 27;
         }
         break;
      case 943567908:
         if (var1.equals("TRADER_LLAMA")) {
            var2 = 39;
         }
         break;
      case 973178214:
         if (var1.equals("COPPER_GOLEM")) {
            var2 = 42;
         }
         break;
      case 1148457240:
         if (var1.equals("WANDERING_TRADER")) {
            var2 = 29;
         }
         break;
      case 1295910038:
         if (var1.equals("ARMOR_STAND")) {
            var2 = 12;
         }
         break;
      case 1826013977:
         if (var1.equals("WITHER_SKELETON")) {
            var2 = 20;
         }
         break;
      case 1964667724:
         if (var1.equals("BOGGED")) {
            var2 = 23;
         }
         break;
      case 2022138428:
         if (var1.equals("DONKEY")) {
            var2 = 5;
         }
         break;
      case 2057262010:
         if (var1.equals("EVOKER")) {
            var2 = 36;
         }
      }

      switch(var2) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
         return EQUIPMENT_SLOTS_SADDLE;
      case 8:
      case 9:
      case 10:
         return EQUIPMENT_SLOTS_BODY_AND_SADDLE;
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
         return EQUIPMENT_SLOTS_HANDS_AND_ARMOR;
      case 27:
         return EQUIPMENT_SLOTS_HANDS_AND_HEAD;
      case 28:
      case 29:
      case 30:
         return EQUIPMENT_SLOTS_MAINHAND_AND_HEAD;
      case 31:
      case 32:
         return EQUIPMENT_SLOTS_HANDS;
      case 33:
      case 34:
      case 35:
         return EQUIPMENT_SLOTS_MAINHAND;
      case 36:
      case 37:
         return EQUIPMENT_SLOTS_HEAD;
      case 38:
      case 39:
      case 40:
         return EQUIPMENT_SLOTS_BODY;
      case 41:
         return EQUIPMENT_SLOTS_BODY;
      case 42:
         return EQUIPMENT_SLOTS_HANDS_HEAD_SADDLE;
      default:
         return Collections.emptyList();
      }
   }

   public static boolean supportsSaddle(EntityType entityType) {
      String var1 = entityType.name();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1670492626:
         if (var1.equals("CAMEL_HUSK")) {
            var2 = 10;
         }
         break;
      case -1428246125:
         if (var1.equals("NAUTILUS")) {
            var2 = 2;
         }
         break;
      case -1288904373:
         if (var1.equals("SKELETON_HORSE")) {
            var2 = 5;
         }
         break;
      case -1163786087:
         if (var1.equals("STRIDER")) {
            var2 = 1;
         }
         break;
      case -499196828:
         if (var1.equals("ZOMBIE_HORSE")) {
            var2 = 6;
         }
         break;
      case 79214:
         if (var1.equals("PIG")) {
            var2 = 0;
         }
         break;
      case 2378017:
         if (var1.equals("MULE")) {
            var2 = 7;
         }
         break;
      case 17859468:
         if (var1.equals("ZOMBIE_NAUTILUS")) {
            var2 = 3;
         }
         break;
      case 63888534:
         if (var1.equals("CAMEL")) {
            var2 = 9;
         }
         break;
      case 68928445:
         if (var1.equals("HORSE")) {
            var2 = 4;
         }
         break;
      case 72516629:
         if (var1.equals("LLAMA")) {
            var2 = 11;
         }
         break;
      case 943567908:
         if (var1.equals("TRADER_LLAMA")) {
            var2 = 12;
         }
         break;
      case 2022138428:
         if (var1.equals("DONKEY")) {
            var2 = 8;
         }
      }

      switch(var2) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
         return true;
      case 11:
      case 12:
      default:
         return false;
      }
   }

   private EquipmentUtils() {
   }

   static {
      EquipmentSlot bodySlot = (EquipmentSlot)EnumUtils.valueOf(EquipmentSlot.class, "BODY");
      EQUIPMENT_SLOT_BODY = Optional.ofNullable(bodySlot);
      EquipmentSlot saddleSlot = (EquipmentSlot)EnumUtils.valueOf(EquipmentSlot.class, "SADDLE");
      EQUIPMENT_SLOT_SADDLE = Optional.ofNullable(saddleSlot);
      EQUIPMENT_SLOTS = Collections.unmodifiableList(Arrays.asList(EquipmentSlot.values()));
      EQUIPMENT_SLOTS_HANDS_AND_ARMOR = Collections.unmodifiableList(Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD));
      EQUIPMENT_SLOTS_HANDS_AND_HEAD = Collections.unmodifiableList(Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD));
      EQUIPMENT_SLOTS_HANDS_HEAD_SADDLE = Collections.unmodifiableList((List)Unsafe.cast(Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD, saddleSlot).stream().filter((x) -> {
         return x != null;
      }).toList()));
      EQUIPMENT_SLOTS_MAINHAND_AND_HEAD = Collections.unmodifiableList(Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.HEAD));
      EQUIPMENT_SLOTS_HANDS = Collections.unmodifiableList(Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND));
      EQUIPMENT_SLOTS_MAINHAND = Collections.singletonList(EquipmentSlot.HAND);
      EQUIPMENT_SLOTS_HEAD = Collections.singletonList(EquipmentSlot.HEAD);
      EQUIPMENT_SLOTS_BODY = bodySlot == null ? Collections.emptyList() : Collections.singletonList((EquipmentSlot)Unsafe.assertNonNull(bodySlot));
      EQUIPMENT_SLOTS_SADDLE = saddleSlot == null ? Collections.emptyList() : Collections.singletonList((EquipmentSlot)Unsafe.assertNonNull(saddleSlot));
      EQUIPMENT_SLOTS_BODY_AND_SADDLE = Collections.unmodifiableList((List)Unsafe.cast(Arrays.asList(bodySlot, saddleSlot).stream().filter((x) -> {
         return x != null;
      }).toList()));
   }
}
