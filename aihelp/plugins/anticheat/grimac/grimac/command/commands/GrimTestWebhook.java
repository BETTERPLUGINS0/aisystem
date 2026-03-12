package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.data.webhook.discord.WebhookMessage;

public class GrimTestWebhook implements BuildableCommand {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("testwebhook").permission("grim.testwebhook").handler(this::handleTestWebhook));
   }

   private void handleTestWebhook(@NotNull CommandContext<Sender> context) {
      if (GrimAPI.INSTANCE.getDiscordManager().isDisabled()) {
         ((Sender)context.sender()).sendMessage(MessageUtil.miniMessage(GrimAPI.INSTANCE.getConfigManager().getWebhookNotEnabled()));
      } else {
         WebhookMessage webhookMessage = (new WebhookMessage()).content(GrimAPI.INSTANCE.getConfigManager().getWebhookTestMessage());
         GrimAPI.INSTANCE.getDiscordManager().sendWebhookMessage(webhookMessage).whenCompleteAsync((successful, throwable) -> {
            if (successful) {
               ((Sender)context.sender()).sendMessage(MessageUtil.miniMessage(GrimAPI.INSTANCE.getConfigManager().getWebhookTestSucceeded()));
            } else {
               ((Sender)context.sender()).sendMessage(MessageUtil.miniMessage(GrimAPI.INSTANCE.getConfigManager().getWebhookTestFailed()));
               if (throwable != null) {
                  LogUtil.error("Exception caught while sending a Discord webhook test alert", throwable);
               }

            }
         });
      }
   }
}
