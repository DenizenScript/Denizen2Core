package org.mcmonkey.denizen2core.utilities;

@FunctionalInterface
public interface Function2<A, B, R> {
    public R apply (A a, B b);
}
