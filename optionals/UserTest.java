package optionals;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;   
    
public class UserTest {
    static final String NAME_1 = "Bulbek";
    static final String NAME_2 = "Adam";
    static final String EMAIL = "example@email.com";
    static final String COUNTRY_1 = "Polska";
    static final String COUNTRY_2 = "USA";
    static final Address ADDRESS = new Address(new Country(COUNTRY_1));
    static final String DEFAULT = "Default";
    
    private User createNewUser() {
        System.out.println("createNewUser()");
        return new User(NAME_2, ADDRESS);
    }

    // --- Creating Optionals ---

    @Test
    public void whenCreateEmptyOptional_thenNull() throws Exception{
        assertThrows(NoSuchElementException.class, () -> {
            Optional<User> emptyOpt = Optional.empty();
            emptyOpt.get();
        });
    }

    @Test
    public void whenCreateOfEmptyOptional_thenNullPointerException() throws Exception {
        User user = null;
        
        //Optional.of() will throw exception when the value is null
        assertThrows(NullPointerException.class, () -> {
            Optional<User> emptyUser = Optional.of(user);
        });
        
        //Optional.ofNullable() won't
        assertDoesNotThrow(() -> {
            Optional<User> emptyUser = Optional.ofNullable(user);
        });
    }

    // --- Accessing Optinals' value ---

    @Test
    public void whenCreateOfNullableOptional_thenOk() throws Exception {
        String name = NAME_1;

        //will throw an exception if the value is null
        Optional<String> nameOpt = Optional.ofNullable(name);
        assertEquals(NAME_1, nameOpt.get());
    }

    //checking whether the value in optional is not null
    @Test
    public void whenCheckIfPresent_thenOk() throws Exception {
        User u = new User(NAME_1, ADDRESS);
        Optional<User> userOpt = Optional.ofNullable(u);

        assertTrue(userOpt.isPresent());
        assertEquals(NAME_1, userOpt.get().getName());
    }

    //using ifPresent() method
    @Test
    public void ifPresentMethodTest() throws Exception {
        User u = new User(NAME_1, ADDRESS);
        Optional<User> userOpt = Optional.ofNullable(u);

        userOpt.ifPresent(user -> assertEquals(NAME_1, user.getName()));
    }

    // --- Returning Default Values ---
    
    //orElse() method will return optional value if it's present or the value in the argument
    @Test
    public void whenEmptyValue_thenReturnDefault() throws Exception {
        User u1 = null;
        User u2 = new User(NAME_1, ADDRESS);

        User result = Optional.ofNullable(u1).orElse(u2);

        assertEquals(result.getName(), u2.getName());
    }

    @Test
    public void whenEmptyValue_thenExecuteMethod() throws Exception {
        User u1 = null;

        Optional<User> result = Optional.ofNullable(u1);

        //orElse() or orElseGet() is used instead of get()
        User user = result.orElse(createNewUser());
    }

    @Test
    public void whenNotEmptyValue_thenReturnDefault() throws Exception {
        User u1 = new User(NAME_2);
        User u2 = new User(NAME_1);

        User result = Optional.ofNullable(u1).orElse(u2);

        assertEquals(result.getName(), u1.getName());
    }

    //orElseGet() method will execute the argument if value is null
    @Test
    public void givenEmptyValue_whenCompare_thenOk() throws Exception {
        User u = null;

        //in this case result is the same

        System.out.println("Using orElse():");
        User userOpt1 = Optional.ofNullable(u).orElse(createNewUser());

        System.out.println("Using orElse():");
        User userOpt2 = Optional.ofNullable(u).orElseGet(() -> createNewUser());
    }

    @Test
    public void givenPresentValue_whenCompare_thenOk() throws Exception {
        User u = new User(NAME_1);

        //in this case orElse() whill tun method and onElseGet won't - effect on performence

        System.out.println("Using orElse():");
        User userOpt1 = Optional.ofNullable(u).orElse(createNewUser());

        System.out.println("Using orElse():");
        User userOpt2 = Optional.ofNullable(u).orElseGet(() -> createNewUser());

        //but the objects in Optional are the same
        assertEquals(userOpt1.getName(), userOpt2.getName());
    }

    // --- Returning an Exception ---
    
