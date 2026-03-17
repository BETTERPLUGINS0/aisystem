/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import com.mojang.authlib.GameProfile;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.GameprofileUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ObjectCreator;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTGameProfile {
    @Deprecated
    public static NBTCompound toNBT(GameProfile gameProfile) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            return (NBTCompound)GameprofileUtil.writeGameProfile(NBT.createNBTObject(), gameProfile);
        }
        return new NBTContainer(ReflectionMethod.GAMEPROFILE_SERIALIZE.run(null, ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]), gameProfile));
    }

    @Deprecated
    public static GameProfile fromNBT(NBTCompound nBTCompound) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            return GameprofileUtil.readGameProfile(nBTCompound);
        }
        return (GameProfile)ReflectionMethod.GAMEPROFILE_DESERIALIZE.run(null, NBTReflectionUtil.getToCompount(nBTCompound.getCompound(), nBTCompound));
    }
}

