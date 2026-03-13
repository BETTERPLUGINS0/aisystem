/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.effects.effects.effects;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class AdvancedEffect
implements Listener {
    private final String effectName;
    private final String description;
    private final String usage;
    private final JavaPlugin plugin;
    private ImmutableMap<Integer, Class> argumentType = ImmutableMap.builder().build();
    private int weight = 0;
    private boolean blockEffect = false;
    private boolean exemptFromAC = false;

    public AdvancedEffect(JavaPlugin javaPlugin, String string) {
        this.effectName = string;
        this.plugin = javaPlugin;
        this.description = "";
        this.usage = "";
    }

    public AdvancedEffect(JavaPlugin javaPlugin, String string, String string2, String string3) {
        this.effectName = string;
        this.plugin = javaPlugin;
        this.description = string2;
        this.usage = string3.replace("%e", string);
    }

    public AdvancedEffect(JavaPlugin javaPlugin, String string, String string2, String string3, int n) {
        this.effectName = string;
        this.plugin = javaPlugin;
        this.description = string2;
        this.usage = string3.replace("%e", string);
        this.weight = n;
    }

    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return false;
    }

    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        return false;
    }

    public boolean hasStringArgument() {
        return this.argumentType.containsValue(String.class);
    }

    public void addArgument(int n, Class clazz) {
        this.argumentType = ImmutableMap.builder().putAll(this.argumentType).put(n, clazz).build();
    }

    public String getName() {
        return this.effectName;
    }

    @NonNull
    public LivingEntity getOtherEntity(LivingEntity livingEntity, ExecutionTask executionTask) {
        if (executionTask.getBuilder().getAttacker() != null && executionTask.getBuilder().getAttacker().equals((Object)livingEntity)) {
            return executionTask.getBuilder().getVictim();
        }
        return executionTask.getBuilder().getAttacker();
    }

    public static void updateItem(ActionExecutionBuilder actionExecutionBuilder, LivingEntity livingEntity, @Nullable ItemStack itemStack, RollItemType rollItemType) {
        ItemsAdderHook itemsAdderHook;
        actionExecutionBuilder.setItem(itemStack);
        PluginHookInstance pluginHookInstance = HooksHandler.getHook(HookPlugin.ITEMSADDER);
        if (pluginHookInstance instanceof ItemsAdderHook && (itemsAdderHook = (ItemsAdderHook)pluginHookInstance).isCustomItem(itemStack) || itemStack == null || itemStack.isEmpty()) {
            boolean bl = livingEntity instanceof Player;
            switch (rollItemType) {
                case HAND: {
                    livingEntity.getEquipment().setItem(EquipmentSlot.HAND, itemStack);
                    break;
                }
                case OFFHAND: {
                    livingEntity.getEquipment().setItemInOffHand(itemStack);
                    break;
                }
                case HELMET: 
                case CHESTPLATE: 
                case LEGGINGS: 
                case BOOTS: {
                    ArmorType.setArmorItem(livingEntity, itemStack);
                    break;
                }
                default: {
                    if (ASManager.itemStackEquals(livingEntity.getEquipment().getItemInMainHand(), itemStack, false)) {
                        livingEntity.getEquipment().setItemInMainHand(itemStack);
                        break;
                    }
                    if (ASManager.itemStackEquals(livingEntity.getEquipment().getItemInOffHand(), itemStack, false)) {
                        livingEntity.getEquipment().setItemInOffHand(itemStack);
                        break;
                    }
                    if (ArmorType.matchType(itemStack) != null) {
                        ArmorType.setArmorItem(livingEntity, itemStack);
                        break;
                    }
                    if (bl) {
                        ASManager.giveItem((Player)livingEntity, itemStack);
                        break;
                    }
                    if (itemStack == null) break;
                    livingEntity.getWorld().dropItem(livingEntity.getLocation(), itemStack);
                }
            }
        }
    }

    public void warn(String string) {
        EffectsHandler.getInstance().getLogger().warning("Abilities warning: " + string);
    }

    public String getEffectName() {
        return this.effectName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public ImmutableMap<Integer, Class> getArgumentType() {
        return this.argumentType;
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean isBlockEffect() {
        return this.blockEffect;
    }

    public boolean isExemptFromAC() {
        return this.exemptFromAC;
    }

    public void setWeight(int n) {
        this.weight = n;
    }

    public void setBlockEffect(boolean bl) {
        this.blockEffect = bl;
    }

    public void setExemptFromAC(boolean bl) {
        this.exemptFromAC = bl;
    }
}

