package optionals;

import java.util.Optional;

public class User {
    private String name;
    private String address;


    public User(String name, String address) {
        this.name = name;
        this.address = address;
    }


    public User() {
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

}