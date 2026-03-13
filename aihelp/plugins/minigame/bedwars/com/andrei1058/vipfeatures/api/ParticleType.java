/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.vipfeatures.api;

public enum ParticleType {
    NONE(null, ""),
    SPIRAL(null, "vipfeatures.particles.spiral"),
    ANGRY_VILLAGER("VILLAGER_ANGRY", "vipfeatures.particles.angryvillager"),
    DOUBLE_WITCH("SPELL_WITCH", "vipfeatures.particles.doublewitch"),
    NOTES("NOTE", "vipfeatures.particles.notes"),
    MAGIC("CRIT_MAGIC", "vipfeatures.particles.magic"),
    HAPPY_VILLAGER("VILLAGER_HAPPY", "vipfeatures.particles.happyvillager");

    private final String particle;
    private final String permission;

    private ParticleType(String particle, String permission) {
        this.particle = particle;
        this.permission = permission;
    }

    public String getParticle() {
        return this.particle;
    }

    public String getPermission() {
        return this.permission;
    }
}

