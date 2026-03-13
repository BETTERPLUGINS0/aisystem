package github.nighter.smartspawner.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.clear.ClearSubCommand;
import github.nighter.smartspawner.commands.give.GiveSubCommand;
import github.nighter.smartspawner.commands.hologram.HologramSubCommand;
import github.nighter.smartspawner.commands.list.ListSubCommand;
import github.nighter.smartspawner.commands.prices.PricesSubCommand;
import github.nighter.smartspawner.commands.reload.ReloadSubCommand;
import github.nighter.smartspawner.language.MessageService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MainCommand {
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final List<BaseSubCommand> subCommands;

   public MainCommand(SmartSpawner plugin) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.subCommands = List.of(new ReloadSubCommand(plugin), new GiveSubCommand(plugin), new ListSubCommand(plugin), new HologramSubCommand(plugin), new PricesSubCommand(plugin), new ClearSubCommand(plugin));
   }

   public LiteralCommandNode<CommandSourceStack> buildCommand() {
      return this.buildCommandWithName("smartspawner");
   }

   public LiteralCommandNode<CommandSourceStack> buildAliasCommand() {
      return this.buildCommandWithName("spawner");
   }

   public LiteralCommandNode<CommandSourceStack> buildAliasCommand2() {
      return this.buildCommandWithName("ss");
   }

   private LiteralCommandNode<CommandSourceStack> buildCommandWithName(String name) {
      LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(name);
      builder.requires((source) -> {
         CommandSender sender = source.getSender();
         if (!(sender instanceof ConsoleCommandSender) && !(sender instanceof RemoteConsoleCommandSender)) {
            if (!(sender instanceof Player)) {
               return sender.hasPermission("smartspawner.command.use");
            } else {
               Player player = (Player)sender;
               return player.hasPermission("smartspawner.command.use") || player.isOp();
            }
         } else {
            return true;
         }
      });
      Iterator var3 = this.subCommands.iterator();

      while(var3.hasNext()) {
         BaseSubCommand subCommand = (BaseSubCommand)var3.next();
         builder.then(subCommand.build());
      }

      return builder.build();
   }

   @Generated
   public MainCommand(SmartSpawner plugin, MessageService messageService, List<BaseSubCommand> subCommands) {
      this.plugin = plugin;
      this.messageService = messageService;
      this.subCommands = subCommands;
   }
}
