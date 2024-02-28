package com.tom.banking.service;

import com.tom.banking.domain.User;
import com.tom.banking.domain.UserDTO;
import com.tom.banking.repository.UserRepository;
import com.tom.banking.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Util util;

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User findUserByIBAN(String IBAN) {
        return userRepository.findUserByIBAN(IBAN);
    }

    public void updateUser(UserDTO userDTO, User existingUser) {

        existingUser.setPassword(util.hashPassword(userDTO.getPassword()));
        existingUser.setEmail(userDTO.getEmail());

        userRepository.updateUser(existingUser.getPassword(), existingUser.getEmail());

    }
    public void createUser(UserDTO UserDTO) {

        User newUser = new User();

        newUser.setFirstName(UserDTO.getFirstName());
        newUser.setLastName(UserDTO.getLastName());
        newUser.setIBAN(UserDTO.getIBAN());
        newUser.setBalance(UserDTO.getBalance());
        newUser.setUsername(UserDTO.getUsername());
        newUser.setPassword(UserDTO.getPassword());
        newUser.setBirthDate(UserDTO.getBirthDate());
        newUser.setEmail(UserDTO.getEmail());

        userRepository.saveUser(newUser.getFirstName(), newUser.getLastName(), newUser.getIBAN(), newUser.getBalance(), newUser.getUsername(), newUser.getPassword(), newUser.getBirthDate(), newUser.getEmail());
    }
}
