/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.vipfeatures.api;

public enum BoosterType {
    NONE(1, ""),
    BOOSTER_X_2(2, "vipfeatures.boosters.x2"),
    BOOSTER_X_3(3, "vipfeatures.boosters.x3");

    int multiplier;
    String permission;

    private BoosterType(int multiplier, String permission) {
        this.multiplier = multiplier;
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    public int getMultiplier() {
        return this.multiplier;
    }
}

