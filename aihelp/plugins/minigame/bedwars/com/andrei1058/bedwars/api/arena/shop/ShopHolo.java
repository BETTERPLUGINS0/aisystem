/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.arena.shop;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ShopHolo {
    private static List<ShopHolo> shopHolo = new ArrayList<ShopHolo>();
    private static BedWars api = null;
    private String iso;
    private ArmorStand a1;
    private ArmorStand a2;
    private Location l;
    private IArena a;

    public ShopHolo(String iso, ArmorStand a1, ArmorStand a2, Location l, IArena a) {
        this.l = l;
        for (ShopHolo sh : ShopHolo.getShopHolo()) {
            if (sh.l != l || !sh.iso.equalsIgnoreCase(iso)) continue;
            if (a1 != null) {
                a1.remove();
            }
            if (a2 != null) {
                a2.remove();
            }
            return;
        }
        this.a1 = a1;
        this.a2 = a2;
        this.iso = iso;
        this.a = a;
        if (a1 != null) {
            a1.setMarker(true);
        }
        if (a2 != null) {
            a2.setMarker(true);
        }
        shopHolo.add(this);
        if (api == null) {
            api = (BedWars)Bukkit.getServer().getServicesManager().getRegistration(BedWars.class).getProvider();
        }
    }

    public void update() {
        if (this.l == null) {
            Bukkit.broadcastMessage((String)"LOCATION IS NULL");
        }
        for (Player p2 : this.l.getWorld().getPlayers()) {
            if (Language.getPlayerLanguage(p2).getIso().equalsIgnoreCase(this.iso)) continue;
            if (this.a1 != null) {
                api.getVersionSupport().hideEntity((Entity)this.a1, p2);
            }
            if (this.a2 == null) continue;
            api.getVersionSupport().hideEntity((Entity)this.a2, p2);
        }
    }

    public void updateForPlayer(Player p, String lang) {
        if (lang.equalsIgnoreCase(this.iso)) {
            return;
        }
        if (this.a1 != null) {
            api.getVersionSupport().hideEntity((Entity)this.a1, p);
        }
        if (this.a2 != null) {
            api.getVersionSupport().hideEntity((Entity)this.a2, p);
        }
    }

    public static void clearForArena(IArena a) {
        for (ShopHolo sh : new ArrayList<ShopHolo>(ShopHolo.getShopHolo())) {
            if (sh.a != a) continue;
            shopHolo.remove(sh);
        }
    }

    public IArena getA() {
        return this.a;
    }

    public String getIso() {
        return this.iso;
    }

    public static List<ShopHolo> getShopHolo() {
        return shopHolo;
    }
}

