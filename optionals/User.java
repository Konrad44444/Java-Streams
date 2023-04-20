package optionals;

import java.util.Optional;

public class User {
    private String name;
    private Address address;
    private String email;


    public User(String name, Address address, String email) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public User(String name, Address address) {
        this.name = name;
        this.address = address;
        this.email = null;
    }

    public User(String name) {
        this.name = name;
        this.address = null;
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

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
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

    public Optional<Address> getAddressOptional() {
        return Optional.ofNullable(address);
    }

}