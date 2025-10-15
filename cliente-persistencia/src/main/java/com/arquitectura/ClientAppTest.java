package com.arquitectura;

import com.arquitectura.configdb.ConfiguracionPersistencia;
import com.arquitectura.entidades.User;
// We remove the import for IMessageService as it cannot be resolved
import com.arquitectura.persistence.UserRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientAppTest {

    public static void main(String[] args) {
        System.out.println("Starting Spring context...");

        // 1. Initialize Spring, loading our configuration
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfiguracionPersistencia.class);

        // 2. Get the UserRepository bean that Spring created
        UserRepository userRepository = context.getBean(UserRepository.class);

        System.out.println("Spring context started successfully. UserRepository retrieved.");

        // 3. Let's test the repository directly
        try {
            // Create and save a new user
            User testUser = new User();
            testUser.setUsername("localUser");
            User savedUser = userRepository.save(testUser);
            System.out.println("SUCCESS: Saved a test user directly via repository with ID: " + savedUser.getId());

            // Verify that we can find the user we just saved
            long userCount = userRepository.count();
            System.out.println("Verification: The database now contains " + userCount + " user(s).");

        } catch (Exception e) {
            System.err.println("!!! TEST FAILED !!!");
            e.printStackTrace();
        }

        // 4. Close the Spring context
        context.close();
    }
}