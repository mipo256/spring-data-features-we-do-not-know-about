package io.mpolivaha;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public class AbstractIntegrationTest {

  public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
      .withDatabaseName("local")
      .withPassword("local")
      .withUsername("local")
      .withExposedPorts(5432);

  @DynamicPropertySource
  static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "spring.datasource.url",
        () -> String.format("jdbc:postgresql://%s:%d/local", postgreSQLContainer.getHost(), postgreSQLContainer.getFirstMappedPort())
    );
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
  }

  @BeforeAll
  static void beforeAll() {
    postgreSQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    postgreSQLContainer.close();
  }
}