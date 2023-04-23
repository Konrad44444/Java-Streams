package streams;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class EmployeeTest {
    static final Integer ID1 = 1;
    static final Integer ID2 = 2;
    static final Integer ID3 = 3;
    static final String NAME1 = "Bulbek";
    static final String NAME2 = "Adam";
    static final String NAME3 = "Ewa";
    static final Double SALARY1 = Double.valueOf(10000.0);
    static final Double SALARY2 = Double.valueOf(20000.0);
    static final Double SALARY3 = Double.valueOf(30000.0);

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
        streamFromList.forEach(e -> e.salaryIncrement(Double.valueOf(10.0)));

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
            .peek(e -> e.salaryIncrement(Double.valueOf(10.0)))
            .peek(System.out::println)
            .collect(Collectors.toList());

        assertAll(
            () -> assertEquals(testEmployeeList.get(0).getSalary(), SALARY1 * 1.1),
            () -> assertEquals(testEmployeeList.get(1).getSalary(), SALARY2 * 1.1),
            () -> assertEquals(testEmployeeList.get(2).getSalary(), SALARY3 * 1.1)
        );
    }

    // --- Comparison Based Stream Operations ---

    // - sorted()
    @Test
    public void testSorted() throws Exception {
        List<Employee> testList = streamFromArray
            .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
            .collect(Collectors.toList());

        assertAll(
            () -> assertEquals(testList.get(0).getName(), NAME2),
            () -> assertEquals(testList.get(1).getName(), NAME1),
            () -> assertEquals(testList.get(2).getName(), NAME3)
        );
    }

    // - min() and max()
    // they return Optional
    @Test
    public void testFindMinID() throws Exception {
        Employee e = streamFromArray
            .min((e1, e2) -> e1.getId() - e2.getId())
            .orElseThrow(NoSuchElementException::new);

        assertEquals(e.getId(), ID1);
    }

    @Test
    public void testFindMaxSalary() throws Exception {
        Employee e = streamFromArray
            .max(Comparator.comparing(Employee::getSalary))
            .orElseThrow(NoSuchElementException::new);

        assertEquals(e.getSalary(), SALARY3);
    }

    // - distinct()
    // removes duplicates from stream
    @Test
    public void testDistinct() throws Exception {
        List<Integer> integers = Arrays.asList(1, 2, 3, 2, 1, 4, 5, 5, 1);
        List<Integer> integersFromStream = integers.stream().distinct().collect(Collectors.toList());

        assertEquals(integersFromStream.size(), 5);
    }

    // - allMatch(), anyMatch(), noneMatch()
    // they all take predicate and return boolean
    @Test
    public void testMatches() throws Exception {
        List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);
    
        boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
        boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
        boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);
    
        assertFalse(allEven);
        assertTrue(oneEven);
        assertFalse(noneMultipleOfThree);
    }


    // --- Stream Specializations ---

    // - creation
    // mapToInt() 
    @Test
    public void testMapToInt() throws Exception {
        Integer result = streamFromArray
            .mapToInt(Employee::getId)
            .max()
            .orElseThrow(NoSuchElementException::new);

        assertEquals(ID3, result);
    }

    // - other ways to create IntStream
    IntStream intStream1 = IntStream.of(1, 2, 3, 4, 5);
    IntStream intStream2 = IntStream.range(10, 19); // values 10, 11, ..., 19

    // create Stream<Integer> from IntStream
    Stream<Integer> streamFromIntStream = intStream1.boxed();

    // - specialized streams provide additional operations, for example: sum(), average(), range()...
    @Test
    public void testAverageIntStream() {
        Double avgSalary = streamFromArray
            .mapToDouble(Employee::getSalary)
            .average()
            .orElseThrow(NoSuchElementException::new);

        assertEquals(avgSalary, 20000.0);
    }

    // -- other specialized streams
    // mapToLong, mapToDouble
    
}
    