/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Bukkit;

public class VehicleVersion
extends MTVSubCommand {
    public VehicleVersion() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.admin")) {
            return true;
        }
        String pluginVersion = VersionModule.getPluginVersion();
        String isLatest = PluginUpdater.isLatestVersion() && !VersionModule.isDevRelease ? " (latest)" : "";
        String serverVersion = Bukkit.getVersion();
        this.sender.sendMessage(String.format("\u00a72Running \u00a7aMTVehicles v%s\u00a72%s.", pluginVersion, isLatest));
        this.sender.sendMessage(String.format("\u00a72Your server is running \u00a7a%s\u00a72.", serverVersion));
        if (!DependencyModule.loadedDependencies.isEmpty()) {
            String dependencies = "";
            int numberOfDependencies = 0;
            for (SoftDependency dependency : DependencyModule.loadedDependencies) {
                dependencies = numberOfDependencies == 0 ? dependencies + dependency.getName() : dependencies + ", " + dependency.getName();
                ++numberOfDependencies;
            }
            if (DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
                dependencies = !DependencyModule.vault.isEconomySetUp() ? dependencies.replace("Vault", "\u00a7a\u00a7mVault\u00a7a") : dependencies.replace("Vault", "Vault (" + DependencyModule.vault.getEconomyName() + ")");
            }
            this.sender.sendMessage(String.format("\u00a72Loaded dependencies (%s\u00a72): \u00a7a%s\u00a72.", numberOfDependencies, dependencies));
        } else {
            this.sender.sendMessage(String.format("\u00a72There are no loaded dependencies.", new Object[0]));
        }
        if (VersionModule.isPreRelease) {
            this.sender.sendMessage("\u00a7e-----");
            if (VersionModule.isDevRelease) {
                this.sender.sendMessage(TextUtils.colorize("&cWarning: You're using a dev-build. Auto-updater may be confused."));
            }
            this.sendMessage(Message.USING_PRE_RELEASE);
        }
        return true;
    }
}

