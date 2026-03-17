package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ITrainProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.tickets.Ticket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TicketSetProperty implements ITrainProperty<Set<String>> {
   @CommandTargetTrain
   @PropertyCheckPermission("ticket")
   @Command("train ticket list assigned")
   @CommandDescription("Displays the ticket names assigned to the train")
   private void getTrainTickets(CommandSender sender, TrainProperties properties) {
      Set<String> tickets = properties.getTickets();
      if (!tickets.isEmpty()) {
         sender.sendMessage(ChatColor.YELLOW + "Train has tickets: " + ChatColor.WHITE + StringUtil.combineNames(tickets));
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Train has tickets: " + ChatColor.RED + "None");
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("ticket")
   @Command("train ticket assign <ticket>")
   @CommandDescription("Assigns the ticket with the given name to the train")
   private void assignTrainTicket(CommandSender sender, TrainProperties properties, @Argument("ticket") Ticket ticket) {
      properties.addTicket(ticket.getName());
      sender.sendMessage(ChatColor.GREEN + "Ticket '" + ticket.getName() + "' assigned to train '" + properties.getTrainName() + "'!");
   }

   @PropertyCheckPermission("ticket")
   @Command("train ticket assign")
   @CommandDescription("Assigns the currently-edited ticket to the train")
   private void assignEditedTrainTicket(Player sender, TrainProperties properties, Ticket ticket) {
      this.assignTrainTicket(sender, properties, ticket);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ticket")
   @Command("train ticket unassign <ticket>")
   @CommandDescription("Un-assigns the ticket with the given name from the train")
   private void unassignTrainTicket(CommandSender sender, TrainProperties properties, @Argument("ticket") Ticket ticket) {
      if (properties.getTickets().contains(ticket.getName())) {
         properties.removeTicket(ticket.getName());
         sender.sendMessage(ChatColor.GREEN + "Ticket '" + ticket.getName() + "' un-assigned from train '" + properties.getTrainName() + "'!");
      } else {
         sender.sendMessage(ChatColor.RED + "Ticket '" + ticket.getName() + "' was not assigned");
      }

   }

   @PropertyCheckPermission("ticket")
   @Command("train ticket unassign")
   @CommandDescription("Un-assigns the currently-edited ticket from the train")
   private void unassignEditedTrainTicket(Player sender, TrainProperties properties, Ticket ticket) {
      this.unassignTrainTicket(sender, properties, ticket);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ticket")
   @Command("train ticket clearassigned")
   @CommandDescription("Un-assigns all tickets currently assigned to a train")
   private void clearAssignedTickets(CommandSender sender, TrainProperties properties) {
      properties.clearTickets();
      sender.sendMessage(ChatColor.YELLOW + "Tickets cleared of train '" + properties.getTrainName() + "'");
   }

   @PropertyParser("setticket|tickets set")
   public Set<String> parseSet(String input) {
      return input.isEmpty() ? Collections.emptySet() : Collections.singleton(input);
   }

   @PropertyParser("clrticket|cleartickets|tickets clear")
   public Set<String> parseClear(String input) {
      return Collections.emptySet();
   }

   @PropertyParser(
      value = "addticket|tickets add",
      processPerCart = true
   )
   public Set<String> parseAdd(PropertyParseContext<Set<String>> context) {
      if (!context.input().isEmpty() && !((Set)context.current()).contains(context.input())) {
         HashSet<String> newPerms = new HashSet((Collection)context.current());
         newPerms.add(context.input());
         return Collections.unmodifiableSet(newPerms);
      } else {
         return (Set)context.current();
      }
   }

   @PropertyParser(
      value = "remticket|tickets rem|tickets remove",
      processPerCart = true
   )
   public Set<String> parseRemove(PropertyParseContext<Set<String>> context) {
      if (!context.input().isEmpty() && ((Set)context.current()).contains(context.input())) {
         HashSet<String> newPerms = new HashSet((Collection)context.current());
         newPerms.remove(context.input());
         return Collections.unmodifiableSet(newPerms);
      } else {
         return (Set)context.current();
      }
   }

   @PropertySelectorCondition("ticket")
   public boolean selectorMatchesAnyTicket(TrainProperties properties, SelectorCondition condition) {
      return condition.matchesAnyText((Collection)this.get(properties));
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_TICKETS.has(sender);
   }

   public Set<String> getDefault() {
      return Collections.emptySet();
   }

   public Optional<Set<String>> readFromConfig(ConfigurationNode config) {
      return Util.getConfigStringSetOptional(config, "tickets");
   }

   public void writeToConfig(ConfigurationNode config, Optional<Set<String>> value) {
      Util.setConfigStringCollectionOptional(config, "tickets", value);
   }
}
