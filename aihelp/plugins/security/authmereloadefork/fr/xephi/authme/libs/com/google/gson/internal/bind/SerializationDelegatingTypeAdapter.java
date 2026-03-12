package fr.xephi.authme.libs.com.google.gson.internal.bind;

import fr.xephi.authme.libs.com.google.gson.TypeAdapter;

public abstract class SerializationDelegatingTypeAdapter<T> extends TypeAdapter<T> {
   public abstract TypeAdapter<T> getSerializationDelegate();
}
