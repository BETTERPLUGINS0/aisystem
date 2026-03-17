/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.model;

import java.util.Arrays;

public class Animation {
    private final AnimationType type;
    private final String id;
    private final String[] args;

    public Animation(AnimationType animationType, String string, String ... stringArray) {
        this.type = animationType;
        this.id = string;
        this.args = stringArray;
    }

    public AnimationType getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public String[] getArgs() {
        return this.args;
    }

    public String toString() {
        return "Animation{type=" + (Object)((Object)this.type) + ", id='" + this.id + '\'' + ", args=" + Arrays.toString(this.args) + '}';
    }

    public static enum AnimationType {
        SPAWN,
        DESPAWN,
        ENTER,
        BREAK,
        RUN;


        public static AnimationType getType(String string) {
            for (AnimationType animationType : AnimationType.values()) {
                if (!animationType.name().equalsIgnoreCase(string)) continue;
                return animationType;
            }
            return null;
        }
    }
}

