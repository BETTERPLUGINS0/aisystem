package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public class GrimVerbose implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("verbose").permission("grim.verbose").handler(this::handleVerbose));
   }

   private void handleVerbose(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      if (sender.isPlayer()) {
         GrimAPI.INSTANCE.getAlertManager().toggleVerbose(((Sender)context.sender()).getPlatformPlayer(), false);
      } else if (sender.isConsole()) {
         GrimAPI.INSTANCE.getAlertManager().toggleConsoleVerbose();
      }

   }
}
