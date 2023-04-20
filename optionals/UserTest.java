package optionals;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;   
    
public class UserTest {
    static final String NAME = "Bulbek";
    static final String ADDRESS = "Krakow";
    static final String NAME_IN_METHOD = "Adam";


    private User createNewUser() {
        System.out.println("createNewUser()");
        return new User(NAME_IN_METHOD, ADDRESS);
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
        String name = NAME;

        //will throw an exception if the value is null
        Optional<String> nameOpt = Optional.ofNullable(name);
        assertEquals(NAME, nameOpt.get());
    }

    //checking whether the value in optional is not null
    @Test
    public void whenCheckIfPresent_thenOk() throws Exception {
        User u = new User(NAME, ADDRESS);
        Optional<User> userOpt = Optional.ofNullable(u);

        assertTrue(userOpt.isPresent());
        assertEquals(NAME, userOpt.get().getName());
    }

    //using ifPresent() method
    @Test
    public void ifPresentMethodTest() throws Exception {
        User u = new User(NAME, ADDRESS);
        Optional<User> userOpt = Optional.ofNullable(u);

        userOpt.ifPresent(user -> assertEquals(NAME, user.getName()));
    }

    // --- Returning Default Values ---
    
    //orElse() method will return optional value if it's present or the value in the argument
    @Test
    public void whenEmptyValue_thenReturnDefault() throws Exception {
        User u1 = null;
        User u2 = new User(NAME, ADDRESS);

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
        User u1 = new User(ADDRESS, NAME);
        User u2 = new User(NAME, ADDRESS);

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
        User u = new User(NAME, ADDRESS);

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
        User user = new User(NAME, ADDRESS);

        //if user is not null String name is user's name else it is the other name
        String name = Optional.ofNullable(user)
            .map(u -> user.getName()).orElse(NAME_IN_METHOD);

        assertEquals(name, user.getName());
    }

    //flatMap() transformation - when method returns Optional value
    @Test
    public void whenFlatMap_thenOk() throws Exception {
        User user = new User(NAME, ADDRESS);
        String name = Optional.ofNullable(user)
            .flatMap(u -> u.getNameOptional()).orElse(NAME_IN_METHOD);
        
        assertEquals(name, user.getNameOptional().get());
        
    }

}
    