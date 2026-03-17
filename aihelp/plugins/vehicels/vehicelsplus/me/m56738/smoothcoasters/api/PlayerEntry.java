/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.m56738.smoothcoasters.api;

import me.m56738.smoothcoasters.api.implementation.Implementation;

class PlayerEntry {
    private Implementation implementation;
    private String version;

    PlayerEntry() {
    }

    public Implementation getImplementation() {
        return this.implementation;
    }

    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String string) {
        this.version = string;
    }
}

