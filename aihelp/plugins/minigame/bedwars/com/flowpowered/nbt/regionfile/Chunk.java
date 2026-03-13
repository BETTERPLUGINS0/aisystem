/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt.regionfile;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import com.flowpowered.nbt.stream.NBTInputStream;
import com.flowpowered.nbt.stream.NBTOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Chunk {
    public final int x;
    public final int z;
    public final int timestamp;
    protected final ByteBuffer data;

    public Chunk(int x, int z, int timestamp, ByteBuffer data) {
        if (x < 0 || z < 0 || x >= 32 || z >= 32) {
            throw new IllegalArgumentException("Coordinates must be in range [0..32), but were x=" + x + ", z=" + z + ")");
        }
        this.x = x;
        this.z = z;
        this.timestamp = timestamp;
        if ((data.capacity() & 0xFFF) != 0) {
            throw new IllegalArgumentException("Data buffer size must be multiple of 4096, but is " + data.capacity());
        }
        this.data = Objects.requireNonNull(data);
    }

    Chunk(int x, int z, int timestamp, FileChannel raf, int start, int length) throws IOException {
        if (x < 0 || z < 0 || x >= 32 || z >= 32) {
            throw new IllegalArgumentException("Coordinates must be in range [0..32), but were x=" + x + ", z=" + z + ")");
        }
        this.x = x;
        this.z = z;
        this.timestamp = timestamp;
        this.data = ByteBuffer.allocate(4096 * length);
        raf.read(this.data, 4096 * start);
        this.data.flip();
    }

    public Chunk(int x, int z, int timestamp, Tag<?> data, byte compression) throws IOException {
        if (x < 0 || z < 0 || x >= 32 || z >= 32) {
            throw new IllegalArgumentException("Coordinates must be in range [0..32), but were x=" + x + ", z=" + z + ")");
        }
        this.x = x;
        this.z = z;
        this.timestamp = timestamp;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
             NBTOutputStream out = new NBTOutputStream(baos, compression);){
            out.writeTag(data);
            out.flush();
            out.close();
            byte[] bytes = baos.toByteArray();
            int sectionLength = (bytes.length + 5) / 4096 + 1;
            this.data = ByteBuffer.allocate(sectionLength * 4096);
            this.data.putInt(bytes.length + 1);
            this.data.put(compression);
            this.data.put(bytes);
            this.data.flip();
        }
    }

    public Chunk(Chunk data, int timestamp) {
        this.x = data.x;
        this.z = data.z;
        this.timestamp = timestamp;
        this.data = data.data;
    }

    public byte getCompression() {
        return this.data.get(4);
    }

    public int getRealLength() {
        return this.data.getInt(0) - 1;
    }

    public int getSectorLength() {
        return (this.getRealLength() + 5) / 4096 + 1;
    }

    public ByteBuffer getData() {
        return this.data;
    }

    public NBTInputStream getInputStream() throws IOException {
        return new NBTInputStream(new ByteArrayInputStream(this.data.array(), 5, this.getRealLength()), this.getCompression());
    }

    public CompoundTag readTag() throws IOException {
        try (NBTInputStream nbtIn = this.getInputStream();){
            CompoundTag compoundTag = new CompoundTag("chunk", ((CompoundTag)nbtIn.readTag()).getValue());
            return compoundTag;
        }
    }

    public static int getCurrentTimestamp() {
        return (int)(System.currentTimeMillis() / 1000L);
    }

    public static int bitsPerIndex(long[] blocks) {
        return blocks.length * 64 / 4096;
    }

    public static long extractFromLong(long[] blocks, int i, int bitsPerIndex) {
        int startByte = bitsPerIndex * i >> 6;
        int endByte = bitsPerIndex * (i + 1) >> 6;
        int startByteBit = bitsPerIndex * i & 0x3F;
        int endByteBit = bitsPerIndex * (i + 1) & 0x3F;
        long blockIndex = startByte == endByte ? blocks[startByte] << 64 - endByteBit >>> 64 + startByteBit - endByteBit : (endByteBit == 0 ? blocks[startByte] >>> startByteBit : blocks[startByte] >>> startByteBit | blocks[endByte] << 64 - endByteBit >>> startByteBit - endByteBit);
        return blockIndex;
    }

    public static void moveChunk(CompoundTag level, int sourceX, int sourceZ, int destX, int destZ) {
        CompoundMap map;
        CompoundMap value = level.getValue();
        int diffX = destX - sourceX << 4;
        int diffY = 0;
        int diffZ = destZ - sourceZ << 4;
        value.put(new IntTag("xPos", destX));
        value.put(new IntTag("zPos", destZ));
        Iterator iterator = ((ListTag)value.get("Entities")).getValue().iterator();
        while (iterator.hasNext()) {
            CompoundTag entity = (CompoundTag)iterator.next();
            Object pos = ((ListTag)entity.getValue().get("Pos")).getValue();
            entity.getValue().put(new ListTag<DoubleTag>("Pos", TagType.TAG_DOUBLE, Arrays.asList(new DoubleTag(null, ((DoubleTag)pos.get(0)).getValue() + (double)diffX), new DoubleTag(null, ((DoubleTag)pos.get(1)).getValue() + (double)diffY), new DoubleTag(null, ((DoubleTag)pos.get(2)).getValue() + (double)diffZ))));
        }
        iterator = ((ListTag)value.get("TileEntities")).getValue().iterator();
        while (iterator.hasNext()) {
            CompoundTag tileEntity = (CompoundTag)iterator.next();
            map = tileEntity.getValue();
            map.put(new IntTag("x", ((IntTag)map.get("x")).getValue() + diffX));
            map.put(new IntTag("y", ((IntTag)map.get("y")).getValue() + diffY));
            map.put(new IntTag("z", ((IntTag)map.get("z")).getValue() + diffZ));
        }
        iterator = ((ListTag)value.get("TileTicks")).getValue().iterator();
        while (iterator.hasNext()) {
            CompoundTag tileTick = (CompoundTag)iterator.next();
            map = tileTick.getValue();
            map.put(new IntTag("x", ((IntTag)map.get("x")).getValue() + diffX));
            map.put(new IntTag("y", ((IntTag)map.get("y")).getValue() + diffY));
            map.put(new IntTag("z", ((IntTag)map.get("z")).getValue() + diffZ));
        }
        iterator = ((ListTag)value.get("LiquidTicks")).getValue().iterator();
        while (iterator.hasNext()) {
            CompoundTag liquidTick = (CompoundTag)iterator.next();
            map = liquidTick.getValue();
            map.put(new IntTag("x", ((IntTag)map.get("x")).getValue() + diffX));
            map.put(new IntTag("y", ((IntTag)map.get("y")).getValue() + diffY));
            map.put(new IntTag("z", ((IntTag)map.get("z")).getValue() + diffZ));
        }
    }
}

