package com.bergerkiller.bukkit.tc.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.CommandManager;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Greedy;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Quoted;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.suggestion.Suggestions;
import com.bergerkiller.bukkit.common.dep.cloud.component.CommandComponent;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserParameters;
import com.bergerkiller.bukkit.common.dep.cloud.services.type.ConsumerService;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.common.dep.typetoken.TypeToken;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModelStore;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresMultiplePermissions;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.commands.annotations.SavedModelImplicitlyCreated;
import com.bergerkiller.bukkit.tc.commands.annotations.SavedModelRequiresAccess;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayer;
import com.bergerkiller.bukkit.tc.exception.IllegalNameException;
import com.bergerkiller.bukkit.tc.properties.SavedClaim;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainNameFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

@Command("train model")
public class ModelStoreCommands {
   @Suggestions("savedmodelmodules")
   public List<String> getSavedModelConfigModuleNames(CommandContext<CommandSender> context, String input) {
      TrainCarts plugin = (TrainCarts)context.inject(TrainCarts.class).get();
      return new ArrayList(plugin.getSavedAttachmentModels().getModuleNames());
   }

   @Suggestions("savedmodelname")
   public List<String> getSavedModelConfigNames(CommandContext<CommandSender> context, String input) {
      TrainCarts plugin = (TrainCarts)context.inject(TrainCarts.class).get();
      return plugin.getSavedAttachmentModels().getNames();
   }

   public void init(CommandManager<CommandSender> manager) {
      manager.registerCommandPostProcessor((postProcessContext) -> {
         CommandContext<CommandSender> context = postProcessContext.commandContext();
         Object raw_arg = context.getOrDefault("savedmodelname", (Object)null);
         if (raw_arg instanceof SavedAttachmentModel) {
            Stream var10000 = postProcessContext.command().components().stream().map(CommandComponent::parser);
            Objects.requireNonNull(ModelStoreCommands.SavedAttachmentModelParser.class);
            var10000 = var10000.filter(ModelStoreCommands.SavedAttachmentModelParser.class::isInstance);
            Objects.requireNonNull(ModelStoreCommands.SavedAttachmentModelParser.class);
            ModelStoreCommands.SavedAttachmentModelParser parser = (ModelStoreCommands.SavedAttachmentModelParser)var10000.map(ModelStoreCommands.SavedAttachmentModelParser.class::cast).findFirst().orElse((Object)null);
            if (parser != null) {
               SavedAttachmentModel savedModel = (SavedAttachmentModel)raw_arg;
               if (savedModel.isNone()) {
                  if (parser.isImplicitlyCreated()) {
                     TrainCarts plugin = (TrainCarts)context.inject(TrainCarts.class).get();

                     try {
                        savedModel = plugin.getSavedAttachmentModels().setConfigAsPlayer(savedModel.getName(), new ConfigurationNode(), (CommandSender)context.sender());
                        context.set("savedmodelname", savedModel);
                     } catch (IllegalNameException var8) {
                        Localization.COMMAND_MODEL_CONFIG_INVALID_NAME.message((CommandSender)context.sender(), new String[]{savedModel.getName()});
                        ConsumerService.interrupt();
                     }
                  } else {
                     Localization.COMMAND_MODEL_CONFIG_NOTFOUND.message((CommandSender)context.sender(), new String[]{savedModel.getName()});
                     ConsumerService.interrupt();
                  }
               } else if (parser.isMustHaveAccess()) {
                  CommandSender sender = (CommandSender)context.sender();
                  if (savedModel.hasPermission(sender)) {
                     return;
                  }

                  boolean force = context.flags().hasFlag("force");
                  if (!this.checkAccess(sender, savedModel, force)) {
                     ConsumerService.interrupt();
                  }
               }

            }
         }
      });
      manager.parserRegistry().registerAnnotationMapper(SavedModelRequiresAccess.class, (a, typeToken) -> {
         return ParserParameters.single(SavedModelRequiresAccess.PARAM, Boolean.TRUE);
      });
      manager.parserRegistry().registerAnnotationMapper(SavedModelImplicitlyCreated.class, (a, typeToken) -> {
         return ParserParameters.single(SavedModelImplicitlyCreated.PARAM, Boolean.TRUE);
      });
      manager.parserRegistry().registerParserSupplier(TypeToken.get(SavedAttachmentModel.class), (parameters) -> {
         boolean access = (Boolean)parameters.get(SavedModelRequiresAccess.PARAM, Boolean.FALSE);
         boolean implicitlyCreated = (Boolean)parameters.get(SavedModelImplicitlyCreated.PARAM, Boolean.FALSE);
         return new ModelStoreCommands.SavedAttachmentModelParser(access, implicitlyCreated);
      });
   }

