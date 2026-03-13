/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.mutable;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.mutable.Mutable;

public class MutableObject<T>
implements Mutable<T>,
Serializable {
    private static final long serialVersionUID = 86241875189L;
    private T value;

    public MutableObject() {
    }

    public MutableObject(T t) {
        this.value = t;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() == object.getClass()) {
            MutableObject mutableObject = (MutableObject)object;
            return Objects.equals(this.value, mutableObject.value);
        }
        return false;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public void setValue(T t) {
        this.value = t;
    }

    public String toString() {
        return Objects.toString(this.value);
    }
}

