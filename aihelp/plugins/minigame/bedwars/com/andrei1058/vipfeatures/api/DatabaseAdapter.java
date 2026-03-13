/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.vipfeatures.api;

import com.andrei1058.vipfeatures.api.BoosterType;
import com.andrei1058.vipfeatures.api.ParticleType;
import com.andrei1058.vipfeatures.api.SpellType;
import com.andrei1058.vipfeatures.api.TrailType;
import java.util.UUID;

public interface DatabaseAdapter {
    public boolean isRemote();

    public void close();

    public void setupTrailsTable();

    public void setupSpellsTable();

    public void setupParticlesTable();

    public void setupBoostersTable();

    public TrailType getSelectedTrails(UUID var1);

    public SpellType getSelectedSpells(UUID var1);

    public ParticleType getSelectedParticles(UUID var1);

    public BoosterType getSelectedBooster(UUID var1);

    public void setSelectedBooster(UUID var1, BoosterType var2);

    public void setSelectedParticles(UUID var1, ParticleType var2);

    public void setSelectedSpells(UUID var1, SpellType var2);

    public void setSelectedTrails(UUID var1, TrailType var2);
}

