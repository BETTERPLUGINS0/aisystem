package github.nighter.smartspawner.commands.prices;

import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import github.nighter.smartspawner.language.MessageService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public class PricesSubCommand extends BaseSubCommand {
   private final MessageService messageService;
   private final PricesGUI pricesGUI;

   public PricesSubCommand(SmartSpawner plugin) {
      super(plugin);
      this.messageService = plugin.getMessageService();
      this.pricesGUI = new PricesGUI(plugin);
   }

   public String getName() {
      return "prices";
   }

   public String getPermission() {
      return "smartspawner.command.prices";
   }

   public String getDescription() {
      return "View sell prices of spawner items";
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      if (!this.isPlayer(((CommandSourceStack)context.getSource()).getSender())) {
         return 0;
      } else {
         Player player = this.getPlayer(((CommandSourceStack)context.getSource()).getSender());
         if (!this.plugin.hasSellIntegration()) {
            this.messageService.sendMessage(player, "prices_not_available");
            return 0;
         } else {
            this.pricesGUI.openPricesGUI(player, 1);
            return 1;
         }
      }
   }
}
