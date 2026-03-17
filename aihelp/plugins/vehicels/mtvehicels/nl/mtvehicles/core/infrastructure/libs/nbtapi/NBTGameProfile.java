/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import com.mojang.authlib.GameProfile;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTContainer;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTReflectionUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.GameprofileUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ObjectCreator;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTGameProfile {
    @Deprecated
    public static NBTCompound toNBT(GameProfile profile) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            return (NBTCompound)GameprofileUtil.writeGameProfile(NBT.createNBTObject(), profile);
        }
        return new NBTContainer(ReflectionMethod.GAMEPROFILE_SERIALIZE.run(null, ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]), profile));
    }

    @Deprecated
    public static GameProfile fromNBT(NBTCompound compound) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            return GameprofileUtil.readGameProfile(compound);
        }
        return (GameProfile)ReflectionMethod.GAMEPROFILE_DESERIALIZE.run(null, NBTReflectionUtil.getToCompount(compound.getCompound(), compound));
    }
}

