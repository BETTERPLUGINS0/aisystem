package fr.xephi.authme.libs.com.google.protobuf;

@CheckReturnValue
interface SchemaFactory {
   <T> Schema<T> createSchema(Class<T> var1);
}
