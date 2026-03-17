/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.text.serializer.bungeecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

final class GsonInjections {
    private GsonInjections() {
    }

    public static Field field(@NotNull Class<?> clazz, @NotNull String string) {
        Field field = clazz.getDeclaredField(string);
        field.setAccessible(true);
        return field;
    }

    public static boolean injectGson(@NotNull Gson gson, @NotNull Consumer<GsonBuilder> consumer) {
        try {
            Field field = GsonInjections.field(Gson.class, "factories");
            Field field2 = GsonInjections.field(GsonBuilder.class, "factories");
            Field field3 = GsonInjections.field(GsonBuilder.class, "hierarchyFactories");
            GsonBuilder gsonBuilder = new GsonBuilder();
            consumer.accept(gsonBuilder);
            List list = (List)field.get(gson);
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List)field2.get(gsonBuilder));
            Collections.reverse(arrayList);
            arrayList.addAll((List)field3.get(gsonBuilder));
            ArrayList<TypeAdapterFactory> arrayList2 = new ArrayList<TypeAdapterFactory>(list);
            int n = GsonInjections.findExcluderIndex(arrayList2);
            Collections.reverse(arrayList);
            for (TypeAdapterFactory typeAdapterFactory : arrayList) {
                arrayList2.add(n, typeAdapterFactory);
            }
            field.set(gson, arrayList2);
            return true;
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            return false;
        }
    }

    private static int findExcluderIndex(@NotNull List<TypeAdapterFactory> list) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            TypeAdapterFactory typeAdapterFactory = list.get(i);
            if (!(typeAdapterFactory instanceof Excluder)) continue;
            return i + 1;
        }
        return 0;
    }
}

