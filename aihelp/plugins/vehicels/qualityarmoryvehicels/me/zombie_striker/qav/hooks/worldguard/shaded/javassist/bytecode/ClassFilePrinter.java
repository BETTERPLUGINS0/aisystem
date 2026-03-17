/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AccessFlag;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ParameterAnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;

public class ClassFilePrinter {
    public static void print(ClassFile classFile) {
        ClassFilePrinter.print(classFile, new PrintWriter(System.out, true));
    }

    public static void print(ClassFile classFile, PrintWriter printWriter) {
        int n = AccessFlag.toModifier(classFile.getAccessFlags() & 0xFFFFFFDF);
        printWriter.println("major: " + classFile.major + ", minor: " + classFile.minor + " modifiers: " + Integer.toHexString(classFile.getAccessFlags()));
        printWriter.println(Modifier.toString(n) + " class " + classFile.getName() + " extends " + classFile.getSuperclass());
        String[] stringArray = classFile.getInterfaces();
        if (stringArray != null && stringArray.length > 0) {
            printWriter.print("    implements ");
            printWriter.print(stringArray[0]);
            for (int i = 1; i < stringArray.length; ++i) {
                printWriter.print(", " + stringArray[i]);
            }
            printWriter.println();
        }
        printWriter.println();
        List<FieldInfo> list = classFile.getFields();
        for (FieldInfo object2 : list) {
            int methodInfo = object2.getAccessFlags();
            printWriter.println(Modifier.toString(AccessFlag.toModifier(methodInfo)) + " " + object2.getName() + "\t" + object2.getDescriptor());
            ClassFilePrinter.printAttributes(object2.getAttributes(), printWriter, 'f');
        }
        printWriter.println();
        List<MethodInfo> list2 = classFile.getMethods();
        Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            MethodInfo methodInfo = (MethodInfo)iterator.next();
            int n2 = methodInfo.getAccessFlags();
            printWriter.println(Modifier.toString(AccessFlag.toModifier(n2)) + " " + methodInfo.getName() + "\t" + methodInfo.getDescriptor());
            ClassFilePrinter.printAttributes(methodInfo.getAttributes(), printWriter, 'm');
            printWriter.println();
        }
        printWriter.println();
        ClassFilePrinter.printAttributes(classFile.getAttributes(), printWriter, 'c');
    }

    static void printAttributes(List<AttributeInfo> list, PrintWriter printWriter, char c) {
        if (list == null) {
            return;
        }
        for (AttributeInfo attributeInfo : list) {
            AttributeInfo attributeInfo2;
            if (attributeInfo instanceof CodeAttribute) {
                attributeInfo2 = (CodeAttribute)attributeInfo;
                printWriter.println("attribute: " + attributeInfo.getName() + ": " + attributeInfo.getClass().getName());
                printWriter.println("max stack " + ((CodeAttribute)attributeInfo2).getMaxStack() + ", max locals " + ((CodeAttribute)attributeInfo2).getMaxLocals() + ", " + ((CodeAttribute)attributeInfo2).getExceptionTable().size() + " catch blocks");
                printWriter.println("<code attribute begin>");
                ClassFilePrinter.printAttributes(((CodeAttribute)attributeInfo2).getAttributes(), printWriter, c);
                printWriter.println("<code attribute end>");
                continue;
            }
            if (attributeInfo instanceof AnnotationsAttribute) {
                printWriter.println("annnotation: " + attributeInfo.toString());
                continue;
            }
            if (attributeInfo instanceof ParameterAnnotationsAttribute) {
                printWriter.println("parameter annnotations: " + attributeInfo.toString());
                continue;
            }
            if (attributeInfo instanceof StackMapTable) {
                printWriter.println("<stack map table begin>");
                StackMapTable.Printer.print((StackMapTable)attributeInfo, printWriter);
                printWriter.println("<stack map table end>");
                continue;
            }
            if (attributeInfo instanceof StackMap) {
                printWriter.println("<stack map begin>");
                ((StackMap)attributeInfo).print(printWriter);
                printWriter.println("<stack map end>");
                continue;
            }
            if (attributeInfo instanceof SignatureAttribute) {
                attributeInfo2 = (SignatureAttribute)attributeInfo;
                String string = ((SignatureAttribute)attributeInfo2).getSignature();
                printWriter.println("signature: " + string);
                try {
                    String string2 = c == 'c' ? SignatureAttribute.toClassSignature(string).toString() : (c == 'm' ? SignatureAttribute.toMethodSignature(string).toString() : SignatureAttribute.toFieldSignature(string).toString());
                    printWriter.println("           " + string2);
                } catch (BadBytecode badBytecode) {
                    printWriter.println("           syntax error");
                }
                continue;
            }
            printWriter.println("attribute: " + attributeInfo.getName() + " (" + attributeInfo.get().length + " byte): " + attributeInfo.getClass().getName());
        }
    }
}

