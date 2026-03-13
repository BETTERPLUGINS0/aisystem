package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.exceptions.VerificationException;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface DependencyVerifier {
   boolean verify(@NotNull File var1, @NotNull Dependency var2) throws VerificationException;

   @NotNull
   Optional<File> getChecksumFile(@NotNull Dependency var1);
}
