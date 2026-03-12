package fr.xephi.authme.libs.net.kyori.adventure.key;

import org.jetbrains.annotations.NotNull;

public interface Namespaced {
   @NotNull
   @KeyPattern.Namespace
   String namespace();
}
