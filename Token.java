/**
 * File: Token
 * Date: 3/13/16
 * Author: ben risher
 * Purpose:
 */
public class Token extends BaseToken {
    private String s_value;
    private Double d_value;

    public Token(String s) {
        s_value = s;
    }
    public Token(Double d) {
        d_value = d;
    }

    public Double getD_value() {
        return d_value;
    }

    public void setD_value(Double d) {
        d_value = d;
    }

    public String getS_value() {
        return s_value;
    }

    public void setS_value(String s) {
        s_value = s;
    }

    public String getName() {
        if (s_value != null) {
            return s_value;
        }
        else {
            return d_value.toString();
        }

    }

    public String toString() {
        if (s_value != null) {
            return "Token<" + s_value + ">";
        }
        else {
            return "Token<" + d_value.toString() + ">";
        }
    }
}
