/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subroutine {
    private List<Integer> callers = new ArrayList<Integer>();
    private Set<Integer> access = new HashSet<Integer>();
    private int start;

    public Subroutine(int n, int n2) {
        this.start = n;
        this.callers.add(n2);
    }

    public void addCaller(int n) {
        this.callers.add(n);
    }

    public int start() {
        return this.start;
    }

    public void access(int n) {
        this.access.add(n);
    }

    public boolean isAccessed(int n) {
        return this.access.contains(n);
    }

    public Collection<Integer> accessed() {
        return this.access;
    }

    public Collection<Integer> callers() {
        return this.callers;
    }

    public String toString() {
        return "start = " + this.start + " callers = " + this.callers.toString();
    }
}

