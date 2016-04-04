package org.mcmonkey.denizen2core.tags;

public abstract class AbstractTagBase {

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }

    public abstract AbstractTagObject handle(TagData data);
}
