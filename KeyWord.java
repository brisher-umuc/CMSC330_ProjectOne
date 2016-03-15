/**
 * File: KeyWord
 * Date: 3/13/16
 * Author: ben risher
 * Purpose:
 */
public class KeyWord extends BaseToken {
    private static Type type = null;

    public enum Type {
        Window, End, Layout, Flow, Grid, Button, Group, Label, Panel, Textfield, Radio;
    }

    public static Type getType() {
        return type;
    }

    public static void setType(Type type) {
        KeyWord.type = type;
    }

    public KeyWord(String s) throws ParseError{
        try {
            type = Type.valueOf(s);
        }
        catch (IllegalArgumentException e) {
            throw new ParseError("Stuff");
        }

    }

    public String getName() {
        if (type != null) {
            return type.name();
        }
        else {
            return "";
        }
    }

    public String toString() {
        if (type != null) {
            return "KeyWord<" + type.name() + ">";
        }
        else {
            return "";
        }

    }

}
