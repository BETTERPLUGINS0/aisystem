package github.nighter.smartspawner.commands.give;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import github.nighter.smartspawner.spawner.item.SpawnerItemFactory;
import github.nighter.smartspawner.utils.DynamicEntityValidator;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GiveSubCommand extends BaseSubCommand {
   private final SpawnerItemFactory spawnerItemFactory;
   private final List<String> supportedMobs;
   private static final int MAX_AMOUNT = 6400;

   public GiveSubCommand(SmartSpawner plugin) {
      super(plugin);
      this.spawnerItemFactory = plugin.getSpawnerItemFactory();
      this.supportedMobs = (List)DynamicEntityValidator.getValidEntities().stream().map(Enum::name).sorted().collect(Collectors.toList());
   }

   public String getName() {
      return "give";
   }

   public String getPermission() {
      return "smartspawner.command.give";
   }

   public String getDescription() {
      return "Give spawners to players";
   }

   public LiteralArgumentBuilder<CommandSourceStack> build() {
      LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName());
      builder.requires((source) -> {
         return this.hasPermission(source.getSender());
      });
      builder.then(this.buildRegularGiveCommand());
      builder.then(this.buildVanillaGiveCommand());
      builder.then(this.buildItemSpawnerGiveCommand());
      return builder;
   }

   private LiteralArgumentBuilder<CommandSourceStack> buildRegularGiveCommand() {
      return (LiteralArgumentBuilder)Commands.literal("spawner").then(Commands.argument("player", ArgumentTypes.player()).then(((RequiredArgumentBuilder)Commands.argument("mobType", StringArgumentType.word()).suggests(this.createMobSuggestions()).executes((context) -> {
         return this.executeGive(context, false, 1);
      })).then(Commands.argument("amount", IntegerArgumentType.integer(1, 6400)).executes((context) -> {
         return this.executeGive(context, false, IntegerArgumentType.getInteger(context, "amount"));
      }))));
   }

   private LiteralArgumentBuilder<CommandSourceStack> buildVanillaGiveCommand() {
      return (LiteralArgumentBuilder)Commands.literal("vanilla_spawner").then(Commands.argument("player", ArgumentTypes.player()).then(((RequiredArgumentBuilder)Commands.argument("mobType", StringArgumentType.word()).suggests(this.createMobSuggestions()).executes((context) -> {
         return this.executeGive(context, true, 1);
      })).then(Commands.argument("amount", IntegerArgumentType.integer(1, 6400)).executes((context) -> {
         return this.executeGive(context, true, IntegerArgumentType.getInteger(context, "amount"));
      }))));
   }

   private LiteralArgumentBuilder<CommandSourceStack> buildItemSpawnerGiveCommand() {
      return (LiteralArgumentBuilder)Commands.literal("item_spawner").then(Commands.argument("player", ArgumentTypes.player()).then(((RequiredArgumentBuilder)Commands.argument("itemType", StringArgumentType.word()).suggests(this.createItemSuggestions()).executes((context) -> {
         return this.executeGiveItemSpawner(context, 1);
      })).then(Commands.argument("amount", IntegerArgumentType.integer(1, 6400)).executes((context) -> {
         return this.executeGiveItemSpawner(context, IntegerArgumentType.getInteger(context, "amount"));
      }))));
   }

   private SuggestionProvider<CommandSourceStack> createMobSuggestions() {
      return (context, builder) -> {
         String input = builder.getRemaining().toLowerCase();
         Stream var10000 = this.supportedMobs.stream().map(String::toLowerCase).filter((mob) -> {
            return mob.startsWith(input);
         });
         Objects.requireNonNull(builder);
         var10000.forEach(builder::suggest);
         return builder.buildFuture();
      };
   }

   private SuggestionProvider<CommandSourceStack> createItemSuggestions() {
      return (context, builder) -> {
         String input = builder.getRemaining().toLowerCase();
         Stream var10000 = this.plugin.getItemSpawnerSettingsConfig().getValidItemSpawnerMaterials().stream().map((material) -> {
            return material.name().toLowerCase();
         }).filter((item) -> {
            return item.startsWith(input);
         });
         Objects.requireNonNull(builder);
         var10000.forEach(builder::suggest);
         return builder.buildFuture();
      };
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      return 0;
   }

   private int executeGive(CommandContext<CommandSourceStack> context, boolean isVanilla, int amount) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
      this.logCommandExecution(context);

      try {
         PlayerSelectorArgumentResolver playerSelector = (PlayerSelectorArgumentResolver)context.getArgument("player", PlayerSelectorArgumentResolver.class);
         List<Player> players = (List)playerSelector.resolve((CommandSourceStack)context.getSource());
         if (players.isEmpty()) {
            this.plugin.getMessageService().sendMessage(sender, "command_give_player_not_found");
            return 0;
         } else {
            Player target = (Player)players.get(0);
            String mobType = StringArgumentType.getString(context, "mobType");
            if (!this.supportedMobs.contains(mobType.toUpperCase())) {
               this.plugin.getMessageService().sendMessage(sender, "command_give_invalid_mob_type");
               return 0;
            } else {
               EntityType entityType = EntityType.valueOf(mobType.toUpperCase());
               ItemStack spawnerItem;
               if (isVanilla) {
                  spawnerItem = this.spawnerItemFactory.createVanillaSpawnerItem(entityType, amount);
               } else {
                  spawnerItem = this.spawnerItemFactory.createSmartSpawnerItem(entityType, amount);
               }

               if (target.getInventory().firstEmpty() == -1) {
                  target.getWorld().dropItem(target.getLocation(), spawnerItem);
                  this.plugin.getMessageService().sendMessage(target, "command_give_inventory_full");
               } else {
                  target.getInventory().addItem(new ItemStack[]{spawnerItem});
               }

               target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
               String entityName = this.plugin.getLanguageManager().getFormattedMobName(entityType);
               String smallCapsEntityName = this.plugin.getLanguageManager().getSmallCaps(entityName);
               HashMap<String, String> senderPlaceholders = new HashMap();
               senderPlaceholders.put("player", target.getName());
               senderPlaceholders.put("entity", entityName);
               senderPlaceholders.put("ᴇɴᴛɪᴛʏ", smallCapsEntityName);
               senderPlaceholders.put("amount", String.valueOf(amount));
               HashMap<String, String> targetPlaceholders = new HashMap();
               targetPlaceholders.put("amount", String.valueOf(amount));
               targetPlaceholders.put("entity", entityName);
               targetPlaceholders.put("ᴇɴᴛɪᴛʏ", smallCapsEntityName);
               String messageKey = "command_give_spawner_";
               this.plugin.getMessageService().sendMessage((CommandSender)sender, messageKey + "given", senderPlaceholders);
               this.plugin.getMessageService().sendMessage((Player)target, messageKey + "received", targetPlaceholders);
               return 1;
            }
         }
      } catch (Exception var16) {
         this.plugin.getLogger().severe("Error executing give command: " + var16.getMessage());
         return 0;
      }
   }

   private int executeGiveItemSpawner(CommandContext<CommandSourceStack> context, int amount) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
      this.logCommandExecution(context);

      try {
         PlayerSelectorArgumentResolver playerSelector = (PlayerSelectorArgumentResolver)context.getArgument("player", PlayerSelectorArgumentResolver.class);
         List<Player> players = (List)playerSelector.resolve((CommandSourceStack)context.getSource());
         if (players.isEmpty()) {
            this.plugin.getMessageService().sendMessage(sender, "command_give_player_not_found");
            return 0;
         } else {
            Player target = (Player)players.get(0);
            String itemType = StringArgumentType.getString(context, "itemType");

            Material itemMaterial;
            try {
               itemMaterial = Material.valueOf(itemType.toUpperCase());
            } catch (IllegalArgumentException var15) {
               this.plugin.getMessageService().sendMessage(sender, "command_give_invalid_item_type");
               return 0;
            }

            if (!this.plugin.getItemSpawnerSettingsConfig().isValidItemSpawner(itemMaterial)) {
               this.plugin.getMessageService().sendMessage(sender, "command_give_invalid_item_spawner");
               return 0;
            } else {
               ItemStack spawnerItem = this.spawnerItemFactory.createItemSpawnerItem(itemMaterial, amount);
               if (target.getInventory().firstEmpty() == -1) {
                  target.getWorld().dropItem(target.getLocation(), spawnerItem);
                  this.plugin.getMessageService().sendMessage(target, "command_give_inventory_full");
               } else {
                  target.getInventory().addItem(new ItemStack[]{spawnerItem});
               }

               target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
               String itemName = this.plugin.getLanguageManager().getVanillaItemName(itemMaterial);
               String smallCapsItemName = this.plugin.getLanguageManager().getSmallCaps(itemName);
               HashMap<String, String> senderPlaceholders = new HashMap();
               senderPlaceholders.put("player", target.getName());
               senderPlaceholders.put("entity", itemName);
               senderPlaceholders.put("ᴇɴᴛɪᴛʏ", smallCapsItemName);
               senderPlaceholders.put("amount", String.valueOf(amount));
               HashMap<String, String> targetPlaceholders = new HashMap();
               targetPlaceholders.put("amount", String.valueOf(amount));
               targetPlaceholders.put("entity", itemName);
               targetPlaceholders.put("ᴇɴᴛɪᴛʏ", smallCapsItemName);
               String messageKey = "command_give_spawner_";
               this.plugin.getMessageService().sendMessage((CommandSender)sender, messageKey + "given", senderPlaceholders);
               this.plugin.getMessageService().sendMessage((Player)target, messageKey + "received", targetPlaceholders);
               return 1;
            }
         }
      } catch (Exception var16) {
         this.plugin.getLogger().severe("Error executing give item spawner command: " + var16.getMessage());
         return 0;
      }
   }
}
