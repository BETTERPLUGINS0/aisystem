/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.as.impl.effects.effects.actions.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.advancedplugins.as.impl.effects.api.EffectsActivateEvent;
import net.advancedplugins.as.impl.effects.api.EffectsActivatedEvent;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecutionBuilder;
import net.advancedplugins.as.impl.effects.effects.actions.handlers.DamageHandler;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.TargetResults;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ArmorWearTrigger;
import net.advancedplugins.as.impl.effects.effects.variables.DynamicVariable;
import net.advancedplugins.as.impl.effects.effects.variables.Variables;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.LocalLocation;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.MythicMobsHook;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExecutionTask {
    private ActionExecution actionExecution;
    private ActionExecutionBuilder builder;
    private Location location;
    private AdvancedAbility ability;
    private boolean targetsAreDead = false;
    private boolean soulboundOnly = false;
    private boolean sets = false;
    private boolean wait = false;
    private int currentWait = 0;
    private TargetResults targetResults;
    private final DamageHandler damageHandler = new DamageHandler();

    public ExecutionTask(AdvancedAbility advancedAbility, ActionExecution actionExecution, ActionExecutionBuilder actionExecutionBuilder) {
        this.ability = advancedAbility;
        this.actionExecution = actionExecution;
        this.builder = actionExecutionBuilder;
        this.location = actionExecutionBuilder.getMain().getLocation();
    }

    public void init() {
        if (this.soulboundOnly && this.actionExecution.getAllEffects().stream().noneMatch(string -> string.equalsIgnoreCase("KEEP_ON_DEATH"))) {
            return;
        }
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>();
        if (!this.sets && Bukkit.isPrimaryThread()) {
            EffectsActivateEvent effectsActivateEvent = new EffectsActivateEvent(this.ability, this.builder.getMain(), this.builder.getOther(), this);
            Bukkit.getPluginManager().callEvent(effectsActivateEvent);
            if (this.ability.getEffects().isEmpty() || effectsActivateEvent.isCancelled()) {
                return;
            }
            if (effectsActivateEvent.getOtherTargets() != null) {
                arrayList.addAll(effectsActivateEvent.getOtherTargets());
            }
        }
        for (String string2 : this.ability.getEffects()) {
            final ArrayList<Location> arrayList2 = new ArrayList<Location>();
            LinkedList<LivingEntity> linkedList = new LinkedList<LivingEntity>();
            string2 = this.actionExecution.parseVariables(string2);
            this.updateMain(linkedList, arrayList2, string2);
            linkedList.addAll(arrayList);
            if (string2.startsWith("WAIT")) {
                this.wait = true;
            }
            if (this.targetResults.getEffect() != null) {
                string2 = this.actionExecution.parseVariables(this.targetResults.getEffect());
            }
            String[] stringArray = string2.split(":");
            if (string2.startsWith("WAIT")) {
                this.currentWait += ASManager.parseInt(stringArray[1], 0);
                continue;
            }
            if (!this.wait) {
                if (!arrayList2.isEmpty()) {
                    for (Location location : arrayList2) {
                        this.activate(string2, this.builder.getMain(), location);
                    }
                    continue;
                }
                for (LivingEntity livingEntity : linkedList) {
                    this.activate(string2, livingEntity, null);
                }
                continue;
            }
            Iterator iterator = string2;
            new BukkitRunnable((String)((Object)iterator), linkedList){
                final /* synthetic */ String val$finalEffect;
                final /* synthetic */ LinkedList val$targetEntities;
                {
                    this.val$finalEffect = string;
                    this.val$targetEntities = linkedList;
                }

                public void run() {
                    ExecutionTask.this.builder.setDamageEventNotGoingToRun(true);
                    if (!arrayList2.isEmpty()) {
                        for (Location location : arrayList2) {
                            ExecutionTask.this.activate(this.val$finalEffect, ExecutionTask.this.builder.getMain(), location);
                        }
                    } else {
                        for (LivingEntity livingEntity : this.val$targetEntities) {
                            ExecutionTask.this.activate(this.val$finalEffect, livingEntity, null);
                        }
                    }
                }
            }.runTaskLater((Plugin)EffectsHandler.getInstance(), (long)this.currentWait);
        }
    }

    private void activate(String string2, LivingEntity livingEntity, Location location) {
        Object object;
        String string3 = string2;
        ASManager.debug("\u00a7d[ExecutionTask] start of " + string3 + " parsing+");
        string3 = DynamicVariable.parseThroughCustomVariables(Variables.replaceVariables(string3, this.builder.getAttacker(), this.builder.getVictim(), this.actionExecution));
        string3 = EffectsHandler.getVariablesHandler().parseEffectLine(string3, this.builder.getMain(), this);
        string3 = EffectsHandler.getFunctionsHandler().parseEffectLine(string3, this.builder.getMain(), this);
        string3 = EffectsHandler.getPointersHandler().parseEffectLine(string3, this.builder.getType(), this);
        if (string3.isEmpty() || string3.contains("$skip")) {
            return;
        }
        String[] stringArray = string3.replaceAll(" ", "").split(":");
        String[] stringArray2 = Arrays.copyOfRange(stringArray, 1, stringArray.length);
        AdvancedEffect advancedEffect = EffectsHandler.getEffectStorage().getEffect(stringArray[0].replaceAll(" ", ""));
        if (advancedEffect == null) {
            this.reportIssue(string3, "Failed to activate effects as advancedEffect is null or invalid: '" + stringArray[0].replaceAll(" ", "") + "'", string2, "trigger:" + this.builder.getType(), "entity:" + this.builder.getMain().getName());
            return;
        }
        if (advancedEffect.hasStringArgument()) {
            stringArray = string3.split(":");
            stringArray2 = Arrays.copyOfRange(stringArray, 1, stringArray.length);
        }
        if (advancedEffect.isBlockEffect() && location == null && this.builder.getEvent() instanceof BlockBreakEvent) {
            location = this.builder.getBlock().getLocation();
        }
        if (this.builder.getMain() instanceof Player && !this.builder.getMain().isOp() && advancedEffect.isExemptFromAC()) {
            EffectsHandler.getAntiCheatHooks().forEach((string, antiCheatHook) -> antiCheatHook.exempt((Player)this.builder.getMain()));
        }
        if (this.builder.getEvent() instanceof EntityDamageByEntityEvent) {
            object = (EntityDamageByEntityEvent)this.builder.getEvent();
            if (HooksHandler.isEnabled(HookPlugin.MYTHICMOBS) && MythicMobsHook.getIgnoreEnchantsMobs().contains(object.getDamager())) {
                return;
            }
        }
        ASManager.debug("\u00a7d\u00a7l(!) " + string3 + " activating " + (location != null ? "location based " + new LocalLocation(location).getEncode() : "entity based " + (livingEntity != null ? livingEntity.getType().name() : "null")));
        try {
            if (location != null) {
                if (advancedEffect.executeEffect(this, location, stringArray2)) {
                    if (!Bukkit.isPrimaryThread()) {
                        SchedulerUtils.runTask(() -> {
                            EffectsActivatedEvent effectsActivatedEvent = new EffectsActivatedEvent(this.ability, this.builder.getMain(), livingEntity, this);
                            Bukkit.getPluginManager().callEvent((Event)effectsActivatedEvent);
                        });
                    } else {
                        object = new EffectsActivatedEvent(this.ability, this.builder.getMain(), livingEntity, this);
                        Bukkit.getPluginManager().callEvent((Event)object);
                    }
                } else {
                    this.reportIssue(string3, "activating entity-based effect without a valid target (e.g. @Attacker)", this.ability.getName());
                }
            } else if (!advancedEffect.executeEffect(this, livingEntity, stringArray2)) {
                this.reportIssue(string3, "activating location-based effect without a valid target (e.g. @Block)", this.ability.getName());
            } else {
                if (!Bukkit.isPrimaryThread()) {
                    SchedulerUtils.runTask(() -> {
                        EffectsActivatedEvent effectsActivatedEvent = new EffectsActivatedEvent(this.ability, this.builder.getMain(), livingEntity, this);
                        Bukkit.getPluginManager().callEvent((Event)effectsActivatedEvent);
                    });
                } else {
                    object = new EffectsActivatedEvent(this.ability, this.builder.getMain(), livingEntity, this);
                    Bukkit.getPluginManager().callEvent((Event)object);
                }
                if (this.getBuilder().isPermanent() && this.getBuilder().isRemoved()) {
                    SchedulerUtils.runTaskLater(() -> {
                        if (livingEntity != null && !livingEntity.isDead() && ASManager.isOnline(livingEntity)) {
                            ArmorType.getArmorContents(livingEntity).forEach((armorType, itemStack) -> ArmorWearTrigger.getArmorWearTrigger().runCheck(livingEntity, (ItemStack)itemStack, (ArmorType)((Object)((Object)armorType)), false, false));
                        }
                    }, 2L);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            this.reportIssue(string3, "Error while activating effect");
        }
    }

    public void reportIssue(String string, String ... stringArray) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("ae.admin")) continue;
            player.sendMessage(Text.modify("&4Failed to activate effect '&f" + string + "'"));
            player.sendMessage(Text.modify("&cAdditional information: &7" + String.join((CharSequence)", ", stringArray)));
        }
        ASManager.getInstance().getLogger().warning(Text.modify("&4Failed to activate effect '&f" + string + "'"));
        ASManager.getInstance().getLogger().warning(Text.modify("&cAdditional information: &7" + String.join((CharSequence)", ", stringArray)));
    }

    private void updateMain(LinkedList<LivingEntity> linkedList, List<Location> list, String string) {
        linkedList.clear();
        list.clear();
        this.targetResults = EffectsHandler.getTargetHandler().handleTargets(string, this);
        if (this.targetResults.getTargetList() != null) {
            linkedList.addAll(this.targetResults.getTargetList());
        }
        if (this.targetResults.getTargetLocations() != null) {
            list.addAll(this.targetResults.getTargetLocations());
        }
        list = ASManager.removeDuplicateLocations(list);
    }

    public ExecutionTask asSets(boolean bl) {
        this.sets = bl;
        return this;
    }

    public ExecutionTask setLocation(Location location) {
        this.location = location;
        return this;
    }

    public ExecutionTask soulboundOnly(boolean bl) {
        this.soulboundOnly = bl;
        return this;
    }

    public ActionExecution getActionExecution() {
        return this.actionExecution;
    }

    public ActionExecutionBuilder getBuilder() {
        return this.builder;
    }

    public AdvancedAbility getAbility() {
        return this.ability;
    }

    public DamageHandler getDamageHandler() {
        return this.damageHandler;
    }
}

