/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFilePrinter;

public class Dump {
    private Dump() {
    }

    public static void main(String[] stringArray) {
        if (stringArray.length != 1) {
            System.err.println("Usage: java Dump <class file name>");
            return;
        }
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(stringArray[0]));
        ClassFile classFile = new ClassFile(dataInputStream);
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println("*** constant pool ***");
        classFile.getConstPool().print(printWriter);
        printWriter.println();
        printWriter.println("*** members ***");
        ClassFilePrinter.print(classFile, printWriter);
    }
}

