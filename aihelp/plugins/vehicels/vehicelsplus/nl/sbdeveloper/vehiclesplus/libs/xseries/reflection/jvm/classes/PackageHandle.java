/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface PackageHandle {
    @Language(value="RegExp")
    @ApiStatus.Internal
    public static final String JAVA_PACKAGE_PATTERN = "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    @Language(value="RegExp")
    @ApiStatus.Internal
    public static final String JAVA_IDENTIFIER_PATTERN = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";

    @NotNull
    public String packageId();

    @NotNull
    public String getBasePackageName();

    @NotNull
    public String getPackage(@NotNull String var1);
}

