package functions;

/**
 * The {@code Person} class contains a simple encapsulation of a human being
 * with various defining attributes for use in a testing environment.
 *
 * <p> Objects of this class are <em>immutable</em>; their properties may not
 * changed after creation.
 *
 * @author Oliver Abdulrahim
 */
public class Person {

    /**
     * Contains an enumeration of the canonical human genders.
     */
    public enum Gender {
        MALE, FEMALE;
    }

    /**
     * Defines this person's name.
     */
    private final String name;

    /**
     * Defines this person's gender.
     */
    private final Gender gender;

    /**
     * Defines this person's age.
     */
    private final int age;

    /**
     * Creates a new person with the given arguments.
     *
     * @param name The name of the person.
     * @param gender The gender of the person.
     * @param age The age of the person.
     */
    public Person(String name, Gender gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Returns the name of this person.
     *
     * @return The name of this person.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the gender of this person.
     *
     * @return The gender of this person.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Returns the age of this person.
     *
     * @return The age of this person.
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns a formatted {@code String} containing the properties of this person.
     *
     * @return A {@code String} representing this person.
     */
    @Override
    public String toString() {
        return '[' + name + ", " + gender + ", " + age + ']';
    }

}