    //orElseThrow() method throws an exception if the object is empty
    @Test
    public void whenThrowException_thenOk() throws Exception {
        User user = null;

        assertThrows(IllegalArgumentException.class, () -> {
            User emptyOpt = Optional.ofNullable(user).orElseThrow(() -> new IllegalArgumentException());
        });
    }

    // --- Transforming Values ---

    //map() transformation - when method return object
    @Test
    public void whenMap_thenOk() throws Exception {
        User user = new User(NAME_1);

        //if user is not null String name is user's name else it is the other name
        String name = Optional.ofNullable(user)
            .map(u -> user.getName()).orElse(NAME_2);

        assertEquals(name, user.getName());
    }

    //flatMap() transformation - when method returns Optional value
    @Test
    public void whenFlatMap_thenOk() throws Exception {
        User user = new User(NAME_1);
        String name = Optional.ofNullable(user)
            .flatMap(u -> u.getNameOptional()).orElse(NAME_2);
        
        assertEquals(name, user.getNameOptional().get());
    }

    // --- Filtering Values ---

    //filter() method takes a Predicate as an argument and returns the value as it is if the test evaluates to true
    //example - simple email validation
    @Test
    public void whenFilter_thenOk() throws Exception {
        User user = new User(NAME_1, ADDRESS, EMAIL);

        Optional<User> result = Optional.ofNullable(user)
            .filter(u -> u.getEmail() != null && u.getEmail().contains("@"));

        assertTrue(result.isPresent());
    }

    // --- Chaining Methods of the Optional class ---

    @Test
    public void whenChaining_thenOk() throws Exception{
        User user = new User(NAME_1, ADDRESS, EMAIL);
        //user country is COUNTRY_1

        String result1 = Optional.ofNullable(user)
            .flatMap(u -> u.getAddressOptional())
            .flatMap(a -> a.getCountryOptional())
            .map(c -> c.getName())
            .orElse(DEFAULT);

        //reduced version
        String result2 = Optional.ofNullable(user)
            .flatMap(User::getAddressOptional)
            .flatMap(Address::getCountryOptional)
            .map(Country::getName)
            .orElse(DEFAULT);

        //with filter
        String result3 = Optional.ofNullable(user)
            .flatMap(User::getAddressOptional)
            .flatMap(Address::getCountryOptional)
            .map(Country::getName)
            .filter(c -> c.equals(COUNTRY_2))
            .orElse(DEFAULT);


        assertEquals(result1, COUNTRY_1);
        assertEquals(result1, result2);
        assertNotEquals(result3, result1);
        assertEquals(result3, DEFAULT);
    }

    // --- Java 9 Additions ---

    //or() method - used before get()
    @Test
    public void whenEmptyOptional_thenGetValueFromOr() throws Exception {
        User user = null;

        //lambda will execute when object is null
        User result = Optional.ofNullable(user)
            .or(() -> Optional.of(new User(DEFAULT))).get();

        //---
        Optional<User> userOpt = Optional.ofNullable(user);
        User result2 = userOpt
            .or(() -> Optional.of(result)).get();

        assertEquals(result.getName(), DEFAULT);
        assertEquals(result.getName(), result2.getName());
    }

    //ifPresentOrElse() method takes two arguments: a Consumer and a Runnable.
    //If the object contains a value, then the Consumer action is executed; otherwise, the Runnable action is performed.
    @Test
    public void whenEmptyOptional_thenGetValueFromConsumer() throws Exception {
        //User user = null;
        User user = new User(NAME_1);

        Optional.ofNullable(user)
            .ifPresentOrElse(
                u -> System.out.println("User name is: " + u.getName()), // Consumer
                () -> System.out.println("User is null") // Runnable
            );
    }

    //stream() method transforms the instance to a Stream object
    //there will be an empty Stream if value is not present or a Stream containing a single value
    @Test
    public void whenGetStream_thenOk() throws Exception {
        User user = new User(NAME_1, ADDRESS, EMAIL);

        List<String> emails = Optional.ofNullable(user)
            .stream()
            .filter(u -> u.getEmail() != null && u.getEmail().contains("@"))
            .map(User::getEmail)
            .collect(Collectors.toList());

        assertTrue(emails.size() == 1);
        assertEquals(emails.get(0), user.getEmail());
    }

}