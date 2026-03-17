/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.sbdeveloper.vehiclesplus.utils.jackson.color.ColorListDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.color.ColorListSerializer;
import org.bukkit.Color;

@JsonSerialize(using=ColorListSerializer.class)
@JsonDeserialize(using=ColorListDeserializer.class)
public class ColorList
extends ArrayList<Color> {
    public ColorList(List<Color> list) {
        super(list);
    }

    public static ColorList of(Color ... colorArray) {
        return new ColorList(Arrays.asList(colorArray));
    }
}

