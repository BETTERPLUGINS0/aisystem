/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.profiles.objects.transformer;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import me.zombie_striker.qav.util.xseries.profiles.PlayerProfiles;
import me.zombie_striker.qav.util.xseries.profiles.objects.Profileable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ProfileTransformer {
    @NotNull
    public GameProfile transform(@NotNull Profileable var1, @NotNull GameProfile var2);

    @ApiStatus.Internal
    public boolean canBeCached();

    @NotNull
    public static ProfileTransformer stackable() {
        return RemoveMetadata.INSTANCE;
    }

    @NotNull
    public static ProfileTransformer nonStackable() {
        return MakeNotStackable.INSTANCE;
    }

    @NotNull
    public static ProfileTransformer removeMetadata() {
        return RemoveMetadata.INSTANCE;
    }

    @NotNull
    public static ProfileTransformer includeOriginalValue() {
        return IncludeOriginalValue.INSTANCE;
    }

    public static final class RemoveMetadata
    implements ProfileTransformer {
        private static final RemoveMetadata INSTANCE = new RemoveMetadata();

        @Override
        public GameProfile transform(Profileable profileable, GameProfile gameProfile) {
            PlayerProfiles.removeTimestamp(gameProfile);
            Map map = gameProfile.getProperties().asMap();
            map.remove("XSeries");
            map.remove("OriginalValue");
            return gameProfile;
        }

        @Override
        public boolean canBeCached() {
            return true;
        }
    }

    public static final class MakeNotStackable
    implements ProfileTransformer {
        private static final MakeNotStackable INSTANCE = new MakeNotStackable();
        private static final String PROPERTY_NAME = "XSeriesSeed";
        private static final AtomicLong NEXT_ID = new AtomicLong();

        @Override
        public GameProfile transform(Profileable profileable, GameProfile gameProfile) {
            String string = System.currentTimeMillis() + "-" + NEXT_ID.getAndIncrement();
            gameProfile.getProperties().put((Object)PROPERTY_NAME, (Object)new Property(PROPERTY_NAME, string));
            return gameProfile;
        }

        @Override
        public boolean canBeCached() {
            return false;
        }
    }

    public static final class IncludeOriginalValue
    implements ProfileTransformer {
        private static final IncludeOriginalValue INSTANCE = new IncludeOriginalValue();
        public static final String PROPERTY_NAME = "OriginalValue";

        @Nullable
        public static String getOriginalValue(@NotNull GameProfile gameProfile) {
            PropertyMap propertyMap = gameProfile.getProperties();
            Collection collection = propertyMap.get((Object)PROPERTY_NAME);
            if (collection.isEmpty()) {
                return null;
            }
            Property property = Iterables.getFirst(collection, null);
            return PlayerProfiles.getPropertyValue(property);
        }

        @Override
        public GameProfile transform(Profileable profileable, GameProfile gameProfile) {
            String string = profileable.getProfileValue();
            gameProfile.getProperties().put((Object)PROPERTY_NAME, (Object)new Property(PROPERTY_NAME, string));
            return gameProfile;
        }

        @Override
        public boolean canBeCached() {
            return true;
        }
    }
}

