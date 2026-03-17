/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;

public class BasicBlock {
    protected int position;
    protected int length;
    protected int incoming;
    protected BasicBlock[] exit;
    protected boolean stop;
    protected Catch toCatch;

    protected BasicBlock(int n) {
        this.position = n;
        this.length = 0;
        this.incoming = 0;
    }

    public static BasicBlock find(BasicBlock[] basicBlockArray, int n) {
        for (BasicBlock basicBlock : basicBlockArray) {
            if (basicBlock.position > n || n >= basicBlock.position + basicBlock.length) continue;
            return basicBlock;
        }
        throw new BadBytecode("no basic block at " + n);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String string = this.getClass().getName();
        int n = string.lastIndexOf(46);
        stringBuilder.append(n < 0 ? string : string.substring(n + 1));
        stringBuilder.append('[');
        this.toString2(stringBuilder);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    /*
     * WARNING - void declaration
     */
    protected void toString2(StringBuilder stringBuilder) {
        void var2_4;
        stringBuilder.append("pos=").append(this.position).append(", len=").append(this.length).append(", in=").append(this.incoming).append(", exit{");
        if (this.exit != null) {
            for (BasicBlock basicBlock : this.exit) {
                stringBuilder.append(basicBlock.position).append(',');
            }
        }
        stringBuilder.append("}, {");
        Catch catch_ = this.toCatch;
        while (var2_4 != null) {
            stringBuilder.append('(').append(var2_4.body.position).append(", ").append(var2_4.typeIndex).append("), ");
            Catch catch_2 = var2_4.next;
        }
        stringBuilder.append('}');
    }

    public static class Catch {
        public Catch next;
        public BasicBlock body;
        public int typeIndex;

        Catch(BasicBlock basicBlock, int n, Catch catch_) {
            this.body = basicBlock;
            this.typeIndex = n;
            this.next = catch_;
        }
    }

    public static class Maker {
        protected BasicBlock makeBlock(int n) {
            return new BasicBlock(n);
        }

        protected BasicBlock[] makeArray(int n) {
            return new BasicBlock[n];
        }

        private BasicBlock[] makeArray(BasicBlock basicBlock) {
            BasicBlock[] basicBlockArray = this.makeArray(1);
            basicBlockArray[0] = basicBlock;
            return basicBlockArray;
        }

        private BasicBlock[] makeArray(BasicBlock basicBlock, BasicBlock basicBlock2) {
            BasicBlock[] basicBlockArray = this.makeArray(2);
            basicBlockArray[0] = basicBlock;
            basicBlockArray[1] = basicBlock2;
            return basicBlockArray;
        }

        public BasicBlock[] make(MethodInfo methodInfo) {
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            if (codeAttribute == null) {
                return null;
            }
            CodeIterator codeIterator = codeAttribute.iterator();
            return this.make(codeIterator, 0, codeIterator.getCodeLength(), codeAttribute.getExceptionTable());
        }

        public BasicBlock[] make(CodeIterator codeIterator, int n, int n2, ExceptionTable exceptionTable) {
            Map<Integer, Mark> map = this.makeMarks(codeIterator, n, n2, exceptionTable);
            BasicBlock[] basicBlockArray = this.makeBlocks(map);
            this.addCatchers(basicBlockArray, exceptionTable);
            return basicBlockArray;
        }

        private Mark makeMark(Map<Integer, Mark> map, int n) {
            return this.makeMark0(map, n, true, true);
        }

        private Mark makeMark(Map<Integer, Mark> map, int n, BasicBlock[] basicBlockArray, int n2, boolean bl) {
            Mark mark = this.makeMark0(map, n, false, false);
            mark.setJump(basicBlockArray, n2, bl);
            return mark;
        }

        private Mark makeMark0(Map<Integer, Mark> map, int n, boolean bl, boolean bl2) {
            Integer n2 = n;
            Mark mark = map.get(n2);
            if (mark == null) {
                mark = new Mark(n);
                map.put(n2, mark);
            }
            if (bl) {
                if (mark.block == null) {
                    mark.block = this.makeBlock(n);
                }
                if (bl2) {
                    ++mark.block.incoming;
                }
            }
            return mark;
        }

        private Map<Integer, Mark> makeMarks(CodeIterator codeIterator, int n, int n2, ExceptionTable exceptionTable) {
            int n3;
            codeIterator.begin();
            codeIterator.move(n);
            HashMap<Integer, Mark> hashMap = new HashMap<Integer, Mark>();
            while (codeIterator.hasNext() && (n3 = codeIterator.next()) < n2) {
                int n4 = codeIterator.byteAt(n3);
                if (153 <= n4 && n4 <= 166 || n4 == 198 || n4 == 199) {
                    Mark mark = this.makeMark(hashMap, n3 + codeIterator.s16bitAt(n3 + 1));
                    Mark mark2 = this.makeMark(hashMap, n3 + 3);
                    this.makeMark(hashMap, n3, this.makeArray(mark.block, mark2.block), 3, false);
                    continue;
                }
                if (167 <= n4 && n4 <= 171) {
                    switch (n4) {
                        case 167: {
                            this.makeGoto(hashMap, n3, n3 + codeIterator.s16bitAt(n3 + 1), 3);
                            break;
                        }
                        case 168: {
                            this.makeJsr(hashMap, n3, n3 + codeIterator.s16bitAt(n3 + 1), 3);
                            break;
                        }
                        case 169: {
                            this.makeMark(hashMap, n3, null, 2, true);
                            break;
                        }
                        case 170: {
                            int n5;
                            int n6 = (n3 & 0xFFFFFFFC) + 4;
                            int n7 = codeIterator.s32bitAt(n6 + 4);
                            int n8 = codeIterator.s32bitAt(n6 + 8);
                            int n9 = n8 - n7 + 1;
                            BasicBlock[] basicBlockArray = this.makeArray(n9 + 1);
                            basicBlockArray[0] = this.makeMark(hashMap, (int)(n3 + codeIterator.s32bitAt((int)n6))).block;
                            int n10 = n5 + n9 * 4;
                            int n11 = 1;
                            for (n5 = n6 + 12; n5 < n10; n5 += 4) {
                                basicBlockArray[n11++] = this.makeMark(hashMap, (int)(n3 + codeIterator.s32bitAt((int)n5))).block;
                            }
                            this.makeMark(hashMap, n3, basicBlockArray, n10 - n3, true);
                            break;
                        }
                        case 171: {
                            int n9;
                            int n12 = (n3 & 0xFFFFFFFC) + 4;
                            int n13 = codeIterator.s32bitAt(n12 + 4);
                            BasicBlock[] basicBlockArray = this.makeArray(n13 + 1);
                            basicBlockArray[0] = this.makeMark(hashMap, (int)(n3 + codeIterator.s32bitAt((int)n12))).block;
                            int n14 = n9 + n13 * 8 - 4;
                            int n5 = 1;
                            for (n9 = n12 + 8 + 4; n9 < n14; n9 += 8) {
                                basicBlockArray[n5++] = this.makeMark(hashMap, (int)(n3 + codeIterator.s32bitAt((int)n9))).block;
                            }
                            this.makeMark(hashMap, n3, basicBlockArray, n14 - n3, true);
                            break;
                        }
                    }
                    continue;
                }
                if (172 <= n4 && n4 <= 177 || n4 == 191) {
                    this.makeMark(hashMap, n3, null, 1, true);
                    continue;
                }
                if (n4 == 200) {
                    this.makeGoto(hashMap, n3, n3 + codeIterator.s32bitAt(n3 + 1), 5);
                    continue;
                }
                if (n4 == 201) {
                    this.makeJsr(hashMap, n3, n3 + codeIterator.s32bitAt(n3 + 1), 5);
                    continue;
                }
                if (n4 != 196 || codeIterator.byteAt(n3 + 1) != 169) continue;
                this.makeMark(hashMap, n3, null, 4, true);
            }
            if (exceptionTable != null) {
                n3 = exceptionTable.size();
                while (--n3 >= 0) {
                    this.makeMark0(hashMap, exceptionTable.startPc(n3), true, false);
                    this.makeMark(hashMap, exceptionTable.handlerPc(n3));
                }
            }
            return hashMap;
        }

        private void makeGoto(Map<Integer, Mark> map, int n, int n2, int n3) {
            Mark mark = this.makeMark(map, n2);
            BasicBlock[] basicBlockArray = this.makeArray(mark.block);
            this.makeMark(map, n, basicBlockArray, n3, true);
        }

        protected void makeJsr(Map<Integer, Mark> map, int n, int n2, int n3) {
            throw new JsrBytecode();
        }

        private BasicBlock[] makeBlocks(Map<Integer, Mark> map) {
            Object[] objectArray = map.values().toArray(new Mark[map.size()]);
            Arrays.sort(objectArray);
            ArrayList<BasicBlock> arrayList = new ArrayList<BasicBlock>();
            int n = 0;
            BasicBlock basicBlock = objectArray.length > 0 && ((Mark)objectArray[0]).position == 0 && ((Mark)objectArray[0]).block != null ? Maker.getBBlock((Mark)objectArray[n++]) : this.makeBlock(0);
            arrayList.add(basicBlock);
            while (n < objectArray.length) {
                Object object;
                BasicBlock basicBlock2;
                if ((basicBlock2 = Maker.getBBlock((Mark)(object = objectArray[n++]))) == null) {
                    if (basicBlock.length > 0) {
                        basicBlock = this.makeBlock(basicBlock.position + basicBlock.length);
                        arrayList.add(basicBlock);
                    }
                    basicBlock.length = ((Mark)object).position + ((Mark)object).size - basicBlock.position;
                    basicBlock.exit = ((Mark)object).jump;
                    basicBlock.stop = ((Mark)object).alwaysJmp;
                    continue;
                }
                if (basicBlock.length == 0) {
                    basicBlock.length = ((Mark)object).position - basicBlock.position;
                    ++basicBlock2.incoming;
                    basicBlock.exit = this.makeArray(basicBlock2);
                } else if (basicBlock.position + basicBlock.length < ((Mark)object).position) {
                    basicBlock = this.makeBlock(basicBlock.position + basicBlock.length);
                    arrayList.add(basicBlock);
                    basicBlock.length = ((Mark)object).position - basicBlock.position;
                    basicBlock.stop = true;
                    basicBlock.exit = this.makeArray(basicBlock2);
                }
                arrayList.add(basicBlock2);
                basicBlock = basicBlock2;
            }
            return arrayList.toArray(this.makeArray(arrayList.size()));
        }

        private static BasicBlock getBBlock(Mark mark) {
            BasicBlock basicBlock = mark.block;
            if (basicBlock != null && mark.size > 0) {
                basicBlock.exit = mark.jump;
                basicBlock.length = mark.size;
                basicBlock.stop = mark.alwaysJmp;
            }
            return basicBlock;
        }

        private void addCatchers(BasicBlock[] basicBlockArray, ExceptionTable exceptionTable) {
            if (exceptionTable == null) {
                return;
            }
            int n = exceptionTable.size();
            while (--n >= 0) {
                BasicBlock basicBlock = BasicBlock.find(basicBlockArray, exceptionTable.handlerPc(n));
                int n2 = exceptionTable.startPc(n);
                int n3 = exceptionTable.endPc(n);
                int n4 = exceptionTable.catchType(n);
                --basicBlock.incoming;
                for (int i = 0; i < basicBlockArray.length; ++i) {
                    BasicBlock basicBlock2 = basicBlockArray[i];
                    int n5 = basicBlock2.position;
                    if (n2 > n5 || n5 >= n3) continue;
                    basicBlock2.toCatch = new Catch(basicBlock, n4, basicBlock2.toCatch);
                    ++basicBlock.incoming;
                }
            }
        }
    }

    static class Mark
    implements Comparable<Mark> {
        int position;
        BasicBlock block;
        BasicBlock[] jump;
        boolean alwaysJmp;
        int size;
        Catch catcher;

        Mark(int n) {
            this.position = n;
            this.block = null;
            this.jump = null;
            this.alwaysJmp = false;
            this.size = 0;
            this.catcher = null;
        }

        @Override
        public int compareTo(Mark mark) {
            if (null == mark) {
                return -1;
            }
            return this.position - mark.position;
        }

        void setJump(BasicBlock[] basicBlockArray, int n, boolean bl) {
            this.jump = basicBlockArray;
            this.size = n;
            this.alwaysJmp = bl;
        }
    }

    static class JsrBytecode
    extends BadBytecode {
        private static final long serialVersionUID = 1L;

        JsrBytecode() {
            super("JSR");
        }
    }
}

