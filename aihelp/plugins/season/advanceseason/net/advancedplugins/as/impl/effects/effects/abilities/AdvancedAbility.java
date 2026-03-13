/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.effects.effects.abilities;

import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class AdvancedAbility {
    private String command = null;
    private String cooldownMessage = null;
    private int cooldown;
    private int repeatingDelay;
    private boolean repeatingInstantApply;
    private int souls;
    private double chance;
    private List<String> conditions;
    private List<String> effects;
    private List<String> whitelist = null;
    private List<String> blacklist = null;
    private List<String> worldBlacklist = null;
    private String name;
    private List<String> type;
    @Nullable
    private ConfigurationSection section;

    public String getNameNoLevel() {
        return this.name.split(", ")[0];
    }

    public AdvancedAbility setSection(ConfigurationSection configurationSection) {
        this.section = configurationSection;
        return this;
    }

    public AdvancedAbility setName(String string) {
        this.name = string;
        return this;
    }

    public AdvancedAbility setCooldown(int n) {
        this.cooldown = n;
        return this;
    }

    public AdvancedAbility setCommand(String string) {
        this.command = string;
        return this;
    }

    public AdvancedAbility setChance(double d) {
        this.chance = d;
        return this;
    }

    public AdvancedAbility setConditions(List<String> list) {
        this.conditions = list;
        return this;
    }

    public AdvancedAbility setBlacklist(List<String> list) {
        this.blacklist = list;
        return this;
    }

    public AdvancedAbility setWhitelist(List<String> list) {
        this.whitelist = list;
        return this;
    }

    public AdvancedAbility setEffects(List<String> list) {
        this.effects = list;
        return this;
    }

    public List<String> getTypes() {
        return this.type;
    }

    public AdvancedAbility setType(List<String> list) {
        this.type = list;
        return this;
    }

    public AdvancedAbility setCooldownMessage(String string) {
        this.cooldownMessage = string;
        return this;
    }

    public AdvancedAbility setRepeatingDelay(int n) {
        this.repeatingDelay = n;
        return this;
    }

    public AdvancedAbility setRepeatingInstantApply(boolean bl) {
        this.repeatingInstantApply = bl;
        return this;
    }

    public AdvancedAbility setSouls(int n) {
        this.souls = n;
        return this;
    }

    public AdvancedAbility setWorldBlacklist(List<String> list) {
        this.worldBlacklist = list;
        return this;
    }

    public static AdvancedAbility builder() {
        return new AdvancedAbility();
    }

    public static AdvancedAbility readFromConfig(String string, List<String> list, ConfigurationSection configurationSection) {
        return AdvancedAbility.builder().setSection(configurationSection).setCommand(configurationSection.getString("command")).setChance(configurationSection.getDouble("chance", 100.0)).setCooldown(configurationSection.getInt("cooldown", 0)).setCooldownMessage(configurationSection.getString("cooldownMessage")).setSouls(configurationSection.getInt("souls", 0)).setEffects(configurationSection.getStringList("effects")).setConditions(configurationSection.getStringList("conditions")).setName(string).setWorldBlacklist(configurationSection.getStringList("settings.worldBlacklist")).setType(list).setBlacklist(configurationSection.getStringList("settings.blacklist")).setRepeatingDelay(configurationSection.getInt("time", 1)).setRepeatingInstantApply(configurationSection.getBoolean("instantApply", true)).setWhitelist(configurationSection.getStringList("settings.whitelist"));
    }

    public String getCommand() {
        return this.command;
    }

    public String getCooldownMessage() {
        return this.cooldownMessage;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public int getRepeatingDelay() {
        return this.repeatingDelay;
    }

    public boolean isRepeatingInstantApply() {
        return this.repeatingInstantApply;
    }

    public int getSouls() {
        return this.souls;
    }

    public double getChance() {
        return this.chance;
    }

    public List<String> getConditions() {
        return this.conditions;
    }

    public List<String> getEffects() {
        return this.effects;
    }

    public List<String> getWhitelist() {
        return this.whitelist;
    }

    public List<String> getBlacklist() {
        return this.blacklist;
    }

    public List<String> getWorldBlacklist() {
        return this.worldBlacklist;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public ConfigurationSection getSection() {
        return this.section;
    }
}

