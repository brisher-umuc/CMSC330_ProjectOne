import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * File: Tokenizer
 * Date: 3/13/16
 * Author: ben risher
 * Purpose:
 */
public class Tokenizer extends StreamTokenizer {
    public static final int NUMBER = StreamTokenizer.TT_NUMBER;
    public static final int WORD = StreamTokenizer.TT_WORD;
    public static final char QUOTE = '"';

    Tokenizer(FileReader filename) {
        super(filename);
    }

    public BaseToken peek() {
        BaseToken tmp = getToken();
        pushBack();
        return tmp;
    }

    public BaseToken getToken() {
        int token = -43214;

        try {
            token = nextToken();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        switch (token) {
            case WORD:
                for (KeyWord.Type kwt : KeyWord.Type.values()) {
                    if (kwt.name().equals(sval)) {
                        try {
                            return new KeyWord(sval);
                        }
                        catch (ParseError e) {
                            e.printStackTrace();
                            System.exit(-1);
                        }
                    }
                }

                if (sval.equals("End.")) {
                    // this should only ever be 'End.'
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
        return null;
    }

    public static void main(String[] args) {
        //TODO: file chooser
        String filename = "input.txt";
        try {
            FileReader reader = new FileReader(filename);
            Tokenizer stream = new Tokenizer(reader);
            System.out.println(stream.getToken());
            System.out.println(stream.peek());
            System.out.println(stream.peek());
            System.out.println(stream.getToken());
            System.out.println(stream.getToken());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    } // end main

}  // end Tokenizer
