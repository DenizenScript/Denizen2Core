package com.denizenscript.denizen2core.utilities.yaml;

import com.denizenscript.denizen2core.utilities.CoreUtilities;

/**
 * This class primarily for internal usage with {@link YAMLConfiguration}.
 */
public class StringHolder {

    public final String str;

    public final String low;

    public StringHolder(String _str) {
        str = _str;
        low = CoreUtilities.toLowerCase(_str);
    }

    @Override
    public int hashCode() {
        return low.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return low.equalsIgnoreCase((String) obj);
        }
        else if (obj instanceof StringHolder) {
            return low.equals(((StringHolder) obj).low);
        }
        return false;
    }

    @Override
    public String toString() {
        return str;
    }
}
