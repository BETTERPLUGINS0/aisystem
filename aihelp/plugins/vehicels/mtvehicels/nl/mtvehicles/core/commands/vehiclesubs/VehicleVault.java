/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;

public class VehicleVault
extends MTVSubCommand {
    public VehicleVault() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.admin")) {
            return true;
        }
        if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
            this.sendMessage(Message.NO_VAULT_DEPENDENCY);
            return true;
        }
        if (this.arguments.length > 1 && this.arguments[1].equalsIgnoreCase("setup")) {
            if (DependencyModule.vault.isEconomySetUp()) {
                this.sendMessage(Message.VAULT_SETUP_ALREADY_HOOKED);
                return true;
            }
            if (DependencyModule.vault.retryEconomySetup()) {
                String msg = TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VAULT_SETUP_SUCCESS));
                this.sendMessage(msg.replace("{plugin}", DependencyModule.vault.getEconomyName()));
            } else {
                this.sendMessage(Message.VAULT_SETUP_FAIL);
            }
            return true;
        }
        if (DependencyModule.vault.isEconomySetUp()) {
            String msg = TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VAULT_INFO));
            this.sendMessage(msg.replace("{plugin}", DependencyModule.vault.getEconomyName()));
        } else {
            this.sendMessage(Message.VAULT_NO_ECONOMY_FOUND);
        }
        return true;
    }
}