   @Command("config")
   @CommandDescription("Shows command usage of /train model config, lists saved model configurations")
   @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST)
   private void commandUsage(CommandSender sender, TrainCarts plugin) {
      sender.sendMessage(ChatColor.YELLOW + "Use /train model config <modelname> [command] to modify saved models");
      sender.sendMessage(ChatColor.YELLOW + "Use /train model config list to list all models");
      this.commandShowInfo(sender, plugin, false, (String)null);
   }

   @Command("config <savedmodelname> info")
   @CommandDescription("Shows detailed information about a saved model configuration")
   @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST)
   private void commandShowInfo(CommandSender sender, @Argument("savedmodelname") SavedAttachmentModel savedModel) {
      MessageBuilder builder = new MessageBuilder();
      builder.newLine();
      builder.green(new Object[]{"Properties of saved model configuration '"}).white(new Object[]{savedModel.getName()}).green(new Object[]{"':"}).newLine();
      if (!savedModel.getModule().isDefault()) {
         builder.yellow(new Object[]{"Stored in module: "}).white(new Object[]{savedModel.getModule().getName()}).newLine();
      }

      builder.yellow(new Object[]{"Number of seats: "}).white(new Object[]{savedModel.getSeatCount()}).newLine();
      builder.yellow(new Object[]{"Claimed by: "});
      SavedClaim.buildClaimList(builder, savedModel.getClaims());
      builder.send(sender);
   }

   @Command("config <savedmodelname> defaultmodule")
   @CommandDescription("Moves a saved model configuration to the default module")
   @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST)
   private void commandSetDefaultModule(CommandSender sender, TrainCarts plugin, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel) {
      if (savedModel.getModule().isDefault()) {
         sender.sendMessage(ChatColor.YELLOW + "Model configuration '" + savedModel.getName() + "' is already stored in the default module");
      } else {
         plugin.getSavedAttachmentModels().setModuleNameOfModel(savedModel.getName(), (String)null);
         sender.sendMessage(ChatColor.GREEN + "Model configuration '" + savedModel.getName() + "' is now stored in the default module!");
      }

   }

   @Command("config <savedmodelname> module <newmodulename>")
   @CommandDescription("Moves a saved model configuration to a new or existing module")
   @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST)
   private void commandSetModule(CommandSender sender, TrainCarts plugin, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel, @Argument(value = "newmodulename",suggestions = "savedmodelmodules") String newModuleName) {
      if (newModuleName.isEmpty()) {
         this.commandSetDefaultModule(sender, plugin, savedModel);
      } else if (newModuleName.equals(savedModel.getModule().getName())) {
         sender.sendMessage(ChatColor.YELLOW + "Model configuration '" + savedModel.getName() + "' is already stored in module '" + newModuleName + "'");
      } else {
         plugin.getSavedAttachmentModels().setModuleNameOfModel(savedModel.getName(), newModuleName);
         sender.sendMessage(ChatColor.GREEN + "Model configuration '" + savedModel.getName() + "' is now stored in module '" + newModuleName + "'!");
      }

   }

   @Command("config <savedmodelname> export")
   @CommandDescription("Exports the saved model configuration configuration to a hastebin server")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_EXPORT)})
   private void commandExport(CommandSender sender, @Argument("savedmodelname") SavedAttachmentModel savedModel) {
      ConfigurationNode exportedConfig = savedModel.getConfig().clone();
      exportedConfig.remove("claims");
      Commands.exportModel(sender, savedModel.getName(), exportedConfig);
   }

   @Command("config <savedmodelname> rename <newsavedmodelname>")
   @CommandDescription("Renames a saved model configuration")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_RENAME)})
   private void commandRename(CommandSender sender, TrainCarts plugin, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel, @Quoted @Argument("newsavedmodelname") String newSavedModelName, @Flag("force") boolean force) {
      if (savedModel.getName().equals(newSavedModelName)) {
         sender.sendMessage(ChatColor.RED + "The new name is the same as the current name");
      } else if (checkSavePermissionsOverwrite(plugin, sender, newSavedModelName, force)) {
         String oldName = savedModel.getName();
         plugin.getSavedAttachmentModels().rename(oldName, newSavedModelName);
         sender.sendMessage(ChatColor.YELLOW + "saved model configuration '" + ChatColor.WHITE + oldName + ChatColor.YELLOW + "' has been renamed to '" + ChatColor.WHITE + newSavedModelName + ChatColor.YELLOW + "'!");
      }
   }

   @Command("config <savedmodelname> copy <targetsavedmodelname>")
   @CommandDescription("Copies an existing saved model configuration and saves it as the target saved model configuration name")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_COPY)})
   private void commandCopy(CommandSender sender, TrainCarts plugin, @Argument("savedmodelname") SavedAttachmentModel savedModel, @Quoted @Argument(value = "targetsavedmodelname",suggestions = "savedmodelname") String targetSavedModelName, @Flag("force") boolean force) {
      if (savedModel.getName().equals(targetSavedModelName)) {
         sender.sendMessage(ChatColor.RED + "The target name is the same as the source name");
      } else if (checkSavePermissionsOverwrite(plugin, sender, targetSavedModelName, force)) {
         try {
            plugin.getSavedAttachmentModels().setConfigAsPlayer(targetSavedModelName, savedModel.getConfig().clone(), sender);
         } catch (IllegalNameException var7) {
            Localization.COMMAND_MODEL_CONFIG_INVALID_NAME.message(sender, new String[]{targetSavedModelName});
            return;
         }

         sender.sendMessage(ChatColor.YELLOW + "saved model configuration '" + ChatColor.WHITE + savedModel.getName() + ChatColor.YELLOW + "' copied and saved as '" + ChatColor.WHITE + targetSavedModelName + ChatColor.YELLOW + "'!");
      }
   }

   @Command("config <savedmodelname> delete")
   @CommandDescription("Deletes a saved model configuration permanently")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_DELETE)})
   private void commandDelete(CommandSender sender, TrainCarts plugin, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel, @Flag("force") boolean force) {
      String name = savedModel.getName();
      if (plugin.getSavedAttachmentModels().remove(name)) {
         sender.sendMessage(ChatColor.YELLOW + "saved model configuration '" + ChatColor.WHITE + name + ChatColor.YELLOW + "' has been deleted!");
      } else {
         sender.sendMessage(ChatColor.RED + "saved model configuration '" + ChatColor.WHITE + name + ChatColor.RED + "' cannot be removed! (read-only)");
         sender.sendMessage(ChatColor.RED + "You can only override these properties by saving a new configuration");
      }

   }

   @Command("config <savedmodelname> claim")
   @CommandDescription("Claims a saved model configuration so that the player has exclusive access")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_CLAIM)})
   private void commandClaimSelf(Player sender, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel) {
      Set<SavedClaim> oldClaims = savedModel.getClaims();
      SavedClaim selfClaim = new SavedClaim(sender);
      if (oldClaims.contains(selfClaim)) {
         sender.sendMessage(ChatColor.RED + "You have already claimed this saved model configuration!");
      } else {
         Set<SavedClaim> newClaims = new HashSet(oldClaims);
         newClaims.add(selfClaim);
         updateClaimList(sender, savedModel, oldClaims, newClaims);
      }

   }

   @Command("config <savedmodelname> claim add <player>")
   @CommandDescription("Adds a claim to a saved model configuration so that the added player has exclusive access")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_CLAIM)})
   private void commandClaimAdd(CommandSender sender, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel, @Quoted @Argument(value = "player",suggestions = "playername") String player, @Flag("force") boolean force) {
      Set<SavedClaim> oldClaims = savedModel.getClaims();
      Set<SavedClaim> newClaims = new HashSet(oldClaims);
      Iterator var7 = SavedClaim.parseClaims(oldClaims, new String[]{player}).iterator();

      while(var7.hasNext()) {
         SavedClaim addedClaim = (SavedClaim)var7.next();
         if (!newClaims.add(addedClaim)) {
            sender.sendMessage(ChatColor.RED + "- Player " + addedClaim.description() + " was already on the claim list!");
         }
      }

      updateClaimList(sender, savedModel, oldClaims, newClaims);
   }

   @Command("config <savedmodelname> claim remove <player>")
   @CommandDescription("Removes a claim from a saved model configuration so that the player no longer has exclusive access")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_CLAIM)})
   private void commandClaimRemove(CommandSender sender, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel, @Quoted @Argument(value = "player",suggestions = "playername") String player, @Flag("force") boolean force) {
      Set<SavedClaim> oldClaims = savedModel.getClaims();
      Set<SavedClaim> newClaims = new HashSet(oldClaims);
      Iterator var7 = SavedClaim.parseClaims(oldClaims, new String[]{player}).iterator();

      while(var7.hasNext()) {
         SavedClaim removedClaim = (SavedClaim)var7.next();
         if (!newClaims.remove(removedClaim)) {
            sender.sendMessage(ChatColor.RED + "- Player " + removedClaim.description() + " was not on the claim list");
         }
      }

      updateClaimList(sender, savedModel, oldClaims, newClaims);
   }

   @Command("config <savedmodelname> claim clear")
   @CommandDescription("Clears all the claims set for the saved model configuration, allowing anyone to access it")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_CLAIM)})
   private void commandClaimClear(CommandSender sender, @SavedModelRequiresAccess @Argument("savedmodelname") SavedAttachmentModel savedModel, @Flag("force") boolean force) {
      Set<SavedClaim> oldClaims = savedModel.getClaims();
      updateClaimList(sender, savedModel, oldClaims, Collections.emptySet());
   }

   @Command("config <savedmodelname> import <url>")
   @CommandDescription("Imports a saved model configuration from an online hastebin server by url")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_IMPORT)})
   private void commandImport(CommandSender sender, TrainCarts plugin, @SavedModelRequiresAccess @SavedModelImplicitlyCreated @Argument("savedmodelname") SavedAttachmentModel savedModel, @Greedy @FlagYielding @Argument(value = "url",description = "The URL to a Hastebin-hosted paste to download from") String url, @Flag("force") boolean force) {
      Commands.importModel(plugin, sender, url, (config) -> {
         boolean isNewModel = savedModel.isEmpty();

         try {
            plugin.getSavedAttachmentModels().setConfigAsPlayer(savedModel.getName(), config, sender);
         } catch (IllegalNameException var6) {
            Localization.COMMAND_MODEL_CONFIG_INVALID_NAME.message(sender, new String[]{savedModel.getName()});
            return;
         }

         if (isNewModel) {
            sender.sendMessage(ChatColor.GREEN + "The model configuration was imported and saved as " + savedModel.getName());
         } else {
            sender.sendMessage(ChatColor.GREEN + "The model configuration was imported and saved as " + savedModel.getName() + ", a previous model was overwritten");
         }

      });
   }

   @Command("config <savedmodelname> edit")
   @CommandDescription("Switches the attachment editor to edit this model configuration")
   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST), @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_IMPORT)})
   private void commandEdit(TrainCartsPlayer player, TrainCarts plugin, @SavedModelRequiresAccess @SavedModelImplicitlyCreated @Argument("savedmodelname") SavedAttachmentModel savedModel, @Flag("force") boolean force) {
      boolean isNewModel = savedModel.isEmpty();

      try {
         plugin.getSavedAttachmentModels().setDefaultConfigIfMissing(savedModel.getName(), player.getOnlinePlayer());
      } catch (IllegalNameException var7) {
         Localization.COMMAND_MODEL_CONFIG_INVALID_NAME.message(player, savedModel.getName());
         return;
      }

      player.editModel(savedModel);
      if (isNewModel) {
         Localization.COMMAND_MODEL_CONFIG_EDIT_NEW.message(player, savedModel.getName());
      } else {
         Localization.COMMAND_MODEL_CONFIG_EDIT_EXISTING.message(player, savedModel.getName());
      }

   }

   @Command("config list")
   @CommandDescription("Lists all the model configurations that exist on the server that a player can modify")
   @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST)
   private void commandShowInfo(CommandSender sender, TrainCarts plugin, @Flag(value = "all",description = "Show all model configurations on this server, not just those owned by the player") boolean showAll, @Flag(value = "module",suggestions = "savedmodelmodules",description = "Selects a module to list the saved model configurations of") String moduleName) {
      SavedAttachmentModelStore module = plugin.getSavedAttachmentModels();
      MessageBuilder builder = new MessageBuilder();
      builder.newLine();
      if (moduleName != null) {
         module = module.getModule(moduleName);
         if (module == null) {
            sender.sendMessage(ChatColor.RED + "Module '" + moduleName + "' does not exist");
            return;
         }

         builder.blue(new Object[]{"The following saved model configurations are stored in module '" + moduleName + "':"});
      } else {
         builder.yellow(new Object[]{"The following saved model configurations are available:"});
      }

      builder.newLine().setSeparator(ChatColor.WHITE, " / ");
      Iterator var7 = module.getNames().iterator();

      while(var7.hasNext()) {
         String name = (String)var7.next();
         if (module.hasPermission(sender, name)) {
            builder.green(new Object[]{name});
         } else if (showAll) {
            builder.red(new Object[]{name});
         }
      }

      builder.send(sender);
   }

   @Command("config list modules")
   @CommandDescription("Lists all modules in which saved model configurations are saved")
   @CommandRequiresPermission(Permission.COMMAND_MODEL_CONFIG_LIST)
   private void commandListModules(CommandSender sender, TrainCarts plugin) {
      MessageBuilder builder = new MessageBuilder();
      builder.newLine();
      builder.blue(new Object[]{"The following modules are available:"});
      builder.newLine().setSeparator(ChatColor.WHITE, " / ");
      Iterator var4 = plugin.getSavedAttachmentModels().getModuleNames().iterator();

      while(var4.hasNext()) {
         String moduleName = (String)var4.next();
         builder.aqua(new Object[]{moduleName});
      }

      builder.send(sender);
   }

   private static void updateClaimList(CommandSender sender, SavedAttachmentModel savedModel, Set<SavedClaim> oldClaims, Set<SavedClaim> newClaims) {
      Iterator var4 = newClaims.iterator();

      SavedClaim claim;
      while(var4.hasNext()) {
         claim = (SavedClaim)var4.next();
         if (!oldClaims.contains(claim)) {
            sender.sendMessage(ChatColor.GREEN + "- Player " + claim.description() + " added to claim list");
         }
      }

      var4 = oldClaims.iterator();

      while(var4.hasNext()) {
         claim = (SavedClaim)var4.next();
         if (!newClaims.contains(claim)) {
            sender.sendMessage(ChatColor.YELLOW + "- Player " + claim.description() + " " + ChatColor.RED + "removed" + ChatColor.YELLOW + " from claim list");
         }
      }

      savedModel.setClaims(newClaims);
      MessageBuilder builder = new MessageBuilder();
      builder.newLine();
      if (newClaims.size() > 1) {
         builder.newLine();
      }

      builder.yellow(new Object[]{"saved model configuration '"}).white(new Object[]{savedModel.getName()}).yellow(new Object[]{"' is now claimed by: "});
      SavedClaim.buildClaimList(builder, newClaims);
      builder.send(sender);
   }

   @CommandRequiresPermission(Permission.COMMAND_MODEL_SEARCH)
   @Command("search")
   @CommandDescription("Shows a dialog with all resource pack item models that are available")
   private void commandSearchModels(TrainCarts plugin, TrainCartsPlayer player) {
      if (plugin.getModelListing().isEmpty()) {
         player.sendMessage(ChatColor.RED + "The currently configured resource pack does not have any custom item models");
      } else {
         plugin.getModelListing().buildDialog(player.getOnlinePlayer()).asCreativeMenu().setCompactingEnabled(player.getModelSearchCompactFolders()).show();
      }

   }

   @CommandRequiresPermission(Permission.COMMAND_MODEL_SEARCH)
   @Command("search-option compacting")
   @CommandDescription("Toggles whether compacting of folders is enabled for the model search dialog")
   private void commandToggleSearchModels(TrainCarts plugin, TrainCartsPlayer player) {
      this.commandSearchModels(plugin, player, !player.getModelSearchCompactFolders());
   }

   @CommandRequiresPermission(Permission.COMMAND_MODEL_SEARCH)
   @Command("search-option compacting <enabled>")
   @CommandDescription("Sets whether compacting of folders is enabled for the model search dialog")
   private void commandSearchModels(TrainCarts plugin, TrainCartsPlayer player, @Argument("enabled") boolean enabled) {
      player.setModelSearchCompactFolders(enabled);
      player.sendMessage(ChatColor.YELLOW + "Compacting of models in the search dialog: " + Localization.boolStr(enabled));
   }

   @CommandRequiresPermission(Permission.COMMAND_MODEL_SEARCH)
   @Command("search <query>")
   @CommandDescription("Shows a dialog with all resource pack item models that are available")
   private void commandSearchModelsQuery(TrainCarts plugin, Player player, @Argument("query") @Greedy String query) {
      if (plugin.getModelListing().isEmpty()) {
         player.sendMessage(ChatColor.RED + "The currently configured resource pack does not have any custom item models");
      } else {
         plugin.getModelListing().buildDialog(player).asCreativeMenu().query(query).show();
      }

   }

   public static boolean checkSavePermissionsOverwrite(TrainCarts plugin, CommandSender sender, String modelName, boolean force) {
      TrainNameFormat.VerifyResult verify = TrainNameFormat.verify(modelName);
      if (verify != TrainNameFormat.VerifyResult.OK) {
         verify.getModelMessage().message(sender, new String[]{modelName});
         return false;
      } else if (!plugin.getSavedAttachmentModels().containsModel(modelName)) {
         return true;
      } else {
         boolean isFromOtherPlayer = false;
         if (!plugin.getSavedAttachmentModels().hasPermission(sender, modelName)) {
            if (!Permission.COMMAND_MODEL_CONFIG_GLOBAL.has(sender)) {
               sender.sendMessage(ChatColor.RED + "You do not have permission to overwrite saved model configuration " + modelName);
               return false;
            }

            isFromOtherPlayer = true;
         }

         if (!force) {
            if (isFromOtherPlayer) {
               sender.sendMessage(ChatColor.RED + "The saved model configuration '" + modelName + "' already exists, and it is not yours!");
            } else {
               sender.sendMessage(ChatColor.RED + "The saved model configuration '" + modelName + "' already exists!");
            }

            sender.sendMessage(ChatColor.RED + "/train model config " + modelName + " info  -  View saved model details");
            sender.sendMessage(ChatColor.RED + "If you are sure you want to overwrite it, pass --force");
            return false;
         } else {
            return true;
         }
      }
   }

   public boolean checkAccess(CommandSender sender, SavedAttachmentModel savedModel, boolean force) {
      if (Permission.COMMAND_MODEL_CONFIG_GLOBAL.has(sender)) {
         if (force) {
            return true;
         } else {
            Localization.COMMAND_MODEL_CONFIG_FORCE.message(sender, new String[]{savedModel.getName()});
            return false;
         }
      } else {
         if (force) {
            Localization.COMMAND_MODEL_CONFIG_GLOBAL_NOPERM.message(sender, new String[0]);
         } else {
            Localization.COMMAND_MODEL_CONFIG_CLAIMED.message(sender, new String[]{savedModel.getName()});
         }

         return false;
      }
   }

   private static class SavedAttachmentModelParser implements ArgumentParser<CommandSender, SavedAttachmentModel>, Strings<CommandSender> {
      private final boolean mustHaveAccess;
      private final boolean implicitlyCreated;

      public SavedAttachmentModelParser(boolean mustHaveAccess, boolean implicitlyCreated) {
         this.mustHaveAccess = mustHaveAccess;
         this.implicitlyCreated = implicitlyCreated;
      }

      public boolean isMustHaveAccess() {
         return this.mustHaveAccess;
      }

      public boolean isImplicitlyCreated() {
         return this.implicitlyCreated;
      }

      @NonNull
      public ArgumentParseResult<SavedAttachmentModel> parse(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
         TrainCarts plugin = (TrainCarts)commandContext.inject(TrainCarts.class).get();
         String input = commandInput.lastRemainingToken();
         TrainNameFormat.VerifyResult verify = TrainNameFormat.verify(input);
         if (verify != TrainNameFormat.VerifyResult.OK) {
            return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, verify.getModelMessage(), new String[]{input}));
         } else {
            commandInput.readString();
            return ArgumentParseResult.success(plugin.getSavedAttachmentModels().getModelOrNone(input));
         }
      }

      @NonNull
      public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
         TrainCarts plugin = (TrainCarts)commandContext.inject(TrainCarts.class).get();
         String input = commandInput.lastRemainingToken();
         List filtered;
         if (input.isEmpty()) {
            filtered = plugin.getSavedAttachmentModels().getNames();
         } else {
            filtered = (List)plugin.getSavedAttachmentModels().getNames().stream().filter((s) -> {
               return s.startsWith(input);
            }).collect(Collectors.toList());
         }

         List<String> claimed = (List)filtered.stream().filter((name) -> {
            return plugin.getSavedAttachmentModels().hasPermission((CommandSender)commandContext.sender(), name);
         }).collect(Collectors.toList());
         return claimed.isEmpty() ? filtered : claimed;
      }
   }
}
