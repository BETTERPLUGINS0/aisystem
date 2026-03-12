package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.command.CloudCommandService;
import ac.grim.grimac.command.requirements.PlayerSenderRequirement;
import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.Requirement;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.Objects;

public class GrimSpectate implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("spectate").permission("grim.spectate").required("target", adapter.singlePlayerSelectorParser()).handler(this::handleSpectate).apply(CloudCommandService.REQUIREMENT_FACTORY.create((Requirement[])(PlayerSenderRequirement.PLAYER_SENDER_REQUIREMENT))));
   }

   private void handleSpectate(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      PlayerSelector targetSelectorResults = (PlayerSelector)context.getOrDefault("target", (Object)null);
      if (targetSelectorResults != null) {
         PlatformPlayer targetPlatformPlayer = targetSelectorResults.getSinglePlayer().getPlatformPlayer();
         if (targetPlatformPlayer != null && targetPlatformPlayer.getUniqueId().equals(sender.getUniqueId())) {
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "cannot-run-on-self", "%prefix% &cYou cannot use this command on yourself!"));
         } else if (targetPlatformPlayer != null && targetPlatformPlayer.isExternalPlayer()) {
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "player-not-this-server", "%prefix% &cThis player isn't on this server!"));
         } else {
            PlatformPlayer platformPlayer = (PlatformPlayer)Objects.requireNonNull(sender.getPlatformPlayer());
            if (GrimAPI.INSTANCE.getSpectateManager().enable(platformPlayer)) {
               sender.sendMessage(MessageUtil.getParsedComponent(sender, "spectate-return", "<click:run_command:/grim stopspectating><hover:show_text:\"/grim stopspectating\">\n%prefix% &fClick here to return to previous location\n</hover></click>"));
            }

            platformPlayer.setGameMode(GameMode.SPECTATOR);
            platformPlayer.teleportAsync(((PlatformPlayer)Objects.requireNonNull(targetPlatformPlayer)).getLocation());
         }
      }
   }
}
