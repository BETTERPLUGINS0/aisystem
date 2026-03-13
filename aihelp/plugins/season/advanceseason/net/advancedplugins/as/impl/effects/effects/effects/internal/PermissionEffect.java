/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.PermissionHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionEffect
extends AdvancedEffect {
    public PermissionEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PERMISSION", "Toggle player's permission", "%e:<TEXT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return true;
        }
        PermissionHook permissionHook = null;
        if (HooksHandler.isEnabled(HookPlugin.LUCKPERMS)) {
            permissionHook = (PermissionHook)((Object)HooksHandler.getHook(HookPlugin.LUCKPERMS));
        } else if (HooksHandler.isEnabled(HookPlugin.VAULT)) {
            permissionHook = (PermissionHook)((Object)HooksHandler.getHook(HookPlugin.VAULT));
        }
        if (permissionHook == null || !permissionHook.isPermEnabled()) {
            executionTask.reportIssue(this.getName(), "No permission source found. Currently supported: LuckPerms/Vault");
            return true;
        }
        String string = stringArray[0];
        if (livingEntity.hasPermission(string)) {
            permissionHook.removePerm((Player)livingEntity, string);
        } else {
            permissionHook.addPerm((Player)livingEntity, string);
        }
        return true;
    }
}

