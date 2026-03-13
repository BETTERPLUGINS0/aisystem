/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix2fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00008\n\u0000\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00022\u0006\u0010\n\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u000b\u001a\u00020\f*\u00020\t2\u0006\u0010\n\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u000b\u001a\u00020\f*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\n\u0010\u000e\u001a\u00020\t*\u00020\u000f\u001a\u0012\u0010\u000e\u001a\u00020\t*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u001a\u0010\u000e\u001a\u00020\t*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u000e\u001a\u00020\t*\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\t\u001a\n\u0010\u000e\u001a\u00020\t*\u00020\u0012\u001a\u0012\u0010\u000e\u001a\u00020\t*\u00020\u00122\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u001a\u0010\u000e\u001a\u00020\t*\u00020\u00122\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u000e\u001a\u00020\t*\u00020\u00122\u0006\u0010\u0003\u001a\u00020\t\u001a\u0015\u0010\u0013\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0014\u001a\u00020\f*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0015\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0016\u001a\u00020\f*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u001a\u0010\u0017\u001a\u00020\u000f*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0017\u001a\u00020\u000f*\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\t\u001a\u001a\u0010\u0017\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0017\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0003\u001a\u00020\t\u001a\u0015\u0010\u0018\u001a\u00020\t*\u00020\u00022\u0006\u0010\n\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u0018\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u001aH\u0086\u0002\u001a\u0015\u0010\u0018\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u001b\u001a\u00020\f*\u00020\t2\u0006\u0010\n\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u001b\u001a\u00020\f*\u00020\t2\u0006\u0010\u0019\u001a\u00020\u001aH\u0086\u0002\u001a\u0015\u0010\u001b\u001a\u00020\f*\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\r\u0010\u001c\u001a\u00020\t*\u00020\u0002H\u0086\u0002\u00a8\u0006\u001d"}, d2={"angle", "", "Lorg/joml/Vector2fc;", "v", "component1", "component2", "distance", "distanceSquared", "div", "Lorg/joml/Vector2f;", "s", "divAssign", "", "dot", "getVector2f", "Ljava/nio/ByteBuffer;", "index", "", "Ljava/nio/FloatBuffer;", "minus", "minusAssign", "plus", "plusAssign", "putVector2f", "times", "m", "Lorg/joml/Matrix2fc;", "timesAssign", "unaryMinus", "joml"})
public final class Vector2fKt {
    public static final float component1(@NotNull Vector2fc $this$component1) {
        Intrinsics.checkNotNullParameter($this$component1, "<this>");
        return $this$component1.x();
    }

    public static final float component2(@NotNull Vector2fc $this$component2) {
        Intrinsics.checkNotNullParameter($this$component2, "<this>");
        return $this$component2.y();
    }

    @NotNull
    public static final Vector2f plus(@NotNull Vector2fc $this$plus, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = $this$plus.add(v, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "add(v, Vector2f())");
        return vector2f;
    }

    public static final void plusAssign(@NotNull Vector2f $this$plusAssign, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$plusAssign.add(v);
    }

    @NotNull
    public static final Vector2f minus(@NotNull Vector2fc $this$minus, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = $this$minus.sub(v, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "sub(v, Vector2f())");
        return vector2f;
    }

    public static final void minusAssign(@NotNull Vector2f $this$minusAssign, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$minusAssign.sub(v);
    }

    @NotNull
    public static final Vector2f times(@NotNull Vector2fc $this$times, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = $this$times.mul(v, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "mul(v, Vector2f())");
        return vector2f;
    }

