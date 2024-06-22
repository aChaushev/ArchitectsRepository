package aChaushev.architects.service.impl;


import aChaushev.architects.model.dto.UserLoginDTO;
import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.UserService;
import aChaushev.architects.user.LoggedUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final LoggedUser loggedUser;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder, ModelMapper modelMapper, LoggedUser loggedUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.loggedUser = loggedUser;
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

        this.userRepository.save(user);

        return true;
    }

    @Override
    public boolean login(UserLoginDTO userLoginDTO) {
        Optional<User> user = userRepository
                .findByUsername(userLoginDTO.getUsername());

        if (user.isPresent() &&
                passwordEncoder.matches(userLoginDTO.getPassword(), user.get().getPassword())) {

            this.loggedUser.login(user.get());
            return true;
        }

        return false;
    }

    @Override
    public void logout() {
        this.loggedUser.logout();
    }
}
