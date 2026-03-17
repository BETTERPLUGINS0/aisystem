/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.profiles;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.PlayerUUIDs;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.ProfilesCore;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.objects.transformer.ProfileTransformer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class PlayerProfiles {
    public static final String XSERIES_SIG = "XSeries";
    private static final Property XSERIES_GAMEPROFILE_SIGNATURE = new Property("XSeries", "12.1.0");
    private static final String TEXTURES_PROPERTY = "textures";
    public static final GameProfile NIL = PlayerProfiles.createGameProfile(PlayerUUIDs.IDENTITY_UUID, "XSeries");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TEXTURES_NBT_PROPERTY_PREFIX = "{\"textures\":{\"SKIN\":{\"url\":\"";
    public static final String TEXTURES_BASE_URL = "http://textures.minecraft.net/texture/";

    public static Optional<Property> getTextureProperty(GameProfile gameProfile) {
        return Optional.ofNullable(Iterables.getFirst(gameProfile.getProperties().get((Object)TEXTURES_PROPERTY), null));
    }

    @Nullable
    public static String getTextureValue(@NotNull GameProfile gameProfile) {
        Objects.requireNonNull(gameProfile, "Game profile cannot be null");
        return PlayerProfiles.getTextureProperty(gameProfile).map(PlayerProfiles::getPropertyValue).orElse(null);
    }

    @Nullable
    public static String getOriginalValue(@Nullable GameProfile gameProfile) {
        if (gameProfile == null) {
            return null;
        }
        String string = ProfileTransformer.IncludeOriginalValue.getOriginalValue(gameProfile);
        if (string != null) {
            return string;
        }
        return PlayerProfiles.getTextureValue(gameProfile);
    }

    @NotNull
    public static String getPropertyValue(@NotNull Property property) {
        if (ProfilesCore.NULLABILITY_RECORD_UPDATE) {
            return property.value();
        }
        try {
            return ProfilesCore.Property_getValue.invoke(property);
        } catch (Throwable throwable) {
            throw new RuntimeException("Unable to get a property value: " + property, throwable);
        }
    }

    public static boolean hasTextures(GameProfile gameProfile) {
        return PlayerProfiles.getTextureProperty(gameProfile).isPresent();
    }

    @NotNull
    public static GameProfile profileFromHashAndBase64(String string, String string2) {
        UUID uUID = UUID.nameUUIDFromBytes(string.getBytes(StandardCharsets.UTF_8));
        GameProfile gameProfile = PlayerProfiles.createNamelessGameProfile(uUID);
        PlayerProfiles.setTexturesProperty(gameProfile, string2);
        return gameProfile;
    }

    public static void removeTimestamp(GameProfile gameProfile) {
        JsonObject jsonObject = Optional.ofNullable(PlayerProfiles.getTextureValue(gameProfile)).map(PlayerProfiles::decodeBase64).map(string -> new JsonParser().parse(string).getAsJsonObject()).orElse(null);
        if (jsonObject == null || !jsonObject.has("timestamp")) {
            return;
        }
        jsonObject.remove("timestamp");
        PlayerProfiles.setTexturesProperty(gameProfile, PlayerProfiles.encodeBase64(GSON.toJson((JsonElement)jsonObject)));
    }

    @Nullable
    public static GameProfile unwrapProfile(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof GameProfile) && ProfilesCore.ResolvableProfile_gameProfile != null) {
            object = ProfilesCore.ResolvableProfile_gameProfile.invoke(object);
        }
        return (GameProfile)object;
    }

    @Nullable
    public static Object wrapProfile(@Nullable GameProfile gameProfile) {
        if (gameProfile == null) {
            return null;
        }
        if (ProfilesCore.ResolvableProfile$bukkitSupports) {
            return ProfilesCore.ResolvableProfile$constructor.invoke(gameProfile);
        }
        return gameProfile;
    }

    public static GameProfile sanitizeProfile(GameProfile gameProfile) {
        if (PlayerUUIDs.isOnlineMode()) {
            return gameProfile;
        }
        UUID uUID = PlayerUUIDs.getOfflineUUID(gameProfile.getName());
        PlayerUUIDs.ONLINE_TO_OFFLINE.put(gameProfile.getId(), uUID);
        GameProfile gameProfile2 = PlayerProfiles.createGameProfile(uUID, gameProfile.getName());
        gameProfile2.getProperties().putAll((Multimap)gameProfile.getProperties());
        return gameProfile2;
    }

    public static GameProfile clone(GameProfile gameProfile) {
        GameProfile gameProfile2 = new GameProfile(gameProfile.getId(), gameProfile.getName());
        gameProfile2.getProperties().putAll((Multimap)gameProfile.getProperties());
        return gameProfile2;
    }

    public static void setTexturesProperty(GameProfile gameProfile, String string) {
        Property property = new Property(TEXTURES_PROPERTY, string);
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.asMap().remove(TEXTURES_PROPERTY);
        propertyMap.put((Object)TEXTURES_PROPERTY, (Object)property);
    }

    public static String encodeBase64(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    public static String decodeBase64(String string) {
        Objects.requireNonNull(string, "Cannot decode null string");
        try {
            byte[] byArray = Base64.getDecoder().decode(string);
            return new String(byArray, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    public static GameProfile createGameProfile(UUID uUID, String string) {
        return PlayerProfiles.signXSeries(new GameProfile(uUID, string));
    }

    public static GameProfile signXSeries(GameProfile gameProfile) {
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.put((Object)XSERIES_SIG, (Object)XSERIES_GAMEPROFILE_SIGNATURE);
        return gameProfile;
    }

    public static GameProfile createNamelessGameProfile(UUID uUID) {
        return PlayerProfiles.createGameProfile(uUID, XSERIES_SIG);
    }
}

