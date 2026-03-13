/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import org.apache.commons.lang3.ArraySorter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.builder.ToStringSummary;
import org.apache.commons.lang3.stream.Streams;

public class ReflectionToStringBuilder
extends ToStringBuilder {
    private boolean appendStatics;
    private boolean appendTransients;
    private boolean excludeNullValues;
    protected String[] excludeFieldNames;
    protected String[] includeFieldNames;
    private Class<?> upToClass;

    static String[] toNoNullStringArray(Collection<String> collection) {
        if (collection == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return ReflectionToStringBuilder.toNoNullStringArray(collection.toArray());
    }

    static String[] toNoNullStringArray(Object[] objectArray) {
        return (String[])Streams.nonNull(objectArray).map(Objects::toString).toArray(String[]::new);
    }

    public static String toString(Object object) {
        return ReflectionToStringBuilder.toString(object, null, false, false, null);
    }

    public static String toString(Object object, ToStringStyle toStringStyle) {
        return ReflectionToStringBuilder.toString(object, toStringStyle, false, false, null);
    }

    public static String toString(Object object, ToStringStyle toStringStyle, boolean bl) {
        return ReflectionToStringBuilder.toString(object, toStringStyle, bl, false, null);
    }

    public static String toString(Object object, ToStringStyle toStringStyle, boolean bl, boolean bl2) {
        return ReflectionToStringBuilder.toString(object, toStringStyle, bl, bl2, null);
    }

    public static <T> String toString(T t, ToStringStyle toStringStyle, boolean bl, boolean bl2, boolean bl3, Class<? super T> clazz) {
        return new ReflectionToStringBuilder(t, toStringStyle, null, clazz, bl, bl2, bl3).toString();
    }

    public static <T> String toString(T t, ToStringStyle toStringStyle, boolean bl, boolean bl2, Class<? super T> clazz) {
        return new ReflectionToStringBuilder(t, toStringStyle, null, clazz, bl, bl2).toString();
    }

    public static String toStringExclude(Object object, Collection<String> collection) {
        return ReflectionToStringBuilder.toStringExclude(object, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static String toStringExclude(Object object, String ... stringArray) {
        return new ReflectionToStringBuilder(object).setExcludeFieldNames(stringArray).toString();
    }

    public static String toStringInclude(Object object, Collection<String> collection) {
        return ReflectionToStringBuilder.toStringInclude(object, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static String toStringInclude(Object object, String ... stringArray) {
        return new ReflectionToStringBuilder(object).setIncludeFieldNames(stringArray).toString();
    }

    public ReflectionToStringBuilder(Object object) {
        super(object);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle toStringStyle) {
        super(object, toStringStyle);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle toStringStyle, StringBuffer stringBuffer) {
        super(object, toStringStyle, stringBuffer);
    }

    public <T> ReflectionToStringBuilder(T t, ToStringStyle toStringStyle, StringBuffer stringBuffer, Class<? super T> clazz, boolean bl, boolean bl2) {
        super(t, toStringStyle, stringBuffer);
        this.setUpToClass(clazz);
        this.setAppendTransients(bl);
        this.setAppendStatics(bl2);
    }

    public <T> ReflectionToStringBuilder(T t, ToStringStyle toStringStyle, StringBuffer stringBuffer, Class<? super T> clazz, boolean bl, boolean bl2, boolean bl3) {
        super(t, toStringStyle, stringBuffer);
        this.setUpToClass(clazz);
        this.setAppendTransients(bl);
        this.setAppendStatics(bl2);
        this.setExcludeNullValues(bl3);
    }

    protected boolean accept(Field field) {
        if (field.getName().indexOf(36) != -1) {
            return false;
        }
        if (Modifier.isTransient(field.getModifiers()) && !this.isAppendTransients()) {
            return false;
        }
        if (Modifier.isStatic(field.getModifiers()) && !this.isAppendStatics()) {
            return false;
        }
        if (this.excludeFieldNames != null && Arrays.binarySearch(this.excludeFieldNames, field.getName()) >= 0) {
            return false;
        }
        if (ArrayUtils.isNotEmpty(this.includeFieldNames)) {
            return Arrays.binarySearch(this.includeFieldNames, field.getName()) >= 0;
        }
        return !field.isAnnotationPresent(ToStringExclude.class);
    }

    protected void appendFieldsIn(Class<?> clazz) {
        if (clazz.isArray()) {
            this.reflectionAppendArray(this.getObject());
            return;
        }
        AccessibleObject[] accessibleObjectArray = ArraySorter.sort(clazz.getDeclaredFields(), Comparator.comparing(Field::getName));
        AccessibleObject.setAccessible(accessibleObjectArray, true);
        for (AccessibleObject accessibleObject : accessibleObjectArray) {
            String string = ((Field)accessibleObject).getName();
            if (!this.accept((Field)accessibleObject)) continue;
            try {
                Object object = this.getValue((Field)accessibleObject);
                if (this.excludeNullValues && object == null) continue;
                this.append(string, object, !accessibleObject.isAnnotationPresent(ToStringSummary.class));
            } catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException(illegalAccessException);
            }
        }
    }

    public String[] getExcludeFieldNames() {
        return (String[])this.excludeFieldNames.clone();
    }

    public String[] getIncludeFieldNames() {
        return (String[])this.includeFieldNames.clone();
    }

    public Class<?> getUpToClass() {
        return this.upToClass;
    }

    protected Object getValue(Field field) {
        return field.get(this.getObject());
    }

    public boolean isAppendStatics() {
        return this.appendStatics;
    }

    public boolean isAppendTransients() {
        return this.appendTransients;
    }

    public boolean isExcludeNullValues() {
        return this.excludeNullValues;
    }

    public ReflectionToStringBuilder reflectionAppendArray(Object object) {
        this.getStyle().reflectionAppendArrayDetail(this.getStringBuffer(), null, object);
        return this;
    }

    public void setAppendStatics(boolean bl) {
        this.appendStatics = bl;
    }

    public void setAppendTransients(boolean bl) {
        this.appendTransients = bl;
    }

    public ReflectionToStringBuilder setExcludeFieldNames(String ... stringArray) {
        this.excludeFieldNames = stringArray == null ? null : ArraySorter.sort(ReflectionToStringBuilder.toNoNullStringArray(stringArray));
        return this;
    }

    public void setExcludeNullValues(boolean bl) {
        this.excludeNullValues = bl;
    }

    public ReflectionToStringBuilder setIncludeFieldNames(String ... stringArray) {
        this.includeFieldNames = stringArray == null ? null : ArraySorter.sort(ReflectionToStringBuilder.toNoNullStringArray(stringArray));
        return this;
    }

    public void setUpToClass(Class<?> clazz) {
        Object object;
        if (clazz != null && (object = this.getObject()) != null && !clazz.isInstance(object)) {
            throw new IllegalArgumentException("Specified class is not a superclass of the object");
        }
        this.upToClass = clazz;
    }

    @Override
    public String toString() {
        Class<?> clazz;
        if (this.getObject() == null) {
            return this.getStyle().getNullText();
        }
        this.validate();
        this.appendFieldsIn(clazz);
        for (clazz = this.getObject().getClass(); clazz.getSuperclass() != null && clazz != this.getUpToClass(); clazz = clazz.getSuperclass()) {
            this.appendFieldsIn(clazz);
        }
        return super.toString();
    }

    private void validate() {
        if (ArrayUtils.containsAny(this.excludeFieldNames, this.includeFieldNames)) {
            ToStringStyle.unregister(this.getObject());
            throw new IllegalStateException("includeFieldNames and excludeFieldNames must not intersect");
        }
    }
}

