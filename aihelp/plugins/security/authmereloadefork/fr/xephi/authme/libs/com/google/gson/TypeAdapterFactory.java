package fr.xephi.authme.libs.com.google.gson;

import fr.xephi.authme.libs.com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
   <T> TypeAdapter<T> create(Gson var1, TypeToken<T> var2);
}
