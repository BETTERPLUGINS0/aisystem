/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4d;
import org.joml.Vector4dc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0007\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0007\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\n\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\f\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0012H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0013*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0014H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0015*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0016H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u00a8\u0006\u0018"}, d2={"difference", "Lorg/joml/Quaterniond;", "Lorg/joml/Quaterniondc;", "q", "div", "s", "", "divAssign", "", "minus", "minusAssign", "plus", "plusAssign", "times", "Lorg/joml/Vector3d;", "v", "Lorg/joml/Vector3dc;", "Lorg/joml/Vector3f;", "Lorg/joml/Vector3fc;", "Lorg/joml/Vector4d;", "Lorg/joml/Vector4dc;", "Lorg/joml/Vector4f;", "Lorg/joml/Vector4fc;", "timesAssign", "joml"})
public final class QuaterniondKt {
    @NotNull
    public static final Quaterniond plus(@NotNull Quaterniondc $this$plus, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaterniond quaterniond = $this$plus.add(q, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "add(q, Quaterniond())");
        return quaterniond;
    }

    public static final void plusAssign(@NotNull Quaterniond $this$plusAssign, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$plusAssign.add(q);
    }

    @NotNull
    public static final Quaterniond minus(@NotNull Quaterniondc $this$minus, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaterniond quaterniond = $this$minus.sub(q, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "sub(q, Quaterniond())");
        return quaterniond;
    }

    public static final void minusAssign(@NotNull Quaterniond $this$minusAssign, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$minusAssign.sub(q);
    }

    @NotNull
    public static final Quaterniond times(@NotNull Quaterniondc $this$times, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaterniond quaterniond = $this$times.mul(q, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "mul(q, Quaterniond())");
        return quaterniond;
    }

    @NotNull
    public static final Quaterniond times(@NotNull Quaterniondc $this$times, double s) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Quaterniond quaterniond = $this$times.mul(s, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "mul(s, Quaterniond())");
        return quaterniond;
    }

    @NotNull
    public static final Vector4d times(@NotNull Quaterniondc $this$times, @NotNull Vector4dc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4d vector4d = $this$times.transform(v, new Vector4d());
        Intrinsics.checkNotNullExpressionValue(vector4d, "transform(v, Vector4d())");
        return vector4d;
    }

    @NotNull
    public static final Vector4f times(@NotNull Quaterniondc $this$times, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4f vector4f = $this$times.transform(v, new Vector4f());
        Intrinsics.checkNotNullExpressionValue(vector4f, "transform(v, Vector4f())");
        return vector4f;
    }

    @NotNull
    public static final Vector3d times(@NotNull Quaterniondc $this$times, @NotNull Vector3dc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3d vector3d = $this$times.transform(v, new Vector3d());
        Intrinsics.checkNotNullExpressionValue(vector3d, "transform(v, Vector3d())");
        return vector3d;
    }

    @NotNull
    public static final Vector3f times(@NotNull Quaterniondc $this$times, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3f vector3f = $this$times.transform(v, new Vector3f());
        Intrinsics.checkNotNullExpressionValue(vector3f, "transform(v, Vector3f())");
        return vector3f;
    }

    public static final void timesAssign(@NotNull Quaterniond $this$timesAssign, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$timesAssign.mul(q);
    }

    public static final void timesAssign(@NotNull Quaterniond $this$timesAssign, double s) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        $this$timesAssign.mul(s);
    }

    @NotNull
    public static final Quaterniond div(@NotNull Quaterniondc $this$div, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaterniond quaterniond = $this$div.div(q, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "div(q, Quaterniond())");
        return quaterniond;
    }

    @NotNull
    public static final Quaterniond div(@NotNull Quaterniondc $this$div, double s) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Quaterniond quaterniond = $this$div.div(s, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "div(s, Quaterniond())");
        return quaterniond;
    }

    public static final void divAssign(@NotNull Quaterniond $this$divAssign, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$divAssign.div(q);
    }

    public static final void divAssign(@NotNull Quaterniond $this$divAssign, double s) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        $this$divAssign.div(s);
    }

    @NotNull
    public static final Quaterniond difference(@NotNull Quaterniondc $this$difference, @NotNull Quaterniondc q) {
        Intrinsics.checkNotNullParameter($this$difference, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaterniond quaterniond = $this$difference.difference(q, new Quaterniond());
        Intrinsics.checkNotNullExpressionValue(quaterniond, "difference(q, Quaterniond())");
        return quaterniond;
    }
}

