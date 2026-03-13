package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.bukkit.NamespacedKeyUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemMigration;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.TimeUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.tag.DamageTypeTags;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ServerAssumptionsTest {
   @Nullable
   private static Boolean result = null;
   private final ItemStack bukkitItemStack = createComplexItemStack();
   private final ItemStack bukkitItemStack2 = createComplexItemStack();
   private final ItemStack craftItemStack;
   private final ItemStack craftItemStack2;
   private final ItemStack deserializedItemStack;
   private final ItemStack deserializedCraftItemStack;

   public static boolean run() {
      if (result != null) {
         Log.debug("Skipping already run server assumption tests.");
         return (Boolean)Unsafe.assertNonNull(result);
      } else {
         long startTime = System.nanoTime();
         Log.debug("Testing server assumptions ...");

         try {
            ServerAssumptionsTest test = new ServerAssumptionsTest();
            test.runAll();
            result = true;
            double durationMillis = TimeUtils.convert((double)(System.nanoTime() - startTime), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
            Log.debug(() -> {
               return "Server assumption tests passed (" + TextUtils.format(durationMillis) + " ms).";
            });
         } catch (Exception var5) {
            result = false;
            Log.severe("Server assumption test failed: " + var5.getMessage());
         }

         return (Boolean)Unsafe.assertNonNull(result);
      }
   }

   private static void assumption(boolean expression, String errorMessage) throws ServerAssumptionsTest.ServerAssumptionFailedException {
      if (!expression) {
         throw new ServerAssumptionsTest.ServerAssumptionFailedException(errorMessage);
      }
   }

   private static void assumeIsSimilar(ItemStack expected, ItemStack actual, String errorMessage) throws ServerAssumptionsTest.ServerAssumptionFailedException {
      if (!expected.isSimilar(actual)) {
         String detailMessage = "";
         if (Settings.debug) {
            String var10000 = ConfigUtils.toConfigYaml("item", expected);
            detailMessage = ": Expected [" + var10000 + "] got [" + ConfigUtils.toConfigYaml("item", actual) + "]";
         }

         throw new ServerAssumptionsTest.ServerAssumptionFailedException(errorMessage + detailMessage);
      }
   }

   private static void assumeEquals(ItemStack expected, ItemStack actual, String errorMessage) throws ServerAssumptionsTest.ServerAssumptionFailedException {
      if (!expected.equals(actual)) {
         String detailMessage = "";
         if (Settings.debug) {
            String var10000 = ConfigUtils.toConfigYaml("item", expected);
            detailMessage = ": Expected [" + var10000 + "] got [" + ConfigUtils.toConfigYaml("item", actual) + "]";
         }

         throw new ServerAssumptionsTest.ServerAssumptionFailedException(errorMessage + detailMessage);
      }
   }

   private ServerAssumptionsTest() {
      this.craftItemStack = toCraftItemStackCopy(this.bukkitItemStack);
      this.craftItemStack2 = toCraftItemStackCopy(this.bukkitItemStack2);
      this.deserializedItemStack = deserialize(this.bukkitItemStack);
      this.deserializedCraftItemStack = toCraftItemStackCopy(this.deserializedItemStack);
   }

   private static ItemStack createComplexItemStack() {
      ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
      ItemUtils.setDisplayNameAndLore(itemStack, "{\"text\":\"Custom Name\",\"color\":\"red\"}", Arrays.asList("{\"text\":\"lore1\",\"color\":\"green\"}", "lore2"));
      ItemUtils.setItemName(itemStack, "{\"text\":\"Custom item name\",\"color\":\"red\"}");
      ItemMeta itemMeta = (ItemMeta)Unsafe.assertNonNull(itemStack.getItemMeta());
      itemMeta.setMaxStackSize(65);
      itemMeta.setRarity(ItemRarity.EPIC);
      itemMeta.setHideTooltip(true);
      CustomModelDataComponent customModelData = itemMeta.getCustomModelDataComponent();
      ArrayList<Float> customModelDataFloats = new ArrayList();
      customModelDataFloats.add(1.0F);
      customModelData.setFloats((List)Unsafe.castNonNull(customModelDataFloats));
      itemMeta.setCustomModelDataComponent(customModelData);
      itemMeta.setDamageResistant(DamageTypeTags.IS_EXPLOSION);
      itemMeta.setUnbreakable(true);
      ((Damageable)itemMeta).setDamage(2);
      ((Damageable)itemMeta).setMaxDamage(10);
      ((Repairable)itemMeta).setRepairCost(3);
      ToolComponent tool = itemMeta.getTool();
      tool.setDefaultMiningSpeed(1.5F);
      tool.setDamagePerBlock(2);
      tool.addRule(Material.STONE, 0.5F, true);
      itemMeta.setTool(tool);
      itemMeta.setEnchantmentGlintOverride(true);
      itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
      itemMeta.addEnchant(Enchantment.SHARPNESS, 2, true);
      itemMeta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(NamespacedKeyUtils.create("some_plugin", "attack-speed-bonus"), 2.0D, Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
      itemMeta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(NamespacedKeyUtils.create("some_plugin", "attack-speed-bonus-2"), 0.5D, Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.OFFHAND));
      itemMeta.addAttributeModifier(Attribute.MAX_HEALTH, new AttributeModifier(NamespacedKeyUtils.create("some_plugin", "max-health-bonus"), 2.0D, Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
      itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
      FoodComponent food = itemMeta.getFood();
      food.setNutrition(2);
      food.setSaturation(2.5F);
      food.setCanAlwaysEat(true);
      itemMeta.setFood(food);
      UseCooldownComponent useCooldown = itemMeta.getUseCooldown();
      useCooldown.setCooldownSeconds(1.5F);
      useCooldown.setCooldownGroup(NamespacedKeyUtils.create("plugin", "cooldown"));
      itemMeta.setUseCooldown(useCooldown);
      itemMeta.setUseRemainder(new ItemStack(Material.BONE));
      itemMeta.setEnchantable(15);
      itemMeta.setTooltipStyle(NamespacedKeyUtils.create("plugin", "tooltip-style"));
      itemMeta.setItemModel(NamespacedKeyUtils.create("plugin", "item-model"));
      itemMeta.setGlider(true);
      PersistentDataContainer customTags = itemMeta.getPersistentDataContainer();
      customTags.set(NamespacedKeyUtils.create("some_plugin", "some-key"), PersistentDataType.STRING, "some value");
      PersistentDataContainer customContainer = customTags.getAdapterContext().newPersistentDataContainer();
      customContainer.set(NamespacedKeyUtils.create("inner_plugin", "inner-key"), PersistentDataType.FLOAT, 0.3F);
      customTags.set(NamespacedKeyUtils.create("some_plugin", "some-other-key"), PersistentDataType.TAG_CONTAINER, customContainer);
      itemStack.setItemMeta(itemMeta);
      return itemStack;
   }

   private static ItemStack toCraftItemStackCopy(ItemStack itemStack) {
      return ItemMigration.migrateNonNullItemStack(itemStack);
   }

   private static ItemStack deserialize(ItemStack itemStack) {
      String serialized = ConfigUtils.toConfigYaml("item", itemStack);
      ItemStack deserialized = (ItemStack)ConfigUtils.fromConfigYaml(serialized, "item");
      return (ItemStack)Validate.State.notNull(deserialized, (Supplier)(() -> {
         return "Deserialized ItemStack is null! Original: " + String.valueOf(itemStack);
      }));
   }

   private void runAll() throws ServerAssumptionsTest.ServerAssumptionFailedException {
      this.testItemStackComparisons();
      this.testDeserializedItemStackComparisons();
      this.testInventoryItems();
   }

   private void testItemStackComparisons() throws ServerAssumptionsTest.ServerAssumptionFailedException {
      assumeIsSimilar(this.bukkitItemStack, this.bukkitItemStack, "Bukkit ItemStack#isSimilar(self)");
      assumeIsSimilar(this.craftItemStack, this.craftItemStack, "CraftItemStack#isSimilar(self)");
      assumeEquals(this.bukkitItemStack, this.bukkitItemStack, "Bukkit ItemStack#equals(self)");
      assumeEquals(this.craftItemStack, this.craftItemStack, "CraftItemStack#equals(self)");
      assumeIsSimilar(this.bukkitItemStack, this.bukkitItemStack2, "Bukkit ItemStack#isSimilar(other Bukkit ItemStack)");
      assumeIsSimilar(this.bukkitItemStack, this.craftItemStack, "Bukkit ItemStack#isSimilar(CraftItemStack)");
      assumeIsSimilar(this.craftItemStack, this.craftItemStack2, "CraftItemStack#isSimilar(other CraftItemStack)");
      assumeIsSimilar(this.craftItemStack, this.bukkitItemStack, "CraftItemStack#isSimilar(Bukkit ItemStack)");
      assumeEquals(this.bukkitItemStack, this.bukkitItemStack2, "Bukkit ItemStack#equals(other Bukkit ItemStack)");
      assumeEquals(this.bukkitItemStack, this.craftItemStack, "Bukkit ItemStack#equals(CraftItemStack)");
      assumeEquals(this.craftItemStack, this.craftItemStack2, "CraftItemStack#equals(other CraftItemStack)");
      assumeEquals(this.craftItemStack, this.bukkitItemStack, "CraftItemStack#equals(Bukkit ItemStack)");
   }

   private void testDeserializedItemStackComparisons() throws ServerAssumptionsTest.ServerAssumptionFailedException {
      assumeIsSimilar(this.bukkitItemStack, this.deserializedItemStack, "Bukkit ItemStack#isSimilar(Deserialized ItemStack)");
      assumeIsSimilar(this.craftItemStack, this.deserializedItemStack, "CraftItemStack#isSimilar(Deserialized ItemStack)");
      assumeIsSimilar(this.bukkitItemStack, this.deserializedCraftItemStack, "Bukkit ItemStack#isSimilar(Deserialized CraftItemStack)");
      assumeIsSimilar(this.craftItemStack, this.deserializedCraftItemStack, "CraftItemStack#isSimilar(Deserialized CraftItemStack)");
      assumeIsSimilar(this.deserializedItemStack, this.bukkitItemStack, "Deserialized ItemStack#isSimilar(Bukkit ItemStack)");
      assumeIsSimilar(this.deserializedItemStack, this.craftItemStack, "Deserialized ItemStack#isSimilar(CraftItemStack)");
      assumeIsSimilar(this.deserializedCraftItemStack, this.bukkitItemStack, "Deserialized CraftItemStack#isSimilar(Bukkit ItemStack)");
      assumeIsSimilar(this.deserializedCraftItemStack, this.craftItemStack, "Deserialized CraftItemStack#isSimilar(CraftItemStack)");
   }

   private void testInventoryItems() throws ServerAssumptionsTest.ServerAssumptionFailedException {
      Inventory inventory = Bukkit.createInventory((InventoryHolder)null, 9);
      ItemStack bukkitItemStack = new ItemStack(Material.STONE, 1);
      inventory.setItem(0, bukkitItemStack);
      bukkitItemStack.setAmount(2);
      ItemStack fromInventory = (ItemStack)Unsafe.assertNonNull(inventory.getItem(0));
      assumption(fromInventory.getAmount() == 1, "Inventory#setItem did not copy the ItemStack!");
      fromInventory.setAmount(3);
      ItemStack fromInventory2 = (ItemStack)Unsafe.assertNonNull(inventory.getItem(0));
      assumption(fromInventory2.getAmount() == 3, "Inventory#getItem did not return a live wrapper of the inventory's ItemStack!");
   }

   private static class ServerAssumptionFailedException extends Exception {
      private static final long serialVersionUID = 8174065546288633858L;

      public ServerAssumptionFailedException(String message) {
         super(Validate.notEmpty(message, "message is null or empty"));
      }
   }
}
