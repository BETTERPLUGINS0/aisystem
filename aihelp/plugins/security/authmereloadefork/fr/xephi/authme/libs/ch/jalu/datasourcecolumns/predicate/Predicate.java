package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate;

public interface Predicate<C> {
   Predicate<C> and(Predicate<C> var1);

   Predicate<C> or(Predicate<C> var1);
}
