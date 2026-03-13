/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.effects.effects.actions;

import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;
import net.advancedplugins.as.impl.effects.effects.utils.Combo;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class AdvancedTrigger
implements Listener {
    private final String triggerName;
    private String description;
    private boolean comboEnabled = false;
    private boolean enabled = true;

    public AdvancedTrigger(String string) {
        this.triggerName = string;
        this.registerListener();
    }

    public void setEnabled(boolean bl) {
        if (!bl) {
            HandlerList.unregisterAll((Listener)this);
        } else if (bl && !this.enabled) {
            this.registerListener();
        }
        this.enabled = bl;
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)EffectsHandler.getInstance());
    }

    public ActionExecutionBuilder executionBuilder() {
        return new ActionExecutionBuilder(this.getTriggerName());
    }

    public void addCombo(UUID uUID) {
        Combo.add(uUID);
    }

    public void resetCombo(UUID uUID) {
        Combo.endCombos(uUID);
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public void setComboEnabled(boolean bl) {
        this.comboEnabled = bl;
    }

    public boolean isComboEnabled() {
        return this.comboEnabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}

