/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.util;

public class MathToolkit {
    public static float lerp(float start, float end, float t) {
        return (1.0f - t) * start + t * end;
    }

    public static float smoothLerp(float start, float end, float t) {
        t = Math.max(0.0f, Math.min(1.0f, t));
        float smoothT = t * t * (3.0f - 2.0f * t);
        return (1.0f - smoothT) * start + smoothT * end;
    }

    public static float bezierLerp(float start, float end, float t, float cp1, float cp2) {
        t = Math.max(0.0f, Math.min(1.0f, t));
        float oneMinusT = 1.0f - t;
        float bezierT = 3.0f * oneMinusT * oneMinusT * t * cp1 + 3.0f * oneMinusT * t * t * cp2 + t * t * t;
        return (1.0f - bezierT) * start + bezierT * end;
    }

    public static float stepLerp(float start, float end, float t, float threshold) {
        return t < threshold ? start : end;
    }

    public static float stepLerp(float start, float end, float t) {
        return MathToolkit.stepLerp(start, end, t, 0.5f);
    }
}

