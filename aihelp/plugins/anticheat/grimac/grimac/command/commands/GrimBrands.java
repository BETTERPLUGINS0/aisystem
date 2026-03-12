package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.description.Description;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Objects;

public class GrimBrands implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("brands", Description.of("Toggle brands for the sender")).permission("grim.brand").handler(this::handleBrands));
   }

   private void handleBrands(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      if (sender.isPlayer()) {
         GrimAPI.INSTANCE.getAlertManager().toggleBrands((PlatformPlayer)Objects.requireNonNull(((Sender)context.sender()).getPlatformPlayer()), false);
      } else if (sender.isConsole()) {
         GrimAPI.INSTANCE.getAlertManager().toggleConsoleBrands();
      }

   }
}
