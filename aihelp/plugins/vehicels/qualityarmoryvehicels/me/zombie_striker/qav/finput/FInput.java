/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.finput;

import me.zombie_striker.qav.VehicleEntity;

public interface FInput {
    public void onInput(VehicleEntity var1);

    public String getName();

    public static enum ClickType {
        RIGHT("RMB"),
        LEFT("LMB"),
        F("F");

        private final String id;

        private ClickType(String string2) {
            this.id = string2;
        }

        public String getId() {
            return this.id;
        }
    }
}