    @NotNull
    public static final Vector2f times(@NotNull Vector2fc $this$times, float s) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Vector2f vector2f = $this$times.mul(s, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "mul(s, Vector2f())");
        return vector2f;
    }

    @NotNull
    public static final Vector2f times(@NotNull Vector2fc $this$times, @NotNull Matrix2fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Vector2f vector2f = $this$times.mul(m, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "mul(m, Vector2f())");
        return vector2f;
    }

    public static final void timesAssign(@NotNull Vector2f $this$timesAssign, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$timesAssign.mul(v);
    }

    public static final void timesAssign(@NotNull Vector2f $this$timesAssign, float s) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        $this$timesAssign.mul(s);
    }

    public static final void timesAssign(@NotNull Vector2f $this$timesAssign, @NotNull Matrix2fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    @NotNull
    public static final Vector2f div(@NotNull Vector2fc $this$div, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = $this$div.div(v, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "div(v, Vector2f())");
        return vector2f;
    }

    @NotNull
    public static final Vector2f div(@NotNull Vector2fc $this$div, float s) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Vector2f vector2f = $this$div.div(s, new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "div(s, Vector2f())");
        return vector2f;
    }

    public static final void divAssign(@NotNull Vector2f $this$divAssign, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        $this$divAssign.div(v);
    }

    public static final void divAssign(@NotNull Vector2f $this$divAssign, float s) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        $this$divAssign.div(s);
    }

    @NotNull
    public static final Vector2f unaryMinus(@NotNull Vector2fc $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        Vector2f vector2f = $this$unaryMinus.negate(new Vector2f());
        Intrinsics.checkNotNullExpressionValue(vector2f, "negate(Vector2f())");
        return vector2f;
    }

    public static final float dot(@NotNull Vector2fc $this$dot, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$dot, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$dot.dot(v);
    }

    public static final float angle(@NotNull Vector2fc $this$angle, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$angle, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$angle.angle(v);
    }

    public static final float distance(@NotNull Vector2fc $this$distance, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$distance, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$distance.distance(v);
    }

    public static final float distanceSquared(@NotNull Vector2fc $this$distanceSquared, @NotNull Vector2fc v) {
        Intrinsics.checkNotNullParameter($this$distanceSquared, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        return $this$distanceSquared.distanceSquared(v);
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull ByteBuffer $this$getVector2f) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        return new Vector2f($this$getVector2f);
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull ByteBuffer $this$getVector2f, int index) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        return new Vector2f(index, $this$getVector2f);
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull ByteBuffer $this$getVector2f, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = v.set($this$getVector2f);
        Intrinsics.checkNotNullExpressionValue(vector2f, "v.set(this)");
        return vector2f;
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull ByteBuffer $this$getVector2f, int index, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = v.set(index, $this$getVector2f);
        Intrinsics.checkNotNullExpressionValue(vector2f, "v.set(index, this)");
        return vector2f;
    }

    @NotNull
    public static final ByteBuffer putVector2f(@NotNull ByteBuffer $this$putVector2f, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$putVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get($this$putVector2f);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(this)");
        return byteBuffer;
    }

    @NotNull
    public static final ByteBuffer putVector2f(@NotNull ByteBuffer $this$putVector2f, int index, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$putVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get(index, $this$putVector2f);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(index, this)");
        return byteBuffer;
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull FloatBuffer $this$getVector2f) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        return new Vector2f($this$getVector2f);
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull FloatBuffer $this$getVector2f, int index) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        return new Vector2f(index, $this$getVector2f);
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull FloatBuffer $this$getVector2f, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = v.set($this$getVector2f);
        Intrinsics.checkNotNullExpressionValue(vector2f, "v.set(this)");
        return vector2f;
    }

    @NotNull
    public static final Vector2f getVector2f(@NotNull FloatBuffer $this$getVector2f, int index, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$getVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector2f vector2f = v.set(index, $this$getVector2f);
        Intrinsics.checkNotNullExpressionValue(vector2f, "v.set(index, this)");
        return vector2f;
    }

    @NotNull
    public static final FloatBuffer putVector2f(@NotNull FloatBuffer $this$putVector2f, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$putVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        FloatBuffer floatBuffer = v.get($this$putVector2f);
        Intrinsics.checkNotNullExpressionValue(floatBuffer, "v.get(this)");
        return floatBuffer;
    }

    @NotNull
    public static final FloatBuffer putVector2f(@NotNull FloatBuffer $this$putVector2f, int index, @NotNull Vector2f v) {
        Intrinsics.checkNotNullParameter($this$putVector2f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        FloatBuffer floatBuffer = v.get(index, $this$putVector2f);
        Intrinsics.checkNotNullExpressionValue(floatBuffer, "v.get(index, this)");
        return floatBuffer;
    }
}

