/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.enums;

public enum ConfigType {
    DEFAULT("config.yml"),
    VEHICLES("vehicles.yml"),
    VEHICLE_DATA("vehicleData.yml"),
    SUPERSECRETSETTINGS("supersecretsettings.yml"),
    MESSAGES;

    private String fileName = null;

    private ConfigType() {
    }

    private ConfigType(String fileName) {
        this.fileName = fileName;
    }

    public boolean isMessages() {
        return this.equals((Object)MESSAGES);
    }

    public String getFileName() {
        return this.fileName;
    }
}

