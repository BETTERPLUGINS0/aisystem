/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ArrayMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.BooleanMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ByteMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.CharMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ClassMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.DoubleMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.EnumMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.FloatMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.IntegerMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.LongMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.ShortMemberValue;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.StringMemberValue;

public interface MemberValueVisitor {
    public void visitAnnotationMemberValue(AnnotationMemberValue var1);

    public void visitArrayMemberValue(ArrayMemberValue var1);

    public void visitBooleanMemberValue(BooleanMemberValue var1);

    public void visitByteMemberValue(ByteMemberValue var1);

    public void visitCharMemberValue(CharMemberValue var1);

    public void visitDoubleMemberValue(DoubleMemberValue var1);

    public void visitEnumMemberValue(EnumMemberValue var1);

    public void visitFloatMemberValue(FloatMemberValue var1);

    public void visitIntegerMemberValue(IntegerMemberValue var1);

    public void visitLongMemberValue(LongMemberValue var1);

    public void visitShortMemberValue(ShortMemberValue var1);

    public void visitStringMemberValue(StringMemberValue var1);

    public void visitClassMemberValue(ClassMemberValue var1);
}

