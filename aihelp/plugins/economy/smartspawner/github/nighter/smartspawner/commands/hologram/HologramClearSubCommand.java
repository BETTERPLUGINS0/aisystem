package github.nighter.smartspawner.commands.hologram;

import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HologramClearSubCommand extends BaseSubCommand {
   public HologramClearSubCommand(SmartSpawner plugin) {
      super(plugin);
   }

   public String getName() {
      return "clear";
   }

   public String getPermission() {
      return "smartspawner.command.hologram";
   }

   public String getDescription() {
      return "Clear all text display holograms";
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();

      try {
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:kill @e[type=text_display]");
         this.plugin.getMessageService().sendMessage(sender, "command_hologram_cleared");
         return 1;
      } catch (Exception var4) {
         this.plugin.getLogger().severe("Error clearing holograms: " + var4.getMessage());
         this.plugin.getMessageService().sendMessage(sender, "command_hologram_clear_error");
         return 0;
      }
   }
}
