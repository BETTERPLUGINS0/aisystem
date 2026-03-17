/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.permission;

import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PermissionCheckers {
    static final PermissionChecker NOT_SET = new Always(TriState.NOT_SET);
    static final PermissionChecker FALSE = new Always(TriState.FALSE);
    static final PermissionChecker TRUE = new Always(TriState.TRUE);

    private PermissionCheckers() {
    }

    private static final class Always
    implements PermissionChecker {
        private final TriState value;

        private Always(TriState triState) {
            this.value = triState;
        }

        @Override
        @NotNull
        public TriState value(@NotNull String string) {
            return this.value;
        }

        public String toString() {
            return PermissionChecker.class.getSimpleName() + ".always(" + (Object)((Object)this.value) + ")";
        }

        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            Always always = (Always)object;
            return this.value == always.value;
        }

        public int hashCode() {
            return this.value.hashCode();
        }
    }
}

