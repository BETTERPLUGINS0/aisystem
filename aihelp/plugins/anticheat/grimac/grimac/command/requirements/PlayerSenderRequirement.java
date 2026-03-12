package ac.grim.grimac.command.requirements;

import ac.grim.grimac.command.SenderRequirement;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;

public final class PlayerSenderRequirement implements SenderRequirement {
   public static final PlayerSenderRequirement PLAYER_SENDER_REQUIREMENT = new PlayerSenderRequirement();

   @NotNull
   public Component errorMessage(Sender sender) {
      return MessageUtil.getParsedComponent(sender, "run-as-player", "%prefix% &cThis command can only be used by players!");
   }

   public boolean evaluateRequirement(@NotNull CommandContext<Sender> commandContext) {
      return ((Sender)commandContext.sender()).isPlayer();
   }
}
