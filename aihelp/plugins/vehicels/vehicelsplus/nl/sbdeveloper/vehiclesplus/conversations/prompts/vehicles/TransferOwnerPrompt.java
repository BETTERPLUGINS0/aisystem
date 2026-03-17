/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 */
package nl.sbdeveloper.vehiclesplus.conversations.prompts.vehicles;

import java.util.Map;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.generic.StringPrompt;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransferOwnerPrompt
extends StringPrompt {
    private final DrivableVehicle vehicle;

    public TransferOwnerPrompt(DrivableVehicle drivableVehicle) {
        this.vehicle = drivableVehicle;
    }

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Locale.getMessage(PluginMessage.PROMPTS_VEHICLES_TRANSFER);
    }

    @Override
    @Nullable
    public Prompt acceptFullyValidatedInput(@NotNull ConversationContext conversationContext, @Nullable String string) {
        if (string == null) {
            return END_OF_CONVERSATION;
        }
        Optional<Garage> optional = VehiclesPlusAPI.getGarage(string);
        if (optional.isEmpty()) {
            conversationContext.getForWhom().sendRawMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_INVALID));
            return END_OF_CONVERSATION;
        }
        Garage garage = optional.get();
        Garage garage2 = this.vehicle.getGarage();
        garage2.removeVehicle(this.vehicle.getUuid());
        garage2.save();
        garage.addVehicle(this.vehicle.getUuid());
        garage.save();
        conversationContext.getForWhom().sendRawMessage(Locale.getMessage(PluginMessage.PROMPTS_VEHICLES_MOVED, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())));
        return END_OF_CONVERSATION;
    }
}

