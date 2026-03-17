package com.bergerkiller.bukkit.tc.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.cloud.parsers.QuotedArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.CommandManager;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Quoted;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.suggestion.Suggestions;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.exception.InvalidCommandSenderException;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.Suggestion;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.SuggestionProvider;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.exception.command.NoTicketSelectedException;
import com.bergerkiller.bukkit.tc.tickets.TCTicketDisplay;
import com.bergerkiller.bukkit.tc.tickets.Ticket;
import com.bergerkiller.bukkit.tc.tickets.TicketStore;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TicketCommands {
   @Suggestions("ticketNames")
   public List<String> getTicketNames(CommandContext<CommandSender> context, String input) {
      return (List)TicketStore.getAll().stream().map(Ticket::getName).collect(Collectors.toList());
   }

   public void init(CommandManager<CommandSender> manager) {
      manager.parameterInjectorRegistry().registerInjector(Ticket.class, (context, annot) -> {
         if (!(context.sender() instanceof Player)) {
            throw new InvalidCommandSenderException(context.sender(), Player.class, Collections.emptyList(), context.command());
         } else {
            Ticket ticket = TicketStore.getEditing((Player)context.sender());
            if (ticket == null) {
               throw new NoTicketSelectedException();
            } else {
               return ticket;
            }
         }
      });
      manager.parserRegistry().registerParser(ParserDescriptor.of((new TicketCommands.TicketParser()).createParser(), Ticket.class));
   }

   @Command("train list tickets")
   @CommandDescription("Lists the names of all tickets that exist")
   private void commandTrainList(CommandSender sender) {
      this.commandList(sender);
   }

   @Command("train ticket list")
   @CommandDescription("Lists the names of all tickets that exist")
   private void commandList(CommandSender sender) {
      MessageBuilder builder = new MessageBuilder();
      builder.yellow(new Object[]{"The following tickets are available:"});
      builder.newLine().setSeparator(ChatColor.WHITE, " / ");
      Iterator var3 = TicketStore.getAll().iterator();

      while(var3.hasNext()) {
         Ticket ticket = (Ticket)var3.next();
         builder.green(new Object[]{ticket.getName()});
      }

      builder.send(sender);
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket edit <name>")
   @CommandDescription("Edits a ticket by name")
   private void commandEdit(Player sender, @Quoted @Argument("name") Ticket ticket) {
      TicketStore.setEditing(sender, ticket);
      sender.sendMessage(ChatColor.GREEN + "You are now editing ticket " + ChatColor.YELLOW + ticket.getName());
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket create")
   @CommandDescription("Creates a new ticket with a unique random name")
   private void commandCreate(Player sender) {
      Ticket newTicket = TicketStore.createTicket(TicketStore.DEFAULT);
      sender.sendMessage(ChatColor.GREEN + "You have created a new ticket with the name " + ChatColor.YELLOW + newTicket.getName());
      TicketStore.setEditing(sender, newTicket);
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket create <name>")
   @CommandDescription("Creates a new ticket with a name as specified")
   private void commandCreateWithName(Player sender, @Quoted @Argument("name") String name) {
      Ticket newTicket = TicketStore.createTicket(TicketStore.DEFAULT, name);
      if (newTicket == null) {
         sender.sendMessage(ChatColor.RED + "Can not create this ticket: name '" + name + "' is already in use!");
      } else {
         sender.sendMessage(ChatColor.GREEN + "You have created a new ticket with the name " + ChatColor.YELLOW + newTicket.getName());
         TicketStore.setEditing(sender, newTicket);
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket give <ticket> <players>")
   @CommandDescription("Gives a ticket by name to one or more players")
   private void commandGiveTicket(CommandSender sender, @Argument("ticket") Ticket ticket, @Argument(value = "players",suggestions = "targetplayer") String[] playerNames) {
      String[] var4 = playerNames;
      int var5 = playerNames.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String playerName = var4[var6];
         Player player = Util.findPlayer(sender, playerName);
         if (player != null) {
            ItemStack item = ticket.createItem(player);
            player.getInventory().addItem(new ItemStack[]{item});
            sender.sendMessage(ChatColor.GREEN + "Ticket " + ChatColor.YELLOW + ticket.getName() + ChatColor.GREEN + " sold to player " + ChatColor.YELLOW + player.getName());
         }
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket sell <ticket> <price> <players>")
   @CommandDescription("Sells a ticket by name to one or more players by charging them money")
   private void commandSellTicket(CommandSender sender, TrainCarts plugin, @Argument("ticket") Ticket ticket, @Argument("price") double price, @Argument(value = "players",suggestions = "targetplayer") String[] playerNames) {
      Economy econ = plugin.getEconomy();
      String[] var8;
      int var9;
      int var10;
      String playerName;
      Player player;
      if (econ == null) {
         sender.sendMessage(ChatColor.RED + "No Vault-compatible economy plugin is installed!");
         var8 = playerNames;
         var9 = playerNames.length;

         for(var10 = 0; var10 < var9; ++var10) {
            playerName = var8[var10];
            player = Util.findPlayer(sender, playerName);
            if (player != null && player != sender) {
               sender.sendMessage(ChatColor.RED + "Failed to buy ticket: no vault-compatible economy plugin is installed!");
            }
         }

      } else if (price < 0.0D) {
         sender.sendMessage(ChatColor.RED + "Price must be positive");
      } else {
         var8 = playerNames;
         var9 = playerNames.length;

         for(var10 = 0; var10 < var9; ++var10) {
            playerName = var8[var10];
            player = Util.findPlayer(sender, playerName);
            if (player != null) {
               if (!econ.has(player, player.getWorld().getName(), price)) {
                  Localization.TICKET_BUYFAIL.message(player, new String[]{TrainCarts.getCurrencyText(price)});
               } else {
                  EconomyResponse resp = econ.withdrawPlayer(player, player.getWorld().getName(), price);
                  if (resp.type != ResponseType.SUCCESS) {
                     Localization.TICKET_BUYFAIL.message(player, new String[]{TrainCarts.getCurrencyText(price)});
                  } else {
                     Localization.TICKET_BUY.message(player, new String[]{TrainCarts.getCurrencyText(price)});
                     ItemStack item = ticket.createItem(player);
                     player.getInventory().addItem(new ItemStack[]{item});
                     sender.sendMessage(ChatColor.GREEN + "Ticket " + ChatColor.YELLOW + ticket.getName() + ChatColor.GREEN + " given to player " + ChatColor.YELLOW + player.getName());
                  }
               }
            }
         }

      }
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket clone")
   @CommandDescription("Clones the currently edited ticket with a random new name")
   private void commandCloneTicket(Player sender, Ticket ticket) {
      Ticket newTicket = TicketStore.createTicket(ticket);
      sender.sendMessage(ChatColor.GREEN + "You cloned the ticket, creating a new ticket with the name " + ChatColor.YELLOW + newTicket.getName());
      TicketStore.setEditing(sender, newTicket);
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket clone <newname>")
   @CommandDescription("Clones the currently edited ticket with the new name specified")
   private void commandCloneTicketWithNewName(Player sender, Ticket ticket, @Argument("newname") String newTicketName) {
      Ticket newTicket = TicketStore.createTicket(ticket, newTicketName);
      if (newTicket == null) {
         sender.sendMessage(ChatColor.RED + "Failed to clone ticket: a ticket with the name " + newTicketName + " already exists");
      } else {
         sender.sendMessage(ChatColor.GREEN + "You cloned the ticket, creating a new ticket with the name " + ChatColor.YELLOW + newTicket.getName());
         TicketStore.setEditing(sender, newTicket);
      }
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket remove")
   @CommandDescription("Permanently removes a ticket")
   private void commandDeleteTicket(Player sender, Ticket ticket) {
      if (ticket.remove()) {
         sender.sendMessage(ChatColor.GREEN + "Ticket has been removed!");
      } else {
         sender.sendMessage(ChatColor.RED + "Failed to remove ticket: not found!");
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket rename <newname>")
   @CommandDescription("Renames the currently edited ticket")
   private void commandRenameTicket(Player sender, Ticket ticket, @Quoted @Argument("newname") String newTicketName) {
      if (ticket.setName(newTicketName)) {
         sender.sendMessage(ChatColor.GREEN + "Ticket has been renamed to " + ChatColor.YELLOW + ticket.getName());
      } else {
         sender.sendMessage(ChatColor.RED + "Failed to rename ticket to " + newTicketName + ": a ticket with this name already exists!");
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket realm <newrealm>")
   @CommandDescription("Changes the realm of the currently edited ticket")
   private void commandSetRealm(Player sender, Ticket ticket, @Argument("newrealm") String newRealm) {
      ticket.setRealm(newRealm);
      TicketStore.markChanged();
      sender.sendMessage(ChatColor.GREEN + "Ticket realm set to " + ChatColor.YELLOW + newRealm);
   }

   @Command("train ticket background|image")
   @CommandDescription("Reads what background image is configured for the currently edited ticket")
   private void commandSetBackground(Player sender, Ticket ticket) {
      if (ticket.getBackgroundImagePath().isEmpty()) {
         sender.sendMessage(ChatColor.YELLOW + "No background image is set for this ticket (default).");
         sender.sendMessage(ChatColor.YELLOW + "To set a background image, use /train ticket background [path]");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Background image is currently set to: " + ChatColor.WHITE + ticket.getBackgroundImagePath());
         sender.sendMessage(ChatColor.YELLOW + "To set a background image, use /train ticket background [path]");
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket background|image <newimage>")
   @CommandDescription("Configures a custom background image for the currently edited ticket")
   private void commandSetBackground(Player sender, Ticket ticket, @Quoted @Argument("newimage") String newImage) {
      ticket.setBackgroundImagePath(newImage);
      TicketStore.markChanged();
      if (newImage.isEmpty()) {
         sender.sendMessage(ChatColor.GREEN + "Ticket background image reset to the default image");
      } else {
         sender.sendMessage(ChatColor.GREEN + "Ticket background image set to " + ChatColor.YELLOW + newImage);
      }

      Iterator var4 = TCTicketDisplay.getAllDisplays(TCTicketDisplay.class).iterator();

      while(var4.hasNext()) {
         TCTicketDisplay display = (TCTicketDisplay)var4.next();
         if (TicketStore.getTicketFromItem(display.getMapItem()) == ticket) {
            display.renderBackground();
         }
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket maximumuses|maxuses|uselimit unlimited|infinite")
   @CommandDescription("Sets the number of uses for the currently edited ticket to unlimited")
   private void commandSetUnlimitedMaximumUses(Player sender, Ticket ticket) {
      this.commandSetMaximumUses(sender, ticket, -1);
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket maximumuses|maxuses|uselimit <newmaxuses>")
   @CommandDescription("Sets the number of uses for the currently edited ticket")
   private void commandSetMaximumUses(Player sender, Ticket ticket, @Argument("newmaxuses") int newMaximumUses) {
      ticket.setMaxNumberOfUses(newMaximumUses);
      TicketStore.markChanged();
      if (newMaximumUses >= 0) {
         sender.sendMessage(ChatColor.GREEN + "Ticket maximum number of uses set to " + ChatColor.YELLOW + newMaximumUses);
      } else {
         sender.sendMessage(ChatColor.GREEN + "Ticket now has unlimited number of uses");
      }

   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket destination <newdestination>")
   @CommandDescription("Sets a destination to apply to the train when the currently edited ticket is used")
   private void commandSetDestination(Player sender, Ticket ticket, @Quoted @Argument("newdestination") String newDestination) {
      ticket.getProperties().set("destination", newDestination);
      sender.sendMessage(ChatColor.GREEN + "Ticket destination set to " + ChatColor.YELLOW + newDestination);
   }

   @CommandRequiresPermission(Permission.TICKET_MANAGE)
   @Command("train ticket tags [newtags]")
   @CommandDescription("Sets tags to apply to the train when the currently edited ticket is used")
   private void commandSetTags(Player sender, Ticket ticket, @Argument("newtags") String[] newTags) {
      if (newTags != null && newTags.length != 0) {
         ticket.getProperties().set("tags", newTags);
         sender.sendMessage(ChatColor.GREEN + "Ticket tags set: " + ChatColor.YELLOW + StringUtil.combineNames(newTags));
      } else {
         ticket.getProperties().set("tags", new String[0]);
         sender.sendMessage(ChatColor.GREEN + "All ticket tags have been cleared");
      }

   }

   private static class TicketParser implements QuotedArgumentParser<CommandSender, Ticket>, SuggestionProvider<CommandSender> {
      private TicketParser() {
      }

      public ArgumentParseResult<Ticket> parseQuotedString(CommandContext<CommandSender> commandContext, String inputString) {
         Ticket ticket = TicketStore.getTicket(inputString);
         return ticket == null ? ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_TICKET_NOTFOUND, new String[]{inputString})) : ArgumentParseResult.success(ticket);
      }

      public CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(CommandContext<CommandSender> context, CommandInput input) {
         return CompletableFuture.completedFuture((List)TicketStore.getAll().stream().map(Ticket::getName).map(Suggestion::suggestion).collect(Collectors.toList()));
      }

      // $FF: synthetic method
      TicketParser(Object x0) {
         this();
      }
   }
}
