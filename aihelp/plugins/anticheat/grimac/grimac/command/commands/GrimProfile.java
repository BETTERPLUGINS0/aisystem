package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.Iterator;
import java.util.Objects;

public class GrimProfile implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("profile").permission("grim.profile").required("target", adapter.singlePlayerSelectorParser()).handler(this::handleProfile));
   }

   private void handleProfile(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      PlayerSelector target = (PlayerSelector)context.get("target");
      PlatformPlayer targetPlatformPlayer = target.getSinglePlayer().getPlatformPlayer();
      if (((PlatformPlayer)Objects.requireNonNull(targetPlatformPlayer)).isExternalPlayer()) {
         sender.sendMessage(MessageUtil.getParsedComponent(sender, "player-not-this-server", "%prefix% &cThis player isn't on this server!"));
      } else {
         GrimPlayer grimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(targetPlatformPlayer.getUniqueId());
         if (grimPlayer == null) {
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "player-not-found", "%prefix% &cPlayer is exempt or offline!"));
         } else {
            Iterator var6 = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringList("profile").iterator();

            while(var6.hasNext()) {
               String message = (String)var6.next();
               Component component = MessageUtil.miniMessage(message);
               sender.sendMessage(MessageUtil.replacePlaceholders(grimPlayer, component));
            }

         }
      }
   }
}
