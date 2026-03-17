package es.outlook.adriansrj.nbt.mca;

@FunctionalInterface
public interface ExceptionFunction<T, R, E extends Exception> {
   R accept(T var1) throws E;
}
