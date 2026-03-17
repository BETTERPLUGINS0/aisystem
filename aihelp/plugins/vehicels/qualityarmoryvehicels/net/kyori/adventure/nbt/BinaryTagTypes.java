/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagScope;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.ByteArrayBinaryTag;
import net.kyori.adventure.nbt.ByteArrayBinaryTagImpl;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTagImpl;
import net.kyori.adventure.nbt.DoubleBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTagImpl;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTagImpl;
import net.kyori.adventure.nbt.LongBinaryTag;
import net.kyori.adventure.nbt.ShortBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.TrackingDataInput;

public final class BinaryTagTypes {
    public static final BinaryTagType<EndBinaryTag> END = BinaryTagType.register(EndBinaryTag.class, (byte)0, dataInput -> EndBinaryTag.endBinaryTag(), null);
    public static final BinaryTagType<ByteBinaryTag> BYTE = BinaryTagType.registerNumeric(ByteBinaryTag.class, (byte)1, dataInput -> ByteBinaryTag.byteBinaryTag(dataInput.readByte()), (byteBinaryTag, dataOutput) -> dataOutput.writeByte(byteBinaryTag.value()));
    public static final BinaryTagType<ShortBinaryTag> SHORT = BinaryTagType.registerNumeric(ShortBinaryTag.class, (byte)2, dataInput -> ShortBinaryTag.shortBinaryTag(dataInput.readShort()), (shortBinaryTag, dataOutput) -> dataOutput.writeShort(shortBinaryTag.value()));
    public static final BinaryTagType<IntBinaryTag> INT = BinaryTagType.registerNumeric(IntBinaryTag.class, (byte)3, dataInput -> IntBinaryTag.intBinaryTag(dataInput.readInt()), (intBinaryTag, dataOutput) -> dataOutput.writeInt(intBinaryTag.value()));
    public static final BinaryTagType<LongBinaryTag> LONG = BinaryTagType.registerNumeric(LongBinaryTag.class, (byte)4, dataInput -> LongBinaryTag.longBinaryTag(dataInput.readLong()), (longBinaryTag, dataOutput) -> dataOutput.writeLong(longBinaryTag.value()));
    public static final BinaryTagType<FloatBinaryTag> FLOAT = BinaryTagType.registerNumeric(FloatBinaryTag.class, (byte)5, dataInput -> FloatBinaryTag.floatBinaryTag(dataInput.readFloat()), (floatBinaryTag, dataOutput) -> dataOutput.writeFloat(floatBinaryTag.value()));
    public static final BinaryTagType<DoubleBinaryTag> DOUBLE = BinaryTagType.registerNumeric(DoubleBinaryTag.class, (byte)6, dataInput -> DoubleBinaryTag.doubleBinaryTag(dataInput.readDouble()), (doubleBinaryTag, dataOutput) -> dataOutput.writeDouble(doubleBinaryTag.value()));
    public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY = BinaryTagType.register(ByteArrayBinaryTag.class, (byte)7, dataInput -> {
        int n = dataInput.readInt();
        try (BinaryTagScope binaryTagScope = TrackingDataInput.enter(dataInput, n);){
            byte[] byArray = new byte[n];
            dataInput.readFully(byArray);
            ByteArrayBinaryTag byteArrayBinaryTag = ByteArrayBinaryTag.byteArrayBinaryTag(byArray);
            return byteArrayBinaryTag;
        }
    }, (byteArrayBinaryTag, dataOutput) -> {
        byte[] byArray = ByteArrayBinaryTagImpl.value(byteArrayBinaryTag);
        dataOutput.writeInt(byArray.length);
        dataOutput.write(byArray);
    });
    public static final BinaryTagType<StringBinaryTag> STRING = BinaryTagType.register(StringBinaryTag.class, (byte)8, dataInput -> StringBinaryTag.stringBinaryTag(dataInput.readUTF()), (stringBinaryTag, dataOutput) -> dataOutput.writeUTF(stringBinaryTag.value()));
    public static final BinaryTagType<ListBinaryTag> LIST = BinaryTagType.register(ListBinaryTag.class, (byte)9, dataInput -> {
        BinaryTagType<BinaryTag> binaryTagType = BinaryTagType.binaryTagType(dataInput.readByte());
        int n = dataInput.readInt();
        try (BinaryTagScope binaryTagScope = TrackingDataInput.enter(dataInput, (long)n * 8L);){
            ArrayList<BinaryTag> arrayList = new ArrayList<BinaryTag>(n);
            for (int i = 0; i < n; ++i) {
                arrayList.add(binaryTagType.read(dataInput));
            }
            ListBinaryTag listBinaryTag = ListBinaryTag.listBinaryTag(binaryTagType, arrayList);
            return listBinaryTag;
        }
    }, (listBinaryTag, dataOutput) -> {
        dataOutput.writeByte(listBinaryTag.elementType().id());
        int n = listBinaryTag.size();
        dataOutput.writeInt(n);
        for (BinaryTag binaryTag : listBinaryTag) {
            BinaryTagType.writeUntyped(binaryTag.type(), binaryTag, dataOutput);
        }
    });
    public static final BinaryTagType<CompoundBinaryTag> COMPOUND = BinaryTagType.register(CompoundBinaryTag.class, (byte)10, dataInput -> {
        try (BinaryTagScope binaryTagScope = TrackingDataInput.enter(dataInput);){
            Object object;
            BinaryTagType<BinaryTag> binaryTagType;
            HashMap<String, BinaryTag> hashMap = new HashMap<String, BinaryTag>();
            while ((binaryTagType = BinaryTagType.binaryTagType(dataInput.readByte())) != END) {
                object = dataInput.readUTF();
                BinaryTag binaryTag = binaryTagType.read(dataInput);
                hashMap.put((String)object, binaryTag);
            }
            object = new CompoundBinaryTagImpl(hashMap);
            return object;
        }
    }, (compoundBinaryTag, dataOutput) -> {
        for (Map.Entry entry : compoundBinaryTag) {
            BinaryTag binaryTag = (BinaryTag)entry.getValue();
            if (binaryTag == null) continue;
            BinaryTagType<? extends BinaryTag> binaryTagType = binaryTag.type();
            dataOutput.writeByte(binaryTagType.id());
            if (binaryTagType == END) continue;
            dataOutput.writeUTF((String)entry.getKey());
            BinaryTagType.writeUntyped(binaryTagType, binaryTag, dataOutput);
        }
        dataOutput.writeByte(END.id());
    });
    public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY = BinaryTagType.register(IntArrayBinaryTag.class, (byte)11, dataInput -> {
        int n = dataInput.readInt();
        try (BinaryTagScope binaryTagScope = TrackingDataInput.enter(dataInput, (long)n * 4L);){
            int[] nArray = new int[n];
            for (int i = 0; i < n; ++i) {
                nArray[i] = dataInput.readInt();
            }
            IntArrayBinaryTag intArrayBinaryTag = IntArrayBinaryTag.intArrayBinaryTag(nArray);
            return intArrayBinaryTag;
        }
    }, (intArrayBinaryTag, dataOutput) -> {
        int[] nArray = IntArrayBinaryTagImpl.value(intArrayBinaryTag);
        int n = nArray.length;
        dataOutput.writeInt(n);
        for (int i = 0; i < n; ++i) {
            dataOutput.writeInt(nArray[i]);
        }
    });
    public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY = BinaryTagType.register(LongArrayBinaryTag.class, (byte)12, dataInput -> {
        int n = dataInput.readInt();
        try (BinaryTagScope binaryTagScope = TrackingDataInput.enter(dataInput, (long)n * 8L);){
            long[] lArray = new long[n];
            for (int i = 0; i < n; ++i) {
                lArray[i] = dataInput.readLong();
            }
            LongArrayBinaryTag longArrayBinaryTag = LongArrayBinaryTag.longArrayBinaryTag(lArray);
            return longArrayBinaryTag;
        }
    }, (longArrayBinaryTag, dataOutput) -> {
        long[] lArray = LongArrayBinaryTagImpl.value(longArrayBinaryTag);
        int n = lArray.length;
        dataOutput.writeInt(n);
        for (int i = 0; i < n; ++i) {
            dataOutput.writeLong(lArray[i]);
        }
    });

    private BinaryTagTypes() {
    }
}

