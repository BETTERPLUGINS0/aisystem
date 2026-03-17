/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;

public class VehicleLanguage
extends MTVSubCommand {
    public VehicleLanguage() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (this.sender.hasPermission("mtvehicles.language") || this.sender.hasPermission("mtvehicles.admin")) {
            LanguageUtils.openLanguageGUI(this.player);
        } else {
            this.sendMessage(Message.NO_PERMISSION);
        }
        return true;
    }
}

