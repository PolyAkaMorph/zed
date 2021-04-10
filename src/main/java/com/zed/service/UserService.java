package com.zed.service;

import com.zed.dto.RegistrationInfo;
import com.zed.repository.PreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class UserService {
    @Autowired
    PreparedStatement preparedStatement;

    @Transactional
    public void register(RegistrationInfo registrationInfo) {
        log.debug("Registrating user with info {}", registrationInfo.toString());
        Integer newUserId = preparedStatement.createNewUser(registrationInfo.getLogin(), registrationInfo.getPassword());
        preparedStatement.createNewPersonal(registrationInfo.getPersonInfo(), newUserId);
        log.debug("Successful registration");
    }

    public boolean isUserReadyForRegister(RegistrationInfo registrationInfo) {
        if (Objects.isNull(registrationInfo)) {
            registrationInfo.setError("No data!");
            return false;
        }
        if (Strings.isEmpty(registrationInfo.getLogin())) {
            registrationInfo.setError("Login is required!");
            return false;
        }
        if (Strings.isEmpty(registrationInfo.getPassword())) {
            registrationInfo.setError("Password is required!");
            return false;
        }
        if (!registrationInfo.getPassword().equals(registrationInfo.getPasswordCheck())) {
            registrationInfo.setError("Passwords dont match!");
            return false;
        }
        if (preparedStatement.checkIfUserExists(registrationInfo)) {
            registrationInfo.setError("Login allready exists!");
            return false;
        }
        return true;
    }
}
