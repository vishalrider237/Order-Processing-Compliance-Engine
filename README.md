1. Testing strategy :
        This project follows Test Driven Development (TDD) and focuses on isolated, reliable unit testing.
        Testing Approach
       JUnit 5 (Jupiter) for test execution
        Mockito for mocking dependencies
       H2 in-memory database for lightweight integration testing
      Business logic tested independently from infrastructure
      Key Testing Practices:-
    i) FIRST principles (Fast, Independent, Repeatable, Self-validating, Timely)
    ii) AAA pattern (Arrange, Act, Assert)
    iii) Readable test names using @DisplayName
    iv) Test lifecycle management using @BeforeEach and @TestInstance
    v) Parameterized tests using @CsvSource and @MethodSource
    vi) Controlled execution order using @TestMethodOrder

2)Test Coverage:
      JaCoCo is used for measuring code coverage
      HTML report available at:root project-> index.html
      Primary focus on:
          Service layer
          Business rules
          Validation logic
      Coverage Gaps (Intentional):
         Configuration classes
        DTOs / model getters & setters
        Simple custom exception classes
These areas contain no business logic and are validated indirectly through service tests.

3)Design Decisions
     Layered architecture (Controller → Service → Repository)
     H2 database chosen for simplicity and fast testing
     Custom exceptions improve clarity and test assertions
     Property dependency injection for loose coupling(@Autowired)
     Strict separation of:
         Unit tests (Mockito, no Spring context)
         Integration tests (H2)
