/**
 * File: PunctuationToken
 * Date: 3/13/16
 * Author: ben risher
 * Purpose:
 */
public class PunctuationToken extends BaseToken {
    private Type type = null;

    public enum Type {
        LEFT_PAREN("("),
        COMMA(","),
        RIGHT_PAREN(")"),
        PERIOD("."),
        COLON(":"),
        SEMI_COLON(";");

        private String value;

        Type(String v) {
            this.value = v;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return this.value;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type t) {
        type = t;
    }

    public PunctuationToken(String s) {
        for (Type t: Type.values()) {
            if (t.value.equals(s)) {
                type = t;
                break;
            }
        }

    }

    public String getName() {
        return type.name();
    }

    public String toString() {
        if (type != null) {
            return "PunctuationToken<" + type.name() + ">";
        }
        else {
            return "";
        }

    }

}
