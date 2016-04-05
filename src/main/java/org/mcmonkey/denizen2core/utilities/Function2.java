package org.mcmonkey.denizen2core.utilities;

@FunctionalInterface
public interface Function2<A, B, R> {
    R apply(A a, B b);
}
