/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.seasons.biomes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomeDescriptor;
import net.advancedplugins.seasons.biomes.SeasonData;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.nms.NMSBiomeHelper;
import net.advancedplugins.seasons.objects.INMSBiome;
import org.jetbrains.annotations.NotNull;

public class AdvancedBiomeBase {
    private List<String> biomes = new ArrayList<String>();
    private int temperature = 0;
    private HashMap<SeasonType, SeasonData> seasons = new HashMap();
    private String name;
    private final HashMap<SeasonType, INMSBiome> biome = new HashMap();
    private final HashMap<SeasonType, HashMap<Integer, INMSBiome>> subbiomes = new HashMap();
    private final List<BiomeDescriptor> subBiomesDescriptorList = new ArrayList<BiomeDescriptor>();

    public void addBiomeDescriptor(@NotNull BiomeDescriptor biomeDescriptor) {
        if (!NMSBiomeHelper.allBiomeDescriptors().contains(biomeDescriptor)) {
            Core.getInstance().getLogger().warning(String.valueOf(biomeDescriptor.getKey()) + " doesn't match with any loaded biome");
        }
        this.subBiomesDescriptorList.add(biomeDescriptor);
        this.biomes.add(biomeDescriptor.upperCaseName());
    }

    public List<String> getBiomes() {
        return this.biomes;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public HashMap<SeasonType, SeasonData> getSeasons() {
        return this.seasons;
    }

    public String getName() {
        return this.name;
    }

    public HashMap<SeasonType, INMSBiome> getBiome() {
        return this.biome;
    }

    public HashMap<SeasonType, HashMap<Integer, INMSBiome>> getSubbiomes() {
        return this.subbiomes;
    }

    public List<BiomeDescriptor> getSubBiomesDescriptorList() {
        return this.subBiomesDescriptorList;
    }

    public void setBiomes(List<String> list) {
        this.biomes = list;
    }

    public void setTemperature(int n) {
        this.temperature = n;
    }

    public void setSeasons(HashMap<SeasonType, SeasonData> hashMap) {
        this.seasons = hashMap;
    }

    public void setName(String string) {
        this.name = string;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof AdvancedBiomeBase)) {
            return false;
        }
        AdvancedBiomeBase advancedBiomeBase = (AdvancedBiomeBase)object;
        if (!advancedBiomeBase.canEqual(this)) {
            return false;
        }
        if (this.getTemperature() != advancedBiomeBase.getTemperature()) {
            return false;
        }
        List<String> list = this.getBiomes();
        List<String> list2 = advancedBiomeBase.getBiomes();
        if (list == null ? list2 != null : !((Object)list).equals(list2)) {
            return false;
        }
        HashMap<SeasonType, SeasonData> hashMap = this.getSeasons();
        HashMap<SeasonType, SeasonData> hashMap2 = advancedBiomeBase.getSeasons();
        if (hashMap == null ? hashMap2 != null : !((Object)hashMap).equals(hashMap2)) {
            return false;
        }
        String string = this.getName();
        String string2 = advancedBiomeBase.getName();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        HashMap<SeasonType, INMSBiome> hashMap3 = this.getBiome();
        HashMap<SeasonType, INMSBiome> hashMap4 = advancedBiomeBase.getBiome();
        if (hashMap3 == null ? hashMap4 != null : !((Object)hashMap3).equals(hashMap4)) {
            return false;
        }
        HashMap<SeasonType, HashMap<Integer, INMSBiome>> hashMap5 = this.getSubbiomes();
        HashMap<SeasonType, HashMap<Integer, INMSBiome>> hashMap6 = advancedBiomeBase.getSubbiomes();
        if (hashMap5 == null ? hashMap6 != null : !((Object)hashMap5).equals(hashMap6)) {
            return false;
        }
        List<BiomeDescriptor> list3 = this.getSubBiomesDescriptorList();
        List<BiomeDescriptor> list4 = advancedBiomeBase.getSubBiomesDescriptorList();
        return !(list3 == null ? list4 != null : !((Object)list3).equals(list4));
    }

    protected boolean canEqual(Object object) {
        return object instanceof AdvancedBiomeBase;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + this.getTemperature();
        List<String> list = this.getBiomes();
        n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
        HashMap<SeasonType, SeasonData> hashMap = this.getSeasons();
        n2 = n2 * 59 + (hashMap == null ? 43 : ((Object)hashMap).hashCode());
        String string = this.getName();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        HashMap<SeasonType, INMSBiome> hashMap2 = this.getBiome();
        n2 = n2 * 59 + (hashMap2 == null ? 43 : ((Object)hashMap2).hashCode());
        HashMap<SeasonType, HashMap<Integer, INMSBiome>> hashMap3 = this.getSubbiomes();
        n2 = n2 * 59 + (hashMap3 == null ? 43 : ((Object)hashMap3).hashCode());
        List<BiomeDescriptor> list2 = this.getSubBiomesDescriptorList();
        n2 = n2 * 59 + (list2 == null ? 43 : ((Object)list2).hashCode());
        return n2;
    }

    public String toString() {
        return "AdvancedBiomeBase(biomes=" + String.valueOf(this.getBiomes()) + ", temperature=" + this.getTemperature() + ", seasons=" + String.valueOf(this.getSeasons()) + ", name=" + this.getName() + ", biome=" + String.valueOf(this.getBiome()) + ", subbiomes=" + String.valueOf(this.getSubbiomes()) + ", subBiomesDescriptorList=" + String.valueOf(this.getSubBiomesDescriptorList()) + ")";
    }
}

