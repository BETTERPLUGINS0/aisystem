/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.config.contentpackages;

import com.magmaguy.magmacore.config.CustomConfigFields;
import java.util.List;

public class ContentPackageConfigFields
extends CustomConfigFields {
    private int version = 0;
    private String name;
    private List<String> description;
    private String downloadLink;
    private String folderName;
    private ContentPackageType contentPackageType;
    private String nightbreakSlug;

    public ContentPackageConfigFields(String filename, boolean isEnabled, String name, List<String> description, String downloadLink, String folderName) {
        super(filename, isEnabled);
        this.name = name;
        this.description = description;
        this.downloadLink = downloadLink;
        this.folderName = folderName;
    }

    public ContentPackageConfigFields(String filename, boolean isEnabled) {
        super(filename, isEnabled);
    }

    @Override
    public void processConfigFields() {
        this.isEnabled = this.processBoolean("isEnabled", this.isEnabled, true, true);
        this.name = this.processString("name", this.name, null, true);
        this.description = this.processStringList("description", this.description, null, true);
        this.downloadLink = this.processString("downloadLink", this.downloadLink, this.downloadLink, false);
        this.version = this.processInt("version", this.version, 0, true);
        this.folderName = this.processString("folderNameV2", this.folderName, null, true);
        this.contentPackageType = this.processEnum("contentPackageType", this.contentPackageType, ContentPackageType.STRUCTURE, ContentPackageType.class, false);
        this.nightbreakSlug = this.processString("nightbreakSlug", this.nightbreakSlug, null, false);
    }

    public int getVersion() {
        return this.version;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public String getDownloadLink() {
        return this.downloadLink;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public ContentPackageType getContentPackageType() {
        return this.contentPackageType;
    }

    public void setContentPackageType(ContentPackageType contentPackageType) {
        this.contentPackageType = contentPackageType;
    }

    public String getNightbreakSlug() {
        return this.nightbreakSlug;
    }

    public void setNightbreakSlug(String nightbreakSlug) {
        this.nightbreakSlug = nightbreakSlug;
    }

    public static enum ContentPackageType {
        STRUCTURE,
        MODULAR;

    }
}

