package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.exceptions.VerificationException;
import java.io.File;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ChecksumCalculator {
   @NotNull
   String calculate(@NotNull File var1) throws VerificationException;
}
