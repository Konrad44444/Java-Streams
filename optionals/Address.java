package optionals;

import java.util.Optional;

public class Address {
    private Country country;
    
    public Optional<Country> getCountryOptional() {
        return Optional.ofNullable(country);
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Address(Country country) {
        this.country = country;
    }

}
