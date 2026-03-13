/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.economy;

import java.util.HashMap;
import java.util.Locale;
import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import net.advancedplugins.as.impl.utils.economy.local.DiamondsEconomy;
import net.advancedplugins.as.impl.utils.economy.local.EmeraldsEconomy;
import net.advancedplugins.as.impl.utils.economy.local.ExpEconomy;
import net.advancedplugins.as.impl.utils.economy.local.GoldEconomy;
import net.advancedplugins.as.impl.utils.economy.local.LevelEconomy;
import net.advancedplugins.as.impl.utils.economy.local.SoulsEconomy;
import net.advancedplugins.as.impl.utils.economy.local.VaultEconomy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyHandler {
    private final HashMap<String, AdvancedEconomy> econMap = new HashMap();

    public EconomyHandler(JavaPlugin javaPlugin) {
        this.registerEconomy(new ExpEconomy());
        this.registerEconomy(new LevelEconomy());
        this.registerEconomy(new DiamondsEconomy());
        this.registerEconomy(new EmeraldsEconomy());
        this.registerEconomy(new GoldEconomy());
        this.registerEconomy(new SoulsEconomy());
        if (javaPlugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
            this.registerEconomy(new VaultEconomy());
        }
    }

    public boolean charge(Player player, String string) {
        String string2 = string.split(":")[0].toUpperCase(Locale.ROOT);
        double d = Double.parseDouble(string.split(":")[1]);
        return this.econMap.get(string2).chargeUser(player, d);
    }

    public AdvancedEconomy getEcon(String string) {
        return this.econMap.get(string.toUpperCase(Locale.ROOT));
    }

    public boolean registerEconomy(AdvancedEconomy advancedEconomy) {
        String string = advancedEconomy.getName().toUpperCase(Locale.ROOT);
        if (this.econMap.containsKey(string)) {
            return false;
        }
        this.econMap.put(string, advancedEconomy);
        return true;
    }
}

