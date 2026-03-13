/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.Zrips.CMI.CMI
 *  com.Zrips.CMI.Modules.Holograms.CMIHologram
 *  com.Zrips.CMI.Modules.Holograms.HologramManager
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.hooks.holograms;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import com.Zrips.CMI.Modules.Holograms.HologramManager;
import java.util.Arrays;
import net.advancedplugins.as.impl.utils.hooks.holograms.HologramHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class CMIHologramHandler
extends HologramHandler {
    private HologramManager manager = ((CMI)Bukkit.getPluginManager().getPlugin("CMI")).getHologramManager();

    public CMIHologramHandler(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @Override
    public String getName() {
        return "CMI";
    }

    @Override
    public void createHologram(Location location, String string, String string2) {
        CMIHologram cMIHologram = new CMIHologram(string, location);
        cMIHologram.setLines(Arrays.asList(string2));
        this.manager.addHologram(cMIHologram);
        cMIHologram.update();
    }

    @Override
    public void removeHologram(String string) {
        CMIHologram cMIHologram = this.manager.getByName(string);
        if (cMIHologram != null) {
            this.manager.removeHolo(cMIHologram);
        }
    }

    @Override
    public void updateHologram(String string, String string2) {
        CMIHologram cMIHologram = this.manager.getByName(string);
        cMIHologram.setLines(Arrays.asList(string2));
    }
}

