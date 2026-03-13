/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.abilities;

public class DisabledAbility {
    private final Long forTime;
    private final Long initialized;
    private final Long activatesOn;
    private final String ability;

    public DisabledAbility(long l, String string) {
        this.ability = string;
        this.forTime = l;
        this.initialized = System.currentTimeMillis();
        this.activatesOn = this.initialized + this.forTime;
    }

    public Long getForTime() {
        return this.forTime;
    }

    public Long getInitialized() {
        return this.initialized;
    }

    public Long getActivatesOn() {
        return this.activatesOn;
    }

    public String getAbility() {
        return this.ability;
    }
}

