package fr.xephi.authme.libs.ch.jalu.configme.properties.inlinearray;

public interface InlineArrayConverter<T> {
   T[] fromString(String var1);

   String toExportValue(T[] var1);
}
