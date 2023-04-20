package optionals;

import java.util.Optional;

public class User {
    private String name;
    private String address;
    private String email;


    public User(String name, String address, String email) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public User(String name, String address) {
        this.name = name;
        this.address = address;
        this.email = null;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

}