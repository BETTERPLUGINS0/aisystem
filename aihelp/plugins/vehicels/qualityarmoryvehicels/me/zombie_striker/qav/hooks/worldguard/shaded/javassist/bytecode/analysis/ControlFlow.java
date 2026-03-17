/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import java.util.ArrayList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Analyzer;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Frame;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.BasicBlock;

public class ControlFlow {
    private CtClass clazz;
    private MethodInfo methodInfo;
    private Block[] basicBlocks;
    private Frame[] frames;

    public ControlFlow(CtMethod ctMethod) {
        this(ctMethod.getDeclaringClass(), ctMethod.getMethodInfo2());
    }

    public ControlFlow(CtClass ctClass, MethodInfo methodInfo) {
        Block block;
        int n;
        this.clazz = ctClass;
        this.methodInfo = methodInfo;
        this.frames = null;
        this.basicBlocks = (Block[])new BasicBlock.Maker(){

            @Override
            protected BasicBlock makeBlock(int n) {
                return new Block(n, ControlFlow.this.methodInfo);
            }

            @Override
            protected BasicBlock[] makeArray(int n) {
                return new Block[n];
            }
        }.make(methodInfo);
        if (this.basicBlocks == null) {
            this.basicBlocks = new Block[0];
        }
        int n2 = this.basicBlocks.length;
        int[] nArray = new int[n2];
        for (n = 0; n < n2; ++n) {
            block = this.basicBlocks[n];
            block.index = n;
            block.entrances = new Block[block.incomings()];
            nArray[n] = 0;
        }
        for (n = 0; n < n2; ++n) {
            block = this.basicBlocks[n];
            for (int i = 0; i < block.exits(); ++i) {
                Block block2 = block.exit(i);
                int n3 = block2.index;
                int n4 = nArray[n3];
                nArray[n3] = n4 + 1;
                block2.entrances[n4] = block;
            }
            Catcher[] catcherArray = block.catchers();
            for (int i = 0; i < catcherArray.length; ++i) {
                Block block3 = catcherArray[i].node;
                int n5 = block3.index;
                int n6 = nArray[n5];
                nArray[n5] = n6 + 1;
                block3.entrances[n6] = block;
            }
        }
    }

    public Block[] basicBlocks() {
        return this.basicBlocks;
    }

    public Frame frameAt(int n) {
        if (this.frames == null) {
            this.frames = new Analyzer().analyze(this.clazz, this.methodInfo);
        }
        return this.frames[n];
    }

