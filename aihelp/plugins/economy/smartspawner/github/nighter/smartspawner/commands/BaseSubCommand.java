package github.nighter.smartspawner.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.logging.SpawnerEventType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseSubCommand {
   protected final SmartSpawner plugin;

   public abstract String getName();

   public abstract String getPermission();

   public abstract String getDescription();

   public abstract int execute(CommandContext<CommandSourceStack> var1);

   public LiteralArgumentBuilder<CommandSourceStack> build() {
      LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName());
      builder.requires((source) -> {
         return this.hasPermission(source.getSender());
      });
      builder.executes((context) -> {
         this.logCommandExecution(context);
         return this.execute(context);
      });
      return builder;
   }

   protected boolean hasPermission(CommandSender sender) {
      if (!(sender instanceof ConsoleCommandSender) && !(sender instanceof RemoteConsoleCommandSender)) {
         return sender.hasPermission(this.getPermission()) || sender.isOp();
      } else {
         return true;
      }
   }

   protected boolean isPlayer(CommandSender sender) {
      return sender instanceof Player;
   }

   protected Player getPlayer(CommandSender sender) {
      return this.isPlayer(sender) ? (Player)sender : null;
   }

   protected boolean isConsoleOrRcon(CommandSender sender) {
      return sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender;
   }

   protected void logCommandExecution(CommandContext<CommandSourceStack> context) {
      if (this.plugin.getSpawnerActionLogger() != null) {
         CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
         SpawnerEventType eventType;
         if (sender instanceof RemoteConsoleCommandSender) {
            eventType = SpawnerEventType.COMMAND_EXECUTE_RCON;
         } else if (sender instanceof ConsoleCommandSender) {
            eventType = SpawnerEventType.COMMAND_EXECUTE_CONSOLE;
         } else {
            eventType = SpawnerEventType.COMMAND_EXECUTE_PLAYER;
         }

         this.plugin.getSpawnerActionLogger().log(eventType, (builder) -> {
            if (sender instanceof Player) {
               Player player = (Player)sender;
               builder.player(player.getName(), player.getUniqueId());
            } else {
               builder.metadata("sender", sender.getName());
            }

            builder.metadata("command", this.getName());
            builder.metadata("full_command", context.getInput());
         });
      }
   }

   @Generated
   public BaseSubCommand(SmartSpawner plugin) {
      this.plugin = plugin;
   }
}
