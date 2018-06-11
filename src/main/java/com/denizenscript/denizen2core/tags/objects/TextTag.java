package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.handlers.EscapeTagBase;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TextTag extends AbstractTagObject {

    // <--[object]
    // @Since 0.3.0
    // @Type TextTag
    // @SubType NONE
    // @Group Mathematics
    // @Description Represents any text. Identified as exactly the input text.
    // @Note <@link explanation Text Tags>What are text tags?<@/link>
    // -->

    private String internal;

    public TextTag(String text) {
        internal = text;
    }

    public String getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // TODO: Tags to add:
        // distance[<TextTag>] return IntegerTag of the Levenshtein distance between the two text values.
        // replace_regex[<ListTag>] returns TextTag of the text after process a regex replacement, input is "regex|replacement" (maybe a cleaner input method? Regex in a list tag requires escaping)
        // ...

        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.to_integer
        // @Updated 2016/08/26
        // @Group Text Modification
        // @ReturnType IntegerTag
        // @Returns the text parsed as an integer.
        // @Example "1" .to_integer returns "1".
        // -->
        handlers.put("to_integer", (dat, obj) -> IntegerTag.getFor(dat.error, ((TextTag) obj).internal));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.to_number
        // @Updated 2016/08/26
        // @Group Text Modification
        // @ReturnType NumberTag
        // @Returns the text parsed as a number.
        // @Example "1" .to_number returns "1".
        // -->
        handlers.put("to_number", (dat, obj) -> NumberTag.getFor(dat.error, ((TextTag) obj).internal));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.to_boolean
        // @Updated 2016/08/26
        // @Group Text Modification
        // @ReturnType BooleanTag
        // @Returns the text parsed as a boolean.
        // @Example "true" .to_boolean returns "true".
        // -->
        handlers.put("to_boolean", (dat, obj) -> BooleanTag.getFor(dat.error, ((TextTag) obj).internal));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.to_upper
        // @Updated 2016/12/05
        // @Group Text Modification
        // @ReturnType TextTag
        // @Returns the text in full uppercase.
        // @Example "Hello" .to_upper returns "HELLO".
        // -->
        handlers.put("to_upper", (dat, obj) -> new TextTag(((TextTag) obj).internal.toUpperCase(Locale.ENGLISH)));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.to_lower
        // @Updated 2016/12/05
        // @Group Text Modification
        // @ReturnType TextTag
        // @Returns the text in full lowercase.
        // @Example "hELLO" .to_lower returns "hello".
        // -->
        handlers.put("to_lower", (dat, obj) -> new TextTag(((TextTag) obj).internal.toLowerCase(Locale.ENGLISH)));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.is_integer
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text can be parsed as an integer.
        // @Example "1" .is_integer returns "true".
        // -->
        handlers.put("is_integer", (dat, obj) -> BooleanTag.getForBoolean(IntegerTag.getFor(TextTag::doNothing, ((TextTag) obj).internal) != null));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.is_number
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text can be parsed as a number.
        // @Example "1" .is_number returns "true".
        // -->
        handlers.put("is_number", (dat, obj) -> BooleanTag.getForBoolean(NumberTag.getFor(TextTag::doNothing, ((TextTag) obj).internal) != null));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.is_boolean
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text can be parsed as a boolean.
        // @Example "true" .is_boolean returns "true".
        // -->
        handlers.put("is_boolean", (dat, obj) -> BooleanTag.getForBoolean(BooleanTag.getFor(TextTag::doNothing, ((TextTag) obj).internal) != null));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.escaped
        // @Updated 2016/09/28
        // @Group Escaping
        // @ReturnType TextTag
        // @Returns an escaped copy of the input text. See <@link explanation Escaping>Escaping<@/link>.
        // @Example "a&b" .escaped returns "a&ampb".
        // -->
        handlers.put("escaped", (dat, obj) -> new TextTag(EscapeTagBase.escape(((TextTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.unescaped
        // @Updated 2016/09/28
        // @Group Escaping
        // @ReturnType TextTag
        // @Returns an unescaped copy of the input text. See <@link explanation Escaping>Escaping<@/link>.
        // @Example "a&ampb" .unescaped returns "a&b".
        // -->
        handlers.put("unescaped", (dat, obj) -> new TextTag(EscapeTagBase.unescape(((TextTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.length
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType IntegerTag
        // @Returns the length of the text, in characters.
        // @Example "abc" .length returns "3".
        // -->
        handlers.put("length", (dat, obj) -> new IntegerTag(((TextTag) obj).internal.length()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.matches_regex
        // @Updated 2017/04/27
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text matches a Regular Expression.
        // @Example "abc" .matches_regex[[a-z]+] returns "true".
        // -->
        handlers.put("matches_regex", (dat, obj) -> BooleanTag.getForBoolean(((TextTag) obj).internal.matches(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.5.0
        // @Name TextTag.contains_any_text[<ListTag>]
        // @Updated 2018/06/09
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text contains any of the specified text values. Case-Insensitive.
        // @Example "abc" .contains_any_text[nope|B] returns "true".
        // -->
        handlers.put("contains_any_text", (dat, obj) -> {
            ListTag lt = ListTag.getFor(dat.error, dat.getNextModifier());
            String inp = CoreUtilities.toLowerCase(((TextTag) obj).internal);
            for (AbstractTagObject ato : lt.getInternal()) {
                if (!inp.contains(CoreUtilities.toLowerCase(ato.toString()))) {
                    return BooleanTag.FALSE;
                }
            }
            return BooleanTag.TRUE;
        });
        // <--[tag]
        // @Since 0.5.0
        // @Name TextTag.contains_any_text_cased[<ListTag>]
        // @Updated 2018/06/09
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text contains any of the specified text value. Case Sensitive.
        // @Example "abc" .contains_any_text_cased[nope|b] returns "true".
        // -->
        handlers.put("contains_any_text_cased", (dat, obj) -> {
            ListTag lt = ListTag.getFor(dat.error, dat.getNextModifier());
            String inp = ((TextTag) obj).internal;
            for (AbstractTagObject ato : lt.getInternal()) {
                if (!inp.contains(ato.toString())) {
                    return BooleanTag.FALSE;
                }
            }
            return BooleanTag.TRUE;
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.contains_text[<TextTag>]
        // @Updated 2017/10/17
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text contains the specified text. Case-Insensitive.
        // @Example "abc" .contains_text[B] returns "true".
        // -->
        handlers.put("contains_text", (dat, obj) -> BooleanTag.getForBoolean(CoreUtilities.toLowerCase(((TextTag) obj).internal)
                .contains(CoreUtilities.toLowerCase(dat.getNextModifier().toString()))));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.contains_text_cased[<TextTag>]
        // @Updated 2017/10/17
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the text contains the specified text. Case Sensitive.
        // @Example "abc" .contains_text_cased[b] returns "true".
        // -->
        handlers.put("contains_text_cased", (dat, obj) -> BooleanTag.getForBoolean(((TextTag) obj).internal.contains(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.equals[<TextTag>]
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the two sets of text are equal. Case-Insensitive.
        // @Example "abc" .equals[abc] returns "true".
        // -->
        handlers.put("equals", (dat, obj) -> BooleanTag.getForBoolean(((TextTag) obj).internal.equalsIgnoreCase(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.equals_cased[<TextTag>]
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the two sets of text are equal. Case Sensitive.
        // @Example "abc" .equals_cased[abc] returns "true".
        // -->
        handlers.put("equals_cased", (dat, obj) -> BooleanTag.getForBoolean(((TextTag) obj).internal.equals(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.starts_with_cased[<TextTag>]
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the the text starts with the specified text. Case Sensitive.
        // @Example "abc" .starts_with_cased[a] returns "true".
        // -->
        handlers.put("starts_with_cased", (dat, obj) -> BooleanTag.getForBoolean(((TextTag) obj).internal.startsWith(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.ends_with_cased[<TextTag>]
        // @Updated 2017/02/17
        // @Group Text Details
        // @ReturnType BooleanTag
        // @Returns whether the the text ends with the specified text. Case Sensitive.
        // @Example "abc" .ends_with_cased[c] returns "true".
        // -->
        handlers.put("ends_with_cased", (dat, obj) -> BooleanTag.getForBoolean(((TextTag) obj).internal.endsWith(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.index_of_cased[<TextTag>]
        // @Updated 2017/02/24
        // @Group Text Details
        // @ReturnType IntegerTag
        // @Returns the location in the text of the exact input text, returns 0 if not found.
        // @Example "abcabc" .index_of_cased[c] returns "3".
        // -->
        handlers.put("index_of_cased", (dat, obj) -> new IntegerTag(((TextTag) obj).internal.indexOf(dat.getNextModifier().toString()) + 1));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.last_index_of_cased[<TextTag>]
        // @Updated 2017/02/24
        // @Group Text Details
        // @ReturnType IntegerTag
        // @Returns the last location in the text of the exact input text, returns 0 if not found.
        // @Example "abcabc" .last_index_of_cased[c] returns "6".
        // -->
        handlers.put("last_index_of_cased", (dat, obj) -> new IntegerTag(((TextTag) obj).internal.lastIndexOf(dat.getNextModifier().toString()) + 1));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.char_at[<IntegerTag>]
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType TextTag
        // @Returns the character at the specified index in the text (treating the text as a list of characters).
        // @Example "abc" .char_at[1] returns "a".
        // -->
        handlers.put("char_at", (dat, obj) -> {
            IntegerTag ind = IntegerTag.getFor(dat.error, dat.getNextModifier());
            int i = (int) ind.getInternal() - 1;
            if (i < 0 || i >= ((TextTag) obj).internal.length()) {
                if (!dat.hasFallback()) {
                    dat.error.run("Invalid TextTag.CHAR_AT[...] index!");
                }
                return NullTag.NULL;
            }
            return new TextTag(String.valueOf(((TextTag) obj).internal.charAt(i)));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.to_list_of_characters
        // @Updated 2016/12/05
        // @Group Text Details
        // @ReturnType ListTag
        // @Returns the characters in the text as a list.
        // @Example "abc" .to_list_of_characters returns "a|b|c|".
        // -->
        handlers.put("to_list_of_characters", (dat, obj) -> {
            ListTag list = new ListTag();
            String text = ((TextTag) obj).internal;
            for (int i = 0; i < text.length(); i++) {
                list.getInternal().add(new TextTag(String.valueOf(text.charAt(i))));
            }
            return list;
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.replace[<MapTag>]
        // @Updated 2016/12/05
        // @Group Text Modification
        // @ReturnType TextTag
        // @Returns the text, with the specified key text swapped for the specified value text.
        // @Example "abc" .replace[b:d] returns "abc".
        // -->
        handlers.put("replace", (dat, obj) -> {
            MapTag map = MapTag.getFor(dat.error, dat.getNextModifier());
            String temp = ((TextTag) obj).internal;
            for (Map.Entry<String, AbstractTagObject> entry : map.getInternal().entrySet()) {
                temp = temp.replace(entry.getKey(), entry.getValue().toString());
            }
            return new TextTag(temp);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.after[<TextTag>]
        // @Updated 2017/02/12
        // @Group Text Modification
        // @ReturnType TextTag
        // @Returns the text after the specified text.
        // @Example "abc" .after[b] returns "c".
        // -->
        handlers.put("after", (dat, obj) -> new TextTag(((TextTag) obj).after(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.before[<TextTag>]
        // @Updated 2017/02/12
        // @Group Text Modification
        // @ReturnType TextTag
        // @Returns the text before the specified text.
        // @Example "abc" .before[b] returns "a".
        // -->
        handlers.put("before", (dat, obj) -> new TextTag(((TextTag) obj).before(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name TextTag.substring[<ListTag>]
        // @Updated 2016/12/05
        // @Group Text Modification
        // @ReturnType TextTag
        // @Returns the portion of text specified by the two sub-indices.
        // @Example "abc" .substring[1|2] returns "ab".
        // -->
        handlers.put("substring", (dat, obj) -> {
            ListTag inputinds = ListTag.getFor(dat.error, dat.getNextModifier());
            if (inputinds.getInternal().size() != 2) {
                if (!dat.hasFallback()) {
                    dat.error.run("Invalid input!");
                }
                return NullTag.NULL;
            }
            int i1 = (int) IntegerTag.getFor(dat.error, inputinds.getInternal().get(0)).getInternal() - 1;
            int i2 = (int) IntegerTag.getFor(dat.error, inputinds.getInternal().get(1)).getInternal();
            String text = ((TextTag) obj).internal;
            if (i1 < 0) {
                i1 = 0;
            }
            if (i2 < 0) {
                i2 = 0;
            }
            if (i1 > text.length()) {
                i1 = text.length();
            }
            if (i2 > text.length()) {
                i2 = text.length();
            }
            String res = text.substring(i1, i2);
            return new TextTag(res);
        });
    }

    public String after(String inp) {
        int ind = internal.indexOf(inp);
        if (ind <= -1) {
            return internal;
        }
        else {
            return internal.substring(ind + inp.length());
        }
    }

    public String before(String inp) {
        int ind = internal.indexOf(inp);
        if (ind <= -1) {
            return internal;
        }
        else {
            return internal.substring(0, ind);
        }
    }

    public static void doNothing(String s) {
    }

    public static TextTag getFor(Action<String> error, String text) {
        return new TextTag(text);
    }

    public static TextTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof TextTag) ? (TextTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public String getTagTypeName() {
        return "TextTag";
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        if (!data.hasFallback()) {
            if (data.currentIndex() > 0 && data.returnsTracked[data.currentIndex() - 1] != null) {
                data.error.run("Unknown tag part '" + ColorSet.emphasis + data.getNext()
                        + ColorSet.warning + "' - tag was of type: "
                        + ColorSet.emphasis + data.returnsTracked[data.currentIndex() - 1].getTagTypeName()
                        + ColorSet.warning);
            }
            else {
                data.error.run("Unknown tag part '" + ColorSet.emphasis + data.getNext()
                        + ColorSet.warning + "' - exact original tag type is unknown.");
            }
        }
        return NullTag.NULL;
    }

    @Override
    public String toString() {
        return internal;
    }
}
