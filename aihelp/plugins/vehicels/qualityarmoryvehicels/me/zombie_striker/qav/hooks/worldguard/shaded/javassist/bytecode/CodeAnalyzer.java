/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;

class CodeAnalyzer
implements Opcode {
    private ConstPool constPool;
    private CodeAttribute codeAttr;

    public CodeAnalyzer(CodeAttribute codeAttribute) {
        this.codeAttr = codeAttribute;
        this.constPool = codeAttribute.getConstPool();
    }

    public int computeMaxStack() {
        int n;
        boolean bl;
        CodeIterator codeIterator = this.codeAttr.iterator();
        int n2 = codeIterator.getCodeLength();
        int[] nArray = new int[n2];
        this.constPool = this.codeAttr.getConstPool();
        this.initStack(nArray, this.codeAttr);
        do {
            bl = false;
            for (n = 0; n < n2; ++n) {
                if (nArray[n] >= 0) continue;
                bl = true;
                this.visitBytecode(codeIterator, nArray, n);
            }
        } while (bl);
        n = 1;
        for (int i = 0; i < n2; ++i) {
            if (nArray[i] <= n) continue;
            n = nArray[i];
        }
        return n - 1;
    }

    private void initStack(int[] nArray, CodeAttribute codeAttribute) {
        nArray[0] = -1;
        ExceptionTable exceptionTable = codeAttribute.getExceptionTable();
        if (exceptionTable != null) {
            int n = exceptionTable.size();
            for (int i = 0; i < n; ++i) {
                nArray[exceptionTable.handlerPc((int)i)] = -2;
            }
        }
    }

    private void visitBytecode(CodeIterator codeIterator, int[] nArray, int n) {
        int n2 = nArray.length;
        codeIterator.move(n);
        int n3 = -nArray[n];
        int[] nArray2 = new int[]{-1};
        while (codeIterator.hasNext()) {
            n = codeIterator.next();
            nArray[n] = n3;
            int n4 = codeIterator.byteAt(n);
            n3 = this.visitInst(n4, codeIterator, n, n3);
            if (n3 < 1) {
                throw new BadBytecode("stack underflow at " + n);
            }
            if (this.processBranch(n4, codeIterator, n, n2, nArray, n3, nArray2) || CodeAnalyzer.isEnd(n4)) break;
            if (n4 != 168 && n4 != 201) continue;
            --n3;
        }
    }

    private boolean processBranch(int n, CodeIterator codeIterator, int n2, int n3, int[] nArray, int n4, int[] nArray2) {
        if (153 <= n && n <= 166 || n == 198 || n == 199) {
            int n5 = n2 + codeIterator.s16bitAt(n2 + 1);
            this.checkTarget(n2, n5, n3, nArray, n4);
        } else {
            switch (n) {
                case 167: {
                    int n6 = n2 + codeIterator.s16bitAt(n2 + 1);
                    this.checkTarget(n2, n6, n3, nArray, n4);
                    return true;
                }
                case 200: {
                    int n7 = n2 + codeIterator.s32bitAt(n2 + 1);
                    this.checkTarget(n2, n7, n3, nArray, n4);
                    return true;
                }
                case 168: 
                case 201: {
                    int n8 = n == 168 ? n2 + codeIterator.s16bitAt(n2 + 1) : n2 + codeIterator.s32bitAt(n2 + 1);
                    this.checkTarget(n2, n8, n3, nArray, n4);
                    if (nArray2[0] < 0) {
                        nArray2[0] = n4;
                        return false;
                    }
                    if (n4 == nArray2[0]) {
                        return false;
                    }
                    throw new BadBytecode("sorry, cannot compute this data flow due to JSR: " + n4 + "," + nArray2[0]);
                }
                case 169: {
                    if (nArray2[0] < 0) {
                        nArray2[0] = n4 + 1;
                        return false;
                    }
                    if (n4 + 1 == nArray2[0]) {
                        return true;
                    }
                    throw new BadBytecode("sorry, cannot compute this data flow due to RET: " + n4 + "," + nArray2[0]);
                }
                case 170: 
                case 171: {
                    int n9 = (n2 & 0xFFFFFFFC) + 4;
                    int n10 = n2 + codeIterator.s32bitAt(n9);
                    this.checkTarget(n2, n10, n3, nArray, n4);
                    if (n == 171) {
                        int n11 = codeIterator.s32bitAt(n9 + 4);
                        n9 += 12;
                        for (int i = 0; i < n11; ++i) {
                            n10 = n2 + codeIterator.s32bitAt(n9);
                            this.checkTarget(n2, n10, n3, nArray, n4);
                            n9 += 8;
                        }
                    } else {
                        int n12 = codeIterator.s32bitAt(n9 + 4);
                        int n13 = codeIterator.s32bitAt(n9 + 8);
                        int n14 = n13 - n12 + 1;
                        n9 += 12;
                        for (int i = 0; i < n14; ++i) {
                            n10 = n2 + codeIterator.s32bitAt(n9);
                            this.checkTarget(n2, n10, n3, nArray, n4);
                            n9 += 4;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void checkTarget(int n, int n2, int n3, int[] nArray, int n4) {
        if (n2 < 0 || n3 <= n2) {
            throw new BadBytecode("bad branch offset at " + n);
        }
        int n5 = nArray[n2];
        if (n5 == 0) {
            nArray[n2] = -n4;
        } else if (n5 != n4 && n5 != -n4) {
            throw new BadBytecode("verification error (" + n4 + "," + n5 + ") at " + n);
        }
    }

    private static boolean isEnd(int n) {
        return 172 <= n && n <= 177 || n == 191;
    }

    private int visitInst(int n, CodeIterator codeIterator, int n2, int n3) {
        switch (n) {
            case 180: {
                n3 += this.getFieldSize(codeIterator, n2) - 1;
                break;
            }
            case 181: {
                n3 -= this.getFieldSize(codeIterator, n2) + 1;
                break;
            }
            case 178: {
                n3 += this.getFieldSize(codeIterator, n2);
                break;
            }
            case 179: {
                n3 -= this.getFieldSize(codeIterator, n2);
                break;
            }
            case 182: 
            case 183: {
                String string = this.constPool.getMethodrefType(codeIterator.u16bitAt(n2 + 1));
                n3 += Descriptor.dataSize(string) - 1;
                break;
            }
            case 184: {
                String string = this.constPool.getMethodrefType(codeIterator.u16bitAt(n2 + 1));
                n3 += Descriptor.dataSize(string);
                break;
            }
            case 185: {
                String string = this.constPool.getInterfaceMethodrefType(codeIterator.u16bitAt(n2 + 1));
                n3 += Descriptor.dataSize(string) - 1;
                break;
            }
            case 186: {
                String string = this.constPool.getInvokeDynamicType(codeIterator.u16bitAt(n2 + 1));
                n3 += Descriptor.dataSize(string);
                break;
            }
            case 191: {
                n3 = 1;
                break;
            }
            case 197: {
                n3 += 1 - codeIterator.byteAt(n2 + 3);
                break;
            }
            case 196: {
                n = codeIterator.byteAt(n2 + 1);
            }
            default: {
                n3 += STACK_GROW[n];
            }
        }
        return n3;
    }

    private int getFieldSize(CodeIterator codeIterator, int n) {
        String string = this.constPool.getFieldrefType(codeIterator.u16bitAt(n + 1));
        return Descriptor.dataSize(string);
    }
}

