package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.DefaultShopTypes;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopType;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopEquipment;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopObject;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PositiveIntegerArgument;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

class CommandDebugCreateShops extends PlayerCommand {
   private static final String ARGUMENT_SHOP_COUNT = "shopCount";
   private static final String ARGUMENT_TEST_EQUIPMENT = "testEquipment";
   private final SKShopkeepersPlugin plugin;

   CommandDebugCreateShops(SKShopkeepersPlugin plugin) {
      super("debugCreateShops");
      this.plugin = plugin;
      this.setPermission("shopkeeper.debug");
      this.setDescription(Text.of("Creates lots of shopkeepers for stress testing."));
      this.setHiddenInParentHelp(true);
      this.addArgument((new PositiveIntegerArgument("shopCount")).orDefaultValue(10));
      this.addArgument((new LiteralArgument("testEquipment")).optional());
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      boolean testEquipment = context.has("testEquipment");
      if (testEquipment) {
         this.spawnEquipmentTest(player);
      } else {
         int shopCount = (Integer)context.get("shopCount");
         if (shopCount > 1000) {
            player.sendMessage(String.valueOf(ChatColor.RED) + "Shopkeeper count to high, limiting to 1000!");
            shopCount = 1000;
         }

         this.spawnCount(player, shopCount);
      }
   }

   private void spawnCount(Player player, int shopCount) {
      String var10001 = String.valueOf(ChatColor.GREEN);
      player.sendMessage(var10001 + "Creating up to " + shopCount + " shopkeepers, starting here!");
      int stepSize = true;
      BlockFace blockFace = BlockFaceUtils.BlockFaceDirections.CARDINAL.fromYaw(player.getEyeLocation().getYaw());
      AdminShopType<?> shopType = DefaultShopTypes.ADMIN_REGULAR();
      ShopObjectType<?> shopObjectType = (ShopObjectType)Unsafe.assertNonNull(DefaultShopObjectTypes.LIVING().get(EntityType.VILLAGER));
      int created = 0;
      Location currrentSpawnLocation = player.getLocation();

      for(int i = 0; i < shopCount; ++i) {
         Shopkeeper shopkeeper = this.plugin.handleShopkeeperCreation(AdminShopCreationData.create(player, (AdminShopType)shopType, shopObjectType, currrentSpawnLocation.clone(), (BlockFace)null));
         if (shopkeeper != null) {
            ++created;
         }

         currrentSpawnLocation.add((double)(2 * blockFace.getModX()), 0.0D, (double)(2 * blockFace.getModZ()));
      }

      var10001 = String.valueOf(ChatColor.GREEN);
      player.sendMessage(var10001 + "Done! Created " + String.valueOf(ChatColor.YELLOW) + created + String.valueOf(ChatColor.GREEN) + " shopkeepers!");
   }

   private void spawnEquipmentTest(Player player) {
      player.sendMessage(String.valueOf(ChatColor.GREEN) + "Creating a shopkeeper with equipment for each enabled mob type, starting here!");
      int stepSize = true;
      BlockFace blockFace = BlockFaceUtils.BlockFaceDirections.CARDINAL.fromYaw(player.getEyeLocation().getYaw());
      AdminShopType<?> shopType = DefaultShopTypes.ADMIN_REGULAR();
      int created = 0;
      Location currrentSpawnLocation = player.getLocation();
      Iterator var7 = DefaultShopObjectTypes.LIVING().getAll().iterator();

      while(var7.hasNext()) {
         ShopObjectType<?> shopObjectType = (ShopObjectType)var7.next();
         if (shopObjectType.isEnabled()) {
            Shopkeeper shopkeeper = this.plugin.handleShopkeeperCreation(AdminShopCreationData.create(player, (AdminShopType)shopType, shopObjectType, currrentSpawnLocation.clone(), (BlockFace)null));
            currrentSpawnLocation.add((double)(3 * blockFace.getModX()), 0.0D, (double)(3 * blockFace.getModZ()));
            if (shopkeeper != null) {
               ++created;
               LivingShopObject shopObject = (LivingShopObject)shopkeeper.getShopObject();
               LivingShopEquipment equipment = shopObject.getEquipment();
               equipment.setItem(EquipmentSlot.HAND, UnmodifiableItemStack.of(new ItemStack(Material.DIAMOND_SWORD)));
               equipment.setItem(EquipmentSlot.OFF_HAND, UnmodifiableItemStack.of(new ItemStack(Material.IRON_SWORD)));
               equipment.setItem(EquipmentSlot.HEAD, UnmodifiableItemStack.of(new ItemStack(Material.EMERALD_BLOCK)));
               equipment.setItem(EquipmentSlot.CHEST, UnmodifiableItemStack.of(new ItemStack(Material.DIAMOND_CHESTPLATE)));
               equipment.setItem(EquipmentSlot.LEGS, UnmodifiableItemStack.of(new ItemStack(Material.DIAMOND_LEGGINGS)));
               equipment.setItem(EquipmentSlot.FEET, UnmodifiableItemStack.of(new ItemStack(Material.DIAMOND_BOOTS)));
            }
         }
      }

      String var10001 = String.valueOf(ChatColor.GREEN);
      player.sendMessage(var10001 + "Done! Created " + String.valueOf(ChatColor.YELLOW) + created + String.valueOf(ChatColor.GREEN) + " shopkeepers!");
   }
}
