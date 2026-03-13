/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt.regionfile;

import com.flowpowered.nbt.regionfile.Chunk;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RegionFile
implements Closeable {
    protected final Path file;
    protected FileChannel raf;
    protected ByteBuffer locations;
    protected IntBuffer locations2;
    protected ByteBuffer timestamps;
    protected IntBuffer timestamps2;

    public RegionFile(Path file) throws IOException {
        this.file = Objects.requireNonNull(file);
        if (!Files.exists(file, new LinkOption[0])) {
            throw new NoSuchFileException(file.toString());
        }
        if (Files.size(file) < 8192L) {
            throw new IllegalArgumentException("File size must be at least 4kiB, is this file corrupt?");
        }
        this.raf = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE);
        this.locations = ByteBuffer.allocate(4096);
        this.raf.read(this.locations);
        this.locations.flip();
        this.locations2 = this.locations.asIntBuffer();
        this.timestamps = ByteBuffer.allocate(4096);
        this.raf.read(this.timestamps);
        this.timestamps.flip();
        this.timestamps2 = this.timestamps.asIntBuffer();
    }

    public Chunk loadChunk(int x, int z) throws IOException {
        return this.loadChunk(RegionFile.coordsToPosition(x, z));
    }

    public Chunk loadChunk(int i) throws IOException {
        int chunkPos = this.locations2.get(i) >>> 8;
        int chunkLength = this.locations2.get(i) & 0xFF;
        if (chunkPos > 0) {
            return new Chunk(i & 0x1F, i >> 5, this.timestamps2.get(i), this.raf, chunkPos, chunkLength);
        }
        return null;
    }

    public boolean hasChunk(int x, int z) {
        return this.hasChunk(x & 0x1F | z << 5);
    }

    public boolean hasChunk(int i) {
        return this.locations2.get(i) >>> 8 > 0;
    }

    public Stream<Integer> streamChunks() {
        return IntStream.range(0, 1024).filter(pos -> this.hasChunk(pos)).boxed().sorted(Comparator.comparingInt(i -> this.locations2.get((int)i) >>> 8));
    }

    public List<Integer> listChunks() {
        return this.streamChunks().collect(Collectors.toList());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void writeChunks(HashMap<Integer, Chunk> changedChunks) throws IOException {
        FileChannel fileChannel = this.raf;
        synchronized (fileChannel) {
            BitSet usedSectors = new BitSet();
            usedSectors.set(0, 2);
            for (int i = 0; i < 1024; ++i) {
                int chunkPos = this.locations2.get(i) >>> 8;
                int chunkLength = this.locations2.get(i) & 0xFF;
                if (chunkLength <= 0 || changedChunks.containsKey(i)) continue;
                usedSectors.set(chunkPos, chunkPos + chunkLength);
            }
            for (Integer chunkPos : changedChunks.keySet()) {
                Chunk chunk = changedChunks.get(chunkPos);
                if (chunk == null) {
                    this.locations2.put(chunkPos, 0);
                    continue;
                }
                int length = 0;
                int start = 0;
                while (length < chunk.getSectorLength()) {
                    if (!usedSectors.get(start + length)) {
                        ++length;
                        continue;
                    }
                    start = usedSectors.nextClearBit(start + length);
                    length = 0;
                }
                if (length > 255) {
                    throw new IOException("Chunks are limited to a length of maximum 255 sectors, or ~1MiB");
                }
                this.raf.position(start * 4096);
                this.raf.write(chunk.data);
                this.timestamps2.put(chunkPos, chunk.timestamp);
                this.locations2.put(chunkPos, start << 8 | length);
                usedSectors.set(start, start + length);
            }
            this.raf.position(0L);
            this.raf.write(this.locations);
            this.raf.write(this.timestamps);
            this.locations.flip();
            this.timestamps.flip();
            this.raf.truncate(4096 * usedSectors.previousSetBit(usedSectors.size()) + 4096);
        }
        changedChunks.clear();
    }

    public Path getPath() {
        return this.file;
    }

    @Override
    public void close() throws IOException {
        this.raf.close();
    }

    public static RegionFile createNew(Path file) throws IOException {
        try (FileChannel raf = FileChannel.open(file, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);){
            raf.write(ByteBuffer.wrap(new byte[8192]));
        }
        return new RegionFile(file);
    }

    public static RegionFile open(Path file) throws IOException {
        if (Files.exists(file, new LinkOption[0])) {
            return new RegionFile(file);
        }
        Files.createDirectories(file.getParent(), new FileAttribute[0]);
        return RegionFile.createNew(file);
    }

    public static int coordsToPosition(int x, int z) {
        return x & 0x1F | (z & 0x1F) << 5;
    }
}

