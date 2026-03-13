/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PermissionHook;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook
extends PluginHookInstance
implements PermissionHook {
    @NotNull
    private final Permission permission;

    public VaultHook() {
        RegisteredServiceProvider registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        this.permission = registeredServiceProvider != null ? (Permission)registeredServiceProvider.getProvider() : NoOpPermission.INSTANCE;
    }

    @Override
    public boolean isEnabled() {
        return this.permission != NoOpPermission.INSTANCE;
    }

    @Override
    public String getName() {
        return HookPlugin.VAULT.getPluginName();
    }

    @Override
    public boolean removePerm(Player player, String string) {
        return this.permission.playerRemove(player, string);
    }

    @Override
    public boolean addPerm(Player player, String string) {
        return this.permission.playerAdd(player, string);
    }

    @Override
    public boolean isPermEnabled() {
        return this.permission.isEnabled();
    }

    public static class NoOpPermission
    extends Permission {
        public static final NoOpPermission INSTANCE = new NoOpPermission();

        private NoOpPermission() {
        }

        public String getName() {
            return "NoOpPermission";
        }

        public boolean isEnabled() {
            return false;
        }

        public boolean hasSuperPermsCompat() {
            return false;
        }

        public boolean playerHas(String string, String string2, String string3) {
            return false;
        }

        public boolean playerAdd(String string, String string2, String string3) {
            return false;
        }

        public boolean playerRemove(String string, String string2, String string3) {
            return false;
        }

        public boolean groupHas(String string, String string2, String string3) {
            return false;
        }

        public boolean groupAdd(String string, String string2, String string3) {
            return false;
        }

        public boolean groupRemove(String string, String string2, String string3) {
            return false;
        }

        public boolean playerInGroup(String string, String string2, String string3) {
            return false;
        }

        public boolean playerAddGroup(String string, String string2, String string3) {
            return false;
        }

        public boolean playerRemoveGroup(String string, String string2, String string3) {
            return false;
        }

        public String[] getPlayerGroups(String string, String string2) {
            return new String[0];
        }

        public String getPrimaryGroup(String string, String string2) {
            return "";
        }

        public String[] getGroups() {
            return new String[0];
        }

        public boolean hasGroupSupport() {
            return false;
        }
    }
}

