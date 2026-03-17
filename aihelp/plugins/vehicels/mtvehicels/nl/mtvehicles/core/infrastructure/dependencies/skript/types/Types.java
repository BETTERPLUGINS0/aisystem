/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.classes.ClassInfo
 *  ch.njol.skript.classes.Parser
 *  ch.njol.skript.lang.ParseContext
 *  ch.njol.skript.registrations.Classes
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

public class Types {
    static {
        if (Classes.getClassInfoNoError((String)"mtvehicle") == null) {
            Classes.registerClass((ClassInfo)new ClassInfo(Vehicle.class, "mtvehicle").user(new String[]{"mtvehicles?"}).name("MTVehicle").description(new String[]{"Represents an MTV Vehicle."}).since("2.5.5").parser((Parser)new Parser<Vehicle>(){

                public boolean canParse(ParseContext context) {
                    return false;
                }

                public String toString(Vehicle v, int flags) {
                    return v.toString();
                }

                public String toVariableNameString(Vehicle v) {
                    return v.toString();
                }
            }));
        }
    }
}

