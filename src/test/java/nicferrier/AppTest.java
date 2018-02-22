package nicferrier;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.StringReader;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp() throws IOException
    {
        App a = new App();
        StringReader s = new StringReader("(let ((a 10)) a)");
        App.Cons lisp = a.read(s);
        System.out.println("list " + lisp.toString());
        assertTrue( true );
    }
}
