/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4d;
import org.joml.AxisAngle4f;
import org.joml.Matrix3d;
import org.joml.Matrix3dc;
import org.joml.Matrix3fc;
import org.joml.Quaterniondc;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000T\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0004\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\f\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u001a\u0010\r\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0012\u0010\r\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u001a\u0010\r\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0012\u0010\r\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0013H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0014H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0001*\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0017H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u0018*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0019H\u0086\u0002\u001a\u0015\u0010\u0010\u001a\u00020\u001a*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u001bH\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0013H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0014H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0016H\u0086\u0002\u001a\u0015\u0010\u001c\u001a\u00020\t*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0017H\u0086\u0002\u00a8\u0006\u001d"}, d2={"getMatrix3d", "Lorg/joml/Matrix3d;", "Ljava/nio/ByteBuffer;", "v", "Ljava/nio/DoubleBuffer;", "minus", "Lorg/joml/Matrix3dc;", "m", "minusAssign", "", "mulComponentWise", "plus", "plusAssign", "putMatrix3d", "index", "", "times", "a", "Lorg/joml/AxisAngle4d;", "Lorg/joml/AxisAngle4f;", "Lorg/joml/Matrix3fc;", "q", "Lorg/joml/Quaterniondc;", "Lorg/joml/Quaternionfc;", "Lorg/joml/Vector3d;", "Lorg/joml/Vector3dc;", "Lorg/joml/Vector3f;", "Lorg/joml/Vector3fc;", "timesAssign", "joml"})
public final class Matrix3dKt {
    @NotNull
    public static final Matrix3d plus(@NotNull Matrix3dc $this$plus, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix3d matrix3d = $this$plus.add(m, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "add(m, Matrix3d())");
        return matrix3d;
    }

    public static final void plusAssign(@NotNull Matrix3d $this$plusAssign, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$plusAssign.add(m);
    }

    @NotNull
    public static final Matrix3d minus(@NotNull Matrix3dc $this$minus, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix3d matrix3d = $this$minus.sub(m, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "sub(m, Matrix3d())");
        return matrix3d;
    }

    public static final void minusAssign(@NotNull Matrix3d $this$minusAssign, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$minusAssign.sub(m);
    }

