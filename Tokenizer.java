import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * File: Tokenizer
 * Date: 3/13/16
 * Author: ben risher
 * Purpose: Tokenize the input stream
 */
public class Tokenizer extends StreamTokenizer {
    public static final int NUMBER = StreamTokenizer.TT_NUMBER;
    public static final int WORD = StreamTokenizer.TT_WORD;
    public static final char QUOTE = '"';

    /**
     * Constructor
     * @param filename string pointing to file location on disk
     */
    Tokenizer(FileReader filename) {
        super(filename);
    }  // end constructor

    /**
     * get the next token from the stream, but don't consume it
     * @return BaseToken
     */
    public BaseToken peek() {
        BaseToken tmp = getToken();
        pushBack();
        if (tmp != null) {
            return tmp;
        }
        else {
            return new BaseToken();
        }
    }  // end peek

    /**
     * get the next token and consume it, progressing the stream
     * @return BaseToken
     */
    public BaseToken getToken() {
        int token = -43214;

        try {
            token = nextToken();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        switch (token) {  // primary logic for determining token type
            case WORD:
                for (KeyWord.Type kwt : KeyWord.Type.values()) {  // proceses keywords
                    if (kwt.name().equals(sval)) {
                        return new KeyWord(sval);
                    }
                }
                if (sval.equals("End.")) {  // didn't mark . as a normal character, so I have a separate case for exiting easily
                    return new Token(sval);
                }
                break;
            case NUMBER:
                return new Token(nval);
            case QUOTE:
                return new Token(sval);
            default:
                String s = String.valueOf((char) token);

                for (PunctuationToken.Type ptt : PunctuationToken.Type.values()) {
                    if (ptt.getValue().equals(s)) {
                        return new PunctuationToken(s);
                    }
                }
                break;
        }
        return new BaseToken();
    }  // end getToken
}  // end Tokenizer