    public Node[] dominatorTree() {
        int n = this.basicBlocks.length;
        if (n == 0) {
            return null;
        }
        Node[] nodeArray = new Node[n];
        boolean[] blArray = new boolean[n];
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            nodeArray[i] = new Node(this.basicBlocks[i]);
            blArray[i] = false;
        }
        Access access = new Access(nodeArray){

            @Override
            BasicBlock[] exits(Node node) {
                return node.block.getExit();
            }

            @Override
            BasicBlock[] entrances(Node node) {
                return ((Node)node).block.entrances;
            }
        };
        nodeArray[0].makeDepth1stTree(null, blArray, 0, nArray, access);
        do {
            for (int i = 0; i < n; ++i) {
                blArray[i] = false;
            }
        } while (nodeArray[0].makeDominatorTree(blArray, nArray, access));
        Node.setChildren(nodeArray);
        return nodeArray;
    }

    public Node[] postDominatorTree() {
        int n;
        int n2 = this.basicBlocks.length;
        if (n2 == 0) {
            return null;
        }
        Node[] nodeArray = new Node[n2];
        boolean[] blArray = new boolean[n2];
        int[] nArray = new int[n2];
        for (int i = 0; i < n2; ++i) {
            nodeArray[i] = new Node(this.basicBlocks[i]);
            blArray[i] = false;
        }
        Access access = new Access(nodeArray){

            @Override
            BasicBlock[] exits(Node node) {
                return ((Node)node).block.entrances;
            }

            @Override
            BasicBlock[] entrances(Node node) {
                return node.block.getExit();
            }
        };
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            if (nodeArray[n].block.exits() != 0) continue;
            n3 = nodeArray[n].makeDepth1stTree(null, blArray, n3, nArray, access);
        }
        do {
            int n4;
            for (n4 = 0; n4 < n2; ++n4) {
                blArray[n4] = false;
            }
            n = 0;
            for (n4 = 0; n4 < n2; ++n4) {
                if (nodeArray[n4].block.exits() != 0 || !nodeArray[n4].makeDominatorTree(blArray, nArray, access)) continue;
                n = 1;
            }
        } while (n != 0);
        Node.setChildren(nodeArray);
        return nodeArray;
    }

    public static class Block
    extends BasicBlock {
        public Object clientData = null;
        int index;
        MethodInfo method;
        Block[] entrances;

        Block(int n, MethodInfo methodInfo) {
            super(n);
            this.method = methodInfo;
        }

        @Override
        protected void toString2(StringBuilder stringBuilder) {
            super.toString2(stringBuilder);
            stringBuilder.append(", incoming{");
            for (int i = 0; i < this.entrances.length; ++i) {
                stringBuilder.append(this.entrances[i].position).append(", ");
            }
            stringBuilder.append('}');
        }

        BasicBlock[] getExit() {
            return this.exit;
        }

        public int index() {
            return this.index;
        }

        public int position() {
            return this.position;
        }

        public int length() {
            return this.length;
        }

        public int incomings() {
            return this.incoming;
        }

        public Block incoming(int n) {
            return this.entrances[n];
        }

        public int exits() {
            return this.exit == null ? 0 : this.exit.length;
        }

        public Block exit(int n) {
            return (Block)this.exit[n];
        }

        public Catcher[] catchers() {
            ArrayList<Catcher> arrayList = new ArrayList<Catcher>();
            BasicBlock.Catch catch_ = this.toCatch;
            while (catch_ != null) {
                arrayList.add(new Catcher(catch_));
                catch_ = catch_.next;
            }
            return arrayList.toArray(new Catcher[arrayList.size()]);
        }
    }

    public static class Catcher {
        private Block node;
        private int typeIndex;

        Catcher(BasicBlock.Catch catch_) {
            this.node = (Block)catch_.body;
            this.typeIndex = catch_.typeIndex;
        }

        public Block block() {
            return this.node;
        }

        public String type() {
            if (this.typeIndex == 0) {
                return "java.lang.Throwable";
            }
            return this.node.method.getConstPool().getClassInfo(this.typeIndex);
        }
    }

    public static class Node {
        private Block block;
        private Node parent;
        private Node[] children;

        Node(Block block) {
            this.block = block;
            this.parent = null;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Node[pos=").append(this.block().position());
            stringBuilder.append(", parent=");
            stringBuilder.append(this.parent == null ? "*" : Integer.toString(this.parent.block().position()));
            stringBuilder.append(", children{");
            for (int i = 0; i < this.children.length; ++i) {
                stringBuilder.append(this.children[i].block().position()).append(", ");
            }
            stringBuilder.append("}]");
            return stringBuilder.toString();
        }

        public Block block() {
            return this.block;
        }

        public Node parent() {
            return this.parent;
        }

        public int children() {
            return this.children.length;
        }

        public Node child(int n) {
            return this.children[n];
        }

        int makeDepth1stTree(Node node, boolean[] blArray, int n, int[] nArray, Access access) {
            int n2 = this.block.index;
            if (blArray[n2]) {
                return n;
            }
            blArray[n2] = true;
            this.parent = node;
            BasicBlock[] basicBlockArray = access.exits(this);
            if (basicBlockArray != null) {
                for (int i = 0; i < basicBlockArray.length; ++i) {
                    Node node2 = access.node(basicBlockArray[i]);
                    n = node2.makeDepth1stTree(this, blArray, n, nArray, access);
                }
            }
            nArray[n2] = n++;
            return n;
        }

        boolean makeDominatorTree(boolean[] blArray, int[] nArray, Access access) {
            BasicBlock[] basicBlockArray;
            int n = this.block.index;
            if (blArray[n]) {
                return false;
            }
            blArray[n] = true;
            boolean bl = false;
            BasicBlock[] basicBlockArray2 = access.exits(this);
            if (basicBlockArray2 != null) {
                for (int i = 0; i < basicBlockArray2.length; ++i) {
                    Node node = access.node(basicBlockArray2[i]);
                    if (!node.makeDominatorTree(blArray, nArray, access)) continue;
                    bl = true;
                }
            }
            if ((basicBlockArray = access.entrances(this)) != null) {
                for (int i = 0; i < basicBlockArray.length; ++i) {
                    Node node;
                    if (this.parent == null || (node = Node.getAncestor(this.parent, access.node(basicBlockArray[i]), nArray)) == this.parent) continue;
                    this.parent = node;
                    bl = true;
                }
            }
            return bl;
        }

        private static Node getAncestor(Node node, Node node2, int[] nArray) {
            while (node != node2) {
                if (nArray[node.block.index] < nArray[node2.block.index]) {
                    node = node.parent;
                } else {
                    node2 = node2.parent;
                }
                if (node != null && node2 != null) continue;
                return null;
            }
            return node;
        }

        private static void setChildren(Node[] nodeArray) {
            Node node;
            int n;
            int n2 = nodeArray.length;
            int[] nArray = new int[n2];
            for (n = 0; n < n2; ++n) {
                nArray[n] = 0;
            }
            for (n = 0; n < n2; ++n) {
                node = nodeArray[n].parent;
                if (node == null) continue;
                int n3 = node.block.index;
                nArray[n3] = nArray[n3] + 1;
            }
            for (n = 0; n < n2; ++n) {
                nodeArray[n].children = new Node[nArray[n]];
            }
            for (n = 0; n < n2; ++n) {
                nArray[n] = 0;
            }
            for (n = 0; n < n2; ++n) {
                node = nodeArray[n];
                Node node2 = node.parent;
                if (node2 == null) continue;
                int n4 = node2.block.index;
                int n5 = nArray[n4];
                nArray[n4] = n5 + 1;
                node2.children[n5] = node;
            }
        }
    }

    static abstract class Access {
        Node[] all;

        Access(Node[] nodeArray) {
            this.all = nodeArray;
        }

        Node node(BasicBlock basicBlock) {
            return this.all[((Block)basicBlock).index];
        }

        abstract BasicBlock[] exits(Node var1);

        abstract BasicBlock[] entrances(Node var1);
    }
}

