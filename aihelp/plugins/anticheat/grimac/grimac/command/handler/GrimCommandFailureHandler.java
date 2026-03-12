package ac.grim.grimac.command.handler;

import ac.grim.grimac.command.SenderRequirement;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.RequirementFailureHandler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public class GrimCommandFailureHandler implements RequirementFailureHandler<Sender, SenderRequirement> {
   public void handleFailure(@NotNull CommandContext<Sender> context, @NotNull SenderRequirement requirement) {
      ((Sender)context.sender()).sendMessage(requirement.errorMessage((Sender)context.sender()));
   }
}
