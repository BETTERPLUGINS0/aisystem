/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.api.nbt;

import nl.sbdeveloper.vehiclesplus.api.nbt.NBTSerializable;
import org.bukkit.Color;

public class NBTColorAdapter
implements NBTSerializable<Color> {
    public static final NBTColorAdapter INSTANCE = new NBTColorAdapter();

    @Override
    public String serialize(Color color) {
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    @Override
    public Color deserialize(String string) {
        String[] stringArray = string.split(",");
        return Color.fromRGB((int)Integer.parseInt(stringArray[0]), (int)Integer.parseInt(stringArray[1]), (int)Integer.parseInt(stringArray[2]));
    }
}

