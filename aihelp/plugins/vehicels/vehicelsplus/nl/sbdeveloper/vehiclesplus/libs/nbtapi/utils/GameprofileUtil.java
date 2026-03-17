/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import javax.annotation.Nullable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBTCompoundList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;

public class GameprofileUtil {
    private static Method GET_NAME = null;
    private static Method GET_ID = null;
    private static Method GET_VALUE = null;
    private static Method GET_SIGNATURE = null;
    private static Method GET_PROPERTY_NAME = null;
    private static Method GET_PROPERTIES = null;

    @Nullable
    public static GameProfile readGameProfile(ReadableNBT readableNBT) {
        String string = null;
        UUID uUID = null;
        if (readableNBT.hasTag("Name") && readableNBT.getType("Name") == NBTType.NBTTagString) {
            string = readableNBT.getString("Name");
        } else if (readableNBT.hasTag("name") && readableNBT.getType("name") == NBTType.NBTTagString) {
            string = readableNBT.getString("name");
        }
        if (readableNBT.hasTag("Id") && readableNBT.getType("Id") == NBTType.NBTTagIntArray && readableNBT.getIntArray("Id").length == 4) {
            uUID = readableNBT.getUUID("Id");
        } else if (readableNBT.hasTag("id") && readableNBT.getType("id") == NBTType.NBTTagIntArray && readableNBT.getIntArray("id").length == 4) {
            uUID = readableNBT.getUUID("id");
        }
        try {
            GameProfile gameProfile = new GameProfile(uUID, string);
            if (readableNBT.hasTag("Properties") && readableNBT.getType("Properties") == NBTType.NBTTagCompound) {
                ReadableNBT readableNBT2 = readableNBT.getCompound("Properties");
                for (String string2 : readableNBT2.getKeys()) {
                    ReadableNBTList<ReadWriteNBT> readableNBTList = readableNBT2.getCompoundList(string);
                    for (int i = 0; i < readableNBTList.size(); ++i) {
                        ReadableNBT readableNBT3 = readableNBTList.get(i);
                        String string3 = readableNBT3.getString("Value");
                        if (readableNBT3.hasTag("Signature") && readableNBT3.getType("Signature") == NBTType.NBTTagString) {
                            gameProfile.getProperties().put((Object)string2, (Object)new Property(string2, string3, readableNBT3.getString("Signature")));
                            continue;
                        }
                        gameProfile.getProperties().put((Object)string2, (Object)new Property(string2, string3));
                    }
                }
            } else if (readableNBT.getType("properties") == NBTType.NBTTagList) {
                ReadableNBTList<ReadWriteNBT> readableNBTList = readableNBT.getCompoundList("properties");
                for (int i = 0; i < readableNBTList.size(); ++i) {
                    ReadableNBT readableNBT4 = readableNBTList.get(i);
                    String string4 = readableNBT4.getString("name");
                    String string5 = readableNBT4.getString("value");
                    if (readableNBT4.hasTag("signature") && readableNBT4.getType("signature") == NBTType.NBTTagString) {
                        gameProfile.getProperties().put((Object)string4, (Object)new Property(string4, string5, readableNBT4.getString("signature")));
                        continue;
                    }
                    gameProfile.getProperties().put((Object)string4, (Object)new Property(string4, string5));
                }
            }
            return gameProfile;
        } catch (Throwable throwable) {
            return null;
        }
    }

    public static ReadWriteNBT writeGameProfile(ReadWriteNBT readWriteNBT, GameProfile gameProfile) {
        block7: {
            Object object;
            Object object2;
            String string = GameprofileUtil.getName(gameProfile);
            if (string != null && !string.isEmpty()) {
                object2 = MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? "name" : "Name";
                readWriteNBT.setString((String)object2, GameprofileUtil.getName(gameProfile));
            }
            if ((object2 = GameprofileUtil.getId(gameProfile)) != null) {
                object = MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? "id" : "Id";
                readWriteNBT.setUUID((String)object, (UUID)object2);
            }
            if ((object = GameprofileUtil.getProperties(gameProfile)).isEmpty()) break block7;
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                ReadWriteNBTCompoundList readWriteNBTCompoundList = readWriteNBT.getCompoundList("properties");
                for (Property property : object.values()) {
                    ReadWriteNBT readWriteNBT2 = readWriteNBTCompoundList.addCompound();
                    readWriteNBT2.setString("name", GameprofileUtil.getPropertyName(property));
                    readWriteNBT2.setString("value", GameprofileUtil.getValue(property));
                    if (!property.hasSignature()) continue;
                    readWriteNBT2.setString("signature", GameprofileUtil.getSignature(property));
                }
            } else {
                ReadWriteNBT readWriteNBT3 = readWriteNBT.getOrCreateCompound("Properties");
                for (String string2 : gameProfile.getProperties().keySet()) {
                    ReadWriteNBTCompoundList readWriteNBTCompoundList = readWriteNBT3.getCompoundList(string2);
                    for (Property property : object.get((Object)string2)) {
                        ReadWriteNBT readWriteNBT4 = readWriteNBTCompoundList.addCompound();
                        readWriteNBT4.setString("Value", GameprofileUtil.getValue(property));
                        if (!property.hasSignature()) continue;
                        readWriteNBT4.setString("Signature", GameprofileUtil.getSignature(property));
                    }
                }
            }
        }
        return readWriteNBT;
    }

    private static String getName(GameProfile gameProfile) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                return GET_NAME.invoke(gameProfile, new Object[0]).toString();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Failed to get GameProfile name via reflection", exception);
            }
        }
        return gameProfile.getName();
    }

    private static UUID getId(GameProfile gameProfile) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                return (UUID)GET_ID.invoke(gameProfile, new Object[0]);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Failed to get GameProfile id via reflection", exception);
            }
        }
        return gameProfile.getId();
    }

    private static String getValue(Property property) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                return GET_VALUE.invoke(property, new Object[0]).toString();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Failed to get Property value via reflection", exception);
            }
        }
        return property.getValue();
    }

    private static String getSignature(Property property) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                return GET_SIGNATURE.invoke(property, new Object[0]).toString();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Failed to get Property signature via reflection", exception);
            }
        }
        return property.getSignature();
    }

    private static String getPropertyName(Property property) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                return GET_PROPERTY_NAME.invoke(property, new Object[0]).toString();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Failed to get Property name via reflection", exception);
            }
        }
        return property.getName();
    }

    private static PropertyMap getProperties(GameProfile gameProfile) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                return (PropertyMap)GET_PROPERTIES.invoke(gameProfile, new Object[0]);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Failed to get GameProfile properties via reflection", exception);
            }
        }
        return gameProfile.getProperties();
    }

    static {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
            try {
                GET_NAME = GameProfile.class.getDeclaredMethod("name", new Class[0]);
                GET_ID = GameProfile.class.getDeclaredMethod("id", new Class[0]);
                GET_VALUE = Property.class.getDeclaredMethod("value", new Class[0]);
                GET_SIGNATURE = Property.class.getDeclaredMethod("signature", new Class[0]);
                GET_PROPERTY_NAME = Property.class.getDeclaredMethod("name", new Class[0]);
                GET_PROPERTIES = GameProfile.class.getDeclaredMethod("properties", new Class[0]);
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
        }
    }
}

