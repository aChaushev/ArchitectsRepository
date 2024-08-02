package aChaushev.architects.service.impl;


import aChaushev.architects.model.dto.UserDetailsDTO;
import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.repository.UserRoleRepository;
import aChaushev.architects.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(
            UserRepository userRepository, UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return false;
        }

        Optional<User> userByNameAndEmail = userRepository
                .findByUsernameAndEmail(
                        userRegisterDTO.getUsername(),
                        userRegisterDTO.getEmail());
        if (userByNameAndEmail.isPresent()) {
            return false;
        }

        User user = this.modelMapper.map(userRegisterDTO, User.class);

        user.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));

        // Assign default role USER
        UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: USER"));
        user.setRoles(List.of(userRole));

        this.userRepository.save(user);

        return true;
    }

    @Override
    public void saveUser(User user, UserRoleEnum roleEnum) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setEnabled(true);
        UserRole userRole = userRoleRepository.findByRole(roleEnum)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleEnum));
        user.setRoles(List.of(userRole));
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(Long id, UserDetailsDTO userDetails) {
        // Find the existing user by ID
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Update fields
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        // Update the password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Save the updated user
        userRepository.save(user);
    }

}
