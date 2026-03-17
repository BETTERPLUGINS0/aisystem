package es.outlook.adriansrj.nbt.io;

@FunctionalInterface
public interface ExceptionTriConsumer<T, U, V, E extends Exception> {
   void accept(T var1, U var2, V var3) throws E;
}
