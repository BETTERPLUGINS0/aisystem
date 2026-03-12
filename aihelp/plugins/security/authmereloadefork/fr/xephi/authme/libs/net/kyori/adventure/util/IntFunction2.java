package fr.xephi.authme.libs.net.kyori.adventure.util;

@FunctionalInterface
public interface IntFunction2<R> {
   R apply(final int first, final int second);
}
