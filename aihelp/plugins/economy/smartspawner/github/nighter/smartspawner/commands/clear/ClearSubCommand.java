package github.nighter.smartspawner.commands.clear;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ClearSubCommand extends BaseSubCommand {
   private final ClearHologramsSubCommand clearHologramsSubCommand;
   private final ClearGhostSpawnersSubCommand clearGhostSpawnersSubCommand;

   public ClearSubCommand(SmartSpawner plugin) {
      super(plugin);
      this.clearHologramsSubCommand = new ClearHologramsSubCommand(plugin);
      this.clearGhostSpawnersSubCommand = new ClearGhostSpawnersSubCommand(plugin);
   }

   public String getName() {
      return "clear";
   }

   public String getPermission() {
      return "smartspawner.command.clear";
   }

   public String getDescription() {
      return "Clear holograms or ghost spawners";
   }

   public LiteralArgumentBuilder<CommandSourceStack> build() {
      LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName());
      builder.requires((source) -> {
         return this.hasPermission(source.getSender());
      });
      builder.then(this.clearHologramsSubCommand.build());
      builder.then(this.clearGhostSpawnersSubCommand.build());
      return builder;
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
      this.plugin.getMessageService().sendMessage(sender, "clear_command_usage");
      return 0;
   }
}
