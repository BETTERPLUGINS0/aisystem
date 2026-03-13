/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3dc;
import org.joml.Matrix3fc;
import org.joml.Matrix3x2dc;
import org.joml.Matrix3x2fc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000J\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\r\u0010\u0007\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\f\u001a\u00020\t*\u00020\u00022\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\f\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\f\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\t2\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u0015\u0010\u0011\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\n\u0010\u0012\u001a\u00020\t*\u00020\u0013\u001a\u0012\u0010\u0012\u001a\u00020\t*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015\u001a\u001a\u0010\u0012\u001a\u00020\t*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0012\u001a\u00020\t*\u00020\u00132\u0006\u0010\u0003\u001a\u00020\t\u001a\n\u0010\u0012\u001a\u00020\t*\u00020\u0016\u001a\u0012\u0010\u0012\u001a\u00020\t*\u00020\u00162\u0006\u0010\u0014\u001a\u00020\u0015\u001a\u001a\u0010\u0012\u001a\u00020\t*\u00020\u00162\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0012\u001a\u00020\t*\u00020\u00162\u0006\u0010\u0003\u001a\u00020\t\u001a\u0015\u0010\u0017\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u0015\u0010\u0018\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0018\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u0015\u0010\u0019\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0019\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u0015\u0010\u001a\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u001a\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u001a\u0010\u001b\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u001b\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\u0003\u001a\u00020\t\u001a\u001a\u0010\u001b\u001a\u00020\u0016*\u00020\u00162\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u001b\u001a\u00020\u0016*\u00020\u00162\u0006\u0010\u0003\u001a\u00020\t\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u001eH\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u001fH\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\u001d\u001a\u00020 H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\u001d\u001a\u00020!H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\r\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001eH\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001fH\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u001d\u001a\u00020 H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u001d\u001a\u00020!H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\"\u001a\u00020\u0010*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u000eH\u0086\u0002\u001a\r\u0010#\u001a\u00020\t*\u00020\u0002H\u0086\u0002\u00a8\u0006$"}, d2={"angle", "", "Lorg/joml/Vector3dc;", "v", "angleCos", "component1", "component2", "component3", "cross", "Lorg/joml/Vector3d;", "distance", "distanceSquared", "div", "s", "Lorg/joml/Vector3fc;", "divAssign", "", "dot", "getVector3d", "Ljava/nio/ByteBuffer;", "index", "", "Ljava/nio/DoubleBuffer;", "minus", "minusAssign", "plus", "plusAssign", "putVector3d", "times", "m", "Lorg/joml/Matrix3dc;", "Lorg/joml/Matrix3fc;", "Lorg/joml/Matrix3x2dc;", "Lorg/joml/Matrix3x2fc;", "timesAssign", "unaryMinus", "joml"})
public final class Vector3dKt {
    public static final double component1(@NotNull Vector3dc $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.x();
    }

    public static final double component2(@NotNull Vector3dc $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.y();
    }

    public static final double component3(@NotNull Vector3dc $this$component3) {
        Intrinsics.checkNotNullParameter($this$component3, "<this>");
        return $this$component3.z();
    }

