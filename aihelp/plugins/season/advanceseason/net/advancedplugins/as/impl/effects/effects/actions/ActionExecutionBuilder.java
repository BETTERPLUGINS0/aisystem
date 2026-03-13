/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.actions.handlers.DropsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ActionExecutionBuilder {
    private final String type;
    private ItemStack item;
    private StackItem stackItem = null;
    private RollItemType itemType;
    private Event event;
    private LivingEntity attacker;
    private LivingEntity victim;
    private Block block;
    private boolean removed = false;
    private boolean repeating = false;
    private boolean skipConditions = false;
    private boolean skipChances = false;
    private boolean skipCooldown = false;
    private boolean attackerMain = false;
    private boolean soulboundOnly = false;
    private boolean isPermanent = false;
    private boolean isDamageEventNotGoingToRun = false;
    private Map<String, String> variables = new HashMap<String, String>();
    private List<AdvancedAbility> effects;
    private final DropsHandler drops = new DropsHandler();
    private List<AdvancedAbility> onlyAllowedAbilities = null;

    public ActionExecutionBuilder(String string) {
        this.type = string;
    }

    public void buildAndExecute() {
        ActionExecution actionExecution = this.build();
        if (this.effects == null || this.effects.isEmpty()) {
            return;
        }
        if (this.getMain() == null) {
            throw new NullPointerException("Main entity cannot be null");
        }
        actionExecution.build();
        actionExecution.run();
        this.drops.setBuilder(this);
        this.drops.handle();
    }

    public ActionExecution build() {
        this.effects = EffectsHandler.getAbilitiesReader().getRawAbilities(this);
        return new ActionExecution(this);
    }

    public ActionExecutionBuilder setItem(ItemStack itemStack) {
        this.item = itemStack;
        if (this.drops != null && this.drops.getSettings().getTool() != null) {
            this.drops.getSettings().setTool(itemStack);
        }
        return this;
    }

    public ActionExecutionBuilder addDrops(Block block, List<ItemStack> list) {
        return this.addDrops(block, (Collection<ItemStack>)list);
    }

    public ActionExecutionBuilder addDrops(Block block, Collection<ItemStack> collection) {
        boolean bl = SettingValues.getIgnoredMetaDrops().stream().anyMatch(arg_0 -> ((Block)block).hasMetadata(arg_0));
        ArrayList<ItemStack> arrayList = bl ? new ArrayList<ItemStack>() : new ArrayList<ItemStack>(collection);
        this.getDrops().addDrops(block, arrayList);
        return this;
    }

    public ActionExecutionBuilder addDrops(Location location, ItemStack ... itemStackArray) {
        this.getDrops().addDrops(location, itemStackArray);
        return this;
    }

    public ActionExecutionBuilder setItemType(RollItemType rollItemType) {
        this.itemType = rollItemType;
        return this;
    }

    public ActionExecutionBuilder setEvent(Event event) {
        this.event = event;
        return this;
    }

    public ActionExecutionBuilder setAttacker(LivingEntity livingEntity) {
        this.attacker = livingEntity;
        return this;
    }

    public ActionExecutionBuilder setVictim(LivingEntity livingEntity) {
        this.victim = livingEntity;
        return this;
    }

    public ActionExecutionBuilder setBlock(Block block) {
        this.block = block;
        return this;
    }

    public ActionExecutionBuilder asRepeating() {
        this.repeating = true;
        return this;
    }

    public ActionExecutionBuilder skipConditions() {
        this.skipConditions = true;
        return this;
    }

    public ActionExecutionBuilder skipChances() {
        this.skipChances = true;
        return this;
    }

    public ActionExecutionBuilder asPermanent() {
        this.isPermanent = true;
        return this;
    }

    public ActionExecutionBuilder setDamageEventNotGoingToRun(boolean bl) {
        this.isDamageEventNotGoingToRun = bl;
        return this;
    }

    public ActionExecutionBuilder setRemoval(boolean bl) {
        this.isPermanent = true;
        this.removed = bl;
        return this;
    }

    public boolean shouldSkipConditions() {
        return this.skipConditions;
    }

    public boolean shouldSkipChances() {
        return this.skipChances;
    }

    public ActionExecutionBuilder setSkipCooldown(boolean bl) {
        this.skipCooldown = bl;
        return this;
    }

    public boolean shouldSkipCooldown() {
        return this.skipCooldown;
    }

    public ActionExecutionBuilder processVariables(String ... stringArray2) {
        this.variables.putAll(Arrays.stream(stringArray2).map(string -> string.split(";")).filter(stringArray -> ((String[])stringArray).length == 2).collect(Collectors.toMap(stringArray -> stringArray[0], stringArray -> stringArray[1])));
        return this;
    }

    public void globalVariables() {
        if (this.event instanceof EntityDamageEvent) {
            EntityDamageEvent entityDamageEvent = (EntityDamageEvent)this.event;
            this.variables.put("%damage%", "" + Math.round(entityDamageEvent.getFinalDamage()));
            this.variables.put("%raw damage%", "" + Math.round(entityDamageEvent.getDamage()));
            this.variables.put("%damage cause%", String.valueOf(entityDamageEvent.getCause()));
        }
        this.variables.put("%is removed%", "" + this.removed);
    }

    public ActionExecutionBuilder setStackItem(StackItem stackItem) {
        this.stackItem = stackItem;
        return this;
    }

    public ActionExecutionBuilder setSoulboundOnly(boolean bl) {
        this.soulboundOnly = bl;
        return this;
    }

    public LivingEntity getMain() {
        return this.attackerMain ? this.attacker : this.victim;
    }

    public LivingEntity getOther() {
        return this.attackerMain ? this.attacker : this.victim;
    }

    public ActionExecutionBuilder setAttackerMain(boolean bl) {
        this.attackerMain = bl;
        return this;
    }

    public boolean isPermanent() {
        return this.isPermanent;
    }

    public List<AdvancedAbility> getAllowedAbilities() {
        return this.onlyAllowedAbilities;
    }

    public ActionExecutionBuilder only(AdvancedAbility ... advancedAbilityArray) {
        this.onlyAllowedAbilities = Arrays.asList(advancedAbilityArray);
        return this;
    }

    public ActionExecutionBuilder() {
        this.type = null;
    }

    public String getType() {
        return this.type;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public StackItem getStackItem() {
        return this.stackItem;
    }

    public RollItemType getItemType() {
        return this.itemType;
    }

    public Event getEvent() {
        return this.event;
    }

    public LivingEntity getAttacker() {
        return this.attacker;
    }

    public LivingEntity getVictim() {
        return this.victim;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public boolean isRepeating() {
        return this.repeating;
    }

    public boolean isDamageEventNotGoingToRun() {
        return this.isDamageEventNotGoingToRun;
    }

    public Map<String, String> getVariables() {
        return this.variables;
    }

    public List<AdvancedAbility> getEffects() {
        return this.effects;
    }

    public DropsHandler getDrops() {
        return this.drops;
    }
}

