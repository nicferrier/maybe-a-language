package nicferrier;

import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

/** Reader.
 */
public class App
{
    class Atom<T> {
        T o;
        Atom(T o) {
            this.o = o;
        }
        public String toString() {
            return this.o.toString();
        }
    }

    class Symbol extends Atom<String> {
        Symbol(String name) {
            super(name);
        }
    }

    class FlispString extends Atom<String> {
        FlispString(String string) {
            super(string);
        }
        public String toString() {
            return "\"" + super.toString() + "\"";
        }
    }
    
    final String symbolChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    final Symbol Nil = new Symbol("nil");

    class Number extends Atom<Double> {
        Number(String number) {
            super(Double.parseDouble(number));
        }
    }

    final String numberChars = "0123456789-.";

    class Cons {
        Object car;
        Object cdr = Nil;
        Cons() {
        }

        Cons(Object car, Object cdr) {
            this.car = car;
            this.cdr = cdr;
        }

        Cons cons() {
            this.cdr = new Cons();
            return (Cons) this.cdr;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (car instanceof Cons) {
                sb.append("(");
                sb.append(car.toString());
                sb.append(")");
            }
            else {
                if (car != null) {
                    sb.append(car.toString());
                }
                else {
                    sb.append("null");
                }
            }

            if (cdr == Nil) {
                return sb.toString();
            }
            else {
                return sb.toString() + " " + cdr.toString();
            }
        }
    }

    class StreamReadReturn {
        String element;
        int terminal;
        StreamReadReturn(String element, int terminal) {
            this.element=element;
            this.terminal = terminal;
        }
    }

    private StreamReadReturn streamRead(String legal, char initial, Reader r) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(initial);
        int ch = r.read();
        while (ch != -1 && legal.indexOf((char) ch) > -1) {
            sb.append((char) ch);
            ch = r.read();
        }
        return new StreamReadReturn(sb.toString(), ch);
    }

    private StreamReadReturn readSymbol(char initial, Reader r, Cons cons) throws IOException {
        StreamReadReturn srr = streamRead(symbolChars, initial, r);
        cons.car = new Symbol(srr.element);
        return srr;
    }

    private StreamReadReturn readNumber(char initial, Reader r, Cons cons) throws IOException {
        StreamReadReturn srr = streamRead(numberChars, initial, r);
        cons.car = new Number(srr.element);
        return srr;
    }

    class Error extends StreamReadReturn {
        Error(String message) {
            super(message, -1);
        }
    }

    class ErrorException extends RuntimeException {
        Error error;
        ErrorException(Error error) {
            super(error.element);
            this.error=error;
        }
    }

    private void readString(Reader r, Cons cons) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch = r.read();
        while (ch != '"') {
            if (ch == '\\') {
                sb.append((char)ch);
                ch = r.read();
            }
            sb.append((char)ch);
            ch = r.read();
        }
        cons.car = new FlispString(sb.toString());
    }

    private Cons readList(Reader r)  throws IOException {
        Cons cons = new Cons();
        read(r, cons);
        return (Cons)cons.cdr;
    }

    private int skipWhitespace(Reader r) throws IOException {
        final String whitespaceChars = " \n\r\t";
        int ch = r.read();
        while (whitespaceChars.indexOf((char) ch) > -1) {
            ch = r.read();
        }
        return ch;
    }

    public void read(Reader r, Cons cons) throws IOException {
        int ch = r.read();
        while (true) {
            switch (ch) {
            case -1:
                return;
            case ')':
                cons.cdr = Nil;
                return;
            case '\t': case ' ': case'\r': case '\n':
                ch = skipWhitespace(r);
                continue;
            }

            cons.cdr = new Cons();
            cons = (Cons)cons.cdr;

            if (ch == '(') {
                cons.car = readList(r);
            }
            else if (ch == '"') {
                readString(r, cons);
            }
            else {
                StreamReadReturn state = (numberChars.indexOf((char) ch) > -1)
                    ? readNumber((char)ch, r, cons)
                    : (symbolChars.indexOf((char) ch) > -1)
                    ? readSymbol((char)ch, r, cons)
                    : new Error("parse error");
                
                if (state instanceof Error) {
                    throw new ErrorException((Error)state);
                }

                ch = state.terminal;
                continue;
            }
            ch = r.read();
        }
    }

    public Cons read(Reader r) throws IOException {
        return readList(r);
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
