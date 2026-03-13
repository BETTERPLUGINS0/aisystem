/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeInstance
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.VersionChecker;
import java.util.Locale;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeManager {
    private static Attribute getAttributeFromString(String string) {
        String attributeKey = string.toUpperCase(Locale.ROOT);
        if (!VersionChecker.serverVersionOlderThan(21, 4) && attributeKey.startsWith("GENERIC_")) {
            attributeKey = attributeKey.replace("GENERIC_", "");
        }
        try {
            return Attribute.valueOf((String)attributeKey);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void setAttribute(LivingEntity livingEntity, String attributeKey, double value) {
        Attribute attribute = AttributeManager.getAttributeFromString(attributeKey);
        if (attribute == null) {
            Logger.warn("Failed to set attribute " + attributeKey + "!");
            return;
        }
        livingEntity.getAttribute(attribute).setBaseValue(value);
    }

    public static double getAttributeValue(LivingEntity livingEntity, String attributeKey) {
        Attribute attribute = AttributeManager.getAttributeFromString(attributeKey);
        if (attribute == null) {
            Logger.warn("Failed to get attribute " + attributeKey + "!");
            return 1.0;
        }
        return livingEntity.getAttribute(attribute).getValue();
    }

    public static double getAttributeBaseValue(LivingEntity livingEntity, String attributeKey) {
        Attribute attribute = AttributeManager.getAttributeFromString(attributeKey);
        if (attribute == null) {
            Logger.warn("Failed to get attribute " + attributeKey + "!");
            return 1.0;
        }
        return livingEntity.getAttribute(attribute).getBaseValue();
    }

    public static double getAttributeDefaultValue(LivingEntity livingEntity, String attributeKey) {
        Attribute attribute = AttributeManager.getAttributeFromString(attributeKey);
        return livingEntity.getAttribute(attribute).getDefaultValue();
    }

    public static boolean containsKey(LivingEntity livingEntity, String attributeKey) {
        Attribute attribute = AttributeManager.getAttributeFromString(attributeKey);
        return livingEntity.getAttribute(attribute) != null;
    }

    public static boolean containsKey(ItemMeta itemMeta, String attributeKey) {
        Attribute attribute = AttributeManager.getAttributeFromString(attributeKey);
        if (!itemMeta.hasAttributeModifiers()) {
            return false;
        }
        return itemMeta.getAttributeModifiers().containsKey(attribute);
    }

    public static Attribute getAttribute(String attributeKey) {
        return AttributeManager.getAttributeFromString(attributeKey);
    }

    public static AttributeInstance getAttributeInstance(LivingEntity livingEntity, String attributeKey) {
        return livingEntity.getAttribute(AttributeManager.getAttribute(attributeKey));
    }
}

