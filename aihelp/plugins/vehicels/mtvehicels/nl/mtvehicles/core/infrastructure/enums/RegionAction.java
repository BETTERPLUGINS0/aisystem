/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.enums;

public enum RegionAction {
    PLACE,
    PICKUP,
    ENTER,
    RIDE;


    public static enum ListType {
        DISABLED,
        WHITELIST,
        BLACKLIST;


        public boolean isEnabled() {
            return !this.equals((Object)DISABLED);
        }

        public boolean isWhitelist() {
            return this.equals((Object)WHITELIST);
        }

        public boolean isBlacklist() {
            return this.equals((Object)BLACKLIST);
        }
    }
}

