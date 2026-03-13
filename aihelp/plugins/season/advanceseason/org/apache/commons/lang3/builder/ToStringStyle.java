/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.builder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToStringStyle
implements Serializable {
    private static final long serialVersionUID = -2587890625525655916L;
    public static final ToStringStyle DEFAULT_STYLE = new DefaultToStringStyle();
    public static final ToStringStyle MULTI_LINE_STYLE = new MultiLineToStringStyle();
    public static final ToStringStyle NO_FIELD_NAMES_STYLE = new NoFieldNameToStringStyle();
    public static final ToStringStyle SHORT_PREFIX_STYLE = new ShortPrefixToStringStyle();
    public static final ToStringStyle SIMPLE_STYLE = new SimpleToStringStyle();
    public static final ToStringStyle NO_CLASS_NAME_STYLE = new NoClassNameToStringStyle();
    public static final ToStringStyle JSON_STYLE = new JsonToStringStyle();
    private static final ThreadLocal<WeakHashMap<Object, Object>> REGISTRY = ThreadLocal.withInitial(WeakHashMap::new);
    private boolean useFieldNames = true;
    private boolean useClassName = true;
    private boolean useShortClassName;
    private boolean useIdentityHashCode = true;
    private String contentStart = "[";
    private String contentEnd = "]";
    private String fieldNameValueSeparator = "=";
    private boolean fieldSeparatorAtStart;
    private boolean fieldSeparatorAtEnd;
    private String fieldSeparator = ",";
    private String arrayStart = "{";
    private String arraySeparator = ",";
    private boolean arrayContentDetail = true;
    private String arrayEnd = "}";
    private boolean defaultFullDetail = true;
    private String nullText = "<null>";
    private String sizeStartText = "<size=";
    private String sizeEndText = ">";
    private String summaryObjectStartText = "<";
    private String summaryObjectEndText = ">";

    public static Map<Object, Object> getRegistry() {
        return REGISTRY.get();
    }

    static boolean isRegistered(Object object) {
        return ToStringStyle.getRegistry().containsKey(object);
    }

    static void register(Object object) {
        if (object != null) {
            ToStringStyle.getRegistry().put(object, null);
        }
    }

    static void unregister(Object object) {
        if (object != null) {
            Map<Object, Object> map = ToStringStyle.getRegistry();
            map.remove(object);
            if (map.isEmpty()) {
                REGISTRY.remove();
            }
        }
    }

    protected ToStringStyle() {
    }

    public void append(StringBuffer stringBuffer, String string, boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, bl);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, boolean[] blArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (blArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, blArray);
        } else {
            this.appendSummary(stringBuffer, string, blArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, byte by) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, by);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, byte[] byArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (byArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, byArray);
        } else {
            this.appendSummary(stringBuffer, string, byArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, char c2) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, c2);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, char[] cArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (cArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, cArray);
        } else {
            this.appendSummary(stringBuffer, string, cArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, double d) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, d);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, double[] dArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (dArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, dArray);
        } else {
            this.appendSummary(stringBuffer, string, dArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, float f) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, f);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, float[] fArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (fArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, fArray);
        } else {
            this.appendSummary(stringBuffer, string, fArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, int n) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, n);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, int[] nArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (nArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, nArray);
        } else {
            this.appendSummary(stringBuffer, string, nArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, long l) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, l);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, long[] lArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (lArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, lArray);
        } else {
            this.appendSummary(stringBuffer, string, lArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, Object object, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (object == null) {
            this.appendNullText(stringBuffer, string);
        } else {
            this.appendInternal(stringBuffer, string, object, this.isFullDetail(bl));
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, Object[] objectArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (objectArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, objectArray);
        } else {
            this.appendSummary(stringBuffer, string, objectArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, short s) {
        this.appendFieldStart(stringBuffer, string);
        this.appendDetail(stringBuffer, string, s);
        this.appendFieldEnd(stringBuffer, string);
    }

    public void append(StringBuffer stringBuffer, String string, short[] sArray, Boolean bl) {
        this.appendFieldStart(stringBuffer, string);
        if (sArray == null) {
            this.appendNullText(stringBuffer, string);
        } else if (this.isFullDetail(bl)) {
            this.appendDetail(stringBuffer, string, sArray);
        } else {
            this.appendSummary(stringBuffer, string, sArray);
        }
        this.appendFieldEnd(stringBuffer, string);
    }

    protected void appendClassName(StringBuffer stringBuffer, Object object) {
        if (this.useClassName && object != null) {
            ToStringStyle.register(object);
            if (this.useShortClassName) {
                stringBuffer.append(this.getShortClassName(object.getClass()));
            } else {
                stringBuffer.append(object.getClass().getName());
            }
        }
    }

    protected void appendContentEnd(StringBuffer stringBuffer) {
        stringBuffer.append(this.contentEnd);
    }

    protected void appendContentStart(StringBuffer stringBuffer) {
        stringBuffer.append(this.contentStart);
    }

    protected void appendCyclicObject(StringBuffer stringBuffer, String string, Object object) {
        ObjectUtils.identityToString(stringBuffer, object);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, boolean bl) {
        stringBuffer.append(bl);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, boolean[] blArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < blArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, blArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, byte by) {
        stringBuffer.append(by);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, byte[] byArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < byArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, byArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, char c2) {
        stringBuffer.append(c2);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, char[] cArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < cArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, cArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, Collection<?> collection) {
        stringBuffer.append(collection);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, double d) {
        stringBuffer.append(d);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, double[] dArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < dArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, dArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, float f) {
        stringBuffer.append(f);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, float[] fArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < fArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, fArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, int n) {
        stringBuffer.append(n);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, int n, Object object) {
        if (n > 0) {
            stringBuffer.append(this.arraySeparator);
        }
        if (object == null) {
            this.appendNullText(stringBuffer, string);
        } else {
            this.appendInternal(stringBuffer, string, object, this.arrayContentDetail);
        }
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, int[] nArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < nArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, nArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, long l) {
        stringBuffer.append(l);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, long[] lArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < lArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, lArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, Map<?, ?> map) {
        stringBuffer.append(map);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, Object object) {
        stringBuffer.append(object);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, Object[] objectArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < objectArray.length; ++i) {
            this.appendDetail(stringBuffer, string, i, objectArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, short s) {
        stringBuffer.append(s);
    }

    protected void appendDetail(StringBuffer stringBuffer, String string, short[] sArray) {
        stringBuffer.append(this.arrayStart);
        for (int i = 0; i < sArray.length; ++i) {
            if (i > 0) {
                stringBuffer.append(this.arraySeparator);
            }
            this.appendDetail(stringBuffer, string, sArray[i]);
        }
        stringBuffer.append(this.arrayEnd);
    }

    public void appendEnd(StringBuffer stringBuffer, Object object) {
        if (!this.fieldSeparatorAtEnd) {
            this.removeLastFieldSeparator(stringBuffer);
        }
        this.appendContentEnd(stringBuffer);
        ToStringStyle.unregister(object);
    }

    protected void appendFieldEnd(StringBuffer stringBuffer, String string) {
        this.appendFieldSeparator(stringBuffer);
    }

    protected void appendFieldSeparator(StringBuffer stringBuffer) {
        stringBuffer.append(this.fieldSeparator);
    }

    protected void appendFieldStart(StringBuffer stringBuffer, String string) {
        if (this.useFieldNames && string != null) {
            stringBuffer.append(string);
            stringBuffer.append(this.fieldNameValueSeparator);
        }
    }

    protected void appendIdentityHashCode(StringBuffer stringBuffer, Object object) {
        if (this.isUseIdentityHashCode() && object != null) {
            ToStringStyle.register(object);
            stringBuffer.append('@');
            stringBuffer.append(ObjectUtils.identityHashCodeHex(object));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void appendInternal(StringBuffer stringBuffer, String string, Object object, boolean bl) {
        if (ToStringStyle.isRegistered(object) && !(object instanceof Number) && !(object instanceof Boolean) && !(object instanceof Character)) {
            this.appendCyclicObject(stringBuffer, string, object);
            return;
        }
        ToStringStyle.register(object);
        try {
            if (object instanceof Collection) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (Collection)object);
                } else {
                    this.appendSummarySize(stringBuffer, string, ((Collection)object).size());
                }
            } else if (object instanceof Map) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (Map)object);
                } else {
                    this.appendSummarySize(stringBuffer, string, ((Map)object).size());
                }
            } else if (object instanceof long[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (long[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (long[])object);
                }
            } else if (object instanceof int[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (int[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (int[])object);
                }
            } else if (object instanceof short[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (short[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (short[])object);
                }
            } else if (object instanceof byte[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (byte[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (byte[])object);
                }
            } else if (object instanceof char[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (char[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (char[])object);
                }
            } else if (object instanceof double[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (double[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (double[])object);
                }
            } else if (object instanceof float[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (float[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (float[])object);
                }
            } else if (object instanceof boolean[]) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (boolean[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (boolean[])object);
                }
            } else if (ObjectUtils.isArray(object)) {
                if (bl) {
                    this.appendDetail(stringBuffer, string, (Object[])object);
                } else {
                    this.appendSummary(stringBuffer, string, (Object[])object);
                }
            } else if (bl) {
                this.appendDetail(stringBuffer, string, object);
            } else {
                this.appendSummary(stringBuffer, string, object);
            }
        } finally {
            ToStringStyle.unregister(object);
        }
    }

    protected void appendNullText(StringBuffer stringBuffer, String string) {
        stringBuffer.append(this.nullText);
    }

    public void appendStart(StringBuffer stringBuffer, Object object) {
        if (object != null) {
            this.appendClassName(stringBuffer, object);
            this.appendIdentityHashCode(stringBuffer, object);
            this.appendContentStart(stringBuffer);
            if (this.fieldSeparatorAtStart) {
                this.appendFieldSeparator(stringBuffer);
            }
        }
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, boolean[] blArray) {
        this.appendSummarySize(stringBuffer, string, blArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, byte[] byArray) {
        this.appendSummarySize(stringBuffer, string, byArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, char[] cArray) {
        this.appendSummarySize(stringBuffer, string, cArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, double[] dArray) {
        this.appendSummarySize(stringBuffer, string, dArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, float[] fArray) {
        this.appendSummarySize(stringBuffer, string, fArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, int[] nArray) {
        this.appendSummarySize(stringBuffer, string, nArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, long[] lArray) {
        this.appendSummarySize(stringBuffer, string, lArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, Object object) {
        stringBuffer.append(this.summaryObjectStartText);
        stringBuffer.append(this.getShortClassName(object.getClass()));
        stringBuffer.append(this.summaryObjectEndText);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, Object[] objectArray) {
        this.appendSummarySize(stringBuffer, string, objectArray.length);
    }

    protected void appendSummary(StringBuffer stringBuffer, String string, short[] sArray) {
        this.appendSummarySize(stringBuffer, string, sArray.length);
    }

    protected void appendSummarySize(StringBuffer stringBuffer, String string, int n) {
        stringBuffer.append(this.sizeStartText);
        stringBuffer.append(n);
        stringBuffer.append(this.sizeEndText);
    }

    public void appendSuper(StringBuffer stringBuffer, String string) {
        this.appendToString(stringBuffer, string);
    }

    public void appendToString(StringBuffer stringBuffer, String string) {
        int n;
        int n2;
        if (string != null && (n2 = string.indexOf(this.contentStart) + this.contentStart.length()) != (n = string.lastIndexOf(this.contentEnd)) && n2 >= 0 && n >= 0) {
            if (this.fieldSeparatorAtStart) {
                this.removeLastFieldSeparator(stringBuffer);
            }
            stringBuffer.append(string, n2, n);
            this.appendFieldSeparator(stringBuffer);
        }
    }

    protected String getArrayEnd() {
        return this.arrayEnd;
    }

    protected String getArraySeparator() {
        return this.arraySeparator;
    }

    protected String getArrayStart() {
        return this.arrayStart;
    }

    protected String getContentEnd() {
        return this.contentEnd;
    }

    protected String getContentStart() {
        return this.contentStart;
    }

    protected String getFieldNameValueSeparator() {
        return this.fieldNameValueSeparator;
    }

    protected String getFieldSeparator() {
        return this.fieldSeparator;
    }

    protected String getNullText() {
        return this.nullText;
    }

    protected String getShortClassName(Class<?> clazz) {
        return ClassUtils.getShortClassName(clazz);
    }

    protected String getSizeEndText() {
        return this.sizeEndText;
    }

    protected String getSizeStartText() {
        return this.sizeStartText;
    }

    protected String getSummaryObjectEndText() {
        return this.summaryObjectEndText;
    }

    protected String getSummaryObjectStartText() {
        return this.summaryObjectStartText;
    }

    protected boolean isArrayContentDetail() {
        return this.arrayContentDetail;
    }

    protected boolean isDefaultFullDetail() {
        return this.defaultFullDetail;
    }

    protected boolean isFieldSeparatorAtEnd() {
        return this.fieldSeparatorAtEnd;
    }

    protected boolean isFieldSeparatorAtStart() {
        return this.fieldSeparatorAtStart;
    }

    protected boolean isFullDetail(Boolean bl) {
        if (bl == null) {
            return this.defaultFullDetail;
        }
        return bl;
    }

    protected boolean isUseClassName() {
        return this.useClassName;
    }

    protected boolean isUseFieldNames() {
        return this.useFieldNames;
    }

    protected boolean isUseIdentityHashCode() {
        return this.useIdentityHashCode;
    }

    protected boolean isUseShortClassName() {
        return this.useShortClassName;
    }

    protected void reflectionAppendArrayDetail(StringBuffer stringBuffer, String string, Object object) {
        stringBuffer.append(this.arrayStart);
        int n = Array.getLength(object);
        for (int i = 0; i < n; ++i) {
            this.appendDetail(stringBuffer, string, i, Array.get(object, i));
        }
        stringBuffer.append(this.arrayEnd);
    }

    protected void removeLastFieldSeparator(StringBuffer stringBuffer) {
        if (StringUtils.endsWith(stringBuffer, this.fieldSeparator)) {
            stringBuffer.setLength(stringBuffer.length() - this.fieldSeparator.length());
        }
    }

    protected void setArrayContentDetail(boolean bl) {
        this.arrayContentDetail = bl;
    }

    protected void setArrayEnd(String string) {
        if (string == null) {
            string = "";
        }
        this.arrayEnd = string;
    }

    protected void setArraySeparator(String string) {
        if (string == null) {
            string = "";
        }
        this.arraySeparator = string;
    }

    protected void setArrayStart(String string) {
        if (string == null) {
            string = "";
        }
        this.arrayStart = string;
    }

    protected void setContentEnd(String string) {
        if (string == null) {
            string = "";
        }
        this.contentEnd = string;
    }

    protected void setContentStart(String string) {
        if (string == null) {
            string = "";
        }
        this.contentStart = string;
    }

    protected void setDefaultFullDetail(boolean bl) {
        this.defaultFullDetail = bl;
    }

    protected void setFieldNameValueSeparator(String string) {
        if (string == null) {
            string = "";
        }
        this.fieldNameValueSeparator = string;
    }

    protected void setFieldSeparator(String string) {
        if (string == null) {
            string = "";
        }
        this.fieldSeparator = string;
    }

    protected void setFieldSeparatorAtEnd(boolean bl) {
        this.fieldSeparatorAtEnd = bl;
    }

    protected void setFieldSeparatorAtStart(boolean bl) {
        this.fieldSeparatorAtStart = bl;
    }

    protected void setNullText(String string) {
        if (string == null) {
            string = "";
        }
        this.nullText = string;
    }

    protected void setSizeEndText(String string) {
        if (string == null) {
            string = "";
        }
        this.sizeEndText = string;
    }

    protected void setSizeStartText(String string) {
        if (string == null) {
            string = "";
        }
        this.sizeStartText = string;
    }

    protected void setSummaryObjectEndText(String string) {
        if (string == null) {
            string = "";
        }
        this.summaryObjectEndText = string;
    }

    protected void setSummaryObjectStartText(String string) {
        if (string == null) {
            string = "";
        }
        this.summaryObjectStartText = string;
    }

    protected void setUseClassName(boolean bl) {
        this.useClassName = bl;
    }

    protected void setUseFieldNames(boolean bl) {
        this.useFieldNames = bl;
    }

    protected void setUseIdentityHashCode(boolean bl) {
        this.useIdentityHashCode = bl;
    }

    protected void setUseShortClassName(boolean bl) {
        this.useShortClassName = bl;
    }

    private static final class DefaultToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        DefaultToStringStyle() {
        }

        private Object readResolve() {
            return DEFAULT_STYLE;
        }
    }

    private static final class MultiLineToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        MultiLineToStringStyle() {
            this.setContentStart("[");
            this.setFieldSeparator(System.lineSeparator() + "  ");
            this.setFieldSeparatorAtStart(true);
            this.setContentEnd(System.lineSeparator() + "]");
        }

        private Object readResolve() {
            return MULTI_LINE_STYLE;
        }
    }

    private static final class NoFieldNameToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        NoFieldNameToStringStyle() {
            this.setUseFieldNames(false);
        }

        private Object readResolve() {
            return NO_FIELD_NAMES_STYLE;
        }
    }

    private static final class ShortPrefixToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        ShortPrefixToStringStyle() {
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
        }

        private Object readResolve() {
            return SHORT_PREFIX_STYLE;
        }
    }

    private static final class SimpleToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        SimpleToStringStyle() {
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
            this.setUseFieldNames(false);
            this.setContentStart("");
            this.setContentEnd("");
        }

        private Object readResolve() {
            return SIMPLE_STYLE;
        }
    }

    private static final class NoClassNameToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        NoClassNameToStringStyle() {
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
        }

        private Object readResolve() {
            return NO_CLASS_NAME_STYLE;
        }
    }

    private static final class JsonToStringStyle
    extends ToStringStyle {
        private static final long serialVersionUID = 1L;
        private static final String FIELD_NAME_QUOTE = "\"";

        JsonToStringStyle() {
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
            this.setContentStart("{");
            this.setContentEnd("}");
            this.setArrayStart("[");
            this.setArrayEnd("]");
            this.setFieldSeparator(",");
            this.setFieldNameValueSeparator(":");
            this.setNullText("null");
            this.setSummaryObjectStartText("\"<");
            this.setSummaryObjectEndText(">\"");
            this.setSizeStartText("\"<size=");
            this.setSizeEndText(">\"");
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, boolean[] blArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, blArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, byte[] byArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, byArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, char[] cArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, cArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, double[] dArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, dArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, float[] fArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, fArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, int[] nArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, nArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, long[] lArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, lArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, Object object, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, object, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, Object[] objectArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, objectArray, bl);
        }

        @Override
        public void append(StringBuffer stringBuffer, String string, short[] sArray, Boolean bl) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            if (!this.isFullDetail(bl)) {
                throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
            super.append(stringBuffer, string, sArray, bl);
        }

        @Override
        protected void appendDetail(StringBuffer stringBuffer, String string, char c2) {
            this.appendValueAsString(stringBuffer, String.valueOf(c2));
        }

        @Override
        protected void appendDetail(StringBuffer stringBuffer, String string, Collection<?> collection) {
            if (collection != null && !collection.isEmpty()) {
                stringBuffer.append(this.getArrayStart());
                int n = 0;
                for (Object obj : collection) {
                    this.appendDetail(stringBuffer, string, n++, obj);
                }
                stringBuffer.append(this.getArrayEnd());
                return;
            }
            stringBuffer.append(collection);
        }

        @Override
        protected void appendDetail(StringBuffer stringBuffer, String string, Map<?, ?> map) {
            if (map != null && !map.isEmpty()) {
                stringBuffer.append(this.getContentStart());
                boolean bl = true;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String string2 = Objects.toString(entry.getKey(), null);
                    if (string2 == null) continue;
                    if (bl) {
                        bl = false;
                    } else {
                        this.appendFieldEnd(stringBuffer, string2);
                    }
                    this.appendFieldStart(stringBuffer, string2);
                    Object obj = entry.getValue();
                    if (obj == null) {
                        this.appendNullText(stringBuffer, string2);
                        continue;
                    }
                    this.appendInternal(stringBuffer, string2, obj, true);
                }
                stringBuffer.append(this.getContentEnd());
                return;
            }
            stringBuffer.append(map);
        }

        @Override
        protected void appendDetail(StringBuffer stringBuffer, String string, Object object) {
            if (object == null) {
                this.appendNullText(stringBuffer, string);
                return;
            }
            if (object instanceof String || object instanceof Character) {
                this.appendValueAsString(stringBuffer, object.toString());
                return;
            }
            if (object instanceof Number || object instanceof Boolean) {
                stringBuffer.append(object);
                return;
            }
            String string2 = object.toString();
            if (this.isJsonObject(string2) || this.isJsonArray(string2)) {
                stringBuffer.append(object);
                return;
            }
            this.appendDetail(stringBuffer, string, string2);
        }

        @Override
        protected void appendFieldStart(StringBuffer stringBuffer, String string) {
            if (string == null) {
                throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
            }
            super.appendFieldStart(stringBuffer, FIELD_NAME_QUOTE + StringEscapeUtils.escapeJson(string) + FIELD_NAME_QUOTE);
        }

        private void appendValueAsString(StringBuffer stringBuffer, String string) {
            stringBuffer.append('\"').append(StringEscapeUtils.escapeJson(string)).append('\"');
        }

        private boolean isJsonArray(String string) {
            return string.startsWith(this.getArrayStart()) && string.endsWith(this.getArrayEnd());
        }

        private boolean isJsonObject(String string) {
            return string.startsWith(this.getContentStart()) && string.endsWith(this.getContentEnd());
        }

        private Object readResolve() {
            return JSON_STYLE;
        }
    }
}

