import com.study.StudyUp.DTOs.UserRegistrationDto;
import com.study.StudyUp.exception.UserAlreadyExistsException;
import com.study.StudyUp.model.User;
import com.study.StudyUp.repository.UserRepository;
import com.study.StudyUp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_successfulRegistration() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("tester");
        dto.setPassword("password");

        when(userRepository.existsByUsername("tester")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashed");

        User mockUser = new User();
        mockUser.setUsername("tester");
        mockUser.setPassword("hashed");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User savedUser = userService.registerUser(dto);

        // Assert
        assertNotNull(savedUser);
        assertEquals("tester", savedUser.getUsername());
        assertEquals("hashed", savedUser.getPassword());
    }

    @Test
    void findByUsername_returnsUser() {
        // Arrange
        User mockUser = new User("tester", "hashed");
        when(userRepository.findByUsername("tester")).thenReturn(java.util.Optional.of(mockUser));

        // Act
        User foundUser = userService.findByUsername("tester");

        // Assert
        assertNotNull(foundUser);
        assertEquals("tester", foundUser.getUsername());
        assertEquals("hashed", foundUser.getPassword());
    }

    @Test
    void existsByUsername_returnsBoolean() {
        // Arrange
        when(userRepository.existsByUsername("tester")).thenReturn(true);

        // Act
        boolean exists = userService.existsByUsername("tester");

        // Assert
        assertTrue(exists);
    }

    @Test
    void registerUser_usernameAlreadyExists_throwsException() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("tester");
        dto.setPassword("password");

        when(userRepository.existsByUsername("tester")).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(dto));
    }
}
