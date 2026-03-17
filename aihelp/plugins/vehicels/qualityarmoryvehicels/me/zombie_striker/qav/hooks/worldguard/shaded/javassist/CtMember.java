/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClassType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;

public abstract class CtMember {
    CtMember next;
    protected CtClass declaringClass;

    protected CtMember(CtClass ctClass) {
        this.declaringClass = ctClass;
        this.next = null;
    }

    final CtMember next() {
        return this.next;
    }

    void nameReplaced() {
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.getClass().getName());
        stringBuilder.append('@');
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        stringBuilder.append('[');
        stringBuilder.append(Modifier.toString(this.getModifiers()));
        this.extendToString(stringBuilder);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    protected abstract void extendToString(StringBuilder var1);

    public CtClass getDeclaringClass() {
        return this.declaringClass;
    }

    public boolean visibleFrom(CtClass ctClass) {
        int n = this.getModifiers();
        if (Modifier.isPublic(n)) {
            return true;
        }
        if (Modifier.isPrivate(n)) {
            return ctClass == this.declaringClass;
        }
        String string = this.declaringClass.getPackageName();
        String string2 = ctClass.getPackageName();
        boolean bl = string == null ? string2 == null : string.equals(string2);
        if (!bl && Modifier.isProtected(n)) {
            return ctClass.subclassOf(this.declaringClass);
        }
        return bl;
    }

    public abstract int getModifiers();

    public abstract void setModifiers(int var1);

    public boolean hasAnnotation(Class<?> clazz) {
        return this.hasAnnotation(clazz.getName());
    }

    public abstract boolean hasAnnotation(String var1);

    public abstract Object getAnnotation(Class<?> var1);

    public abstract Object[] getAnnotations();

    public abstract Object[] getAvailableAnnotations();

    public abstract String getName();

    public abstract String getSignature();

    public abstract String getGenericSignature();

    public abstract void setGenericSignature(String var1);

    public abstract byte[] getAttribute(String var1);

    public abstract void setAttribute(String var1, byte[] var2);

    static class Cache
    extends CtMember {
        private CtMember methodTail = this;
        private CtMember consTail = this;
        private CtMember fieldTail = this;

        @Override
        protected void extendToString(StringBuilder stringBuilder) {
        }

        @Override
        public boolean hasAnnotation(String string) {
            return false;
        }

        @Override
        public Object getAnnotation(Class<?> clazz) {
            return null;
        }

        @Override
        public Object[] getAnnotations() {
            return null;
        }

        @Override
        public byte[] getAttribute(String string) {
            return null;
        }

        @Override
        public Object[] getAvailableAnnotations() {
            return null;
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getSignature() {
            return null;
        }

        @Override
        public void setAttribute(String string, byte[] byArray) {
        }

        @Override
        public void setModifiers(int n) {
        }

        @Override
        public String getGenericSignature() {
            return null;
        }

        @Override
        public void setGenericSignature(String string) {
        }

        Cache(CtClassType ctClassType) {
            super(ctClassType);
            this.fieldTail.next = this;
        }

        CtMember methodHead() {
            return this;
        }

        CtMember lastMethod() {
            return this.methodTail;
        }

        CtMember consHead() {
            return this.methodTail;
        }

        CtMember lastCons() {
            return this.consTail;
        }

        CtMember fieldHead() {
            return this.consTail;
        }

        CtMember lastField() {
            return this.fieldTail;
        }

        void addMethod(CtMember ctMember) {
            ctMember.next = this.methodTail.next;
            this.methodTail.next = ctMember;
            if (this.methodTail == this.consTail) {
                this.consTail = ctMember;
                if (this.methodTail == this.fieldTail) {
                    this.fieldTail = ctMember;
                }
            }
            this.methodTail = ctMember;
        }

        void addConstructor(CtMember ctMember) {
            ctMember.next = this.consTail.next;
            this.consTail.next = ctMember;
            if (this.consTail == this.fieldTail) {
                this.fieldTail = ctMember;
            }
            this.consTail = ctMember;
        }

        void addField(CtMember ctMember) {
            ctMember.next = this;
            this.fieldTail.next = ctMember;
            this.fieldTail = ctMember;
        }

        static int count(CtMember ctMember, CtMember ctMember2) {
            int n = 0;
            while (ctMember != ctMember2) {
                ++n;
                ctMember = ctMember.next;
            }
            return n;
        }

        void remove(CtMember ctMember) {
            CtMember ctMember2;
            CtMember ctMember3 = this;
            while ((ctMember2 = ctMember3.next) != this) {
                if (ctMember2 == ctMember) {
                    ctMember3.next = ctMember2.next;
                    if (ctMember2 == this.methodTail) {
                        this.methodTail = ctMember3;
                    }
                    if (ctMember2 == this.consTail) {
                        this.consTail = ctMember3;
                    }
                    if (ctMember2 != this.fieldTail) break;
                    this.fieldTail = ctMember3;
                    break;
                }
                ctMember3 = ctMember3.next;
            }
        }
    }
}

