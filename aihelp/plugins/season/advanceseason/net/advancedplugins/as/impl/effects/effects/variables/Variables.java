/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Monster
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.effects.effects.variables;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import me.clip.placeholderapi.PlaceholderAPI;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.variables.DynamicVariable;
import net.advancedplugins.as.impl.effects.effects.variables.StaticVariable;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class Variables {
    /*
     * WARNING - void declaration
     */
    public static String replaceVariables(String string, LivingEntity livingEntity, LivingEntity livingEntity2, ActionExecution actionExecution) {
        Object object;
        Object object2;
        if (ASManager.substringBetween(string, "%", "%") == null) {
            return string;
        }
        if (livingEntity2 == null) {
            livingEntity2 = livingEntity;
        } else if (livingEntity == null) {
            livingEntity = livingEntity2;
        }
        VariableData variableData = new VariableData(string, livingEntity, livingEntity2);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        for (StaticVariable enum_ : StaticVariable.values()) {
            if (!string.contains(enum_.getName())) continue;
            object2 = StaticVariable.getValue(variableData, enum_, string, actionExecution).toLowerCase(Locale.ROOT);
            linkedHashMap.put(enum_.getName(), (String)object2);
        }
        for (Enum enum_ : DynamicVariable.values()) {
            linkedHashMap.putAll(DynamicVariable.getValue(variableData, (DynamicVariable)enum_, string, linkedHashMap, actionExecution));
        }
        if (!(livingEntity2 == null || livingEntity instanceof Player && livingEntity2 instanceof Player)) {
            object = livingEntity instanceof Player ? livingEntity2 : livingEntity;
            linkedHashMap.put("mob type", object.getType().name());
            linkedHashMap.put("is hostile", "" + (object instanceof Monster));
        }
        object = linkedHashMap.entrySet().iterator();
        while (object.hasNext()) {
            Map.Entry entry = (Map.Entry)object.next();
            string = string.replace("%attacker " + (String)entry.getKey() + "%", (CharSequence)entry.getValue());
            string = string.replace("%victim " + (String)entry.getKey() + "%", (CharSequence)entry.getValue());
            string = string.replace("%player " + (String)entry.getKey() + "%", (CharSequence)entry.getValue());
            string = string.replace("%" + (String)entry.getKey() + "%", (CharSequence)entry.getValue());
        }
        string = DynamicVariable.parseThroughCustomVariables(string);
        if (HooksHandler.isEnabled(HookPlugin.PLACEHOLDERAPI) && (object = StringUtils.substringsBetween(string, "%", "%")) != null) {
            void var9_15;
            Object object3 = object;
            int n = ((Object)object3).length;
            boolean bl = false;
            while (var9_15 < n) {
                String string2;
                String string3;
                object2 = object3[var9_15];
                Player player = null;
                if (((String)object2).startsWith("victim ") && livingEntity2 instanceof Player) {
                    player = (Player)livingEntity2;
                } else if (livingEntity instanceof Player) {
                    player = (Player)livingEntity;
                }
                if (player != null && !(string3 = PlaceholderAPI.setPlaceholders((Player)player, (String)("%" + (string2 = ((String)object2).replace("victim ", "").replace("attacker ", "").replace("player ", "")) + "%"))).equals("%" + string2 + "%")) {
                    string = string.replace("%" + (String)object2 + "%", string3);
                }
                ++var9_15;
            }
        }
        string = string.replace(" or ", " || ");
        string = string.replace(" and ", " && ");
        return string;
    }

    public static class VariableData {
        public final String condition;
        public final LivingEntity primary;
        public final LivingEntity secondary;

        public VariableData(String string, LivingEntity livingEntity, LivingEntity livingEntity2) {
            this.condition = string;
            this.primary = livingEntity;
            this.secondary = livingEntity2;
        }
    }

    public static class VariableArgs {
        private final Map<Class<?>, Object> map = new HashMap();

        public void add(Object object) {
            if (object == null) {
                return;
            }
            this.map.put(object.getClass(), object);
        }

        public <T> T get(Class<T> clazz) {
            return clazz.cast(this.map.get(clazz));
        }
    }
}

