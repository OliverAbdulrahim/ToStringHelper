package functions;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test harness for the 
 * {@link functions.AbstractToStringHelper AbstractToStringHelper} class.
 * 
 * @author Oliver Abdulrahim
 */
public class AbstractToStringHelperTest {
    
    /**
     * Test of {@link AbstractToStringHelper#formatClass()}, which is called
     * during construction. Ensures that the operation formats both normal and
     * anonymous classes properly.
     */
    @Test
    public void testFormatClass() {
        String testName = "formatClass";
        System.out.println(testName);
        
        AbstractToStringHelper instance = new ToStringHelper(new Object() {}.getClass());
        
        String expResult = "Object$Anonymous{}";
        String result = instance.toString();
        
        assertEquals(expResult, result);
        
        instance = new ToStringHelper(String.class);
        
        expResult = "String{}";
        result = instance.toString();
        
        assertEquals(expResult, result);
    }

}
