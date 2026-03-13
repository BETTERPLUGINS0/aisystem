/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4dc;
import org.joml.Matrix4fc;
import org.joml.Matrix4x3dc;
import org.joml.Matrix4x3fc;
import org.joml.Vector4d;
import org.joml.Vector4dc;
import org.joml.Vector4fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000J\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\r\u0010\u0007\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\r\u0010\b\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u000b\u001a\u00020\f*\u00020\u00022\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u000b\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u000e\u001a\u00020\u000f*\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u000e\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\n\u0010\u0011\u001a\u00020\f*\u00020\u0012\u001a\u0012\u0010\u0011\u001a\u00020\f*\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014\u001a\u001a\u0010\u0011\u001a\u00020\f*\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\f\u001a\u0012\u0010\u0011\u001a\u00020\f*\u00020\u00122\u0006\u0010\u0003\u001a\u00020\f\u001a\n\u0010\u0011\u001a\u00020\f*\u00020\u0015\u001a\u0012\u0010\u0011\u001a\u00020\f*\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u0014\u001a\u001a\u0010\u0011\u001a\u00020\f*\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\f\u001a\u0012\u0010\u0011\u001a\u00020\f*\u00020\u00152\u0006\u0010\u0003\u001a\u00020\f\u001a\u0015\u0010\u0016\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0016\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0017H\u0086\u0002\u001a\u0015\u0010\u0018\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0018\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0017H\u0086\u0002\u001a\u0015\u0010\u0019\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0019\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0017H\u0086\u0002\u001a\u0015\u0010\u001a\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u001a\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0017H\u0086\u0002\u001a\u001a\u0010\u001b\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\f\u001a\u0012\u0010\u001b\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0003\u001a\u00020\f\u001a\u001a\u0010\u001b\u001a\u00020\u0015*\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\f\u001a\u0012\u0010\u001b\u001a\u00020\u0015*\u00020\u00152\u0006\u0010\u0003\u001a\u00020\f\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u001eH\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u001fH\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\u001d\u001a\u00020 H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\u001d\u001a\u00020!H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0017H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u001d\u001a\u00020\u001eH\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u001d\u001a\u00020\u001fH\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u001d\u001a\u00020 H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u001d\u001a\u00020!H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0017H\u0086\u0002\u001a\r\u0010#\u001a\u00020\f*\u00020\u0002H\u0086\u0002\u00a8\u0006$"}, d2={"angle", "", "Lorg/joml/Vector4dc;", "v", "angleCos", "component1", "component2", "component3", "component4", "distance", "distanceSquared", "div", "Lorg/joml/Vector4d;", "s", "divAssign", "", "dot", "getVector4d", "Ljava/nio/ByteBuffer;", "index", "", "Ljava/nio/DoubleBuffer;", "minus", "Lorg/joml/Vector4fc;", "minusAssign", "plus", "plusAssign", "putVector4d", "times", "m", "Lorg/joml/Matrix4dc;", "Lorg/joml/Matrix4fc;", "Lorg/joml/Matrix4x3dc;", "Lorg/joml/Matrix4x3fc;", "timesAssign", "unaryMinus", "joml"})
public final class Vector4dKt {
    public static final double component1(@NotNull Vector4dc $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.x();
    }

    public static final double component2(@NotNull Vector4dc $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.y();
    }

    public static final double component3(@NotNull Vector4dc $this$component3) {
        Intrinsics.checkNotNullParameter($this$component3, "<this>");
        return $this$component3.z();
    }

    public static final double component4(@NotNull Vector4dc $this$component4) {
        Intrinsics.checkNotNullParameter($this$component4, "<this>");
        return $this$component4.w();
    }

