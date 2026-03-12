package ac.grim.grimac.command;

import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.Requirement;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface SenderRequirement extends Requirement<Sender, SenderRequirement> {
   @NotNull
   Component errorMessage(Sender var1);
}
