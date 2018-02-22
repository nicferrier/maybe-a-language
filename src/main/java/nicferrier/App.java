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

    class ParserReturn<T> {
        T element;
        int terminal;
        ParserReturn(T element, int terminal) {
            this.element=element;
            this.terminal = terminal;
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

    private ParserReturn<Symbol> readSymbol(char initial, Reader r) throws IOException {
        StreamReadReturn srr = streamRead(symbolChars, initial, r);
        return new ParserReturn<Symbol>(new Symbol(srr.element), srr.terminal);
    }

    private ParserReturn<Number> readNumber(char initial, Reader r) throws IOException {
        StreamReadReturn srr = streamRead(numberChars, initial, r);
        return new ParserReturn<Number>(new Number(srr.element), srr.terminal);
    }

    final String whitespaceChars = " \n\r\t";

    class Error extends ParserReturn<String> {
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

    private Cons readList(Reader r)  throws IOException {
        Cons cons = new Cons();
        read(r, cons);
        return (Cons)cons.cdr;
    }

    public void read(Reader r, Cons cons) throws IOException {
        int ch = r.read();
        while (true) {
            switch (ch) {
            case -1: return;
            case ')':
                cons.cdr = Nil;
                return;
            case '\t':case ' ': case'\r':case '\n':
                ch = r.read();
                while (whitespaceChars.indexOf((char) ch) > -1) {
                    ch = r.read();
                }
                continue;
            }

            cons.cdr = new Cons();
            cons = (Cons)cons.cdr;

            if (ch == '(') {
                cons.car = readList(r);
            }
            else {
                ParserReturn state = (numberChars.indexOf((char) ch) > -1)
                    ? readNumber((char)ch, r)
                    : (symbolChars.indexOf((char) ch) > -1)
                    ? readSymbol((char)ch, r)
                    : new Error("parse error");
                
                if (state instanceof Error) {
                    System.out.println("generating error because " + ch + "<<");
                    throw new ErrorException((Error)state);
                }

                cons.car = state.element;
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