package libs.com.ryderbelserion.vital.paper.commands.context;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import libs.com.ryderbelserion.vital.common.api.commands.context.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperCommandInfo extends CommandInfo<CommandSourceStack> {
   public PaperCommandInfo(@NotNull CommandContext<CommandSourceStack> context) {
      super(context);
   }

   @NotNull
   public final CommandSender getCommandSender() {
      return ((CommandSourceStack)this.getSource()).getSender();
   }

   @NotNull
   public final Player getPlayer() {
      return (Player)this.getCommandSender();
   }

   public final boolean isPlayer() {
      return this.getCommandSender() instanceof Player;
   }
}
