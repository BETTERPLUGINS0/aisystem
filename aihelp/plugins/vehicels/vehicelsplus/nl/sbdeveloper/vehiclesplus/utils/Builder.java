/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class Builder {
    protected void validateNonNullFields() {
        Field[] fieldArray;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Field field : fieldArray = this.getClass().getDeclaredFields()) {
            if (!this.hasNonNullAnnotation(field)) continue;
            field.setAccessible(true);
            try {
                if (field.get(this) != null) continue;
                arrayList.add(field.getName());
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        if (!arrayList.isEmpty()) {
            throw new IllegalStateException("The following non-null fields are not initialized: " + String.valueOf(arrayList));
        }
    }

    private boolean hasNonNullAnnotation(Field field) {
        Annotation[] annotationArray;
        for (Annotation annotation : annotationArray = field.getDeclaredAnnotations()) {
            if (!annotation.annotationType().getSimpleName().equals("NotNull")) continue;
            return true;
        }
        return false;
    }
}

