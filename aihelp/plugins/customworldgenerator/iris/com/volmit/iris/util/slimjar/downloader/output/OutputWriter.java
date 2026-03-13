package com.volmit.iris.util.slimjar.downloader.output;

import com.volmit.iris.util.slimjar.exceptions.OutputWriterException;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface OutputWriter {
   @NotNull
   File writeFrom(@NotNull InputStream var1, long var2) throws OutputWriterException;
}
