package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class EmployeeTest {
    static final int ID1 = 1;
    static final int ID2 = 2;
    static final int ID3 = 3;
    static final String NAME1 = "Bulbek";
    static final String NAME2 = "Adam";
    static final String NAME3 = "Ewa";
    static final int SALARY1 = 10000;
    static final int SALARY2 = 20000;
    static final int SALARY3 = 30000;

    // --- Java Stream Creation ---
    static Employee[] employeesArray = {
        new Employee(ID1, NAME1, SALARY1),
        new Employee(ID2, NAME2, SALARY2),
        new Employee(ID3, NAME3, SALARY3)
    };

    static List<Employee> employeesList = Arrays.asList(employeesArray);

    // - stream from array
    Stream<Employee> streamFromArray = Stream.of(employeesArray);
    
    // - stream from list
    Stream<Employee> streamFromList = employeesList.stream();

    // - stream from individual objects
    Stream<Employee> streamFromIndividuals = Stream.of(employeesArray[0], employeesArray[1], employeesArray[2]);

    // - stream from StreamBuilder
    Stream.Builder<Employee> employeeStreamBuilder = Stream.builder();

    Stream<Employee> streamFromBuilder = employeeStreamBuilder
        .add(employeesArray[0])
        .add(employeesArray[1])
        .add(employeesArray[2])
        .build();
    

    @Test
    public void test() {
        
    }
}
    