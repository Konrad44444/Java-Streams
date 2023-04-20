package optionals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;   
    
public class UserTest {

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
}
    