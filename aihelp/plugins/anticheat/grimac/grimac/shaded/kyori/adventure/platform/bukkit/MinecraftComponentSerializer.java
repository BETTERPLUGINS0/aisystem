package ac.grim.grimac.shaded.kyori.adventure.platform.bukkit;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.ComponentSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ApiStatus.Experimental
public final class MinecraftComponentSerializer implements ComponentSerializer<Component, Component, Object> {
   private static final MinecraftComponentSerializer INSTANCE = new MinecraftComponentSerializer();
   @Nullable
   private static final Class<?> CLASS_JSON_DESERIALIZER = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonDeserializer"));
   @Nullable
   private static final Class<?> CLASS_JSON_ELEMENT = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonElement"));
   @Nullable
   private static final Class<?> CLASS_JSON_PARSER = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonParser"));
   @Nullable
   private static final Class<?> CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
   @Nullable
   private static final Class<?> CLASS_CRAFT_REGISTRY = MinecraftReflection.findCraftClass("CraftRegistry");
   @Nullable
   private static final Class<?> CLASS_REGISTRY_ACCESS = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.IRegistryCustom"), MinecraftReflection.findMcClassName("core.RegistryAccess"));
   @Nullable
   private static final MethodHandle PARSE_JSON;
   @Nullable
   private static final MethodHandle GET_REGISTRY;
   private static final AtomicReference<RuntimeException> INITIALIZATION_ERROR;
   private static final Object JSON_PARSER_INSTANCE;
   private static final Object MC_TEXT_GSON;
   private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE;
   private static final MethodHandle TEXT_SERIALIZER_SERIALIZE;
   private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE_TREE;
   private static final MethodHandle TEXT_SERIALIZER_SERIALIZE_TREE;
   private static final boolean SUPPORTED;

   public static boolean isSupported() {
      return SUPPORTED;
   }

   @NotNull
   public static MinecraftComponentSerializer get() {
      return INSTANCE;
   }

   @NotNull
   public Component deserialize(@NotNull final Object input) {
      if (!SUPPORTED) {
         throw (RuntimeException)INITIALIZATION_ERROR.get();
      } else {
         try {
            Object element;
            if (TEXT_SERIALIZER_SERIALIZE_TREE != null) {
               element = TEXT_SERIALIZER_SERIALIZE_TREE.invoke(input);
            } else {
               if (MC_TEXT_GSON == null) {
                  return BukkitComponentSerializer.gson().deserialize(TEXT_SERIALIZER_SERIALIZE.invoke(input));
               }

               element = ((Gson)MC_TEXT_GSON).toJsonTree(input);
            }

            return (Component)BukkitComponentSerializer.gson().serializer().fromJson(element.toString(), Component.class);
         } catch (Throwable var3) {
            throw new UnsupportedOperationException(var3);
         }
      }
   }

   @NotNull
   public Object serialize(@NotNull final Component component) {
      if (!SUPPORTED) {
         throw (RuntimeException)INITIALIZATION_ERROR.get();
      } else if (TEXT_SERIALIZER_DESERIALIZE_TREE == null && MC_TEXT_GSON == null) {
         try {
            return TEXT_SERIALIZER_DESERIALIZE.invoke((String)BukkitComponentSerializer.gson().serialize(component));
         } catch (Throwable var4) {
            throw new UnsupportedOperationException(var4);
         }
      } else {
         JsonElement json = BukkitComponentSerializer.gson().serializer().toJsonTree(component);

         try {
            if (TEXT_SERIALIZER_DESERIALIZE_TREE != null) {
               Object unRelocatedJsonElement = PARSE_JSON.invoke(JSON_PARSER_INSTANCE, json.toString());
               return TEXT_SERIALIZER_DESERIALIZE_TREE.invoke(unRelocatedJsonElement);
            } else {
               return ((Gson)MC_TEXT_GSON).fromJson(json, CLASS_CHAT_COMPONENT);
            }
         } catch (Throwable var5) {
            throw new UnsupportedOperationException(var5);
         }
      }
   }

