package ac.grim.grimac.command.commands;

import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.predictionengine.MovementCheckRunner;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;

public class GrimPerf {
   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      Command.Builder<Sender> grimCommand = commandManager.commandBuilder("grim", new String[]{"grimac"});
      Command.Builder<Sender> configuredBuilder = grimCommand.literal("perf", "performance").permission("grim.performance").handler(this::handlePerformance);
      commandManager.command(configuredBuilder);
   }

   private void handlePerformance(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      double millis = MovementCheckRunner.predictionNanos / 1000000.0D;
      double longMillis = MovementCheckRunner.longPredictionNanos / 1000000.0D;
      Component message1 = ((TextComponent.Builder)((TextComponent.Builder)Component.text().append(Component.text("Milliseconds per prediction (avg. 500): ", (TextColor)NamedTextColor.GRAY))).append(Component.text(millis, (TextColor)NamedTextColor.WHITE))).build();
      Component message2 = ((TextComponent.Builder)((TextComponent.Builder)Component.text().append(Component.text("Milliseconds per prediction (avg. 20k): ", (TextColor)NamedTextColor.GRAY))).append(Component.text(longMillis, (TextColor)NamedTextColor.WHITE))).build();
      sender.sendMessage((Component)message1);
      sender.sendMessage((Component)message2);
   }
}
