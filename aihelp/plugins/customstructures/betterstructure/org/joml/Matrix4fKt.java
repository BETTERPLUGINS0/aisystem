/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0004\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\f\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u001a\u0010\r\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0012\u0010\r\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u001a\u0010\r\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0012\u0010\r\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0014H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0015*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0016H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0014H\u0086\u0002\u00a8\u0006\u0018"}, d2={"getMatrix4f", "Lorg/joml/Matrix4f;", "Ljava/nio/ByteBuffer;", "v", "Ljava/nio/FloatBuffer;", "minus", "Lorg/joml/Matrix4fc;", "m", "minusAssign", "", "mulComponentWise", "plus", "plusAssign", "putMatrix4f", "index", "", "times", "a", "Lorg/joml/AxisAngle4f;", "q", "Lorg/joml/Quaternionfc;", "Lorg/joml/Vector4f;", "Lorg/joml/Vector4fc;", "timesAssign", "joml"})
public final class Matrix4fKt {
    @NotNull
    public static final Matrix4f plus(@NotNull Matrix4fc $this$plus, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix4f matrix4f = $this$plus.add(m, new Matrix4f());
        Intrinsics.checkNotNullExpressionValue(matrix4f, "add(m, Matrix4f())");
        return matrix4f;
    }

    public static final void plusAssign(@NotNull Matrix4f $this$plusAssign, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$plusAssign.add(m);
    }

    @NotNull
    public static final Matrix4f minus(@NotNull Matrix4fc $this$minus, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix4f matrix4f = $this$minus.sub(m, new Matrix4f());
        Intrinsics.checkNotNullExpressionValue(matrix4f, "sub(m, Matrix4f())");
        return matrix4f;
    }

    public static final void minusAssign(@NotNull Matrix4f $this$minusAssign, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$minusAssign.sub(m);
    }

    @NotNull
    public static final Matrix4f times(@NotNull Matrix4fc $this$times, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix4f matrix4f = $this$times.mul(m, new Matrix4f());
        Intrinsics.checkNotNullExpressionValue(matrix4f, "mul(m, Matrix4f())");
        return matrix4f;
    }

    @NotNull
    public static final Matrix4f times(@NotNull Matrix4fc $this$times, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Matrix4f matrix4f = $this$times.rotate(q, new Matrix4f());
        Intrinsics.checkNotNullExpressionValue(matrix4f, "rotate(q, Matrix4f())");
        return matrix4f;
    }

    @NotNull
    public static final Matrix4f times(@NotNull Matrix4fc $this$times, @NotNull AxisAngle4f a) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(a, "a");
        Matrix4f matrix4f = $this$times.rotate(a, new Matrix4f());
        Intrinsics.checkNotNullExpressionValue(matrix4f, "rotate(a, Matrix4f())");
        return matrix4f;
    }

    @NotNull
    public static final Vector4f times(@NotNull Matrix4fc $this$times, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4f vector4f = $this$times.transform(v, new Vector4f());
        Intrinsics.checkNotNullExpressionValue(vector4f, "transform(v, Vector4f())");
        return vector4f;
    }

    public static final void timesAssign(@NotNull Matrix4f $this$timesAssign, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Matrix4f $this$timesAssign, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$timesAssign.rotate(q);
    }

    public static final void timesAssign(@NotNull Matrix4f $this$timesAssign, @NotNull AxisAngle4f a) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(a, "a");
        $this$timesAssign.rotate(a);
    }

    @NotNull
    public static final Matrix4f mulComponentWise(@NotNull Matrix4fc $this$mulComponentWise, @NotNull Matrix4fc m) {
        Intrinsics.checkNotNullParameter($this$mulComponentWise, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix4f matrix4f = $this$mulComponentWise.mulComponentWise(m, new Matrix4f());
        Intrinsics.checkNotNullExpressionValue(matrix4f, "mulComponentWise(m, Matrix4f())");
        return matrix4f;
    }

    @NotNull
    public static final Matrix4f getMatrix4f(@NotNull ByteBuffer $this$getMatrix4f, @NotNull Matrix4f v) {
        Intrinsics.checkNotNullParameter($this$getMatrix4f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Matrix4f matrix4f = v.set($this$getMatrix4f);
        Intrinsics.checkNotNullExpressionValue(matrix4f, "v.set(this)");
        return matrix4f;
    }

    @NotNull
    public static final ByteBuffer putMatrix4f(@NotNull ByteBuffer $this$putMatrix4f, @NotNull Matrix4f v) {
        Intrinsics.checkNotNullParameter($this$putMatrix4f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get($this$putMatrix4f);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(this)");
        return byteBuffer;
    }

    @NotNull
    public static final ByteBuffer putMatrix4f(@NotNull ByteBuffer $this$putMatrix4f, int index, @NotNull Matrix4f v) {
        Intrinsics.checkNotNullParameter($this$putMatrix4f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get(index, $this$putMatrix4f);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(index, this)");
        return byteBuffer;
    }

    @NotNull
    public static final Matrix4f getMatrix4f(@NotNull FloatBuffer $this$getMatrix4f) {
        Intrinsics.checkNotNullParameter($this$getMatrix4f, "<this>");
        return new Matrix4f($this$getMatrix4f);
    }

    @NotNull
    public static final Matrix4f getMatrix4f(@NotNull FloatBuffer $this$getMatrix4f, @NotNull Matrix4f v) {
        Intrinsics.checkNotNullParameter($this$getMatrix4f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Matrix4f matrix4f = v.set($this$getMatrix4f);
        Intrinsics.checkNotNullExpressionValue(matrix4f, "v.set(this)");
        return matrix4f;
    }

    @NotNull
    public static final FloatBuffer putMatrix4f(@NotNull FloatBuffer $this$putMatrix4f, @NotNull Matrix4f v) {
        Intrinsics.checkNotNullParameter($this$putMatrix4f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        FloatBuffer floatBuffer = v.get($this$putMatrix4f);
        Intrinsics.checkNotNullExpressionValue(floatBuffer, "v.get(this)");
        return floatBuffer;
    }

    @NotNull
    public static final FloatBuffer putMatrix4f(@NotNull FloatBuffer $this$putMatrix4f, int index, @NotNull Matrix4f v) {
        Intrinsics.checkNotNullParameter($this$putMatrix4f, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        FloatBuffer floatBuffer = v.get(index, $this$putMatrix4f);
        Intrinsics.checkNotNullExpressionValue(floatBuffer, "v.get(index, this)");
        return floatBuffer;
    }
}

