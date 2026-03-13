/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt.stream;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.EndTag;
import com.flowpowered.nbt.FloatTag;
import com.flowpowered.nbt.IntArrayTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.LongArrayTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.ShortArrayTag;
import com.flowpowered.nbt.ShortTag;
import com.flowpowered.nbt.StringTag;
import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import com.flowpowered.nbt.stream.LittleEndianInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public final class NBTInputStream
implements Closeable {
    public static final int NO_COMPRESSION = 0;
    public static final int GZIP_COMPRESSION = 1;
    public static final int ZLIB_COMPRESSION = 2;
    private final DataInput dataIn;
    private final InputStream inputStream;

    public NBTInputStream(InputStream is) throws IOException {
        this(is, 1, ByteOrder.BIG_ENDIAN);
    }

    public NBTInputStream(InputStream is, int compression) throws IOException {
        this(is, compression, ByteOrder.BIG_ENDIAN);
    }

    public NBTInputStream(InputStream is, int compression, ByteOrder endianness) throws IOException {
        switch (compression) {
            case 0: {
                break;
            }
            case 1: {
                is = new GZIPInputStream(is);
                break;
            }
            case 2: {
                is = new InflaterInputStream(is);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported compression type, must be between 0 and 2 (inclusive)");
            }
        }
        if (endianness == ByteOrder.LITTLE_ENDIAN) {
            this.dataIn = new LittleEndianInputStream(is);
            this.inputStream = (InputStream)((Object)this.dataIn);
        } else {
            this.dataIn = new DataInputStream(is);
            this.inputStream = (InputStream)((Object)this.dataIn);
        }
    }

    public Tag<?> readTag() throws IOException {
        return this.readTag(0);
    }

    private Tag<?> readTag(int depth) throws IOException {
        int typeId = this.dataIn.readByte() & 0xFF;
        TagType type = TagType.getById(typeId);
        String name = type != TagType.TAG_END ? this.dataIn.readUTF() : "";
        return this.readTagPayload(type, name, depth);
    }

    private Tag readTagPayload(TagType type, String name, int depth) throws IOException {
        switch (type) {
            case TAG_END: {
                if (depth == 0) {
                    throw new IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
                }
                return new EndTag();
            }
            case TAG_BYTE: {
                return new ByteTag(name, this.dataIn.readByte());
            }
            case TAG_SHORT: {
                return new ShortTag(name, this.dataIn.readShort());
            }
            case TAG_INT: {
                return new IntTag(name, this.dataIn.readInt());
            }
            case TAG_LONG: {
                return new LongTag(name, this.dataIn.readLong());
            }
            case TAG_FLOAT: {
                return new FloatTag(name, this.dataIn.readFloat());
            }
            case TAG_DOUBLE: {
                return new DoubleTag(name, this.dataIn.readDouble());
            }
            case TAG_BYTE_ARRAY: {
                int length = this.dataIn.readInt();
                byte[] bytes = new byte[length];
                this.dataIn.readFully(bytes);
                return new ByteArrayTag(name, bytes);
            }
            case TAG_STRING: {
                return new StringTag(name, this.dataIn.readUTF());
            }
            case TAG_LIST: {
                TagType childType = TagType.getById(this.dataIn.readByte());
                int length = this.dataIn.readInt();
                Class<Tag<?>> clazz = childType.getTagClass();
                ArrayList<Tag> tagList = new ArrayList<Tag>(length);
                for (int i = 0; i < length; ++i) {
                    Tag tag = this.readTagPayload(childType, "", depth + 1);
                    if (tag instanceof EndTag) {
                        throw new IOException("TAG_End not permitted in a list.");
                    }
                    if (!clazz.isInstance(tag)) {
                        throw new IOException("Mixed tag types within a list.");
                    }
                    tagList.add(tag);
                }
                return new ListTag(name, childType, tagList);
            }
            case TAG_COMPOUND: {
                Tag<?> tag;
                CompoundMap compoundTagList = new CompoundMap();
                while (!((tag = this.readTag(depth + 1)) instanceof EndTag)) {
                    compoundTagList.put(tag);
                }
                return new CompoundTag(name, compoundTagList);
            }
            case TAG_INT_ARRAY: {
                int length = this.dataIn.readInt();
                int[] ints = new int[length];
                for (int i = 0; i < length; ++i) {
                    ints[i] = this.dataIn.readInt();
                }
                return new IntArrayTag(name, ints);
            }
            case TAG_LONG_ARRAY: {
                int length = this.dataIn.readInt();
                long[] longs = new long[length];
                for (int i = 0; i < length; ++i) {
                    longs[i] = this.dataIn.readLong();
                }
                return new LongArrayTag(name, longs);
            }
            case TAG_SHORT_ARRAY: {
                int length = this.dataIn.readInt();
                short[] shorts = new short[length];
                for (int i = 0; i < length; ++i) {
                    shorts[i] = this.dataIn.readShort();
                }
                return new ShortArrayTag(name, shorts);
            }
        }
        throw new IOException("Invalid tag type: " + (Object)((Object)type) + ".");
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}

