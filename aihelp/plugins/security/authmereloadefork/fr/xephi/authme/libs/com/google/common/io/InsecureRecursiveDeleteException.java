package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.nio.file.FileSystemException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class InsecureRecursiveDeleteException extends FileSystemException {
   public InsecureRecursiveDeleteException(@CheckForNull String file) {
      super(file, (String)null, "unable to guarantee security of recursive delete");
   }
}
