package fr.xephi.authme.util.lazytags;

public interface Tag<A> {
   String getName();

   String getValue(A var1);
}
