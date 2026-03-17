/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.gui.components.exception;

public final class GuiException
extends RuntimeException {
    public GuiException(String string) {
        super(string);
    }

    public GuiException(String string, Exception exception) {
        super(string, exception);
    }
}

