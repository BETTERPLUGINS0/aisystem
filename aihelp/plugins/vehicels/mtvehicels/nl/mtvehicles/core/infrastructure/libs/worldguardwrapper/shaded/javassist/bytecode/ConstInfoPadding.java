/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstInfo;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;

class ConstInfoPadding
extends ConstInfo {
    public ConstInfoPadding(int i) {
        super(i);
    }

    @Override
    public int getTag() {
        return 0;
    }

    @Override
    public int copy(ConstPool src, ConstPool dest, Map<String, String> map) {
        return dest.addConstInfoPadding();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
    }

    @Override
    public void print(PrintWriter out) {
        out.println("padding");
    }
}

