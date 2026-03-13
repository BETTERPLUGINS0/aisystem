package github.nighter.smartspawner.commands.hologram;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HologramSubCommand extends BaseSubCommand {
   private final SpawnerManager spawnerManager;
   private final HologramClearSubCommand clearSubCommand;

   public HologramSubCommand(SmartSpawner plugin) {
      super(plugin);
      this.spawnerManager = plugin.getSpawnerManager();
      this.clearSubCommand = new HologramClearSubCommand(plugin);
   }

   public String getName() {
      return "hologram";
   }

   public String getPermission() {
      return "smartspawner.command.hologram";
   }

   public String getDescription() {
      return "Toggle hologram display for spawners";
   }

   public LiteralArgumentBuilder<CommandSourceStack> build() {
      LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName());
      builder.requires((source) -> {
         return this.hasPermission(source.getSender());
      });
      builder.executes(this::execute);
      builder.then(this.clearSubCommand.build());
      return builder;
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
      this.logCommandExecution(context);

      try {
         boolean newValue = !this.plugin.getConfig().getBoolean("hologram.enabled");
         this.plugin.getConfig().set("hologram.enabled", newValue);
         this.plugin.saveConfig();
         this.spawnerManager.refreshAllHolograms();
         String messageKey = newValue ? "command_hologram_enabled" : "command_hologram_disabled";
         this.plugin.getMessageService().sendMessage(sender, messageKey);
         return 1;
      } catch (Exception var5) {
         this.plugin.getLogger().severe("Error toggling holograms: " + var5.getMessage());
         return 0;
      }
   }
}