    @NotNull
    public static final Matrix3d times(@NotNull Matrix3dc $this$times, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix3d matrix3d = $this$times.mul(m, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "mul(m, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Matrix3d times(@NotNull Matrix3dc $this$times, @NotNull Matrix3fc m) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix3d matrix3d = $this$times.mul(m, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "mul(m, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Matrix3d times(@NotNull Matrix3dc $this$times, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Matrix3d matrix3d = $this$times.rotate(q, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "rotate(q, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Matrix3d times(@NotNull Matrix3dc $this$times, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Matrix3d matrix3d = $this$times.rotate(q, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "rotate(q, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Matrix3d times(@NotNull Matrix3dc $this$times, @NotNull AxisAngle4d a) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(a, "a");
        Matrix3d matrix3d = $this$times.rotate(a, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "rotate(a, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Matrix3d times(@NotNull Matrix3dc $this$times, @NotNull AxisAngle4f a) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(a, "a");
        Matrix3d matrix3d = $this$times.rotate(a, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "rotate(a, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Vector3d times(@NotNull Matrix3dc $this$times, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$times.transform(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "transform(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3f times(@NotNull Matrix3dc $this$times, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3f vector3f = $this$times.transform(v, new Vector3f());
        Intrinsics.checkNotNullExpressionValue(vector3f, "transform(v, Vector3f())");
        return vector3f;
    }

    public static final void timesAssign(@NotNull Matrix3d $this$timesAssign, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Matrix3d $this$timesAssign, @NotNull Matrix3fc m) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        $this$timesAssign.mul(m);
    }

    public static final void timesAssign(@NotNull Matrix3d $this$timesAssign, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$timesAssign.rotate(q);
    }

    public static final void timesAssign(@NotNull Matrix3d $this$timesAssign, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$timesAssign.rotate(q);
    }

    public static final void timesAssign(@NotNull Matrix3d $this$timesAssign, @NotNull AxisAngle4d a) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(a, "a");
        $this$timesAssign.rotate(a);
    }

    public static final void timesAssign(@NotNull Matrix3d $this$timesAssign, @NotNull AxisAngle4f a) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(a, "a");
        $this$timesAssign.rotate(a);
    }

    @NotNull
    public static final Matrix3d mulComponentWise(@NotNull Matrix3dc $this$mulComponentWise, @NotNull Matrix3dc m) {
        Intrinsics.checkNotNullParameter($this$mulComponentWise, "<this>");
        Intrinsics.checkNotNullParameter(m, "m");
        Matrix3d matrix3d = $this$mulComponentWise.mulComponentWise(m, new Matrix3d());
        Intrinsics.checkNotNullExpressionValue(matrix3d, "mulComponentWise(m, Matrix3d())");
        return matrix3d;
    }

    @NotNull
    public static final Matrix3d getMatrix3d(@NotNull ByteBuffer $this$getMatrix3d, @NotNull Matrix3d v) {
        Intrinsics.checkNotNullParameter($this$getMatrix3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Matrix3d matrix3d = v.set($this$getMatrix3d);
        Intrinsics.checkNotNullExpressionValue(matrix3d, "v.set(this)");
        return matrix3d;
    }

    @NotNull
    public static final ByteBuffer putMatrix3d(@NotNull ByteBuffer $this$putMatrix3d, @NotNull Matrix3d v) {
        Intrinsics.checkNotNullParameter($this$putMatrix3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get($this$putMatrix3d);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(this)");
        return byteBuffer;
    }

    @NotNull
    public static final ByteBuffer putMatrix3d(@NotNull ByteBuffer $this$putMatrix3d, int index, @NotNull Matrix3d v) {
        Intrinsics.checkNotNullParameter($this$putMatrix3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        ByteBuffer byteBuffer = v.get(index, $this$putMatrix3d);
        Intrinsics.checkNotNullExpressionValue(byteBuffer, "v.get(index, this)");
        return byteBuffer;
    }

    @NotNull
    public static final Matrix3d getMatrix3d(@NotNull DoubleBuffer $this$getMatrix3d) {
        Intrinsics.checkNotNullParameter($this$getMatrix3d, "<this>");
        return new Matrix3d($this$getMatrix3d);
    }

    @NotNull
    public static final Matrix3d getMatrix3d(@NotNull DoubleBuffer $this$getMatrix3d, @NotNull Matrix3d v) {
        Intrinsics.checkNotNullParameter($this$getMatrix3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Matrix3d matrix3d = v.set($this$getMatrix3d);
        Intrinsics.checkNotNullExpressionValue(matrix3d, "v.set(this)");
        return matrix3d;
    }

    @NotNull
    public static final DoubleBuffer putMatrix3d(@NotNull DoubleBuffer $this$putMatrix3d, @NotNull Matrix3d v) {
        Intrinsics.checkNotNullParameter($this$putMatrix3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        DoubleBuffer doubleBuffer = v.get($this$putMatrix3d);
        Intrinsics.checkNotNullExpressionValue(doubleBuffer, "v.get(this)");
        return doubleBuffer;
    }

    @NotNull
    public static final DoubleBuffer putMatrix3d(@NotNull DoubleBuffer $this$putMatrix3d, int index, @NotNull Matrix3d v) {
        Intrinsics.checkNotNullParameter($this$putMatrix3d, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        DoubleBuffer doubleBuffer = v.get(index, $this$putMatrix3d);
        Intrinsics.checkNotNullExpressionValue(doubleBuffer, "v.get(index, this)");
        return doubleBuffer;
    }
}

