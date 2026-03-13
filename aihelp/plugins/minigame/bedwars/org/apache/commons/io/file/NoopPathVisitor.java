/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import org.apache.commons.io.file.SimplePathVisitor;
import org.apache.commons.io.function.IOBiFunction;

public class NoopPathVisitor
extends SimplePathVisitor {
    public static final NoopPathVisitor INSTANCE = new NoopPathVisitor();

    public NoopPathVisitor() {
    }

    public NoopPathVisitor(IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        super(visitFileFailed);
    }
}

