/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.yggdrasil.ProfileActionType
 *  com.mojang.authlib.yggdrasil.ProfileResult
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.profiles.mojang;

import com.google.common.base.Strings;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileActionType;
import com.mojang.authlib.yggdrasil.ProfileResult;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.profiles.PlayerProfiles;
import me.zombie_striker.qav.util.xseries.profiles.mojang.PlayerProfile;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
abstract class MojangProfileCache {
    MojangProfileCache() {
    }

    abstract void cache(PlayerProfile var1);

    @Nullable
    abstract Optional<GameProfile> get(UUID var1, GameProfile var2);

    protected static final class GameProfileCache
    extends MojangProfileCache {
        private final LoadingCache<GameProfile, GameProfile> insecureProfiles;

        GameProfileCache(LoadingCache<?, ?> loadingCache) {
            this.insecureProfiles = loadingCache;
        }

        @Override
        void cache(PlayerProfile playerProfile) {
            if (playerProfile.exists()) {
                this.insecureProfiles.put(playerProfile.requestedGameProfile, playerProfile.fetchedGameProfile);
            } else {
                this.insecureProfiles.put(playerProfile.requestedGameProfile, PlayerProfiles.NIL);
            }
        }

        @Override
        @Nullable
        Optional<GameProfile> get(UUID uUID, GameProfile gameProfile) {
            String string = gameProfile.getName();
            if (Strings.isNullOrEmpty(string) || string.equals("XSeries")) {
                return null;
            }
            GameProfile gameProfile2 = (GameProfile)this.insecureProfiles.getIfPresent(new GameProfile(uUID, gameProfile.getName()));
            if (gameProfile2 == PlayerProfiles.NIL) {
                return Optional.empty();
            }
            return gameProfile2 == null ? null : Optional.of(gameProfile2);
        }
    }

    protected static final class ProfileResultCache
    extends MojangProfileCache {
        private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles;

        ProfileResultCache(LoadingCache<?, ?> loadingCache) {
            this.insecureProfiles = loadingCache;
        }

        @Override
        void cache(PlayerProfile playerProfile) {
            if (playerProfile.exists()) {
                ProfileResult profileResult = new ProfileResult(playerProfile.fetchedGameProfile, playerProfile.profileActions.stream().map(string -> {
                    try {
                        return ProfileActionType.valueOf((String)string);
                    } catch (IllegalArgumentException illegalArgumentException) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toSet()));
                this.insecureProfiles.put(playerProfile.realUUID, Optional.of(profileResult));
            } else {
                this.insecureProfiles.put(playerProfile.realUUID, Optional.empty());
            }
        }

        @Override
        Optional<GameProfile> get(UUID uUID, GameProfile gameProfile) {
            Optional optional = (Optional)this.insecureProfiles.getIfPresent(uUID);
            return optional == null ? null : optional.map(ProfileResult::profile);
        }
    }
}

