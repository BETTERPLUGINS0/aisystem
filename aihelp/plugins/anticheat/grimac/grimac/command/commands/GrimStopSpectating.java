package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.command.CloudCommandService;
import ac.grim.grimac.command.requirements.PlayerSenderRequirement;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringParser;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.Requirement;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.List;
import java.util.Objects;

public class GrimStopSpectating implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("stopspectating").permission("grim.spectate").optional("here", StringParser.stringParser(), SuggestionProvider.blocking((ctx, in) -> {
         return ((Sender)ctx.sender()).hasPermission("grim.spectate.stophere") ? List.of(Suggestion.suggestion("here")) : List.of();
      })).handler(this::onStopSpectate).apply(CloudCommandService.REQUIREMENT_FACTORY.create((Requirement[])(PlayerSenderRequirement.PLAYER_SENDER_REQUIREMENT))));
   }

   public void onStopSpectate(CommandContext<Sender> commandContext) {
      Sender sender = (Sender)commandContext.sender();
      String string = (String)commandContext.getOrDefault("here", (Object)null);
      if (GrimAPI.INSTANCE.getSpectateManager().isSpectating(sender.getUniqueId())) {
         boolean teleportBack = string == null || !string.equalsIgnoreCase("here") || !sender.hasPermission("grim.spectate.stophere");
         GrimAPI.INSTANCE.getSpectateManager().disable((PlatformPlayer)Objects.requireNonNull(sender.getPlatformPlayer()), teleportBack);
      } else {
         sender.sendMessage(MessageUtil.getParsedComponent(sender, "cannot-spectate-return", "%prefix% &cYou can only do this after spectating a player."));
      }

   }
}
