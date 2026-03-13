/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.builder;

final class IDKey {
    private final Object value;
    private final int id;

    IDKey(Object object) {
        this.id = System.identityHashCode(object);
        this.value = object;
    }

    public boolean equals(Object object) {
        if (!(object instanceof IDKey)) {
            return false;
        }
        IDKey iDKey = (IDKey)object;
        if (this.id != iDKey.id) {
            return false;
        }
        return this.value == iDKey.value;
    }

    public int hashCode() {
        return this.id;
    }
}

