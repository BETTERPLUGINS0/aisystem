package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.caption.Caption;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayer;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Localization extends LocalizationEnum {
   public static final Localization COMMAND_USAGE;
   public static final Localization COMMAND_NOPERM;
   public static final Localization COMMAND_SAVEDTRAIN_CLAIMED;
   public static final Localization COMMAND_SAVEDTRAIN_GLOBAL_NOPERM;
   public static final Localization COMMAND_SAVEDTRAIN_NOTFOUND;
   public static final Localization COMMAND_SAVEDTRAIN_FORCE;
   public static final Localization COMMAND_SAVEDTRAIN_CLAIM_INVALID;
   public static final Localization COMMAND_SAVEDTRAIN_INVALID_NAME;
   public static final Localization COMMAND_IMPORT_MISSING_MODELS;
   public static final Localization COMMAND_IMPORT_UPDATED_MODELS;
   public static final Localization COMMAND_IMPORT_NO_CARTS;
   public static final Localization COMMAND_IMPORT_ERROR;
   public static final Localization COMMAND_IMPORT_FORBIDDEN_CONTENTS;
   public static final Localization COMMAND_SAVE_NEW;
   public static final Localization COMMAND_SAVE_OVERWRITTEN;
   public static final Localization COMMAND_SAVE_LOCK_ORIENTATION;
   public static final Localization COMMAND_SAVE_FORBIDDEN_CONTENTS;
   public static final Localization COMMAND_MODEL_CONFIG_CLAIMED;
   public static final Localization COMMAND_MODEL_CONFIG_GLOBAL_NOPERM;
   public static final Localization COMMAND_MODEL_CONFIG_NOTFOUND;
   public static final Localization COMMAND_MODEL_CONFIG_FORCE;
   public static final Localization COMMAND_MODEL_CONFIG_INVALID_NAME;
   public static final Localization COMMAND_MODEL_CONFIG_INPUT_NAME_EMPTY;
   public static final Localization COMMAND_MODEL_CONFIG_INPUT_NAME_INVALID;
   public static final Localization COMMAND_MODEL_CONFIG_EDIT_EXISTING;
   public static final Localization COMMAND_MODEL_CONFIG_EDIT_NEW;
   public static final Localization COMMAND_TICKET_NOTFOUND;
   public static final Localization COMMAND_TICKET_NOTEDITING;
   public static final Localization COMMAND_EFFECT_PLAY;
   public static final Localization COMMAND_EFFECT_STOP;
   public static final Localization COMMAND_EFFECT_REPLAY;
   public static final Localization COMMAND_TRAIN_NOT_FOUND;
   public static final Localization COMMAND_CART_NOT_FOUND_IN_TRAIN;
   public static final Localization COMMAND_CART_NOT_FOUND_BY_UUID;
   public static final Localization COMMAND_CART_NOT_FOUND_NEARBY;
   public static final Localization COMMAND_INPUT_SPEED_INVALID;
   public static final Localization COMMAND_INPUT_ACCELERATION_INVALID;
   public static final Localization COMMAND_INPUT_DIRECTION_INVALID;
   public static final Localization COMMAND_INPUT_CHUNK_LOADING_MODE_INVALID;
   public static final Localization COMMAND_INPUT_NAME_EMPTY;
   public static final Localization COMMAND_INPUT_NAME_INVALID;
   public static final Localization COMMAND_INPUT_ATTACHMENTS_NO_SEATS;
   public static final Localization COMMAND_INPUT_ATTACHMENTS_NO_EFFECTS;
   public static final Localization COMMAND_INPUT_SELECTOR_INVALID;
   public static final Localization COMMAND_INPUT_SELECTOR_NOPERM;
   public static final Localization COMMAND_INPUT_SELECTOR_EXCEEDEDLIMIT;
   public static final Localization PROPERTY_NOTFOUND;
   public static final Localization PROPERTY_ERROR;
   public static final Localization PROPERTY_INVALID_INPUT;
   public static final Localization PROPERTY_NOPERM_ANY;
   public static final Localization PROPERTY_NOPERM;
   public static final Localization EDIT_SUCCESS;
   public static final Localization EDIT_NOSELECT;
   public static final Localization EDIT_NOTALLOWED;
   public static final Localization EDIT_NONEFOUND;
   public static final Localization EDIT_NOTFOUND;
   public static final Localization EDIT_NOTOWNED;
   public static final Localization EDIT_NOTLOADED;
   public static final Localization SPAWN_DISALLOWED_TYPE;
   public static final Localization SPAWN_DISALLOWED_INVENTORY;
   public static final Localization SPAWN_FORBIDDEN_CONTENTS;
   public static final Localization SPAWN_MAX_PER_WORLD;
   public static final Localization SPAWN_TOO_LONG;
   public static final Localization SELECT_DESTINATION;
   public static final Localization TICKET_EXPIRED;
   public static final Localization TICKET_REQUIRED;
   public static final Localization TICKET_USED;
   public static final Localization TICKET_CONFLICT;
   public static final Localization TICKET_CONFLICT_OWNER;
   public static final Localization TICKET_CONFLICT_TYPE;
   public static final Localization WAITER_TARGET_NOT_FOUND;
   public static final Localization TICKET_ADD;
   public static final Localization TICKET_CHECK;
   public static final Localization TICKET_BUYFAIL;
   public static final Localization TICKET_BUY;
   public static final Localization TICKET_BUYOWNER;
   public static final Localization TICKET_MAP_INVALID;
   public static final Localization TICKET_MAP_EXPIRED;
   public static final Localization TICKET_MAP_USES;
   public static final Localization PATHING_BUSY;
   public static final Localization PATHING_FAILED;
   public static final Localization CHEST_NOPERM;
   public static final Localization CHEST_NOITEM;
   public static final Localization CHEST_GIVE;
   public static final Localization CHEST_GIVE_TO;
   public static final Localization CHEST_UPDATE;
   public static final Localization CHEST_LOCKED;
   public static final Localization CHEST_PICKUP;
   public static final Localization CHEST_FULL;
   public static final Localization CHEST_IMPORTED;
   public static final Localization CHEST_SPAWN_SUCCESS;
   public static final Localization CHEST_SPAWN_EMPTY;
   public static final Localization CHEST_SPAWN_NORAIL;
   public static final Localization CHEST_SPAWN_NORAIL_LOOK;
   public static final Localization CHEST_SPAWN_RAILTOOSHORT;
   public static final Localization CHEST_SPAWN_BLOCKED;
   public static final Localization CHEST_SPAWN_LIMIT_REACHED;
   public static final Localization SIGN_NO_PERMISSION;
   public static final Localization SIGN_NO_RC_PERMISSION;
   public static final Localization COMMAND_ANIMATE_SUCCESS;
   public static final Localization COMMAND_ANIMATE_FAILURE;
   public static final Localization ATTACHMENTS_LOAD_CLIPBOARD;
   public static final Localization ATTACHMENTS_LOAD_MODEL_STORE;
   public static final Localization ATTACHMENTS_LOAD_PASTE_SERVER;
   public static final Localization ATTACHMENTS_SAVE_CLIPBOARD;
   public static final Localization ATTACHMENTS_SAVE_MODEL_STORE;
   public static final Localization ATTACHMENTS_SAVE_PASTE_SERVER;

   private Localization(String name, String defValue) {
      super(name, defValue);
   }

   public String get(String... arguments) {
      return TrainCarts.plugin.getLocale(this.getName(), arguments);
   }

   public Caption getCaption() {
      return Caption.of(this.getName());
   }

   public void broadcast(MinecartGroup group, String... arguments) {
      HashSet<Player> receivers = new HashSet();
      Iterator var4 = group.iterator();

      while(var4.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var4.next();
         receivers.addAll(member.getProperties().getEditingPlayers());
         if (((CommonMinecart)member.getEntity()).hasPlayerPassenger()) {
            receivers.add(((CommonMinecart)member.getEntity()).getPlayerPassenger());
         }
      }

      var4 = receivers.iterator();

      while(var4.hasNext()) {
         Player player = (Player)var4.next();
         this.message(player, arguments);
      }

   }

   public void message(TrainCartsPlayer player, String... arguments) {
      Player onlinePlayer = player.getOnlinePlayer();
      if (onlinePlayer != null) {
         this.message(onlinePlayer, arguments);
      }

   }

   public static String boolStr(boolean value) {
      return value ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No";
   }

   // $FF: synthetic method
   Localization(String x0, String x1, Object x2) {
      this(x0, x1);
   }

   static {
      COMMAND_USAGE = new Localization("command.usage", ChatColor.GREEN + "See [" + ChatColor.WHITE + ChatColor.UNDERLINE + "the WIKI](https://wiki.traincarts.net/p/TrainCarts)" + ChatColor.RESET + ChatColor.GREEN + " for more information, or use /train help");
      COMMAND_NOPERM = new Localization("command.noperm", ChatColor.RED + "You do not have permission, ask an admin to do this for you.");
      COMMAND_SAVEDTRAIN_CLAIMED = new Localization("command.savedtrain.claimed", ChatColor.RED + "Saved train with name %0% is claimed by someone else, you can not access it!");
      COMMAND_SAVEDTRAIN_GLOBAL_NOPERM = new Localization("command.savedtrain.global.noperm", ChatColor.RED + "You do not have permission to force access to saved trains by others, ask an admin to do this for you.");
      COMMAND_SAVEDTRAIN_NOTFOUND = new Localization("command.savedtrain.notfound", ChatColor.RED + "Saved train with name %0% does not exist!");
      COMMAND_SAVEDTRAIN_FORCE = new Localization("command.savedtrain.force", ChatColor.RED + "Saved train with name %0% is claimed by someone else, you can access it anyway with --force");
      COMMAND_SAVEDTRAIN_CLAIM_INVALID = new Localization("command.savedtrain.claim.invalid", ChatColor.RED + "Invalid player name specified: %0%");
      COMMAND_SAVEDTRAIN_INVALID_NAME = new Localization("command.savedtrain.name.invalid", ChatColor.RED + "Invalid train name: %0%");
      COMMAND_IMPORT_MISSING_MODELS = new Localization("command.import.models.missing", ChatColor.YELLOW + "The imported train configuration includes model configurations [" + ChatColor.WHITE + "%0%" + ChatColor.YELLOW + "], import them by specifying --import-models");
      COMMAND_IMPORT_UPDATED_MODELS = new Localization("command.import.models.updated", ChatColor.GREEN + "Imported model configurations: %0%");
      COMMAND_IMPORT_NO_CARTS = new Localization("command.import.nocarts", ChatColor.RED + "Imported configuration does not include any carts!");
      COMMAND_IMPORT_ERROR = new Localization("command.import.error", ChatColor.RED + "An error occurred trying to import the train configuration: %0%");
      COMMAND_IMPORT_FORBIDDEN_CONTENTS = new Localization("command.import.forbiddencontents", ChatColor.RED + "The train configuration could not be imported because it contains things you have no permission to use or spawn");
      COMMAND_SAVE_NEW = new Localization("command.save.new", ChatColor.GREEN + "The train was saved as %0%");
      COMMAND_SAVE_OVERWRITTEN = new Localization("command.save.overwritten", ChatColor.GREEN + "The train was saved as %0%, a previous train was overwritten");
      COMMAND_SAVE_LOCK_ORIENTATION = new Localization("command.save.lockorientation", ChatColor.YELLOW + "Train orientation is now locked to the current forward direction!\n" + ChatColor.YELLOW + "Future saves without --lockorientation passed will remember this orientation.\n" + ChatColor.YELLOW + "This can be turned off using " + ChatColor.WHITE + "/savedtrain %0% lockorientation false");
      COMMAND_SAVE_FORBIDDEN_CONTENTS = new Localization("command.save.forbiddencontents", ChatColor.RED + "The train configuration could not be saved because the train contains things you have no permission to use or spawn");
      COMMAND_MODEL_CONFIG_CLAIMED = new Localization("command.model.config.claimed", ChatColor.RED + "Saved model configuration with name %0% is claimed by someone else, you can not access it!");
      COMMAND_MODEL_CONFIG_GLOBAL_NOPERM = new Localization("command.model.config.global.noperm", ChatColor.RED + "You do not have permission to force access to saved model configurations by others, ask an admin to do this for you.");
      COMMAND_MODEL_CONFIG_NOTFOUND = new Localization("command.model.config.notfound", ChatColor.RED + "Saved model configuration with name %0% does not exist!");
      COMMAND_MODEL_CONFIG_FORCE = new Localization("command.model.config.force", ChatColor.RED + "Saved model configuration with name %0% is claimed by someone else, you can access it anyway with --force");
      COMMAND_MODEL_CONFIG_INVALID_NAME = new Localization("command.model.config.name.invalid", ChatColor.RED + "Invalid model configuration name: %0%");
      COMMAND_MODEL_CONFIG_INPUT_NAME_EMPTY = new Localization("command.model.config.name.empty", ChatColor.RED + "Input model name is empty!");
      COMMAND_MODEL_CONFIG_INPUT_NAME_INVALID = new Localization("command.model.config.name.invalid", ChatColor.RED + "Input model name '%0%' contains invalid characters!");
      COMMAND_MODEL_CONFIG_EDIT_EXISTING = new Localization("command.model.config.edit.existing", ChatColor.GREEN + "You are now editing the model configuration '" + ChatColor.YELLOW + "%0%" + ChatColor.GREEN + "'!");
      COMMAND_MODEL_CONFIG_EDIT_NEW = new Localization("command.model.config.edit.new", ChatColor.GREEN + "You are now editing the " + ChatColor.BLUE + "NEW" + ChatColor.GREEN + " model configuration '" + ChatColor.YELLOW + "%0%" + ChatColor.GREEN + "'!");
      COMMAND_TICKET_NOTFOUND = new Localization("command.ticket.notfound", ChatColor.RED + "Ticket with name %0% does not exist");
      COMMAND_TICKET_NOTEDITING = new Localization("command.ticket.notediting", ChatColor.RED + "You are not editing any tickets right now\n" + ChatColor.RED + "To create a new train ticket, use /train ticket create\n" + ChatColor.RED + "To edit an existing train ticket, use /train ticket edit [name]");
      COMMAND_EFFECT_PLAY = new Localization("command.effect.play", ChatColor.GREEN + "Playing effect " + ChatColor.YELLOW + "%0%");
      COMMAND_EFFECT_STOP = new Localization("command.effect.stop", ChatColor.YELLOW + "Stopping effect " + ChatColor.WHITE + "%0%");
      COMMAND_EFFECT_REPLAY = new Localization("command.effect.replay", ChatColor.GREEN + "Re-playing effect " + ChatColor.YELLOW + "%0%");
      COMMAND_TRAIN_NOT_FOUND = new Localization("command.input.train.notfound", ChatColor.RED + "Train with name %0% does not exist");
      COMMAND_CART_NOT_FOUND_IN_TRAIN = new Localization("command.input.cart.notintrain", ChatColor.RED + "Cart '%0%' does not exist in the selected train");
      COMMAND_CART_NOT_FOUND_BY_UUID = new Localization("command.input.cart.uuidnotfound", ChatColor.RED + "Cart with unique ID %0% does not exist");
      COMMAND_CART_NOT_FOUND_NEARBY = new Localization("command.input.cart.notnearby", ChatColor.RED + "No cart was found near the specified coordinates");
      COMMAND_INPUT_SPEED_INVALID = new Localization("command.input.speed.invalid", ChatColor.RED + "Input value %0% is not a valid number or speed expression");
      COMMAND_INPUT_ACCELERATION_INVALID = new Localization("command.input.acceleration.invalid", ChatColor.RED + "Input value %0% is not a valid number or acceleration expression");
      COMMAND_INPUT_DIRECTION_INVALID = new Localization("command.input.direction.invalid", ChatColor.RED + "Input value %0% is not a valid direction");
      COMMAND_INPUT_CHUNK_LOADING_MODE_INVALID = new Localization("command.input.chunkloading.mode.invalid", ChatColor.RED + "Input value %0% is not a valid chunk loading mode");
      COMMAND_INPUT_NAME_EMPTY = new Localization("command.input.name.empty", ChatColor.RED + "Input train name is empty!");
      COMMAND_INPUT_NAME_INVALID = new Localization("command.input.name.invalid", ChatColor.RED + "Input train name '%0%' contains invalid characters!");
      COMMAND_INPUT_ATTACHMENTS_NO_SEATS = new Localization("command.input.attachments.noseats", ChatColor.RED + "No seats with name '%0%' found!");
      COMMAND_INPUT_ATTACHMENTS_NO_EFFECTS = new Localization("command.input.attachments.noeffects", ChatColor.RED + "No effects with name '%0%' found!");
      COMMAND_INPUT_SELECTOR_INVALID = new Localization("command.input.selector.invalid", ChatColor.RED + "[TrainCarts] Selector condition contains syntax errors!");
      COMMAND_INPUT_SELECTOR_NOPERM = new Localization("command.input.selector.noperm", ChatColor.RED + "[TrainCarts] You do not have permission to use TrainCarts command selectors!");
      COMMAND_INPUT_SELECTOR_EXCEEDEDLIMIT = new Localization("command.input.selector.exceededlimit", ChatColor.RED + "[TrainCarts] Selector expression matched too many results!");
      PROPERTY_NOTFOUND = new Localization("property.notfound", ChatColor.RED + "Property with name '%0%' does not exist");
      PROPERTY_ERROR = new Localization("property.error", ChatColor.RED + "An internal error occurred while parsing value '%1%' for property '%0%'");
      PROPERTY_INVALID_INPUT = new Localization("property.invalidinput", ChatColor.RED + "Value '%1%' for property '%0%' is invalid: %2%");
      PROPERTY_NOPERM_ANY = new Localization("property.nopermissionany", ChatColor.RED + "You do not have permission to modify train properties");
      PROPERTY_NOPERM = new Localization("property.nopermission", ChatColor.RED + "You do not have permission to modify the property with name '%0%'");
      EDIT_SUCCESS = new Localization("edit.success", ChatColor.GREEN + "You are now editing train '" + ChatColor.YELLOW + "%0%" + ChatColor.GREEN + "'!");
      EDIT_NOSELECT = new Localization("edit.noselect", ChatColor.YELLOW + "You haven't selected a train to edit yet!");
      EDIT_NOTALLOWED = new Localization("edit.notallowed", ChatColor.RED + "You are not allowed to own trains!");
      EDIT_NONEFOUND = new Localization("edit.nonefound", ChatColor.RED + "You do not own any trains you can edit.");
      EDIT_NOTFOUND = new Localization("edit.notfound", ChatColor.RED + "Could not find a valid train named '%0%'!");
      EDIT_NOTOWNED = new Localization("edit.notowned", ChatColor.RED + "You do not own this train!");
      EDIT_NOTLOADED = new Localization("edit.notloaded", ChatColor.RED + "The selected train is not loaded right now!");
      SPAWN_DISALLOWED_TYPE = new Localization("spawn.type.notallowed", ChatColor.RED + "You do not have permission to create minecarts of type %0%");
      SPAWN_DISALLOWED_INVENTORY = new Localization("spawn.inventoryitems.notallowed", ChatColor.RED + "You do not have permission to create minecarts with pre-existing inventory items");
      SPAWN_FORBIDDEN_CONTENTS = new Localization("spawn.forbiddencontents", ChatColor.RED + "The train configuration cannot be spawned because the train contains things you have no permission to use or spawn");
      SPAWN_MAX_PER_WORLD = new Localization("spawn.maxperworld", ChatColor.RED + "Cannot spawn because the maximum number of Minecarts on this world has been reached!");
      SPAWN_TOO_LONG = new Localization("spawn.toolong", ChatColor.RED + "Cannot spawn because the spawned train is too long! (Too many Minecarts)");
      SELECT_DESTINATION = new Localization("select.destination", ChatColor.YELLOW + "You have selected " + ChatColor.WHITE + "%0%" + ChatColor.YELLOW + " as your destination!");
      TICKET_EXPIRED = new Localization("ticket.expired", ChatColor.RED + "Your ticket for %0% is expired");
      TICKET_REQUIRED = new Localization("ticket.required", ChatColor.RED + "You do not own a ticket for this train!");
      TICKET_USED = new Localization("ticket.used", ChatColor.GREEN + "You have used your " + ChatColor.YELLOW + "%0%" + ChatColor.GREEN + " ticket!");
      TICKET_CONFLICT = new Localization("ticket.conflict", ChatColor.RED + "You own multiple tickets that can be used for this train. Please hold the right ticket in your hand!");
      TICKET_CONFLICT_OWNER = new Localization("ticket.ownerConflict", ChatColor.RED + "The train ticket %0% is not yours, it belongs to %1%!");
      TICKET_CONFLICT_TYPE = new Localization("ticket.typeConflict", ChatColor.RED + "The train ticket %0% can not be used for this train!");
      WAITER_TARGET_NOT_FOUND = new Localization("waiter.notfound", ChatColor.RED + "Didn't find a " + ChatColor.YELLOW + "%0%" + ChatColor.RED + " sign on the track!");
      TICKET_ADD = new Localization("ticket.add", ChatColor.WHITE + "[Ticket System]" + ChatColor.YELLOW + " You received %0% in your bank account!");
      TICKET_CHECK = new Localization("ticket.check", ChatColor.WHITE + "[Ticket System]" + ChatColor.YELLOW + " You currently have %0% in your bank account!");
      TICKET_BUYFAIL = new Localization("ticket.buyfail", ChatColor.WHITE + "[Ticket System]" + ChatColor.RED + " You can't afford a Ticket for %0%, sorry.");
      TICKET_BUY = new Localization("ticket.buy", ChatColor.WHITE + "[Ticket System]" + ChatColor.YELLOW + " You bought a Ticket for %0%.");
      TICKET_BUYOWNER = new Localization("ticket.buyowner", ChatColor.WHITE + "[Ticket System]" + ChatColor.YELLOW + " %0% " + ChatColor.YELLOW + "bought a Ticket for %1% on " + ChatColor.WHITE + "%2%" + ChatColor.YELLOW + ".");
      TICKET_MAP_INVALID = new Localization("ticket.map.invalid", "Invalid Ticket");
      TICKET_MAP_EXPIRED = new Localization("ticket.map.expired", "EXPIRED");
      TICKET_MAP_USES = new Localization("ticket.map.uses", "%1%/%0% uses") {
         public void writeDefaults(ConfigurationNode config, String path) {
            ConfigurationNode node = config.getNode(path);
            node.set("1", "Single use");
            node.set("-1", "Unlimited uses");
            node.set("default", "%1%/%0% uses");
         }
      };
      PATHING_BUSY = new Localization("pathfinding.busy", ChatColor.YELLOW + "Looking for a way to reach the destination...");
      PATHING_FAILED = new Localization("pathfinding.failed", ChatColor.RED + "Destination " + ChatColor.YELLOW + "%0%" + ChatColor.RED + " could not be reached from here!");
      CHEST_NOPERM = new Localization("chest.noperm", ChatColor.RED + "You do not have permission to use the train storage chest!");
      CHEST_NOITEM = new Localization("chest.noitem", ChatColor.RED + "You are not currently holding a train storage chest item!");
      CHEST_GIVE = new Localization("chest.give", ChatColor.GREEN + "You have been given a train storage chest item. Use it to store and spawn trains");
      CHEST_GIVE_TO = new Localization("chest.giveto", ChatColor.GREEN + "Gave a train storage chest item to player %0%");
      CHEST_UPDATE = new Localization("chest.update", ChatColor.GREEN + "Your train storage chest item has been updated");
      CHEST_LOCKED = new Localization("chest.locked", ChatColor.RED + "Your train storage chest item is locked and can not pick up the train");
      CHEST_PICKUP = new Localization("chest.pickup", ChatColor.GREEN + "Train picked up and stored inside the item!");
      CHEST_FULL = new Localization("chest.full", ChatColor.RED + "Your train storage chest item is full and can not pick up the train");
      CHEST_IMPORTED = new Localization("chest.imported", ChatColor.GREEN + "The train was imported into the chest item");
      CHEST_SPAWN_SUCCESS = new Localization("chest.spawn.success", ChatColor.GREEN + "Train stored inside the item has been spawned on the rails");
      CHEST_SPAWN_EMPTY = new Localization("chest.spawn.empty", ChatColor.RED + "Train can not be spawned, no train is stored in the item");
      CHEST_SPAWN_NORAIL = new Localization("chest.spawn.norail", ChatColor.RED + "Train can not be spawned, clicked block is not a known rail");
      CHEST_SPAWN_NORAIL_LOOK = new Localization("chest.spawn.noraillook", ChatColor.RED + "Train can not be spawned, not looking at any rail or too far away");
      CHEST_SPAWN_RAILTOOSHORT = new Localization("chest.spawn.railtooshort", ChatColor.RED + "Train can not be spawned, rails not long enough to fit the train");
      CHEST_SPAWN_BLOCKED = new Localization("chest.spawn.blocked", ChatColor.RED + "Train can not be spawned, no space on rails because another train is in the way");
      CHEST_SPAWN_LIMIT_REACHED = new Localization("chest.spawn.limitreached", ChatColor.RED + "Train can not be spawned, the maximum number of spawns was reached");
      SIGN_NO_PERMISSION = new Localization("sign.noperm", ChatColor.RED + "You do not have permission to use this sign! [%0%]");
      SIGN_NO_RC_PERMISSION = new Localization("sign.noremotecontrolperm", ChatColor.RED + "You do not have permission to use the remote control sign feature!");
      COMMAND_ANIMATE_SUCCESS = new Localization("command.animate.success", ChatColor.GREEN + "Now playing animation " + ChatColor.YELLOW + "%0%" + ChatColor.GREEN + " at speed " + ChatColor.YELLOW + "%1%" + ChatColor.GREEN + " with phase delay " + ChatColor.YELLOW + "%2%");
      COMMAND_ANIMATE_FAILURE = new Localization("command.animate.failure", ChatColor.RED + "Failed to find animation " + ChatColor.YELLOW + "%0%" + ChatColor.RED + "!");
      ATTACHMENTS_LOAD_CLIPBOARD = new Localization("attachments.load.clipboard", ChatColor.GREEN + "Attachment loaded from your clipboard!");
      ATTACHMENTS_LOAD_MODEL_STORE = new Localization("attachments.load.modelstore", ChatColor.GREEN + "Attachment '%0%' loaded from the model store!");
      ATTACHMENTS_LOAD_PASTE_SERVER = new Localization("attachments.load.pasteserver", ChatColor.GREEN + "Attachment imported from paste server!");
      ATTACHMENTS_SAVE_CLIPBOARD = new Localization("attachments.save.clipboard", ChatColor.GREEN + "Attachment saved to your clipboard!");
      ATTACHMENTS_SAVE_MODEL_STORE = new Localization("attachments.save.modelstore", ChatColor.GREEN + "Attachment saved to the model store as %0%!");
      ATTACHMENTS_SAVE_PASTE_SERVER = new Localization("attachments.save.pasteserver", ChatColor.GREEN + "Attachment exported to paste server: " + ChatColor.WHITE + ChatColor.UNDERLINE + "%0%");
   }
}
