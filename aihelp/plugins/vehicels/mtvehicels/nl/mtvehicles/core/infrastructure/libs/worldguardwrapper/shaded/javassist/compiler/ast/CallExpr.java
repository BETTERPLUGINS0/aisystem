/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.CompileError;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.MemberResolver;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.ASTList;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.ASTree;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.Expr;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.Visitor;

public class CallExpr
extends Expr {
    private static final long serialVersionUID = 1L;
    private MemberResolver.Method method = null;

    private CallExpr(ASTree _head, ASTList _tail) {
        super(67, _head, _tail);
    }

    public void setMethod(MemberResolver.Method m) {
        this.method = m;
    }

    public MemberResolver.Method getMethod() {
        return this.method;
    }

    public static CallExpr makeCall(ASTree target, ASTree args) {
        return new CallExpr(target, new ASTList(args));
    }

    @Override
    public void accept(Visitor v) throws CompileError {
        v.atCallExpr(this);
    }
}

