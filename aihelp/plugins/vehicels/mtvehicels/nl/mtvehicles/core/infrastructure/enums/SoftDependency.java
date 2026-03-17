/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.enums;

public enum SoftDependency {
    WORLD_GUARD("WorldGuard"),
    VAULT("Vault"),
    PLACEHOLDER_API("PlaceholderAPI"),
    SKRIPT("Skript");

    private final String name;

    private SoftDependency(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

