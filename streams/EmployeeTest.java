package streams;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    
    // - stream from array
    Stream<Employee> streamFromArray = Stream.of(employeesArray);
    
    // - stream from list
    static List<Employee> employeesList = Arrays.asList(employeesArray);
    
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
    
    // --- Stream Operations ---

    // - forEach()
    @Test
    public void streamForEachTest() throws Exception {
        streamFromList.forEach(e -> e.salaryIncrement(10.0f));

        assertAll(
            () -> assertEquals(employeesList.get(0).getSalary(), 1.1 * SALARY1),
            () -> assertEquals(employeesList.get(1).getSalary(), 1.1 * SALARY2),
            () -> assertEquals(employeesList.get(2).getSalary(), 1.1 * SALARY3)
        );

        // forEach() is a terminal operation, which means that,
        // after the operation is performed, the stream pipeline is considered consumed, and can no longer be used
    }

    // - map()
    // produces a new stream after applying a function to each element of the original stream
    // the new stream could be of different type
    @Test
    public void mapTest() throws Exception {
        List<String> employeesNames = streamFromArray
            .map(Employee::getName)
            .collect(Collectors.toList());

        assertEquals(employeesArray.length, employeesNames.size());
    }

    // - collect()
    //  get stuff out of the stream once all the processing is done
    @Test
    public void collectTest() throws Exception {
        List<Employee> testEmployeesList = employeesList.stream().collect(Collectors.toList());
        
        assertEquals(testEmployeesList, employeesList);
    }

    // - filter()
    // produces a new stream that contains elements of the original stream that pass a given test
    @Test
    public void filterTest() throws Exception {
        List<Employee> result = streamFromArray
            .filter(i -> i.getId() == 2)
            .collect(Collectors.toList());

        assertEquals(result.size(), 1);
    }

    // - findFirst()
    // returns an Optional for the first entry in the stream
    @Test
    public void findFirstTest() throws Exception {
        Optional<Employee> employeeOptional = streamFromArray
            .filter(e -> e.getSalary() > SALARY1)
            .findFirst();

        // two employees has bigger salary than SALARY1 but the first found is in optional
        assertEquals(employeeOptional.orElse(null).getSalary(), SALARY2);
    }

    // - toArray()
    @Test
    public void toArrayTest() throws Exception {
        Employee[] testEmployeesArray = streamFromArray.toArray(Employee[]::new);

        assertEquals(testEmployeesArray.length, employeesArray.length);
    }

    // - flatMap()
    // stream can hold complex data structures like Stream<List<String>>
    // flatMap() helps to flatten the data structure to simplify further operations
    @Test
    public void flatMap() throws Exception {
        List<List<String>> namesNested = Arrays.asList( 
            Arrays.asList("Jeff", "Bezos"), 
            Arrays.asList("Bill", "Gates"), 
            Arrays.asList("Mark", "Zuckerberg")
        );

        List<String> names = namesNested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        assertEquals(names.size(), namesNested.size() * 2);
    }

    // - peek()
    // peek does the same things as forEach() but stream can be used more times
    @Test
    public void testPeek() throws Exception {
        List<Employee> testEmployeeList =  employeesList.stream()
            .peek(e -> e.salaryIncrement(10.0f))
            .peek(System.out::println)
            .collect(Collectors.toList());

        assertAll(
            () -> assertEquals(testEmployeeList.get(0).getSalary(), SALARY1 * 1.1),
            () -> assertEquals(testEmployeeList.get(1).getSalary(), SALARY2 * 1.1),
            () -> assertEquals(testEmployeeList.get(2).getSalary(), SALARY3 * 1.1)
        );
    }

}
    