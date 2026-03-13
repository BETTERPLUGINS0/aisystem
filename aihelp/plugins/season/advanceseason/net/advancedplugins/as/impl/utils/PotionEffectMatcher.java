/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectMatcher {
    private static final HashMap<PotionEffectType, List<String>> potionAliases = new HashMap();

    public static PotionEffectType matchPotion(String string) {
        try {
            string = string.toUpperCase(Locale.ROOT);
            if (potionAliases.isEmpty()) {
                PotionEffectMatcher.init();
            }
            for (Map.Entry<PotionEffectType, List<String>> entry : potionAliases.entrySet()) {
                for (String string2 : entry.getValue()) {
                    if (!string2.equalsIgnoreCase(string)) continue;
                    return entry.getKey();
                }
            }
            return PotionEffectType.getByName((String)string);
        } catch (Exception exception) {
            exception.printStackTrace();
            return PotionEffectType.getByName((String)string);
        }
    }

    private static void init() {
        for (PotionEffectType potionEffectType : PotionEffectType.values()) {
            String string;
            ArrayList<String> arrayList = new ArrayList<String>();
            switch (string = potionEffectType.getName().toUpperCase(Locale.ROOT)) {
                case "CONFUSION": {
                    arrayList.add("NAUSEA");
                    break;
                }
                case "DAMAGE_RESISTANCE": {
                    arrayList.add("RESISTANCE");
                    arrayList.add("RES");
                    break;
                }
                case "FAST_DIGGING": {
                    arrayList.add("HASTE");
                    break;
                }
                case "FIRE_RESISTANCE": {
                    arrayList.add("FIRE_RESISTANCE");
                    arrayList.add("FIRE_RES");
                    break;
                }
                case "HARM": {
                    arrayList.add("HARMNESS");
                    break;
                }
                case "INCREASE_DAMAGE": {
                    arrayList.add("STRENGTH");
                    arrayList.add("STRENGHT");
                    break;
                }
                case "JUMP": {
                    arrayList.add("JUMP_BOOST");
                    break;
                }
                case "SLOW": {
                    arrayList.add("SLOWNESS");
                    break;
                }
                case "BLINDNESS": {
                    arrayList.add("BLIND");
                }
            }
            if (arrayList.isEmpty()) continue;
            potionAliases.put(potionEffectType, arrayList);
        }
    }
}

