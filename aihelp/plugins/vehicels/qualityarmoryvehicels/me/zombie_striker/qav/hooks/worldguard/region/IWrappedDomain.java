/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.region;

import java.util.Set;
import java.util.UUID;

public interface IWrappedDomain {
    public Set<UUID> getPlayers();

    public void addPlayer(UUID var1);

    public void removePlayer(UUID var1);

    public Set<String> getGroups();

    public void addGroup(String var1);

    public void removeGroup(String var1);
}

