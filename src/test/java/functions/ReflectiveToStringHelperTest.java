package functions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test harness for the {@link ReflectiveToStringHelper ReflectiveToStringHelper}
 * class.
 *
 * @author Oliver Abdulrahim
 */
public class ReflectiveToStringHelperTest {

    /**
     * Test of {@link ReflectiveToStringHelper#toString()}. Ensures proper formatting
     * of attributes of the given class.
     */
    @Test
    public void testToString() {
        String testName = "toString";
        System.out.println(testName);

        Person p = new Person("Daniel", Person.Gender.MALE, 18);
        ReflectiveToStringHelper instance = new ReflectiveToStringHelper(p);

        String expResult = "Person{gender = MALE, name = Daniel, age = 18}";
        String result = instance.toString();

        assertEquals(expResult, result);
    }

}
