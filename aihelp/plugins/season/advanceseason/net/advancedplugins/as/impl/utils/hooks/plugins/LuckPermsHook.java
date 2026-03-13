/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.luckperms.api.LuckPermsProvider
 *  net.luckperms.api.model.user.User
 *  net.luckperms.api.model.user.UserManager
 *  net.luckperms.api.node.Node
 *  net.luckperms.api.node.types.PermissionNode
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PermissionHook;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;

public class LuckPermsHook
extends PluginHookInstance
implements PermissionHook {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.LUCKPERMS.getPluginName();
    }

    @Override
    public boolean removePerm(Player player, String string) {
        UserManager userManager = LuckPermsProvider.get().getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        boolean bl = user.data().remove((Node)PermissionNode.builder((String)string).build()).wasSuccessful();
        userManager.saveUser(user);
        return bl;
    }

    @Override
    public boolean addPerm(Player player, String string) {
        UserManager userManager = LuckPermsProvider.get().getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        boolean bl = user.data().add((Node)PermissionNode.builder((String)string).build()).wasSuccessful();
        userManager.saveUser(user);
        return bl;
    }

    public String getGroup(Player player) {
        UserManager userManager = LuckPermsProvider.get().getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        try {
            return user.getPrimaryGroup();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    @Override
    public boolean isPermEnabled() {
        return this.isEnabled();
    }
}

