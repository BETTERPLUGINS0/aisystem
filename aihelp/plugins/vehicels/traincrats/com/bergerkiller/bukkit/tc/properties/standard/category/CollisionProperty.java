package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.CollisionMode;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionMobCategory;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionOptions;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class CollisionProperty extends FieldBackedStandardTrainProperty<CollisionOptions> {
   public void appendCollisionInfo(MessageBuilder builder, TrainProperties properties) {
      CollisionOptions opt = properties.getCollision();
      builder.yellow(new Object[]{"Collision rules for the train:"});
      this.appendCollisionMode(builder, opt.blockMode(), "blocks");
      if (TCConfig.collisionIgnoreGlobalOwners) {
         this.appendCollisionMode(builder, CollisionMode.DEFAULT, "administrators");
         builder.newLine().white(new Object[]{"      collision.ignoreGlobalOwners = true "}).blue(new Object[]{"[config.yml]"});
      }

      if (TCConfig.collisionIgnoreOwners) {
         this.appendCollisionMode(builder, CollisionMode.DEFAULT, "owners of this train");
         builder.newLine().white(new Object[]{"      collision.ignoreOwners = true "}).blue(new Object[]{"[config.yml]"});
      }

      this.appendCollisionMode(builder, opt.playerMode(), !TCConfig.collisionIgnoreGlobalOwners && !TCConfig.collisionIgnoreOwners ? "players" : "other players");
      this.appendCollisionMode(builder, opt.trainMode(), "other trains");
      Iterator var4 = opt.mobModes().entrySet().iterator();

      while(var4.hasNext()) {
         Entry<CollisionMobCategory, CollisionMode> entry = (Entry)var4.next();
         this.appendCollisionMode(builder, (CollisionMode)entry.getValue(), ((CollisionMobCategory)entry.getKey()).getPluralMobType());
      }

      this.appendCollisionMode(builder, opt.miscMode(), "miscellaneous entities");
   }

   private void appendCollisionMode(MessageBuilder builder, CollisionMode mode, String who) {
      builder.newLine().yellow(new Object[]{" - "}).red(new Object[]{mode.getOperationName()}).yellow(new Object[]{" "}).blue(new Object[]{who});
   }

   @CommandTargetTrain
   @PropertyCheckPermission("collision")
   @Command("train collision default|true")
   @CommandDescription("Configures the default collision settings")
   private void trainSetCollisionDefault(CommandSender sender, TrainProperties properties) {
      properties.setCollision(CollisionOptions.DEFAULT);
      this.trainGetCollisionInfo(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("collision")
   @Command("train collision none|false")
   @CommandDescription("Disables collision with all entities and blocks")
   private void trainSetCollisionNone(CommandSender sender, TrainProperties properties) {
      properties.setCollision(CollisionOptions.CANCEL);
      this.trainGetCollisionInfo(sender, properties);
   }

   @Command("train collision")
   @CommandDescription("Gets all collision rules configured for a train")
   private void trainGetCollisionInfo(CommandSender sender, TrainProperties properties) {
      MessageBuilder builder = new MessageBuilder();
      this.appendCollisionInfo(builder, properties);
      builder.send(sender);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("mobcollision")
   @Command("train collision <mobcategory> <mode>")
   @CommandDescription("Sets new behavior when colliding with a given mob category")
   private void trainSetMobCollision(CommandSender sender, TrainProperties properties, @Argument("mobcategory") CollisionMobCategory category, @Argument("mode") CollisionMode mode) {
      properties.setCollisionMode(category, mode);
      this.trainGetMobCollision(sender, properties, category);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("mobcollision")
   @Command("train collision mobs|mob <mode>")
   @CommandDescription("Sets new behavior when colliding with all types of mob")
   private void trainSetAllMobCollision(CommandSender sender, TrainProperties properties, @Argument("mode") CollisionMode mode) {
      properties.setCollisionModeForMobs(mode);
      showMode(sender, "mobs", mode);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("mobcollision")
   @Command("train collision <mobcategory> none")
   @CommandDescription("Resets behavior when colliding with a given mob category")
   private void trainResetMobCollision(CommandSender sender, TrainProperties properties, @Argument("mobcategory") CollisionMobCategory category) {
      properties.setCollisionMode((CollisionMobCategory)category, (CollisionMode)null);
      this.trainGetMobCollision(sender, properties, category);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("mobcollision")
   @Command("train collision mobs|mob none")
   @CommandDescription("Resets behavior when colliding with any type of mob")
   private void trainResetAllMobsCollision(CommandSender sender, TrainProperties properties) {
      properties.setCollisionModeForMobs((CollisionMode)null);
      sender.sendMessage(ChatColor.YELLOW + "Reset collision rules for all mob types. Will default to misc.");
   }

   @Command("train collision <mobcategory>")
   @CommandDescription("Gets the current behavior when colliding with a given mob category")
   private void trainGetMobCollision(CommandSender sender, TrainProperties properties, @Argument("mobcategory") CollisionMobCategory category) {
      CollisionMode mode = properties.getCollision().mobMode(category);
      if (mode == null) {
         sender.sendMessage(ChatColor.YELLOW + "The train has no specific mob collision mode set");
         sender.sendMessage(ChatColor.YELLOW + "Other mob collision rules might be set. If none are, behavior defaults to what is set for misc: ");
         showMode(sender, category.getPluralMobType(), properties.getCollision().miscMode());
      } else {
         showMode(sender, category.getPluralMobType(), mode);
      }

   }

   @Command("train collision mobs|mob")
   @CommandDescription("Gets the current behavior when colliding with a given mob category")
   private void trainGetAllMobCollision(CommandSender sender, TrainProperties properties) {
      CollisionOptions options = properties.getCollision();
      if (options.mobModes().isEmpty()) {
         sender.sendMessage(ChatColor.YELLOW + "The train has no specific mob collision modes set");
         sender.sendMessage(ChatColor.YELLOW + "Behavior will default to what is set for misc: ");
         showMode(sender, "mobs", options.miscMode());
      } else {
         CollisionMode foundMode = null;
         boolean hasNonMobModes = false;
         CollisionMobCategory[] var6 = CollisionMobCategory.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CollisionMobCategory category = var6[var8];
            CollisionMode modeForMob = options.mobMode(category);
            if (category.isMobCategory()) {
               if (modeForMob == null || foundMode != null && foundMode != modeForMob) {
                  foundMode = null;
                  break;
               }

               if (foundMode == null) {
                  foundMode = modeForMob;
               }
            } else if (modeForMob != null) {
               hasNonMobModes = true;
               showMode(sender, category.getPluralMobType(), modeForMob);
            }
         }

         if (foundMode != null) {
            showMode(sender, hasNonMobModes ? "other mobs" : "mobs", foundMode);
         } else {
            Iterator var11 = options.mobModes().entrySet().iterator();

            while(var11.hasNext()) {
               Entry<CollisionMobCategory, CollisionMode> mode = (Entry)var11.next();
               if (((CollisionMobCategory)mode.getKey()).isMobCategory()) {
                  showMode(sender, ((CollisionMobCategory)mode.getKey()).getPluralMobType(), (CollisionMode)mode.getValue());
               }
            }

         }
      }
   }

   @CommandTargetTrain
   @PropertyCheckPermission("blockcollision")
   @Command("train collision block <mode>")
   @CommandDescription("Sets the behavior of the train when colliding with blocks")
   private void trainSetBlockCollision(CommandSender sender, TrainProperties properties, @Argument("mode") CollisionMode mode) {
      properties.setCollision(properties.getCollision().cloneAndSetBlockMode(mode));
      this.trainGetBlockCollision(sender, properties);
   }

   @Command("train collision block")
   @CommandDescription("Gets the behavior of the train when colliding with blocks")
   private void trainGetBlockCollision(CommandSender sender, TrainProperties properties) {
      showMode(sender, "blocks", properties.getCollision().blockMode());
   }

   @CommandTargetTrain
   @PropertyCheckPermission("playercollision")
   @Command("train collision player <mode>")
   @CommandDescription("Sets the behavior of the train when colliding with players")
   private void trainSetPlayerCollision(CommandSender sender, TrainProperties properties, @Argument("mode") CollisionMode mode) {
      properties.setCollision(properties.getCollision().cloneAndSetPlayerMode(mode));
      this.trainGetPlayerCollision(sender, properties);
   }

   @Command("train collision player")
   @CommandDescription("Gets the behavior of the train when colliding with players")
   private void trainGetPlayerCollision(CommandSender sender, TrainProperties properties) {
      showMode(sender, "players", properties.getCollision().playerMode());
   }

   @CommandTargetTrain
   @PropertyCheckPermission("traincollision")
   @Command("train collision train <mode>")
   @CommandDescription("Sets the behavior of the train when colliding with other trains")
   private void trainSetTrainCollision(CommandSender sender, TrainProperties properties, @Argument("mode") CollisionMode mode) {
      properties.setCollision(properties.getCollision().cloneAndSetTrainMode(mode));
      this.trainGetTrainCollision(sender, properties);
   }

   @Command("train collision train")
   @CommandDescription("Gets the behavior of the train when colliding with other trains")
   private void trainGetTrainCollision(CommandSender sender, TrainProperties properties) {
      showMode(sender, "other trains", properties.getCollision().trainMode());
   }

   @CommandTargetTrain
   @PropertyCheckPermission("misccollision")
   @Command("train collision misc <mode>")
   @CommandDescription("Sets the behavior of the train when colliding with miscellaneous mobs and entities")
   private void trainSetMiscCollision(CommandSender sender, TrainProperties properties, @Argument("mode") CollisionMode mode) {
      properties.setCollision(properties.getCollision().cloneAndSetMiscMode(mode));
      this.trainGetMiscCollision(sender, properties);
   }

   @Command("train collision misc")
   @CommandDescription("Gets the behavior of the train when colliding with miscellaneous mobs and entities")
   private void trainGetMiscCollision(CommandSender sender, TrainProperties properties) {
      showMode(sender, "miscellaneous mobs and entities", properties.getCollision().miscMode());
   }

   private static void showMode(CommandSender sender, String category, CollisionMode mode) {
      MessageBuilder builder = new MessageBuilder();
      builder.yellow(new Object[]{"The train "}).red(new Object[]{mode.getOperationName()});
      builder.yellow(new Object[]{" "}).blue(new Object[]{category}).yellow(new Object[]{" when colliding"});
      builder.send(sender);
   }

   @PropertyParser("playercollision")
   public CollisionOptions parsePlayerCollisionMode(PropertyParseContext<CollisionOptions> context) {
      return ((CollisionOptions)context.current()).cloneAndSetPlayerMode(this.parseMode(context));
   }

   @PropertyParser("misccollision")
   public CollisionOptions parseMiscCollisionMode(PropertyParseContext<CollisionOptions> context) {
      return ((CollisionOptions)context.current()).cloneAndSetMiscMode(this.parseMode(context));
   }

   @PropertyParser("traincollision")
   public CollisionOptions parseTrainCollisionMode(PropertyParseContext<CollisionOptions> context) {
      return ((CollisionOptions)context.current()).cloneAndSetTrainMode(this.parseMode(context));
   }

   @PropertyParser("blockcollision")
   public CollisionOptions parseBlockCollisionMode(PropertyParseContext<CollisionOptions> context) {
      return ((CollisionOptions)context.current()).cloneAndSetBlockMode(this.parseMode(context));
   }

   @PropertyParser("([a-z]+)collision")
   public CollisionOptions parseCollisionMobsType(PropertyParseContext<CollisionOptions> context) {
      return this.parseUpdateForMobs(context, context.nameGroup(1), this.parseModeOrReset(context));
   }

   @PropertyParser("linking|link")
   public CollisionOptions parseLinkingMode(PropertyParseContext<CollisionOptions> context) {
      if (context.inputBoolean()) {
         return ((CollisionOptions)context.current()).cloneAndSetTrainMode(CollisionMode.LINK);
      } else {
         return ((CollisionOptions)context.current()).trainMode() == CollisionMode.LINK ? ((CollisionOptions)context.current()).cloneAndSetTrainMode(CollisionMode.DEFAULT) : (CollisionOptions)context.current();
      }
   }

   @PropertyParser("pushplayers")
   public CollisionOptions parsePushPlayers(PropertyParseContext<CollisionOptions> context) {
      return ((CollisionOptions)context.current()).cloneAndSetPlayerMode(CollisionMode.fromPushing(context.inputBoolean()));
   }

   @PropertyParser("pushmisc")
   public CollisionOptions parsePushMisc(PropertyParseContext<CollisionOptions> context) {
      return ((CollisionOptions)context.current()).cloneAndSetMiscMode(CollisionMode.fromPushing(context.inputBoolean()));
   }

   @PropertyParser("push([a-z]+)")
   public CollisionOptions parsePushingMobsType(PropertyParseContext<CollisionOptions> context) {
      CollisionMode mode;
      if (ParseUtil.isBool(context.input())) {
         mode = CollisionMode.fromPushing(context.inputBoolean());
      } else {
         mode = this.parseModeOrReset(context);
      }

      return this.parseUpdateForMobs(context, context.nameGroup(1), mode);
   }

   @PropertyParser("push|pushing")
   public CollisionOptions parsePushing(PropertyParseContext<CollisionOptions> context) {
      CollisionMode mode = CollisionMode.fromPushing(context.inputBoolean());
      return ((CollisionOptions)context.current()).cloneAndSetPlayerMode(mode).cloneAndSetMiscMode(mode).cloneAndSetForAllMobs(mode);
   }

   @PropertyParser("mobenter|mobsenter")
   public CollisionOptions parseMobsEnter(PropertyParseContext<CollisionOptions> context) {
      return context.inputBoolean() ? ((CollisionOptions)context.current()).cloneAndSetForAllMobs(CollisionMode.ENTER) : ((CollisionOptions)context.current()).cloneCompareAndSetForAllMobs(CollisionMode.ENTER, CollisionMode.DEFAULT);
   }

   @PropertyParser("collision|collide")
   public CollisionOptions parseDefaultOrNoCollision(PropertyParseContext<CollisionOptions> context) {
      return context.inputBoolean() ? CollisionOptions.DEFAULT : CollisionOptions.CANCEL;
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_COLLISION.has(sender);
   }

   public CollisionOptions getDefault() {
      return CollisionOptions.DEFAULT;
   }

   public CollisionOptions getData(FieldBackedProperty.TrainInternalData data) {
      return data.collision;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, CollisionOptions value) {
      data.collision = value;
   }

   public Optional<CollisionOptions> readFromConfig(ConfigurationNode config) {
      if (config.contains("trainCollision") && !(Boolean)config.get("trainCollision", true)) {
         CollisionOptions collision = CollisionOptions.CANCEL;
         if (config.contains("collision.block")) {
            collision = collision.cloneAndSetBlockMode((CollisionMode)config.get("collision.block", CollisionMode.DEFAULT));
         }

         return Optional.of(collision);
      } else if (!config.isNode("collision")) {
         return Optional.empty();
      } else {
         ConfigurationNode collisionConfig = config.getNode("collision");
         CollisionOptions.Builder builder = CollisionOptions.builder();
         if (collisionConfig.contains("players")) {
            builder.setPlayerMode(this.readMode(collisionConfig, "players", CollisionOptions.DEFAULT.playerMode()));
         }

         if (collisionConfig.contains("misc")) {
            builder.setMiscMode(this.readMode(collisionConfig, "misc", CollisionOptions.DEFAULT.miscMode()));
         }

         if (collisionConfig.contains("train")) {
            builder.setTrainMode(this.readMode(collisionConfig, "train", CollisionOptions.DEFAULT.trainMode()));
         }

         if (collisionConfig.contains("block")) {
            builder.setBlockMode(this.readMode(collisionConfig, "block", CollisionOptions.DEFAULT.blockMode()));
         }

         if (collisionConfig.contains("mobs")) {
            builder.setModeForAllMobs(this.readMode(collisionConfig, "mobs", (CollisionMode)null));
         } else if (collisionConfig.contains("mob")) {
            builder.setModeForAllMobs(this.readMode(collisionConfig, "mob", (CollisionMode)null));
         }

         CollisionMobCategory[] var4 = CollisionMobCategory.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            CollisionMobCategory category = var4[var6];
            CollisionMode mode;
            if (collisionConfig.contains(category.getMobType())) {
               mode = this.readMode(collisionConfig, category.getMobType(), (CollisionMode)null);
            } else {
               if (!collisionConfig.contains(category.getPluralMobType())) {
                  continue;
               }

               mode = this.readMode(collisionConfig, category.getPluralMobType(), (CollisionMode)null);
            }

            if (mode != null) {
               builder.setMobMode(category, mode);
            }
         }

         return Optional.of(builder.build());
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<CollisionOptions> value) {
      config.remove("trainCollision");
      if (value.isPresent()) {
         ConfigurationNode collisionConfig = config.getNode("collision");
         CollisionOptions data = (CollisionOptions)value.get();
         Stream var10000 = Stream.of(CollisionMobCategory.values()).filter(CollisionMobCategory::isMobCategory);
         Objects.requireNonNull(data);
         List<CollisionMode> mobCollisionModes = (List)var10000.map(data::mobMode).distinct().collect(Collectors.toList());
         boolean hasMobsMode = mobCollisionModes.size() == 1 && mobCollisionModes.get(0) != null;
         if (hasMobsMode) {
            collisionConfig.set("mobs", mobCollisionModes.get(0));
         } else {
            collisionConfig.remove("mobs");
         }

         collisionConfig.remove("mob");
         CollisionMobCategory[] var11 = CollisionMobCategory.values();
         int var7 = var11.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CollisionMobCategory category = var11[var8];
            CollisionMode mode;
            if (hasMobsMode && category.isMobCategory()) {
               mode = null;
            } else {
               mode = data.mobMode(category);
            }

            if (mode != null) {
               collisionConfig.set(category.getMobType(), mode);
            } else {
               collisionConfig.remove(category.getMobType());
            }

            collisionConfig.remove(category.getPluralMobType());
         }

         collisionConfig.set("players", data.playerMode());
         collisionConfig.set("misc", data.miscMode());
         collisionConfig.set("train", data.trainMode());
         collisionConfig.set("block", data.blockMode());
      } else {
         config.remove("collision");
      }

   }

   private CollisionMode readMode(ConfigurationNode config, String key, CollisionMode defValue) {
      String name = (String)config.get(key, String.class, (Object)null);
      CollisionMode parsed;
      return name != null && (parsed = CollisionMode.parse(name)) != null ? parsed : null;
   }

   private CollisionMode parseMode(PropertyParseContext<CollisionOptions> context) {
      CollisionMode mode = CollisionMode.parse(context.input());
      if (mode == null) {
         throw new PropertyInvalidInputException("Not a valid collision mode");
      } else {
         return mode;
      }
   }

   private CollisionMode parseModeOrReset(PropertyParseContext<CollisionOptions> context) {
      return context.input().equalsIgnoreCase("reset") ? null : this.parseMode(context);
   }

   private CollisionOptions parseUpdateForMobs(PropertyParseContext<CollisionOptions> context, String mobType, CollisionMode newMode) {
      if (!mobType.equals("mob") && !mobType.equals("mobs")) {
         boolean matchedMode = false;
         CollisionOptions newCollision = (CollisionOptions)context.current();
         CollisionMobCategory[] var6 = CollisionMobCategory.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CollisionMobCategory mobCategory = var6[var8];
            if (mobType.equals(mobCategory.getMobType()) || mobType.equals(mobCategory.getPluralMobType())) {
               newCollision = newCollision.cloneAndSetMobMode(mobCategory, newMode);
               matchedMode = true;
            }
         }

         if (!matchedMode) {
            throw new PropertyInvalidInputException("Invalid collision category: " + mobType);
         } else {
            return newCollision;
         }
      } else {
         return ((CollisionOptions)context.current()).cloneAndSetForAllMobs(newMode);
      }
   }
}