    @NotNull
    public static final Vector3d plus(@NotNull Vector3dc $this$plus, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$plus.add(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "add(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d plus(@NotNull Vector3dc $this$plus, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$plus.add(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "add(v, Vector3d())");
        return vector3d;
    }

    public static final void plusAssign(@NotNull Vector3d $this$plusAssign, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$plusAssign.add(v);
    }

    public static final void plusAssign(@NotNull Vector3d $this$plusAssign, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$plusAssign.add(v);
    }

    @NotNull
    public static final Vector3d minus(@NotNull Vector3dc $this$minus, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$minus.sub(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "sub(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d minus(@NotNull Vector3dc $this$minus, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$minus.sub(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "sub(v, Vector3d())");
        return vector3d;
    }

    public static final void minusAssign(@NotNull Vector3d $this$minusAssign, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$minusAssign.sub(v);
    }

    public static final void minusAssign(@NotNull Vector3d $this$minusAssign, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$minusAssign.sub(v);
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$times.mul(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$times.mul(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, double s) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Vector3d vector3d = $this$times.mul(s, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(s, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector3d vector3d = $this$times.mul(m, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(m, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, @NotNull Matrix3x2dc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector3d vector3d = $this$times.mul(m, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(m, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, @NotNull Matrix3fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector3d vector3d = $this$times.mul(m, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(m, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Vector3dc $this$times, @NotNull Matrix3x2fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector3d vector3d = $this$times.mul(m, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "mul(m, Vector3d())");
        return vector3d;
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$timesAssign.mul(v);
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$timesAssign.mul(v);
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, double s) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        $this$timesAssign.mul(s);
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, @NotNull Matrix3x2dc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, @NotNull Matrix3fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Vector3d $this$timesAssign, @NotNull Matrix3x2fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    @NotNull
    public static final Vector3d div(@NotNull Vector3dc $this$div, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$div.div(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "div(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d div(@NotNull Vector3dc $this$div, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$div.div(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "div(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3d div(@NotNull Vector3dc $this$div, double s) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Vector3d vector3d = $this$div.div(s, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "div(s, Vector3d())");
        return vector3d;
    }

    public static final void divAssign(@NotNull Vector3d $this$divAssign, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$divAssign.div(v);
    }

    public static final void divAssign(@NotNull Vector3d $this$divAssign, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$divAssign.div(v);
    }

    public static final void divAssign(@NotNull Vector3d $this$divAssign, double s) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        $this$divAssign.div(s);
    }

    @NotNull
    public static final Vector3d unaryMinus(@NotNull Vector3dc $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        Vector3d vector3d = $this$unaryMinus.negate(new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "negate(Vector3d())");
        return vector3d;
    }

    public static final double dot(@NotNull Vector3dc $this$dot, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$dot, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$dot.dot(v);
    }

    @NotNull
    public static final Vector3d cross(@NotNull Vector3dc $this$cross, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$cross, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$cross.cross(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "cross(v, Vector3d())");
        return vector3d;
    }

    public static final double distance(@NotNull Vector3dc $this$distance, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$distance, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$distance.distance(v);
    }

    public static final double distanceSquared(@NotNull Vector3dc $this$distanceSquared, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$distanceSquared, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$distanceSquared.distanceSquared(v);
    }

    public static final double angleCos(@NotNull Vector3dc $this$angleCos, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$angleCos, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$angleCos.angleCos(v);
    }

    public static final double angle(@NotNull Vector3dc $this$angle, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$angle, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$angle.angle(v);
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull ByteBuffer $this$getVector3d) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        return new Vector3d($this$getVector3d);
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull ByteBuffer $this$getVector3d, int index) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        return new Vector3d(index, $this$getVector3d);
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull ByteBuffer $this$getVector3d, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = v.set($this$getVector3d);
        Intrinsics.checkNotNullExpressionValue(vector3d, "v.set(this)");
        return vector3d;
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull ByteBuffer $this$getVector3d, int index, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = v.set(index, $this$getVector3d);
        Intrinsics.checkNotNullExpressionValue(vector3d, "v.set(index, this)");
        return vector3d;
    }

    @NotNull
    public static final ByteBuffer putVector3d(@NotNull ByteBuffer $this$putVector3d, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$putVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get($this$putVector3d);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(this)");
        return byteBuffer;
    }

    @NotNull
    public static final ByteBuffer putVector3d(@NotNull ByteBuffer $this$putVector3d, int index, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$putVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get(index, $this$putVector3d);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(index, this)");
        return byteBuffer;
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull DoubleBuffer $this$getVector3d) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        return new Vector3d($this$getVector3d);
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull DoubleBuffer $this$getVector3d, int index) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        return new Vector3d(index, $this$getVector3d);
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull DoubleBuffer $this$getVector3d, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = v.set($this$getVector3d);
        Intrinsics.checkNotNullExpressionValue(vector3d, "v.set(this)");
        return vector3d;
    }

    @NotNull
    public static final Vector3d getVector3d(@NotNull DoubleBuffer $this$getVector3d, int index, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$getVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = v.set(index, $this$getVector3d);
        Intrinsics.checkNotNullExpressionValue(vector3d, "v.set(index, this)");
        return vector3d;
    }

    @NotNull
    public static final DoubleBuffer putVector3d(@NotNull DoubleBuffer $this$putVector3d, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$putVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        DoubleBuffer doubleBuffer = v.get($this$putVector3d);
        Intrinsics.checkNotNullExpressionValue(doubleBuffer, "v.get(this)");
        return doubleBuffer;
    }

    @NotNull
    public static final DoubleBuffer putVector3d(@NotNull DoubleBuffer $this$putVector3d, int index, @NotNull Vector3d v) {
        Intrinsics.checkNotNullParameter($this$putVector3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        DoubleBuffer doubleBuffer = v.get(index, $this$putVector3d);
        Intrinsics.checkNotNullExpressionValue(doubleBuffer, "v.get(index, this)");
        return doubleBuffer;
    }
}

