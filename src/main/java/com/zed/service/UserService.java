package com.zed.service;

import com.zed.dto.PersonInfo;
import com.zed.dto.RegistrationInfo;
import com.zed.repository.PreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    @Autowired
    PreparedStatement preparedStatement;
    @Autowired
    SecurityService securityService;

    @Transactional
    public void register(RegistrationInfo registrationInfo) {
        log.debug("Registrating user with info {}", registrationInfo.toString());
        Integer newUserId = preparedStatement.createNewUser(registrationInfo.getLogin(), registrationInfo.getPassword());
        Integer newPersonId = preparedStatement.createNewPersonal(registrationInfo.getPersonInfo(), newUserId);
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
        if (!Objects.isNull(preparedStatement.getUserID(registrationInfo.getLogin()))) {
            registrationInfo.setError("Login allready exists!");
            return false;
        }
        return true;
    }

    public void editPersonal(PersonInfo personInfo) {
        String login = securityService.getCurrentLogin();
        preparedStatement.updatePersonInfo(login, personInfo);
    }

    public PersonInfo getCurrentPerson() {
        String login = securityService.getCurrentLogin();
        return preparedStatement.getPersonInfo(login);
    }

    public List<RegistrationInfo> getAllPersons() {
        return preparedStatement.getAllPersons(securityService.getCurrentLogin());
    }
}
