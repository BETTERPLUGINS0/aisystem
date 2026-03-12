package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;

public class GrimReload implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("reload").permission("grim.reload").handler(this::handleReload));
   }

   private void handleReload(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      sender.sendMessage(MessageUtil.getParsedComponent(sender, "reloading", "%prefix% &7Reloading config..."));
      GrimAPI.INSTANCE.getExternalAPI().reloadAsync().exceptionally((throwable) -> {
         return false;
      }).thenAccept((bool) -> {
         Component message = bool ? MessageUtil.getParsedComponent(sender, "reloaded", "%prefix% &fConfig has been reloaded.") : MessageUtil.getParsedComponent(sender, "reload-failed", "%prefix% &cFailed to reload config.");
         sender.sendMessage(message);
      });
   }
}
