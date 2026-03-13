/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.joml;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u00002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0004\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0007\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0007\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\n\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\f\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u0015\u0010\r\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0012H\u0086\u0002\u001a\u0015\u0010\u0013\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002\u001a\u0015\u0010\u0013\u001a\u00020\b*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002\u00a8\u0006\u0014"}, d2={"difference", "Lorg/joml/Quaternionf;", "Lorg/joml/Quaternionfc;", "q", "div", "s", "", "divAssign", "", "minus", "minusAssign", "plus", "plusAssign", "times", "Lorg/joml/Vector3f;", "v", "Lorg/joml/Vector3fc;", "Lorg/joml/Vector4f;", "Lorg/joml/Vector4fc;", "timesAssign", "joml"})
public final class QuaternionfKt {
    @NotNull
    public static final Quaternionf plus(@NotNull Quaternionfc $this$plus, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaternionf quaternionf = $this$plus.add(q, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "add(q, Quaternionf())");
        return quaternionf;
    }

    public static final void plusAssign(@NotNull Quaternionf $this$plusAssign, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$plusAssign.add(q);
    }

    @NotNull
    public static final Quaternionf minus(@NotNull Quaternionfc $this$minus, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaternionf quaternionf = $this$minus.sub(q, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "sub(q, Quaternionf())");
        return quaternionf;
    }

    public static final void minusAssign(@NotNull Quaternionf $this$minusAssign, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$minusAssign.sub(q);
    }

    @NotNull
    public static final Quaternionf times(@NotNull Quaternionfc $this$times, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaternionf quaternionf = $this$times.mul(q, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "mul(q, Quaternionf())");
        return quaternionf;
    }

    @NotNull
    public static final Quaternionf times(@NotNull Quaternionfc $this$times, float s) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Quaternionf quaternionf = $this$times.mul(s, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "mul(s, Quaternionf())");
        return quaternionf;
    }

    @NotNull
    public static final Vector4f times(@NotNull Quaternionfc $this$times, @NotNull Vector4fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector4f vector4f = $this$times.transform(v, new Vector4f());
        Intrinsics.checkNotNullExpressionValue(vector4f, "transform(v, Vector4f())");
        return vector4f;
    }

    @NotNull
    public static final Vector3f times(@NotNull Quaternionfc $this$times, @NotNull Vector3fc v) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(v, "v");
        Vector3f vector3f = $this$times.transform(v, new Vector3f());
        Intrinsics.checkNotNullExpressionValue(vector3f, "transform(v, Vector3f())");
        return vector3f;
    }

    public static final void timesAssign(@NotNull Quaternionf $this$timesAssign, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$timesAssign.mul(q);
    }

    public static final void timesAssign(@NotNull Quaternionf $this$timesAssign, float s) {
        Intrinsics.checkNotNullParameter($this$timesAssign, "<this>");
        $this$timesAssign.mul(s);
    }

    @NotNull
    public static final Quaternionf div(@NotNull Quaternionfc $this$div, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaternionf quaternionf = $this$div.div(q, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "div(q, Quaternionf())");
        return quaternionf;
    }

    @NotNull
    public static final Quaternionf div(@NotNull Quaternionfc $this$div, float s) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Quaternionf quaternionf = $this$div.div(s, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "div(s, Quaternionf())");
        return quaternionf;
    }

    public static final void divAssign(@NotNull Quaternionf $this$divAssign, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        $this$divAssign.div(q);
    }

    public static final void divAssign(@NotNull Quaternionf $this$divAssign, float s) {
        Intrinsics.checkNotNullParameter($this$divAssign, "<this>");
        $this$divAssign.div(s);
    }

    @NotNull
    public static final Quaternionf difference(@NotNull Quaternionfc $this$difference, @NotNull Quaternionfc q) {
        Intrinsics.checkNotNullParameter($this$difference, "<this>");
        Intrinsics.checkNotNullParameter(q, "q");
        Quaternionf quaternionf = $this$difference.difference(q, new Quaternionf());
        Intrinsics.checkNotNullExpressionValue(quaternionf, "difference(q, Quaternionf())");
        return quaternionf;
    }
}

