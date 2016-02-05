package util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import util.ToStringHelper;

/**
 * Test harness for the {@link ToStringHelper ToStringHelper} class.
 *
 * @author Oliver Abdulrahim
 */
public class ToStringHelperTest {

    /**
     * Test of {@link ToStringHelper#omitNullValues()}. Ensures proper inclusion
     * and omission of a {@code null} value as well as the inclusion of a
     * non-{@code null} one.
     */
    @Test
    public void testOmitNullValues() {
        String testName = "omitNullValues";
        System.out.println(testName);

        ToStringHelper instance = new ToStringHelper(testName)
                .add("Non-null property", 1)
                .add("Null property", null);

        String nonOmitExpResult = testName + '{'
                + "Non-null property = 1"
                + ", Null property = null"
                + '}';
        String nonOmitResult = instance.toString();

        String omitExpResult = testName + '{'
                + "Non-null property = 1"
                + '}';
        String omitResult = instance.omitNullValues().toString();

        assertEquals(nonOmitExpResult, nonOmitResult);
        assertEquals(omitExpResult, omitResult);
    }

    /**
     * Test of {@link ToStringHelper#toString()}. Ensures proper formatting of
     * {@code null} objects, arrays (both one-dimensional and
     * multi-dimensional), and circular references.
     */
    @Test
    public void testToString() {
        String testName = "toString";
        System.out.println(testName);

        ToStringHelper instance = new ToStringHelper(testName)
                .add("Hello, World!", 42)
                .add("1-D Array", new int[] {1, 2, 3})
                .add("2-D Array", new double[][] {{1.1, 2.2}, {3.3, 4.4, 5.5}})
                .add("Nothing", null);
        instance.add("Don't Recurse", instance);

        String expResult = testName + '{'
                + "Hello, World! = 42"
                + ", 1-D Array = [1, 2, 3]"
                + ", 2-D Array = [[1.1, 2.2], [3.3, 4.4, 5.5]]"
                + ", Nothing = null"
                + ", Don't Recurse = (this ToStringHelper)"
                + '}';
        String result = instance.toString();

        assertEquals(expResult, result);
    }

}
