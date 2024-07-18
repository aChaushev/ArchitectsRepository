package aChaushev.architects.web;


import aChaushev.architects.model.entity.User;
import aChaushev.architects.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void testRegistration() throws Exception {

    mockMvc.perform(post("/users/register")
        .param("email", "anna@example.com")
        .param("password", "123")
            .with(csrf())
    ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

    Optional<User> userEntityOpt = userRepository.findByEmail("anna@example.com");

    Assertions.assertTrue(userEntityOpt.isPresent());

    User userEntity = userEntityOpt.get();

    Assertions.assertEquals("Anna", userEntity.getUsername());

    Assertions.assertTrue(passwordEncoder.matches("123", userEntity.getPassword()));
  }
}
