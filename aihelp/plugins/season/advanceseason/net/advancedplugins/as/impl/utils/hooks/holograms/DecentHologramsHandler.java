/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  eu.decentsoftware.holograms.api.DHAPI
 *  eu.decentsoftware.holograms.api.DecentHologramsAPI
 *  eu.decentsoftware.holograms.api.holograms.Hologram
 *  org.bukkit.Location
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.hooks.holograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.util.Arrays;
import net.advancedplugins.as.impl.utils.hooks.holograms.HologramHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class DecentHologramsHandler
extends HologramHandler {
    public DecentHologramsHandler(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @Override
    public String getName() {
        return "DecentHolograms";
    }

    @Override
    public void createHologram(Location location, String string, String string2) {
        if (DHAPI.getHologram((String)string) != null) {
            return;
        }
        DHAPI.createHologram((String)string, (Location)location, (boolean)false, Arrays.asList(string2));
    }

    @Override
    public void removeHologram(String string) {
        Hologram hologram = DHAPI.getHologram((String)string);
        if (hologram != null) {
            hologram.delete();
            DecentHologramsAPI.get().getHologramManager().removeHologram(string);
        }
    }

    @Override
    public void updateHologram(String string, String string2) {
        Hologram hologram = DHAPI.getHologram((String)string);
        if (hologram != null) {
            DHAPI.setHologramLines((Hologram)hologram, Arrays.asList(string2));
        }
    }
}

