/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.version.v1_19_R3.despawnable;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableAttributes;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableProvider;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.TeamIronGolem;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.TeamSilverfish;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class DespawnableFactory {
    private final VersionSupport versionSupport;
    private final List<DespawnableProvider<? extends LivingEntity>> providers = new ArrayList<DespawnableProvider<? extends LivingEntity>>();

    public DespawnableFactory(VersionSupport versionSupport) {
        this.versionSupport = versionSupport;
        this.providers.add(new TeamIronGolem());
        this.providers.add(new TeamSilverfish());
    }

    public LivingEntity spawn(@NotNull DespawnableAttributes attr, @NotNull Location location, @NotNull ITeam team) {
        return (LivingEntity)((DespawnableProvider)this.providers.stream().filter(provider -> provider.getType() == attr.type()).findFirst().orElseThrow()).spawn(attr, location, team, this.versionSupport);
    }
}

