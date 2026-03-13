/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.biomes;

import java.util.HashMap;
import net.advancedplugins.seasons.enums.ColorType;

public class SeasonData {
    private boolean snow = false;
    private HashMap<ColorType, String> colors = new HashMap();
    private Boolean winterFreeze = false;

    public void setColor(ColorType colorType, String string) {
        this.colors.put(colorType, string);
    }

    public boolean isSnow() {
        return this.snow;
    }

    public HashMap<ColorType, String> getColors() {
        return this.colors;
    }

    public Boolean getWinterFreeze() {
        return this.winterFreeze;
    }

    public void setSnow(boolean bl) {
        this.snow = bl;
    }

    public void setColors(HashMap<ColorType, String> hashMap) {
        this.colors = hashMap;
    }

    public void setWinterFreeze(Boolean bl) {
        this.winterFreeze = bl;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SeasonData)) {
            return false;
        }
        SeasonData seasonData = (SeasonData)object;
        if (!seasonData.canEqual(this)) {
            return false;
        }
        if (this.isSnow() != seasonData.isSnow()) {
            return false;
        }
        Boolean bl = this.getWinterFreeze();
        Boolean bl2 = seasonData.getWinterFreeze();
        if (bl == null ? bl2 != null : !((Object)bl).equals(bl2)) {
            return false;
        }
        HashMap<ColorType, String> hashMap = this.getColors();
        HashMap<ColorType, String> hashMap2 = seasonData.getColors();
        return !(hashMap == null ? hashMap2 != null : !((Object)hashMap).equals(hashMap2));
    }

    protected boolean canEqual(Object object) {
        return object instanceof SeasonData;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + (this.isSnow() ? 79 : 97);
        Boolean bl = this.getWinterFreeze();
        n2 = n2 * 59 + (bl == null ? 43 : ((Object)bl).hashCode());
        HashMap<ColorType, String> hashMap = this.getColors();
        n2 = n2 * 59 + (hashMap == null ? 43 : ((Object)hashMap).hashCode());
        return n2;
    }

    public String toString() {
        return "SeasonData(snow=" + this.isSnow() + ", colors=" + String.valueOf(this.getColors()) + ", winterFreeze=" + this.getWinterFreeze() + ")";
    }
}

