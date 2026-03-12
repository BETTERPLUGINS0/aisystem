package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.shopobjects.living.types.VillagerShop;
import com.nisovin.shopkeepers.util.bukkit.MerchantUtils;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.MerchantRecipe;
import org.checkerframework.checker.nullness.qual.Nullable;

class CommandReplaceAllWithVanillaVillagers extends Command {
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final Confirmations confirmations;

   CommandReplaceAllWithVanillaVillagers(SKShopkeepersPlugin plugin, SKShopkeeperRegistry shopkeeperRegistry, Confirmations confirmations) {
      super("replaceAllWithVanillaVillagers");
      this.plugin = plugin;
      this.shopkeeperRegistry = shopkeeperRegistry;
      this.confirmations = confirmations;
      this.setPermission("shopkeeper.debug");
      this.setDescription(Messages.commandDescriptionReplaceAllWithVanillaVillagers);
      this.setHiddenInParentHelp(true);
   }

   public boolean testPermission(CommandSender sender) {
      if (!super.testPermission(sender)) {
         return false;
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.remove-all.player") && PermissionUtils.hasPermission(sender, "shopkeeper.remove-all.admin");
      }
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      int shopsCount = this.shopkeeperRegistry.getAllShopkeepers().size();
      if (shopsCount == 0) {
         TextUtils.sendMessage(sender, Messages.noShopsFound);
      } else {
         this.confirmations.awaitConfirmation(sender, () -> {
            this.replaceAllShopsWithVillagers(sender);
         });
         TextUtils.sendMessage(sender, Messages.confirmReplaceAllShopsWithVanillaVillagers, "shopsCount", shopsCount);
         TextUtils.sendMessage(sender, Messages.confirmationRequired);
      }
   }

   private void replaceAllShopsWithVillagers(CommandSender sender) {
      List<? extends AbstractShopkeeper> shopkeepers = new ArrayList(this.shopkeeperRegistry.getAllShopkeepers());
      if (shopkeepers.isEmpty()) {
         TextUtils.sendMessage(sender, Messages.noShopsFound);
      } else {
         int invalidShops = 0;
         int deletedAdminShopsCount = 0;
         int deletedPlayerShopsCount = 0;
         int skippedShopsCount = 0;
         Iterator var7 = shopkeepers.iterator();

         while(var7.hasNext()) {
            AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var7.next();
            if (!shopkeeper.isValid()) {
               ++invalidShops;
            } else if (!this.spawnVanillaVillager(sender, shopkeeper)) {
               ++skippedShopsCount;
            } else {
               shopkeeper.delete();
               if (shopkeeper instanceof PlayerShopkeeper) {
                  ++deletedPlayerShopsCount;
               } else {
                  ++deletedAdminShopsCount;
               }
            }
         }

         this.plugin.getShopkeeperStorage().save();
         if (invalidShops > 0) {
            TextUtils.sendMessage(sender, Messages.shopsAlreadyRemoved, "shopsCount", invalidShops);
         }

         TextUtils.sendMessage(sender, Messages.allShopsReplacedWithVanillaVillagers, "adminShopsCount", deletedAdminShopsCount, "playerShopsCount", deletedPlayerShopsCount, "skippedShopsCount", skippedShopsCount);
      }
   }

   private boolean spawnVanillaVillager(CommandSender sender, AbstractShopkeeper shopkeeper) {
      assert shopkeeper.isValid();

      if (shopkeeper.isVirtual()) {
         String message = shopkeeper.getLogPrefix() + "Skipping virtual shopkeeper.";
         if (!(sender instanceof ConsoleCommandSender)) {
            Log.debug(message);
         }

         TextUtils.sendMessage(sender, message);
         return false;
      } else {
         Location spawnLocation = shopkeeper.getLocation();
         if (spawnLocation == null) {
            String var10000 = shopkeeper.getLogPrefix();
            String message = var10000 + "Skipping shopkeeper without location. Is world '" + shopkeeper.getWorldName() + "' loaded?";
            if (!(sender instanceof ConsoleCommandSender)) {
               Log.debug(message);
            }

            TextUtils.sendMessage(sender, message);
            return false;
         } else {
            spawnLocation.add(0.5D, 0.0D, 0.5D);
            AbstractShopObject shopObject = shopkeeper.getShopObject();
            boolean isShopkeeperSpawned = shopObject.isSpawned();
            Class<? extends Entity> entityClass = (Class)Unsafe.assertNonNull(EntityType.VILLAGER.getEntityClass());
            World world = (World)Unsafe.assertNonNull(spawnLocation.getWorld());

            try {
               shopObject.despawn();
               world.spawn(spawnLocation, entityClass, (entity) -> {
                  assert entity != null;

                  this.prepareEntity((Villager)entity, shopkeeper);
                  this.plugin.getForcingEntitySpawner().forceEntitySpawn(spawnLocation, EntityType.VILLAGER);
               });
               return true;
            } catch (Exception var10) {
               String message = shopkeeper.getLogPrefix() + "Failed to spawn corresponding vanilla villager.";
               if (!(sender instanceof ConsoleCommandSender)) {
                  Log.debug((Throwable)var10, (Supplier)(() -> {
                     return message;
                  }));
               }

               TextUtils.sendMessage(sender, message);
               if (isShopkeeperSpawned) {
                  shopObject.spawn();
               }

               return false;
            }
         }
      }
   }

   private void prepareEntity(Villager entity, Shopkeeper shopkeeper) {
      this.applyName(entity, shopkeeper.getName());
      EntityEquipment equipment = entity.getEquipment();
      if (equipment != null) {
         equipment.clear();
      }

      entity.setRemoveWhenFarAway(false);
      entity.setCanPickupItems(false);
      entity.setInvulnerable(true);
      entity.setAdult();
      entity.setBreed(false);
      entity.setAgeLock(true);
      entity.setAI(false);
      if (Settings.silenceShopEntities) {
         entity.setSilent(true);
      }

      if (Settings.disableGravity) {
         entity.setGravity(false);
      }

      Compat.getProvider().setOnGround(entity, true);
      entity.setVillagerExperience(1);
      if (shopkeeper.getShopObject() instanceof VillagerShop) {
         VillagerShop villagerShop = (VillagerShop)shopkeeper.getShopObject();
         entity.setProfession(villagerShop.getProfession());
         entity.setVillagerType(villagerShop.getVillagerType());
         entity.setVillagerLevel(villagerShop.getVillagerLevel());
      }

      this.applyTradingRecipes(entity, shopkeeper);
   }

   private void applyName(Villager entity, @Nullable String name) {
      if (Settings.showNameplates && name != null && !name.isEmpty()) {
         String preparedName = Messages.nameplatePrefix + name;
         entity.setCustomName(preparedName);
         entity.setCustomNameVisible(Settings.alwaysShowNameplates);
      } else {
         entity.setCustomName((String)null);
         entity.setCustomNameVisible(false);
      }

   }

   private void applyTradingRecipes(Villager entity, Shopkeeper shopkeeper) {
      List<? extends TradingRecipe> tradingRecipes = shopkeeper.getTradingRecipes((Player)null);
      List<MerchantRecipe> merchantRecipes = MerchantUtils.createMerchantRecipes(tradingRecipes);
      entity.setRecipes(merchantRecipes);
   }
}
