/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;

public class VehicleUpdate
extends MTVSubCommand {
    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.update")) {
            return true;
        }
        if (!((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)).booleanValue()) {
            this.sendMessage(Message.UPDATE_DISABLED);
            return false;
        }
        PluginUpdater.updatePlugin(this.sender);
        return true;
    }
}

