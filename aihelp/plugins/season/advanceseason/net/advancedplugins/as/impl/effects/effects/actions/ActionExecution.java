/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.api.AbilityPreactivateEvent;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.abilities.DisabledAbility;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.conditions.ConResult;
import net.advancedplugins.as.impl.effects.effects.conditions.ConditionType;
import net.advancedplugins.as.impl.effects.effects.conditions.Fractor;
import net.advancedplugins.as.impl.utils.ACooldown;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

public class ActionExecution {
    private boolean cancelled = false;
    private static final HashMap<UUID, List<DisabledAbility>> disabledAbilities = new HashMap();
    private final LinkedList<AdvancedAbility> effects = new LinkedList();
    private final LinkedList<String> allEffectsRaw = new LinkedList();
    private final ActionExecutionBuilder builder;
    private final HashMap<String, String> variables = new HashMap();

    public ActionExecution(ActionExecutionBuilder actionExecutionBuilder) {
        this.builder = actionExecutionBuilder;
    }

    public static void addDisabledAbility(UUID uUID2, String string, int n) {
        disabledAbilities.computeIfAbsent(uUID2, uUID -> new ArrayList()).add(new DisabledAbility((long)n * 1000L, string));
    }

    public static List<String> getDisabledAbilities(UUID uUID) {
        return ((List)disabledAbilities.getOrDefault(uUID, new ArrayList())).stream().filter(disabledAbility -> disabledAbility.getActivatesOn() - System.currentTimeMillis() > 0L).map(DisabledAbility::getAbility).collect(Collectors.toList());
    }

    public void run() {
        for (AdvancedAbility advancedAbility : this.effects) {
            if (this.isCancelled()) break;
            ExecutionTask executionTask = new ExecutionTask(advancedAbility, this, this.builder);
            executionTask.init();
        }
    }

    public LinkedList<String> getAllEffects() {
        return this.allEffectsRaw;
    }

    public void build() {
        Object object;
        if (this.builder == null || this.builder.getEffects() == null) {
            return;
        }
        ItemStack itemStack = this.builder.getItem();
        this.builder.globalVariables();
        this.variables.putAll(this.builder.getVariables());
        RollItemType rollItemType = this.builder.getItemType();
        boolean bl = false;
        if (!bl && rollItemType != null && itemStack != null) {
            object = itemStack.getType().name();
            if (!(switch (rollItemType) {
                case RollItemType.HELMET -> ArmorType.getHelmets().contains(object);
                case RollItemType.CHESTPLATE -> ArmorType.getChestPlates().contains(object);
                case RollItemType.LEGGINGS -> ArmorType.getLeggings().contains(object);
                case RollItemType.BOOTS -> ArmorType.getBoots().contains(object);
                default -> true;
            })) {
                return;
            }
        }
        object = this.builder.getAttacker();
        LivingEntity livingEntity = this.builder.getVictim();
        for (LivingEntity livingEntity2 : new LivingEntity[]{object, livingEntity}) {
            if (livingEntity2 != null) continue;
        }
        if (this.builder == null || this.builder.getEffects() == null) {
            return;
        }
        List<String> list = ActionExecution.getDisabledAbilities(this.builder.getMain().getUniqueId());
        block13: for (AdvancedAbility advancedAbility : this.builder.getEffects()) {
            double d;
            if (this.builder.getAllowedAbilities() != null && !this.builder.getAllowedAbilities().contains(advancedAbility) || advancedAbility == null) continue;
            if (advancedAbility.getCommand() != null) {
                PlayerCommandPreprocessEvent playerCommandPreprocessEvent = (PlayerCommandPreprocessEvent)this.builder.getEvent();
                if (!advancedAbility.getCommand().replace("/", "").equalsIgnoreCase(playerCommandPreprocessEvent.getMessage().replace("/", ""))) continue;
                playerCommandPreprocessEvent.setCancelled(true);
            }
            if (advancedAbility.getWorldBlacklist() != null && !advancedAbility.getWorldBlacklist().isEmpty()) {
                World world = this.builder.getMain().getWorld();
                if (advancedAbility.getWorldBlacklist().contains(world.getName())) continue;
            }
            if (!list.isEmpty() && list.contains(advancedAbility.getName().toLowerCase(Locale.ROOT).split(",")[0])) continue;
            int n = advancedAbility.getCooldown();
            if (ACooldown.isInCooldown(this.builder.getMain(), advancedAbility.getNameNoLevel())) {
                if (advancedAbility.getCooldownMessage() == null || !(this.builder.getMain() instanceof Player)) continue;
                this.builder.getMain().sendMessage(Text.modify(advancedAbility.getCooldownMessage()));
                continue;
            }
            double d2 = advancedAbility.getChance();
            if (!this.builder.shouldSkipConditions() && advancedAbility.getConditions() != null && !advancedAbility.getConditions().isEmpty()) {
                for (String string : advancedAbility.getConditions()) {
                    for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                        String string2 = entry.getKey();
                        String string3 = entry.getValue();
                        string = string.replace(string2, string3);
                    }
                    ConResult conResult = Fractor.getResult(string, object, livingEntity, this);
                    if (conResult.getOriginalCondition() == null) {
                        ASManager.reportIssue(new NullPointerException(), "Invalid condition for enchant '" + advancedAbility.getName() + "'");
                        continue;
                    }
                    if (conResult.getOriginalCondition() == ConditionType.ALLOW && conResult.getCondition() != ConditionType.ALLOW) continue block13;
                    switch (conResult.getCondition()) {
                        case ADD: {
                            d2 += (double)((Integer)conResult.getResult()).intValue();
                            break;
                        }
                        case FORCE: {
                            d2 = 100.0;
                            break;
                        }
                        case REMOVE: {
                            d2 -= (double)((Integer)conResult.getResult()).intValue();
                            break;
                        }
                        case STOP: {
                            continue block13;
                        }
                    }
                }
            }
            if (!this.builder.shouldSkipChances() && d2 < (d = ThreadLocalRandom.current().nextDouble() * 100.0)) continue;
            if (Bukkit.isPrimaryThread()) {
                AbilityPreactivateEvent abilityPreactivateEvent = new AbilityPreactivateEvent(advancedAbility, this.builder.getMain(), this.builder.getOther(), this);
                Bukkit.getPluginManager().callEvent(abilityPreactivateEvent);
                if (abilityPreactivateEvent.isCancelled()) continue;
            }
            if (n > 0) {
                ACooldown.putToCooldown(this.builder.getMain(), advancedAbility.getNameNoLevel(), n);
            }
            this.allEffectsRaw.addAll(advancedAbility.getEffects());
            this.effects.add(advancedAbility);
        }
    }

    public String parseVariables(String string) {
        for (Map.Entry<String, String> entry : this.variables.entrySet()) {
            String string2 = entry.getKey();
            String string3 = entry.getValue();
            string = string.replace(string2, string3);
        }
        return string;
    }

    public void addVariable(String string, String string2) {
        this.variables.put(string, string2);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    public LinkedList<AdvancedAbility> getEffects() {
        return this.effects;
    }

    public LinkedList<String> getAllEffectsRaw() {
        return this.allEffectsRaw;
    }

    public ActionExecutionBuilder getBuilder() {
        return this.builder;
    }

    public HashMap<String, String> getVariables() {
        return this.variables;
    }
}

