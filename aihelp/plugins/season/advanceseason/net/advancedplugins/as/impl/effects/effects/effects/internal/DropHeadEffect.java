/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.utils.YamlFile
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.advancedplugins.ae.utils.YamlFile;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.EntityHead;
import net.advancedplugins.as.impl.utils.SkullCreator;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.items.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DropHeadEffect
extends AdvancedEffect {
    private final Random random = new Random();

    public DropHeadEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DROP_HEAD", "Drop player or mob head", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = null;
        if (livingEntity instanceof Player) {
            try {
                itemStack = new ItemBuilder(ASManager.matchMaterial("PLAYER_HEAD", 1, 3)).setSkullOwner(livingEntity.getName()).toItemStack();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            ItemStack itemStack2 = this.getFromConfig(livingEntity).orElse(EntityHead.matchFromName(livingEntity.getType().name()));
            if (itemStack2 == null) {
                return true;
            }
            String string = livingEntity.getType().name();
            string = string.replaceAll("_", " ");
            string = WordUtils.capitalizeFully((String)string);
            itemStack = new ItemBuilder(itemStack2).setName(ColorUtils.format("&e" + string + "'s head")).toItemStack();
        }
        if (itemStack != null) {
            ASManager.dropItem(livingEntity.getLocation(), itemStack);
        }
        return true;
    }

    private Optional<ItemStack> getFromConfig(LivingEntity livingEntity) {
        if (!HooksHandler.isEnabled(HookPlugin.ADVANCEDENCHANTMENTS)) {
            return Optional.empty();
        }
        ConfigurationSection configurationSection = YamlFile.MOB_HEADS.getConfigSection("mobs");
        List list = configurationSection.getStringList(livingEntity.getType().name());
        if (list.isEmpty()) {
            return Optional.empty();
        }
        int n = this.random.nextInt(list.size());
        return Optional.of(SkullCreator.itemFromBase64(list.toArray(new String[0])[n]));
    }
}

