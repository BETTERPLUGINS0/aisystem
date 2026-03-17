/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.identity;

import java.util.UUID;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class IdentityImpl
implements Examinable,
Identity {
    private final UUID uuid;

    IdentityImpl(UUID uUID) {
        this.uuid = uUID;
    }

    @Override
    @NotNull
    public UUID uuid() {
        return this.uuid;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Identity)) {
            return false;
        }
        Identity identity = (Identity)object;
        return this.uuid.equals(identity.uuid());
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }
}