    @NotNull
    public static final Vector4d plus(@NotNull Vector4dc $this$plus, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$plus.add(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "add(v, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d plus(@NotNull Vector4dc $this$plus, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$plus.add(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "add(v, Vector4d())");
        return vector4d;
    }

    public static final void plusAssign(@NotNull Vector4d $this$plusAssign, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$plusAssign.add(v);
    }

    public static final void plusAssign(@NotNull Vector4d $this$plusAssign, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$plusAssign.add(v);
    }

    @NotNull
    public static final Vector4d minus(@NotNull Vector4dc $this$minus, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$minus.sub(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "sub(v, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d minus(@NotNull Vector4dc $this$minus, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$minus.sub(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "sub(v, Vector4d())");
        return vector4d;
    }

    public static final void minusAssign(@NotNull Vector4d $this$minusAssign, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$minusAssign.sub(v);
    }

    public static final void minusAssign(@NotNull Vector4d $this$minusAssign, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$minusAssign.sub(v);
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$times.mul(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(v, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$times.mul(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(v, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, double s) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Vector4d vector4d = $this$times.mul(s, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(s, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, @NotNull Matrix4dc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector4d vector4d = $this$times.mul(m, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(m, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, @NotNull Matrix4x3dc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector4d vector4d = $this$times.mul(m, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(m, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector4d vector4d = $this$times.mul(m, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(m, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d times(@NotNull Vector4dc $this$times, @NotNull Matrix4x3fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector4d vector4d = $this$times.mul(m, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "mul(m, Vector4d())");
        return vector4d;
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$timesAssign.mul(v);
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$timesAssign.mul(v);
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, double s) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        $this$timesAssign.mul(s);
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, @NotNull Matrix4dc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, @NotNull Matrix4x3dc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Vector4d $this$timesAssign, @NotNull Matrix4x3fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    @NotNull
    public static final Vector4d div(@NotNull Vector4dc $this$div, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$div.div(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "div(v, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4d div(@NotNull Vector4dc $this$div, double s) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Vector4d vector4d = $this$div.div(s, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "div(s, Vector4d())");
        return vector4d;
    }

    public static final void divAssign(@NotNull Vector4d $this$divAssign, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$divAssign.div(v);
    }

    public static final void divAssign(@NotNull Vector4d $this$divAssign, double s) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        $this$divAssign.div(s);
    }

    @NotNull
    public static final Vector4d unaryMinus(@NotNull Vector4dc $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        Vector4d vector4d = $this$unaryMinus.negate(new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "negate(Vector4d())");
        return vector4d;
    }

    public static final double dot(@NotNull Vector4dc $this$dot, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$dot, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$dot.dot(v);
    }

    public static final double distance(@NotNull Vector4dc $this$distance, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$distance, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$distance.distance(v);
    }

    public static final double distanceSquared(@NotNull Vector4dc $this$distanceSquared, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$distanceSquared, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$distanceSquared.distanceSquared(v);
    }

    public static final double angleCos(@NotNull Vector4dc $this$angleCos, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$angleCos, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$angleCos.angleCos(v);
    }

    public static final double angle(@NotNull Vector4dc $this$angle, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$angle, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$angle.angle(v);
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull ByteBuffer $this$getVector4d) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        return new Vector4d($this$getVector4d);
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull ByteBuffer $this$getVector4d, int index) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        return new Vector4d(index, $this$getVector4d);
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull ByteBuffer $this$getVector4d, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = v.set($this$getVector4d);
        Intrinsics.checkNotNullExpressionValue(vector4d, "v.set(this)");
        return vector4d;
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull ByteBuffer $this$getVector4d, int index, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = v.set(index, $this$getVector4d);
        Intrinsics.checkNotNullExpressionValue(vector4d, "v.set(index, this)");
        return vector4d;
    }

    @NotNull
    public static final ByteBuffer putVector4d(@NotNull ByteBuffer $this$putVector4d, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$putVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get($this$putVector4d);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(this)");
        return byteBuffer;
    }

    @NotNull
    public static final ByteBuffer putVector4d(@NotNull ByteBuffer $this$putVector4d, int index, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$putVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get(index, $this$putVector4d);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(index, this)");
        return byteBuffer;
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull DoubleBuffer $this$getVector4d) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        return new Vector4d($this$getVector4d);
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull DoubleBuffer $this$getVector4d, int index) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        return new Vector4d(index, $this$getVector4d);
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull DoubleBuffer $this$getVector4d, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = v.set($this$getVector4d);
        Intrinsics.checkNotNullExpressionValue(vector4d, "v.set(this)");
        return vector4d;
    }

    @NotNull
    public static final Vector4d getVector4d(@NotNull DoubleBuffer $this$getVector4d, int index, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$getVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = v.set(index, $this$getVector4d);
        Intrinsics.checkNotNullExpressionValue(vector4d, "v.set(index, this)");
        return vector4d;
    }

    @NotNull
    public static final DoubleBuffer putVector4d(@NotNull DoubleBuffer $this$putVector4d, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$putVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        DoubleBuffer doubleBuffer = v.get($this$putVector4d);
        Intrinsics.checkNotNullExpressionValue(doubleBuffer, "v.get(this)");
        return doubleBuffer;
    }

    @NotNull
    public static final DoubleBuffer putVector4d(@NotNull DoubleBuffer $this$putVector4d, int index, @NotNull Vector4d v) {
        Intrinsics.checkNotNullParameter($this$putVector4d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        DoubleBuffer doubleBuffer = v.get(index, $this$putVector4d);
        Intrinsics.checkNotNullExpressionValue(doubleBuffer, "v.get(index, this)");
        return doubleBuffer;
    }
}

