/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries.profiles.objects;

import com.mojang.authlib.GameProfile;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileException;
import me.zombie_striker.qav.util.xseries.profiles.objects.Profileable;
import me.zombie_striker.qav.util.xseries.profiles.objects.transformer.ProfileTransformer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Internal
public interface DelegateProfileable
extends Profileable {
    @ApiStatus.Internal
    @NotNull
    public Profileable getDelegateProfile();

    @Override
    @Nullable
    default public @Unmodifiable GameProfile getProfile() {
        return this.getDelegateProfile().getProfile();
    }

    @Override
    @Nullable
    default public ProfileException test() {
        return this.getDelegateProfile().test();
    }

    @Override
    @Nullable
    default public GameProfile getDisposableProfile() {
        return this.getDelegateProfile().getDisposableProfile();
    }

    @Override
    @NotNull
    default public Profileable transform(@NotNull ProfileTransformer ... transformers) {
        return this.getDelegateProfile().transform(transformers);
    }

    @Override
    @Nullable
    default public String getProfileValue() {
        return this.getDelegateProfile().getProfileValue();
    }
}

