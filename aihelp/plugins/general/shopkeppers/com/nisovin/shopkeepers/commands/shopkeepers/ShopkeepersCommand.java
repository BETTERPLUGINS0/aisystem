package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.DefaultShopTypes;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopType;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopType;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.arguments.ShopObjectTypeArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopTypeArgument;
import com.nisovin.shopkeepers.commands.lib.BaseCommand;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.CommandRegistry;
import com.nisovin.shopkeepers.commands.lib.NoPermissionException;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.shopkeepers.snapshot.CommandSnapshot;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopcreation.ShopkeeperPlacement;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class ShopkeepersCommand extends BaseCommand {
   private static final String COMMAND_NAME = "shopkeeper";
   private static final String ARGUMENT_SHOP_TYPE = "shop-type";
   private static final String ARGUMENT_OBJECT_TYPE = "object-type";
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;

   public ShopkeepersCommand(SKShopkeepersPlugin plugin, Confirmations confirmations) {
      super(plugin, "shopkeeper");
      this.plugin = plugin;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
      this.setDescription(Messages.commandDescriptionShopkeeper);
      this.setHelpTitleFormat(Messages.commandHelpTitle.setPlaceholderArguments("version", plugin.getDescription().getVersion()));
      this.setHelpUsageFormat(Messages.commandHelpUsageFormat);
      this.setHelpDescFormat(Messages.commandHelpDescriptionFormat);
      this.addArgument((new ShopTypeArgument("shop-type")).optional());
      this.addArgument((new ShopObjectTypeArgument("object-type")).optional());
      CommandRegistry childCommands = this.getChildCommands();
      childCommands.register(new CommandHelp((Command)Unsafe.initialized(this)));
      childCommands.register(new CommandReload(plugin));
      childCommands.register(new CommandDebug());
      childCommands.register(new CommandNotify());
      childCommands.register(new CommandList(this.shopkeeperRegistry));
      childCommands.register(new CommandHistory(plugin));
      childCommands.register(new CommandRemove(confirmations));
      childCommands.register(new CommandRemoveAll(plugin, this.shopkeeperRegistry, confirmations));
      childCommands.register(new CommandGive());
      childCommands.register(new CommandGiveCurrency());
      childCommands.register(new CommandSetCurrency());
      childCommands.register(new CommandUpdateItems());
      childCommands.register(new CommandRemote());
      childCommands.register(new CommandEdit());
      childCommands.register(new CommandTeleport());
      childCommands.register(new CommandTransfer());
      childCommands.register(new CommandSetTradePerm());
      childCommands.register(new CommandSetTradedCommand());
      childCommands.register(new CommandSetForHire());
      childCommands.register(new CommandSnapshot(confirmations));
      childCommands.register(new CommandEditVillager());
      childCommands.register(new CommandConfirm(confirmations));
      childCommands.register(new CommandReplaceAllWithVanillaVillagers(plugin, this.shopkeeperRegistry, confirmations));
      childCommands.register(new CommandDeleteUnspawnableShopkeepers(this.shopkeeperRegistry, confirmations));
      childCommands.register(new CommandCleanupCitizenShopkeepers());
      childCommands.register(new CommandCheck(plugin));
      childCommands.register(new CommandCheckItem());
      childCommands.register(new CommandYaml());
      childCommands.register(new CommandDebugCreateShops(plugin));
      childCommands.register(new CommandTestDamage(plugin));
      childCommands.register(new CommandTestSpawn(plugin));
   }

   public boolean testPermission(CommandSender sender) {
      return !super.testPermission(sender) ? false : PermissionUtils.hasPermission(sender, "shopkeeper.create");
   }

   protected NoPermissionException noPermissionException() {
      return new NoPermissionException(Messages.commandCreateNoPermission);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      Player player = (Player)ObjectUtils.castOrNull(input.getSender(), Player.class);
      if (player == null) {
         throw PlayerCommand.createCommandSourceRejectedException(input.getSender());
      } else {
         Block playerBlock = player.getEyeLocation().getBlock();
         FluidCollisionMode fluidCollisionMode;
         if (playerBlock.isLiquid()) {
            fluidCollisionMode = FluidCollisionMode.NEVER;
         } else {
            fluidCollisionMode = FluidCollisionMode.ALWAYS;
         }

         RayTraceResult targetBlockInfo = player.rayTraceBlocks(10.0D, fluidCollisionMode);
         if (targetBlockInfo == null) {
            TextUtils.sendMessage(player, (Text)Messages.mustTargetBlock);
         } else {
            Block targetBlock = (Block)Unsafe.assertNonNull(targetBlockInfo.getHitBlock());

            assert !targetBlock.isEmpty();

            BlockFace targetBlockFace = (BlockFace)Unsafe.assertNonNull(targetBlockInfo.getHitBlockFace());
            ShopType<?> shopType = (ShopType)context.getOrNull("shop-type");
            ShopObjectType<?> shopObjectType = (ShopObjectType)context.getOrNull("object-type");
            boolean containerTargeted = ItemUtils.isContainer(targetBlock.getType());
            if (containerTargeted) {
               if (shopType == null) {
                  shopType = (ShopType)this.plugin.getShopTypeRegistry().getDefaultSelection(player);
               }
            } else if (shopType == null) {
               shopType = DefaultShopTypes.ADMIN_REGULAR();
            }

            if (shopObjectType == null) {
               shopObjectType = (ShopObjectType)this.plugin.getShopObjectTypeRegistry().getDefaultSelection(player);
            }

            if (shopType != null && shopObjectType != null) {
               assert shopType != null && shopObjectType != null;

               boolean isPlayerShopType = shopType instanceof PlayerShopType;
               if (isPlayerShopType && !containerTargeted) {
                  TextUtils.sendMessage(player, (Text)Messages.mustTargetContainer);
               } else {
                  ShopkeeperPlacement shopkeeperPlacement = this.plugin.getShopkeeperCreation().getShopkeeperPlacement();
                  Location spawnLocation = shopkeeperPlacement.determineSpawnLocation(player, targetBlock, targetBlockFace);
                  Object shopCreationData;
                  if (isPlayerShopType) {
                     shopCreationData = PlayerShopCreationData.create(player, (PlayerShopType)shopType, shopObjectType, spawnLocation, targetBlockFace, targetBlock);
                  } else {
                     shopCreationData = AdminShopCreationData.create(player, (AdminShopType)shopType, shopObjectType, spawnLocation, targetBlockFace);
                  }

                  assert shopCreationData != null;

                  this.plugin.handleShopkeeperCreation((ShopCreationData)shopCreationData);
               }
            } else {
               TextUtils.sendMessage(player, (Text)Messages.noPermission);
            }
         }
      }
   }
}
