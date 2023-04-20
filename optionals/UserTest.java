package optionals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;   
    
public class UserTest {
    final String NAME = "Bulbek";
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
        User u = new User(NAME, "123");
        Optional<User> userOpt = Optional.ofNullable(u);

        assertTrue(userOpt.isPresent());
        assertEquals(NAME, userOpt.get().getName());
    }

    //using ifPresent() method
    @Test
    public void ifPresentMethodTest() throws Exception {
        User u = new User(NAME, "123");
        Optional<User> userOpt = Optional.ofNullable(u);

        userOpt.ifPresent(user -> assertEquals(NAME, user.getName()));
    }
    

}
    