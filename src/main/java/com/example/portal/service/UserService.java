package com.example.portal.service;



import com.example.portal.model.dto.request.UserLoginRequest;
import com.example.portal.model.dto.request.UserRegistrationRequest;
import com.example.portal.model.dto.response.BasicResponse;
import com.example.portal.model.dto.response.UserLoginResponse;
import com.example.portal.model.entity.User;
import com.example.portal.model.exception.WebException;
import com.example.portal.repository.UserRepository;
import com.example.portal.util.Utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;



@Service
@Slf4j
public class UserService {

    @Value("${number.of.login.try}")
    private String numberOfLoginTry;
    @Value("${login.cooldown.window}")
    private String loginCoolDownWindow;

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public BasicResponse userRegistration(UserRegistrationRequest userRegistrationRequest) throws WebException {
        log.info("user Registration call ...");
        User findUser = userRepository.findByEmailAddress(userRegistrationRequest.getEmailAddress());
        if (findUser != null)
            throw new WebException("user already exist!");
        User user = new User();
        user.setUserType(userRegistrationRequest.getUserType());
        user.setEmailAddress(userRegistrationRequest.getEmailAddress());
        setPassword(userRegistrationRequest.getPassword(), user);
        userRepository.save(user);
        log.info("user Registration success  for user : "+user.getEmailAddress());
        return new BasicResponse(BasicResponse.Status.SUCCESS);
    }

    public UserLoginResponse userLogin(UserLoginRequest userLoginRequest) throws WebException {
        log.info("user login call ...");
        User user = userRepository.findByEmailAddress(userLoginRequest.getEmailAddress());
        if (user == null)
            throw new WebException("user not found!");
        avoidBruteForce(user.getUnsuccessfulLoginCount(), user.getLastUnsuccessfulLoginDate());
        if (!Utility.verifyPassword(user.getPasswordSalt(), user.getPasswordHash(), userLoginRequest.getPassword())) {
            if ((user.getUnsuccessfulLoginCount() != null))
                user.setUnsuccessfulLoginCount(user.getUnsuccessfulLoginCount() + 1);
            else user.setUnsuccessfulLoginCount(1);
            user.setLastUnsuccessfulLoginDate(new Date().toInstant().toEpochMilli());
            userRepository.save(user);
            throw new WebException("invalid username or password");
        }
        user.setUnsuccessfulLoginCount(0);
        userRepository.save(user);
        String token = tokenService.generateToken(user.getEmailAddress(), user.getUserType());
        log.info("user login success and token create for user : "+user.getEmailAddress());
        return new UserLoginResponse(token, user, user.getUserType());
    }


    private void setPassword(String password, User user) throws WebException {
        String salt = Utility.generateSeed();
        String passwordHash = Utility.sha256(password.concat(salt).getBytes());
        user.setPasswordHash(passwordHash);
        user.setPasswordSalt(salt);
    }

    private void avoidBruteForce(Integer numberOfRetry, Long lastTryDate) throws WebException {
        if (numberOfRetry != null && numberOfRetry > Integer.parseInt(numberOfLoginTry)) {
            int coefficient = numberOfRetry % Integer.parseInt(numberOfLoginTry);
            int coolDown = coefficient * Integer.parseInt(loginCoolDownWindow) * 60 * 1000;
            if (lastTryDate + coolDown > new Date().toInstant().toEpochMilli())
                throw new WebException("login temporary disabled for this account try later");
        }
    }
}
