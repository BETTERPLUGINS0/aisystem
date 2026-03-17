/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.profiles.builder;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import me.zombie_striker.qav.util.xseries.profiles.ProfileLogger;
import me.zombie_striker.qav.util.xseries.profiles.builder.ProfileFallback;
import me.zombie_striker.qav.util.xseries.profiles.builder.XSkull;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.InvalidProfileException;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileChangeException;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileException;
import me.zombie_striker.qav.util.xseries.profiles.mojang.PlayerProfileFetcherThread;
import me.zombie_striker.qav.util.xseries.profiles.mojang.ProfileRequestConfiguration;
import me.zombie_striker.qav.util.xseries.profiles.objects.DelegateProfileable;
import me.zombie_striker.qav.util.xseries.profiles.objects.ProfileContainer;
import me.zombie_striker.qav.util.xseries.profiles.objects.Profileable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ProfileInstruction<T>
implements DelegateProfileable {
    private final ProfileContainer<T> profileContainer;
    private Profileable profileable;
    private final List<Profileable> fallbacks = new ArrayList<Profileable>();
    private Consumer<ProfileFallback<T>> onFallback;
    private ProfileRequestConfiguration profileRequestConfiguration;
    private boolean lenient = false;

    protected ProfileInstruction(ProfileContainer<T> profileContainer) {
        this.profileContainer = profileContainer;
    }

    @NotNull
    @Contract(mutates="this")
    public T removeProfile() {
        this.profileContainer.setProfile(null);
        return this.profileContainer.getObject();
    }

    @ApiStatus.Experimental
    @NotNull
    @Contract(value="_ -> this", mutates="this")
    public ProfileInstruction<T> profileRequestConfiguration(ProfileRequestConfiguration profileRequestConfiguration) {
        this.profileRequestConfiguration = profileRequestConfiguration;
        return this;
    }

    @NotNull
    @Contract(value="-> this", mutates="this")
    public ProfileInstruction<T> lenient() {
        this.lenient = true;
        return this;
    }

    @Override
    @Nullable
    public GameProfile getProfile() {
        return this.profileContainer.getProfile();
    }

    @Override
    @Contract(pure=true)
    public Profileable getDelegateProfile() {
        return this.profileContainer;
    }

    @NotNull
    @Contract(value="_ -> this", mutates="this")
    public ProfileInstruction<T> profile(@NotNull Profileable profileable) {
        this.profileable = Objects.requireNonNull(profileable, "Profileable is null");
        return this;
    }

    @NotNull
    @Contract(value="_ -> this", mutates="this")
    public ProfileInstruction<T> fallback(@NotNull Profileable ... profileableArray) {
        Objects.requireNonNull(profileableArray, "fallbacks array is null");
        this.fallbacks.addAll(Arrays.asList(profileableArray));
        return this;
    }

    @NotNull
    @Contract(value="_ -> this", mutates="this")
    public ProfileInstruction<T> onFallback(@Nullable Consumer<ProfileFallback<T>> consumer) {
        this.onFallback = consumer;
        return this;
    }

    @NotNull
    @Contract(value="_ -> this", mutates="this")
    public ProfileInstruction<T> onFallback(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "onFallback runnable is null");
        this.onFallback = profileFallback -> runnable.run();
        return this;
    }

    @NotNull
    public T apply() {
        Objects.requireNonNull(this.profileable, "No profile was set");
        Throwable throwable = null;
        ArrayList<Profileable> arrayList = new ArrayList<Profileable>(2 + this.fallbacks.size());
        arrayList.add(this.profileable);
        arrayList.addAll(this.fallbacks);
        if (this.lenient) {
            arrayList.add(XSkull.getDefaultProfile());
        }
        boolean bl = false;
        boolean bl2 = false;
        for (Profileable object : arrayList) {
            try {
                GameProfile profileException = object.getDisposableProfile();
                if (profileException != null) {
                    this.profileContainer.setProfile(profileException);
                    bl = true;
                    break;
                }
                if (throwable == null) {
                    throwable = new ProfileChangeException("Could not set the profile for " + this.profileContainer);
                }
                throwable.addSuppressed(new InvalidProfileException(object.toString(), "Profile doesn't have a value: " + object));
                bl2 = true;
            } catch (ProfileException profileException) {
                if (throwable == null) {
                    throwable = new ProfileChangeException("Could not set the profile for " + this.profileContainer);
                }
                throwable.addSuppressed(profileException);
                bl2 = true;
            }
        }
        if (throwable != null) {
            if (bl || this.lenient) {
                ProfileLogger.debug("apply() silenced exception {}", throwable);
            } else {
                throw throwable;
            }
        }
        Iterator<Object> iterator = this.profileContainer.getObject();
        if (bl2 && this.onFallback != null) {
            ProfileFallback profileFallback = new ProfileFallback(this, iterator, (ProfileChangeException)throwable);
            this.onFallback.accept(profileFallback);
            iterator = profileFallback.getObject();
        }
        return (T)iterator;
    }

    @NotNull
    public CompletableFuture<T> applyAsync() {
        return CompletableFuture.supplyAsync(this::apply, PlayerProfileFetcherThread.EXECUTOR);
    }
}

