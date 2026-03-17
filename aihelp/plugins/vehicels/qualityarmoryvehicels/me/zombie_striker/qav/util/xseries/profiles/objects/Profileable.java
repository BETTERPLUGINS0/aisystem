/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Skull
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries.profiles.objects;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import me.zombie_striker.qav.util.xseries.profiles.PlayerProfiles;
import me.zombie_striker.qav.util.xseries.profiles.PlayerUUIDs;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.InvalidProfileException;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileException;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.UnknownPlayerException;
import me.zombie_striker.qav.util.xseries.profiles.mojang.MojangAPI;
import me.zombie_striker.qav.util.xseries.profiles.mojang.PlayerProfileFetcherThread;
import me.zombie_striker.qav.util.xseries.profiles.mojang.ProfileRequestConfiguration;
import me.zombie_striker.qav.util.xseries.profiles.objects.ProfileContainer;
import me.zombie_striker.qav.util.xseries.profiles.objects.ProfileInputType;
import me.zombie_striker.qav.util.xseries.profiles.objects.cache.TimedCacheableProfileable;
import me.zombie_striker.qav.util.xseries.profiles.objects.transformer.ProfileTransformer;
import me.zombie_striker.qav.util.xseries.profiles.objects.transformer.TransformableProfile;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public interface Profileable {
    @Nullable
    @ApiStatus.Internal
    public @Unmodifiable GameProfile getProfile();

    @Nullable
    default public ProfileException test() {
        try {
            this.getProfile();
            return null;
        } catch (ProfileException ex) {
            return ex;
        }
    }

    @Nullable
    @ApiStatus.Internal
    default public GameProfile getDisposableProfile() {
        GameProfile profile = this.getProfile();
        return profile == null ? null : PlayerProfiles.clone(profile);
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    default public Profileable transform(@NotNull ProfileTransformer ... transformers) {
        return new TransformableProfile(this, Arrays.asList(transformers));
    }

    @Nullable
    default public String getProfileValue() {
        return PlayerProfiles.getOriginalValue(this.getProfile());
    }

    @NotNull
    @ApiStatus.Experimental
    public static <C extends Collection<Profileable>> CompletableFuture<C> prepare(@NotNull C profileables) {
        return Profileable.prepare(profileables, null, null);
    }

    @NotNull
    @ApiStatus.Experimental
    public static <C extends Collection<Profileable>> CompletableFuture<C> prepare(@NotNull C profileables, @Nullable ProfileRequestConfiguration config, @Nullable Function<Throwable, Boolean> errorHandler) {
        Objects.requireNonNull(profileables, "Profile list is null");
        CompletableFuture initial = CompletableFuture.completedFuture(new HashMap());
        ArrayList<String> usernameRequests = new ArrayList<String>();
        if (!PlayerUUIDs.isOnlineMode()) {
            for (Profileable profileable : profileables) {
                String username = null;
                if (profileable instanceof UsernameProfileable) {
                    username = ((UsernameProfileable)profileable).username;
                } else if (profileable instanceof PlayerProfileable) {
                    username = ((PlayerProfileable)profileable).username;
                } else if (profileable instanceof StringProfileable && ((StringProfileable)profileable).determineType().type == ProfileInputType.USERNAME) {
                    username = ((StringProfileable)profileable).string;
                }
                if (username == null) continue;
                usernameRequests.add(username);
            }
            if (!usernameRequests.isEmpty()) {
                initial = CompletableFuture.supplyAsync(() -> MojangAPI.usernamesToUUIDs(usernameRequests, config), PlayerProfileFetcherThread.EXECUTOR);
            }
        }
        return XReflection.stacktrace(((CompletableFuture)initial.thenCompose(a -> {
            ArrayList<CompletableFuture<GameProfile>> requests = new ArrayList<CompletableFuture<GameProfile>>(profileables.size());
            for (Profileable profileable : profileables) {
                CompletionStage<Object> async = CompletableFuture.supplyAsync(profileable::getProfile, PlayerProfileFetcherThread.EXECUTOR);
                if (errorHandler != null) {
                    async = XReflection.stacktrace(async).exceptionally(ex -> {
                        boolean rethrow = (Boolean)errorHandler.apply((Throwable)ex);
                        if (rethrow) {
                            throw XReflection.throwCheckedException(ex);
                        }
                        return null;
                    });
                }
                requests.add((CompletableFuture<GameProfile>)async);
            }
            return CompletableFuture.allOf(requests.toArray(new CompletableFuture[0]));
        })).thenApply(a -> profileables));
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable username(@NotNull String username) {
        return new UsernameProfileable(username);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull UUID uuid) {
        return new UUIDProfileable(uuid);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull GameProfile profile) {
        return new GameProfileProfileable(profile);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull OfflinePlayer offlinePlayer) {
        return new PlayerProfileable(offlinePlayer);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull BlockState blockState) {
        return new ProfileContainer.BlockStateProfileContainer((Skull)blockState);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull Block block) {
        return new ProfileContainer.BlockProfileContainer(block);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull ItemStack item) {
        return new ProfileContainer.ItemStackProfileContainer(item);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull ItemMeta meta) {
        return new ProfileContainer.ItemMetaProfileContainer((SkullMeta)meta);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable detect(@NotNull String input) {
        return new StringProfileable(input, null);
    }

    @NotNull
    @Contract(pure=true)
    public static Profileable of(@NotNull ProfileInputType type, @NotNull String input) {
        Objects.requireNonNull(type, () -> "Cannot profile from a null input type: " + input);
        Objects.requireNonNull(input, () -> "Cannot profile from a null input: " + (Object)((Object)type));
        return new StringProfileable(input, type);
    }

    @ApiStatus.Internal
    public static final class UsernameProfileable
    extends TimedCacheableProfileable {
        private final String username;
        private Boolean valid;

        public UsernameProfileable(String string) {
            this.username = Objects.requireNonNull(string);
        }

        @Override
        public String getProfileValue() {
            return this.username;
        }

        @Override
        protected GameProfile getProfile0() {
            if (this.valid == null) {
                this.valid = ProfileInputType.USERNAME.pattern.matcher(this.username).matches();
            }
            if (!this.valid.booleanValue()) {
                throw new InvalidProfileException(this.username, "Invalid username: '" + this.username + '\'');
            }
            Optional<GameProfile> optional = MojangAPI.getMojangCachedProfileFromUsername(this.username);
            if (!optional.isPresent()) {
                throw new UnknownPlayerException((Object)this.username, "Cannot find player named '" + this.username + '\'');
            }
            GameProfile gameProfile = optional.get();
            if (PlayerProfiles.hasTextures(gameProfile)) {
                return gameProfile;
            }
            return MojangAPI.getOrFetchProfile(gameProfile);
        }
    }

    @ApiStatus.Internal
    public static final class PlayerProfileable
    extends TimedCacheableProfileable {
        @Nullable
        private final String username;
        @NotNull
        private final UUID id;

        public PlayerProfileable(OfflinePlayer offlinePlayer) {
            Objects.requireNonNull(offlinePlayer);
            this.username = offlinePlayer.getName();
            this.id = offlinePlayer.getUniqueId();
        }

        @Override
        public String getProfileValue() {
            return Strings.isNullOrEmpty(this.username) ? this.id.toString() : this.username;
        }

        @Override
        protected GameProfile getProfile0() {
            if (Strings.isNullOrEmpty(this.username)) {
                return new UUIDProfileable(this.id).getProfile();
            }
            return new UsernameProfileable(this.username).getProfile();
        }
    }

    @ApiStatus.Internal
    public static final class StringProfileable
    extends TimedCacheableProfileable {
        private final String string;
        @Nullable
        private ProfileInputType type;

        public StringProfileable(String string, @Nullable ProfileInputType profileInputType) {
            this.string = Objects.requireNonNull(string, "Input string is null");
            this.type = profileInputType;
        }

        @Override
        public String getProfileValue() {
            return this.string;
        }

        private StringProfileable determineType() {
            if (this.type == null) {
                this.type = ProfileInputType.typeOf(this.string);
            }
            return this;
        }

        @Override
        protected GameProfile getProfile0() {
            this.determineType();
            if (this.type == null) {
                throw new InvalidProfileException(this.string, "Unknown skull string value: " + this.string);
            }
            return this.type.getProfile(this.string);
        }
    }

    @ApiStatus.Internal
    public static final class UUIDProfileable
    extends TimedCacheableProfileable {
        private final UUID id;

        public UUIDProfileable(UUID uUID) {
            this.id = Objects.requireNonNull(uUID, "UUID cannot be null");
        }

        @Override
        public String getProfileValue() {
            return this.id.toString();
        }

        @Override
        protected GameProfile getProfile0() {
            GameProfile gameProfile = MojangAPI.getCachedProfileByUUID(this.id);
            if (PlayerProfiles.hasTextures(gameProfile)) {
                return gameProfile;
            }
            return MojangAPI.getOrFetchProfile(gameProfile);
        }
    }

    @ApiStatus.Internal
    public static final class GameProfileProfileable
    extends TimedCacheableProfileable {
        private final GameProfile profile;

        public GameProfileProfileable(GameProfile gameProfile) {
            this.profile = Objects.requireNonNull(gameProfile);
        }

        @Override
        protected GameProfile getProfile0() {
            if (PlayerProfiles.hasTextures(this.profile)) {
                return this.profile;
            }
            return (PlayerUUIDs.isOnlineMode() ? new UUIDProfileable(this.profile.getId()) : new UsernameProfileable(this.profile.getName())).getProfile();
        }
    }
}

