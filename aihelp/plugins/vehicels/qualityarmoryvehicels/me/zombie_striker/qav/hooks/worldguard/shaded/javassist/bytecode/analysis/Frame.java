/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Type;

public class Frame {
    private Type[] locals;
    private Type[] stack;
    private int top;
    private boolean jsrMerged;
    private boolean retMerged;

    public Frame(int n, int n2) {
        this.locals = new Type[n];
        this.stack = new Type[n2];
    }

    public Type getLocal(int n) {
        return this.locals[n];
    }

    public void setLocal(int n, Type type) {
        this.locals[n] = type;
    }

    public Type getStack(int n) {
        return this.stack[n];
    }

    public void setStack(int n, Type type) {
        this.stack[n] = type;
    }

    public void clearStack() {
        this.top = 0;
    }

    public int getTopIndex() {
        return this.top - 1;
    }

    public int localsLength() {
        return this.locals.length;
    }

    public Type peek() {
        if (this.top < 1) {
            throw new IndexOutOfBoundsException("Stack is empty");
        }
        return this.stack[this.top - 1];
    }

    public Type pop() {
        if (this.top < 1) {
            throw new IndexOutOfBoundsException("Stack is empty");
        }
        return this.stack[--this.top];
    }

    public void push(Type type) {
        this.stack[this.top++] = type;
    }

    public Frame copy() {
        Frame frame = new Frame(this.locals.length, this.stack.length);
        System.arraycopy(this.locals, 0, frame.locals, 0, this.locals.length);
        System.arraycopy(this.stack, 0, frame.stack, 0, this.stack.length);
        frame.top = this.top;
        return frame;
    }

    public Frame copyStack() {
        Frame frame = new Frame(this.locals.length, this.stack.length);
        System.arraycopy(this.stack, 0, frame.stack, 0, this.stack.length);
        frame.top = this.top;
        return frame;
    }

    public boolean mergeStack(Frame frame) {
        boolean bl = false;
        if (this.top != frame.top) {
            throw new RuntimeException("Operand stacks could not be merged, they are different sizes!");
        }
        for (int i = 0; i < this.top; ++i) {
            if (this.stack[i] == null) continue;
            Type type = this.stack[i];
            Type type2 = type.merge(frame.stack[i]);
            if (type2 == Type.BOGUS) {
                throw new RuntimeException("Operand stacks could not be merged due to differing primitive types: pos = " + i);
            }
            this.stack[i] = type2;
            if (type2.equals(type) && !type2.popChanged()) continue;
            bl = true;
        }
        return bl;
    }

    public boolean merge(Frame frame) {
        boolean bl = false;
        for (int i = 0; i < this.locals.length; ++i) {
            if (this.locals[i] != null) {
                Type type;
                Type type2 = this.locals[i];
                this.locals[i] = type = type2.merge(frame.locals[i]);
                if (type.equals(type2) && !type.popChanged()) continue;
                bl = true;
                continue;
            }
            if (frame.locals[i] == null) continue;
            this.locals[i] = frame.locals[i];
            bl = true;
        }
        return bl |= this.mergeStack(frame);
    }

    public String toString() {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("locals = [");
        for (n = 0; n < this.locals.length; ++n) {
            stringBuilder.append(this.locals[n] == null ? "empty" : this.locals[n].toString());
            if (n >= this.locals.length - 1) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append("] stack = [");
        for (n = 0; n < this.top; ++n) {
            stringBuilder.append(this.stack[n]);
            if (n >= this.top - 1) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    boolean isJsrMerged() {
        return this.jsrMerged;
    }

    void setJsrMerged(boolean bl) {
        this.jsrMerged = bl;
    }

    boolean isRetMerged() {
        return this.retMerged;
    }

    void setRetMerged(boolean bl) {
        this.retMerged = bl;
    }
}

