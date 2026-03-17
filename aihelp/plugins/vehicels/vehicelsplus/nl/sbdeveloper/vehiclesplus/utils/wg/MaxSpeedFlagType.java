/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.FlagContext
 *  com.sk89q.worldguard.protection.flags.IntegerFlag
 *  com.sk89q.worldguard.protection.flags.InvalidFlagFormat
 *  com.sk89q.worldguard.protection.flags.MapFlag
 *  com.sk89q.worldguard.protection.flags.StringFlag
 */
package nl.sbdeveloper.vehiclesplus.utils.wg;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.MapFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;

public class MaxSpeedFlagType
extends MapFlag<String, Integer> {
    public MaxSpeedFlagType(String string) {
        super(string, (Flag)new StringFlag("vehicletype"), (Flag)new IntegerFlag("maxspeed"));
    }

    public Map<String, Integer> parseInput(FlagContext flagContext) {
        Map map = super.parseInput(flagContext);
        for (String string : map.keySet()) {
            if (!VehiclesPlusAPI.getVehicleType(string).isEmpty()) continue;
            throw new InvalidFlagFormat("The vehicle type " + string + " does not exist!");
        }
        return map;
    }
}

