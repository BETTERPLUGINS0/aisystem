/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.andrei1058.bedwars.lobbysocket;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;

public class LoadedUser {
    private static final long waitSeconds = BedWars.config.getYml().getLong("bungee-settings.bwp-time-out");
    private UUID uuid;
    private String partyOwnerOrSpectateTarget = null;
    private long toleranceTime;
    private String arenaIdentifier;
    private Language language = null;
    private static final ConcurrentHashMap<UUID, LoadedUser> loaded = new ConcurrentHashMap();

    public LoadedUser(String uuid, String arenaIdentifier, String langIso, String partyOwnerOrSpectateTarget) {
        if (Bukkit.getWorld((String)arenaIdentifier) == null) {
            return;
        }
        this.arenaIdentifier = arenaIdentifier;
        this.uuid = UUID.fromString(uuid);
        if (partyOwnerOrSpectateTarget != null && !partyOwnerOrSpectateTarget.isEmpty()) {
            this.partyOwnerOrSpectateTarget = partyOwnerOrSpectateTarget;
        }
        this.toleranceTime = System.currentTimeMillis() + waitSeconds;
        Language l = Language.getLang(langIso);
        if (l != null) {
            this.language = l;
        }
        loaded.put(this.uuid, this);
    }

    public static boolean isPreLoaded(UUID uuid) {
        return loaded.containsKey(uuid);
    }

    public boolean isTimedOut() {
        return System.currentTimeMillis() > this.toleranceTime;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getArenaIdentifier() {
        return this.arenaIdentifier;
    }

    public void destroy(String reason) {
        BedWars.debug("Destroyed PreLoaded User: " + String.valueOf(this.uuid) + " Reason: " + reason + ". Tolerance: " + waitSeconds);
        loaded.remove(this.uuid);
    }

    public Language getLanguage() {
        return this.language;
    }

    public static LoadedUser getPreLoaded(UUID uuid) {
        return loaded.get(uuid);
    }

    public String getPartyOwnerOrSpectateTarget() {
        return this.partyOwnerOrSpectateTarget;
    }

    public static ConcurrentHashMap<UUID, LoadedUser> getLoaded() {
        return loaded;
    }
}

