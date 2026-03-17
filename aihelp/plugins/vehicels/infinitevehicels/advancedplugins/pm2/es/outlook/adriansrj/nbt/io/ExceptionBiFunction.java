package es.outlook.adriansrj.nbt.io;

@FunctionalInterface
public interface ExceptionBiFunction<T, U, R, E extends Exception> {
   R accept(T var1, U var2) throws E;
}
