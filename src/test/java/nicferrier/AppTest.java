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
        String form1 = "(let ((a 10)) a)";
        App.Cons lisp1 = a.read(new StringReader(form1));
        assertTrue("(let ((a 10.0)) a)".equals(lisp1.toString()));

        String form2 = "(let ((a \"hello\")) a)";
        App.Cons lisp2 = a.read(new StringReader(form2));
        assertTrue(form2.equals(lisp2.toString()));

        String form3 = "(let ((a true)) a)";
        App.Cons lisp3 = a.read(new StringReader(form3));
        assertTrue(form3.equals(lisp3.toString()));
    }
}
