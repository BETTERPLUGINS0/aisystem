/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.commons.io.IOUtils;

public class RandomAccessFiles {
    public static byte[] read(RandomAccessFile input, long position, int length) throws IOException {
        input.seek(position);
        return IOUtils.toByteArray(input::read, length);
    }
}

