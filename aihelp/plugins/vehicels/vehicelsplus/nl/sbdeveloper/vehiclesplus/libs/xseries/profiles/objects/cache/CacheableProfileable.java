/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.objects.cache;

import com.mojang.authlib.GameProfile;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.exceptions.MojangAPIRetryException;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.objects.Profileable;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public abstract class CacheableProfileable
implements Profileable {
    protected GameProfile cache;
    protected Throwable lastError;

    @Override
    public final synchronized GameProfile getProfile() {
        if (this.hasExpired(true)) {
            this.lastError = null;
            this.cache = null;
        }
        if (this.lastError != null && !(this.lastError instanceof MojangAPIRetryException)) {
            throw XReflection.throwCheckedException(this.lastError);
        }
        if (this.cache == null) {
            try {
                this.cache = this.getProfile0();
                this.lastError = null;
            } catch (Throwable throwable) {
                this.lastError = throwable;
                throw throwable;
            }
        }
        return this.cache;
    }

    public final boolean hasExpired() {
        return this.hasExpired(false);
    }

    protected boolean hasExpired(boolean bl) {
        return this.lastError instanceof MojangAPIRetryException;
    }

    @NotNull
    protected abstract GameProfile getProfile0();

    public final String toString() {
        return this.getClass().getSimpleName() + "[cache=" + this.cache + ", lastError=" + this.lastError + ']';
    }
}

