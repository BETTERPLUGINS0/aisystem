package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.commands.arguments.EntityFilter;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.EntityArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.TargetEntityFallback;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.ui.villager.editor.VillagerEditorViewProvider;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;

class CommandEditVillager extends PlayerCommand {
   private static final String ARGUMENT_VILLAGER = "villager";

   CommandEditVillager() {
      super("editVillager");
      this.setDescription(Messages.commandDescriptionEditVillager);
      this.addArgument(new TargetEntityFallback(new EntityArgument("villager", EntityFilter.VILLAGER), EntityFilter.VILLAGER_TARGET));
   }

   public boolean testPermission(CommandSender sender) {
      if (!super.testPermission(sender)) {
         return false;
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.edit-villagers") || PermissionUtils.hasPermission(sender, "shopkeeper.edit-wandering-traders");
      }
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      AbstractVillager villager = (AbstractVillager)context.get("villager");
      VillagerEditorViewProvider viewProvider = new VillagerEditorViewProvider(villager);
      UISessionManager.getInstance().requestUI(viewProvider, player);
   }
}
