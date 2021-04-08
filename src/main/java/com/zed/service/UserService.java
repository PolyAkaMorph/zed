package com.zed.service;

import com.zed.dto.RegistrationInfo;
import com.zed.repository.PreparedStatement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    PreparedStatement preparedStatement;

    public void register(RegistrationInfo registrationInfo) {
        log.debug("Registrating user with info {}", registrationInfo.toString());
        //todo create password hash
        //todo transactional
        //todo check if exists

        Integer newUserId = preparedStatement.createNewUser(registrationInfo.getLogin(), registrationInfo.getPassword());

        preparedStatement.createNewPersonal(registrationInfo.getPersonInfo(), newUserId);
        log.debug("Successful registration");
    }
}
