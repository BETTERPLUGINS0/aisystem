package fr.xephi.authme.libs.com.google.protobuf;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

final class MessageLiteToString {
   private static final String LIST_SUFFIX = "List";
   private static final String BUILDER_LIST_SUFFIX = "OrBuilderList";
   private static final String MAP_SUFFIX = "Map";
   private static final String BYTES_SUFFIX = "Bytes";
   private static final char[] INDENT_BUFFER = new char[80];

   private MessageLiteToString() {
   }

   static String toString(MessageLite messageLite, String commentString) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("# ").append(commentString);
      reflectivePrintWithIndent(messageLite, buffer, 0);
      return buffer.toString();
   }

   private static void reflectivePrintWithIndent(MessageLite messageLite, StringBuilder buffer, int indent) {
      Set<String> setters = new HashSet();
      Map<String, java.lang.reflect.Method> hazzers = new HashMap();
      Map<String, java.lang.reflect.Method> getters = new TreeMap();
      java.lang.reflect.Method[] var6 = messageLite.getClass().getDeclaredMethods();
      int var7 = var6.length;

      java.lang.reflect.Method getMethod;
      for(int var8 = 0; var8 < var7; ++var8) {
         getMethod = var6[var8];
         if (!Modifier.isStatic(getMethod.getModifiers()) && getMethod.getName().length() >= 3) {
            if (getMethod.getName().startsWith("set")) {
               setters.add(getMethod.getName());
            } else if (Modifier.isPublic(getMethod.getModifiers()) && getMethod.getParameterTypes().length == 0) {
               if (getMethod.getName().startsWith("has")) {
                  hazzers.put(getMethod.getName(), getMethod);
               } else if (getMethod.getName().startsWith("get")) {
                  getters.put(getMethod.getName(), getMethod);
               }
            }
         }
      }

      Iterator iter = getters.entrySet().iterator();

      while(true) {
         Entry getter;
         while(iter.hasNext()) {
            getter = (Entry)iter.next();
            String suffix = ((String)getter.getKey()).substring(3);
            if (suffix.endsWith("List") && !suffix.endsWith("OrBuilderList") && !suffix.equals("List")) {
               getMethod = (java.lang.reflect.Method)getter.getValue();
               if (getMethod != null && getMethod.getReturnType().equals(List.class)) {
                  printField(buffer, indent, suffix.substring(0, suffix.length() - "List".length()), GeneratedMessageLite.invokeOrDie(getMethod, messageLite));
                  continue;
               }
            }

            if (suffix.endsWith("Map") && !suffix.equals("Map")) {
               getMethod = (java.lang.reflect.Method)getter.getValue();
               if (getMethod != null && getMethod.getReturnType().equals(Map.class) && !getMethod.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(getMethod.getModifiers())) {
                  printField(buffer, indent, suffix.substring(0, suffix.length() - "Map".length()), GeneratedMessageLite.invokeOrDie(getMethod, messageLite));
                  continue;
               }
            }

            if (setters.contains("set" + suffix) && (!suffix.endsWith("Bytes") || !getters.containsKey("get" + suffix.substring(0, suffix.length() - "Bytes".length())))) {
               getMethod = (java.lang.reflect.Method)getter.getValue();
               java.lang.reflect.Method hasMethod = (java.lang.reflect.Method)hazzers.get("has" + suffix);
               if (getMethod != null) {
                  Object value = GeneratedMessageLite.invokeOrDie(getMethod, messageLite);
                  boolean hasValue = hasMethod == null ? !isDefaultValue(value) : (Boolean)GeneratedMessageLite.invokeOrDie(hasMethod, messageLite);
                  if (hasValue) {
                     printField(buffer, indent, suffix, value);
                  }
               }
            }
         }

         if (messageLite instanceof GeneratedMessageLite.ExtendableMessage) {
            iter = ((GeneratedMessageLite.ExtendableMessage)messageLite).extensions.iterator();

            while(iter.hasNext()) {
               getter = (Entry)iter.next();
               printField(buffer, indent, "[" + ((GeneratedMessageLite.ExtensionDescriptor)getter.getKey()).getNumber() + "]", getter.getValue());
            }
         }

         if (((GeneratedMessageLite)messageLite).unknownFields != null) {
            ((GeneratedMessageLite)messageLite).unknownFields.printWithIndent(buffer, indent);
         }

         return;
      }
   }

   private static boolean isDefaultValue(Object o) {
      if (o instanceof Boolean) {
         return !(Boolean)o;
      } else if (o instanceof Integer) {
         return (Integer)o == 0;
      } else if (o instanceof Float) {
         return Float.floatToRawIntBits((Float)o) == 0;
      } else if (o instanceof Double) {
         return Double.doubleToRawLongBits((Double)o) == 0L;
      } else if (o instanceof String) {
         return o.equals("");
      } else if (o instanceof ByteString) {
         return o.equals(ByteString.EMPTY);
      } else if (o instanceof MessageLite) {
         return o == ((MessageLite)o).getDefaultInstanceForType();
      } else if (o instanceof java.lang.Enum) {
         return ((java.lang.Enum)o).ordinal() == 0;
      } else {
         return false;
      }
   }

   static void printField(StringBuilder buffer, int indent, String name, Object object) {
      Iterator var5;
      if (object instanceof List) {
         List<?> list = (List)object;
         var5 = list.iterator();

         while(var5.hasNext()) {
            Object entry = var5.next();
            printField(buffer, indent, name, entry);
         }

      } else if (!(object instanceof Map)) {
         buffer.append('\n');
         indent(indent, buffer);
         buffer.append(pascalCaseToSnakeCase(name));
         if (object instanceof String) {
            buffer.append(": \"").append(TextFormatEscaper.escapeText((String)object)).append('"');
         } else if (object instanceof ByteString) {
            buffer.append(": \"").append(TextFormatEscaper.escapeBytes((ByteString)object)).append('"');
         } else if (object instanceof GeneratedMessageLite) {
            buffer.append(" {");
            reflectivePrintWithIndent((GeneratedMessageLite)object, buffer, indent + 2);
            buffer.append("\n");
            indent(indent, buffer);
            buffer.append("}");
         } else if (object instanceof Entry) {
            buffer.append(" {");
            Entry<?, ?> entry = (Entry)object;
            printField(buffer, indent + 2, "key", entry.getKey());
            printField(buffer, indent + 2, "value", entry.getValue());
            buffer.append("\n");
            indent(indent, buffer);
            buffer.append("}");
         } else {
            buffer.append(": ").append(object);
         }

      } else {
         Map<?, ?> map = (Map)object;
         var5 = map.entrySet().iterator();

         while(var5.hasNext()) {
            Entry<?, ?> entry = (Entry)var5.next();
            printField(buffer, indent, name, entry);
         }

      }
   }

   private static void indent(int indent, StringBuilder buffer) {
      while(indent > 0) {
         int partialIndent = indent;
         if (indent > INDENT_BUFFER.length) {
            partialIndent = INDENT_BUFFER.length;
         }

         buffer.append(INDENT_BUFFER, 0, partialIndent);
         indent -= partialIndent;
      }

   }

   private static String pascalCaseToSnakeCase(String pascalCase) {
      if (pascalCase.isEmpty()) {
         return pascalCase;
      } else {
         StringBuilder builder = new StringBuilder();
         builder.append(Character.toLowerCase(pascalCase.charAt(0)));

         for(int i = 1; i < pascalCase.length(); ++i) {
            char ch = pascalCase.charAt(i);
            if (Character.isUpperCase(ch)) {
               builder.append("_");
            }

            builder.append(Character.toLowerCase(ch));
         }

         return builder.toString();
      }
   }

   static {
      Arrays.fill(INDENT_BUFFER, ' ');
   }
}
