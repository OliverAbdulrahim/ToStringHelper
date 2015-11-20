package functions;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test harness for the {@link functions.AutoToStringHelper AutoToStringHelper} 
 * class.
 * 
 * @author Oliver Abdulrahim
 */
public class AutoToStringHelperTest {

    /**
     * Test of {@link AutoToStringHelper#toString()}. Ensures proper formatting 
     * of attributes of the given class.
     */
    @Test
    public void testToString() {
        String testName = "toString";
        System.out.println(testName);
        
        Person p = new Person("Daniel", Person.Gender.MALE, 18);
        AutoToStringHelper instance = new AutoToStringHelper(p);
        
        String expResult = "Person{gender = MALE, name = Daniel, age = 18}";
        String result = instance.toString();
        
        assertEquals(expResult, result);
    }
    
}