   static {
      PARSE_JSON = MinecraftReflection.findMethod(CLASS_JSON_PARSER, "parse", CLASS_JSON_ELEMENT, String.class);
      GET_REGISTRY = MinecraftReflection.findStaticMethod(CLASS_CRAFT_REGISTRY, "getMinecraftRegistry", CLASS_REGISTRY_ACCESS);
      INITIALIZATION_ERROR = new AtomicReference(new UnsupportedOperationException());
      Object gson = null;
      Object jsonParserInstance = null;
      MethodHandle textSerializerDeserialize = null;
      MethodHandle textSerializerSerialize = null;
      MethodHandle textSerializerDeserializeTree = null;
      MethodHandle textSerializerSerializeTree = null;

      try {
         if (CLASS_JSON_PARSER != null) {
            jsonParserInstance = CLASS_JSON_PARSER.getDeclaredConstructor().newInstance();
         }

         if (CLASS_CHAT_COMPONENT != null) {
            Object registryAccess = GET_REGISTRY != null ? GET_REGISTRY.invoke() : null;
            Class<?> chatSerializerClass = (Class)Arrays.stream(CLASS_CHAT_COMPONENT.getClasses()).filter((c) -> {
               if (CLASS_JSON_DESERIALIZER != null) {
                  return CLASS_JSON_DESERIALIZER.isAssignableFrom(c);
               } else {
                  Class[] var1 = c.getInterfaces();
                  int var2 = var1.length;

                  for(int var3 = 0; var3 < var2; ++var3) {
                     Class<?> itf = var1[var3];
                     if (itf.getSimpleName().equals("JsonDeserializer")) {
                        return true;
                     }
                  }

                  return false;
               }
            }).findAny().orElse(MinecraftReflection.findNmsClass("ChatSerializer"));
            if (chatSerializerClass != null) {
               Field gsonField = (Field)Arrays.stream(chatSerializerClass.getDeclaredFields()).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return m.getType().equals(Gson.class);
               }).findFirst().orElse((Object)null);
               if (gsonField != null) {
                  gsonField.setAccessible(true);
                  gson = gsonField.get((Object)null);
               }
            }

            List<Class<?>> candidates = new ArrayList();
            if (chatSerializerClass != null) {
               candidates.add(chatSerializerClass);
            }

            candidates.addAll(Arrays.asList(CLASS_CHAT_COMPONENT.getClasses()));
            Iterator var9 = candidates.iterator();

            while(var9.hasNext()) {
               Class<?> serializerClass = (Class)var9.next();
               Method[] declaredMethods = serializerClass.getDeclaredMethods();
               Method deserialize = (Method)Arrays.stream(declaredMethods).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType());
               }).filter((m) -> {
                  return m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(String.class);
               }).min(Comparator.comparing(Method::getName)).orElse((Object)null);
               Method serialize = (Method)Arrays.stream(declaredMethods).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return m.getReturnType().equals(String.class);
               }).filter((m) -> {
                  return m.getParameterCount() == 1 && CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0]);
               }).findFirst().orElse((Object)null);
               Method deserializeTree = (Method)Arrays.stream(declaredMethods).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType());
               }).filter((m) -> {
                  return m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(CLASS_JSON_ELEMENT);
               }).findFirst().orElse((Object)null);
               Method serializeTree = (Method)Arrays.stream(declaredMethods).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return m.getReturnType().equals(CLASS_JSON_ELEMENT);
               }).filter((m) -> {
                  return m.getParameterCount() == 1 && CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0]);
               }).findFirst().orElse((Object)null);
               Method deserializeTreeWithRegistryAccess = (Method)Arrays.stream(declaredMethods).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType());
               }).filter((m) -> {
                  return m.getParameterCount() == 2;
               }).filter((m) -> {
                  return m.getParameterTypes()[0].equals(CLASS_JSON_ELEMENT);
               }).filter((m) -> {
                  return m.getParameterTypes()[1].isInstance(registryAccess);
               }).findFirst().orElse((Object)null);
               Method serializeTreeWithRegistryAccess = (Method)Arrays.stream(declaredMethods).filter((m) -> {
                  return Modifier.isStatic(m.getModifiers());
               }).filter((m) -> {
                  return m.getReturnType().equals(CLASS_JSON_ELEMENT);
               }).filter((m) -> {
                  return m.getParameterCount() == 2;
               }).filter((m) -> {
                  return CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0]);
               }).filter((m) -> {
                  return m.getParameterTypes()[1].isInstance(registryAccess);
               }).findFirst().orElse((Object)null);
               if (deserialize != null) {
                  textSerializerDeserialize = MinecraftReflection.lookup().unreflect(deserialize);
               }

               if (serialize != null) {
                  textSerializerSerialize = MinecraftReflection.lookup().unreflect(serialize);
               }

               if (deserializeTree != null) {
                  textSerializerDeserializeTree = MinecraftReflection.lookup().unreflect(deserializeTree);
               } else if (deserializeTreeWithRegistryAccess != null) {
                  deserializeTreeWithRegistryAccess.setAccessible(true);
                  textSerializerDeserializeTree = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(deserializeTreeWithRegistryAccess), 1, new Object[]{registryAccess});
               }

               if (serializeTree != null) {
                  textSerializerSerializeTree = MinecraftReflection.lookup().unreflect(serializeTree);
               } else if (serializeTreeWithRegistryAccess != null) {
                  serializeTreeWithRegistryAccess.setAccessible(true);
                  textSerializerSerializeTree = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(serializeTreeWithRegistryAccess), 1, new Object[]{registryAccess});
               }
            }
         }
      } catch (Throwable var18) {
         INITIALIZATION_ERROR.set(new UnsupportedOperationException("Error occurred during initialization", var18));
      }

      MC_TEXT_GSON = gson;
      JSON_PARSER_INSTANCE = jsonParserInstance;
      TEXT_SERIALIZER_DESERIALIZE = textSerializerDeserialize;
      TEXT_SERIALIZER_SERIALIZE = textSerializerSerialize;
      TEXT_SERIALIZER_DESERIALIZE_TREE = textSerializerDeserializeTree;
      TEXT_SERIALIZER_SERIALIZE_TREE = textSerializerSerializeTree;
      SUPPORTED = MC_TEXT_GSON != null || TEXT_SERIALIZER_DESERIALIZE != null && TEXT_SERIALIZER_SERIALIZE != null || TEXT_SERIALIZER_DESERIALIZE_TREE != null && TEXT_SERIALIZER_SERIALIZE_TREE != null;
   }
}
