/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package javassist.tools;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ClassFilePrinter;

public class Dump {
    private Dump() {
    }

    public static void main(String[] args2) throws Exception {
        if (args2.length != 1) {
            System.err.println("Usage: java Dump <class file name>");
            return;
        }
        DataInputStream in = new DataInputStream(new FileInputStream(args2[0]));
        ClassFile w = new ClassFile(in);
        PrintWriter out = new PrintWriter(System.out, true);
        out.println("*** constant pool ***");
        w.getConstPool().print(out);
        out.println();
        out.println("*** members ***");
        ClassFilePrinter.print(w, out);
    }
}

