/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment
 *  org.bukkit.Registry
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.effects.effects.variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.variables.Variables;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.PotionEffectMatcher;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.AdvancedEnchantmentsHook;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DynamicVariable {
    LEVEL_OF_SKILL("level of skill", true),
    HAS_ENCHANTMENT_IN_HAND_OF("has enchantment in hand of", false),
    HAS_ENCHANTMENT_IN_SLOT_OF("has enchantment in", false),
    HAS_POTION_EFFECT("has potion effect", false),
    ITEM_IN_HAND_LEVEL("item in hand level", false),
    POTION_EFFECT_LEVEL("potion effect level", false);

    private final String start;
    private final boolean player;
    private static final HashMap<String, String> customVariables;
    private static final HashMap<String, String> customPermanentVariables;

    public static String parseThroughCustomVariables(String string) {
        for (Map.Entry<String, String> object : customPermanentVariables.entrySet()) {
            string = string.replace(object.getKey(), object.getValue());
        }
        if (!string.contains("%custom_")) {
            return string;
        }
        for (String string2 : ASManager.getVariables(string, "%custom_", "%")) {
            string = string.replace("%custom_" + string2 + "%", customVariables.getOrDefault(string2, "0"));
        }
        return string;
    }

    public boolean needsPlayer() {
        return this.player;
    }

    public static Map<String, String> getValue(Variables.VariableData variableData, DynamicVariable dynamicVariable, String string2, Map<String, String> map, ActionExecution actionExecution) {
        Object object;
        LivingEntity livingEntity = DynamicVariable.getTarget(variableData, dynamicVariable);
        if (dynamicVariable.needsPlayer() && livingEntity instanceof Player) {
            object = (ArmorType[])livingEntity;
            switch (dynamicVariable.ordinal()) {
                default: 
            }
        }
        switch (dynamicVariable.ordinal()) {
            case 1: {
                Object object2 = object = livingEntity instanceof Player ? ((Player)livingEntity).getItemInHand() : null;
                if (!string2.contains(HAS_ENCHANTMENT_IN_HAND_OF.getStart()) || !ASManager.isValid((ItemStack)object)) break;
                Registry.ENCHANTMENT.forEach(arg_0 -> DynamicVariable.lambda$getValue$0((ItemStack)object, map, arg_0));
                if (!HooksHandler.isEnabled(HookPlugin.ADVANCEDENCHANTMENTS)) break;
                AdvancedEnchantmentsHook advancedEnchantmentsHook = (AdvancedEnchantmentsHook)HooksHandler.getHook(HookPlugin.ADVANCEDENCHANTMENTS);
                HashMap<String, Integer> hashMap = advancedEnchantmentsHook.getEnchantmentsOnItem((ItemStack)object);
                advancedEnchantmentsHook.getAllEnchantments().forEach(string -> {
                    if (hashMap.containsKey(string)) {
                        map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string, "true");
                        map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string + " level " + String.valueOf(hashMap.get(string)), "true");
                    } else {
                        map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string, "false");
                        AdvancedEnchantment advancedEnchantment = advancedEnchantmentsHook.getInstance((String)string);
                        for (int i = 1; i <= advancedEnchantment.getHighestLevel(); ++i) {
                            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string + " level " + i, "false");
                        }
                    }
                });
                break;
            }
            case 2: {
                if (!(livingEntity instanceof InventoryHolder)) break;
                for (ArmorType armorType : ArmorType.values()) {
                    ItemStack itemStack = armorType.get(livingEntity);
                    if (!string2.contains("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of ")) continue;
                    Registry.ENCHANTMENT.forEach(enchantment -> {
                        String string = enchantment.getKey().getKey();
                        if (itemStack.containsEnchantment(enchantment)) {
                            map.put("has enchantment in " + armorType.name() + " of " + string, "true");
                            map.put("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of " + string, "true");
                            map.put("has enchantment in " + armorType.name() + " of " + string.toLowerCase(), "true");
                            map.put("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of " + string.toLowerCase(), "true");
                        } else {
                            map.put("has enchantment in " + armorType.name() + " of " + string, "false");
                            map.put("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of " + string, "false");
                            map.put("has enchantment in " + armorType.name() + " of " + string.toLowerCase(), "false");
                            map.put("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of " + string.toLowerCase(), "false");
                        }
                    });
                    if (!HooksHandler.isEnabled(HookPlugin.ADVANCEDENCHANTMENTS)) continue;
                    AdvancedEnchantmentsHook advancedEnchantmentsHook = (AdvancedEnchantmentsHook)HooksHandler.getHook(HookPlugin.ADVANCEDENCHANTMENTS);
                    HashMap<String, Integer> hashMap = advancedEnchantmentsHook.getEnchantmentsOnItem(itemStack);
                    advancedEnchantmentsHook.getAllEnchantments().forEach(string -> {
                        if (hashMap.containsKey(string)) {
                            map.put("has enchantment in " + armorType.name() + " of " + string, "true");
                            map.put("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of " + string, "true");
                        } else {
                            map.put("has enchantment in " + armorType.name() + " of " + string, "false");
                            map.put("has enchantment in " + armorType.name().toLowerCase(Locale.ROOT) + " of " + string, "false");
                        }
                    });
                }
                break;
            }
            case 3: {
                String[] stringArray;
                if (!string2.contains(HAS_POTION_EFFECT.getStart())) break;
                object = new ArrayList();
                for (String string3 : stringArray = string2.split("\\|")) {
                    Arrays.stream(string3.split("%")).filter(string -> string.contains("has potion effect")).forEach(arg_0 -> DynamicVariable.lambda$getValue$5((List)object, arg_0));
                }
                object.forEach(potionEffectType -> map.put(HAS_POTION_EFFECT.getStart() + " " + potionEffectType.getName().toLowerCase(Locale.ROOT), String.valueOf(livingEntity.hasPotionEffect(potionEffectType))));
                break;
            }
            case 4: {
                object = livingEntity.getEquipment().getItemInMainHand();
                if (!string2.contains(ITEM_IN_HAND_LEVEL.getStart()) && !ASManager.isValid((ItemStack)object)) break;
                object.getEnchantments().forEach((enchantment, n) -> {
                    map.put(ITEM_IN_HAND_LEVEL.getStart() + " " + enchantment.getName(), "" + n);
                    map.put(ITEM_IN_HAND_LEVEL.getStart() + " " + enchantment.getName().toLowerCase(), "" + n);
                });
                if (HooksHandler.getHook(HookPlugin.ADVANCEDENCHANTMENTS) == null) break;
                ((AdvancedEnchantmentsHook)HooksHandler.getHook(HookPlugin.ADVANCEDENCHANTMENTS)).getEnchantmentsOnItem((ItemStack)object).forEach((string, n) -> map.put(ITEM_IN_HAND_LEVEL.getStart() + " " + string, n.toString()));
                break;
            }
            case 5: {
                String[] stringArray;
                if (!string2.contains(POTION_EFFECT_LEVEL.getStart())) break;
                object = new ArrayList();
                for (String string4 : stringArray = string2.split("\\|")) {
                    Arrays.stream(string4.split("%")).filter(string -> string.contains("potion effect level")).forEach(arg_0 -> DynamicVariable.lambda$getValue$10((List)object, arg_0));
                }
                object.forEach(potionEffectType -> {
                    PotionEffect potionEffect = livingEntity.getPotionEffect(potionEffectType);
                    int n = -1;
                    if (potionEffect != null) {
                        n = potionEffect.getAmplifier();
                    }
                    map.put(POTION_EFFECT_LEVEL.getStart() + " " + potionEffectType.getName().toLowerCase(Locale.ROOT), String.valueOf(n));
                });
                break;
            }
        }
        return map;
    }

    public static LivingEntity getTarget(Variables.VariableData variableData, DynamicVariable dynamicVariable) {
        return variableData.condition.contains("victim " + dynamicVariable.getStart()) ? variableData.secondary : variableData.primary;
    }

    private DynamicVariable(String string2, boolean bl) {
        this.start = string2;
        this.player = bl;
    }

    public String getStart() {
        return this.start;
    }

    public static HashMap<String, String> getCustomVariables() {
        return customVariables;
    }

    public static HashMap<String, String> getCustomPermanentVariables() {
        return customPermanentVariables;
    }

    private static /* synthetic */ void lambda$getValue$10(List list, String string) {
        String string2 = string.substring(string.indexOf("potion effect level") + "potion effect level ".length());
        PotionEffectType potionEffectType = PotionEffectMatcher.matchPotion(string2);
        if (potionEffectType != null) {
            list.add(potionEffectType);
        }
    }

    private static /* synthetic */ void lambda$getValue$5(List list, String string) {
        String string2 = string.substring(string.indexOf("has potion effect") + "has potion effect ".length());
        PotionEffectType potionEffectType = PotionEffectMatcher.matchPotion(string2);
        if (potionEffectType != null) {
            list.add(potionEffectType);
        }
    }

    private static /* synthetic */ void lambda$getValue$0(ItemStack itemStack, Map map, Enchantment enchantment) {
        String string = enchantment.getKey().getKey();
        if (itemStack.containsEnchantment(enchantment)) {
            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string, "true");
            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string.toLowerCase(), "true");
            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string + " level " + itemStack.getEnchantmentLevel(enchantment), "true");
            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string.toLowerCase() + " level " + itemStack.getEnchantmentLevel(enchantment), "true");
        } else {
            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string, "false");
            map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string.toLowerCase(), "false");
            for (int i = enchantment.getStartLevel(); i <= enchantment.getMaxLevel(); ++i) {
                map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string + " level " + i, "false");
                map.put(HAS_ENCHANTMENT_IN_HAND_OF.getStart() + " " + string.toLowerCase() + " level " + i, "false");
            }
        }
    }

    static {
        customVariables = new HashMap();
        customPermanentVariables = new HashMap();
    }
}

