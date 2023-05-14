package com.example.portal.configuration.aop;



import com.example.portal.model.dto.internal.AccessToken;
import com.example.portal.model.dto.internal.UserType;
import com.example.portal.model.entity.SignedOutToken;
import com.example.portal.model.entity.User;
import com.example.portal.model.exception.UnauthorizedAccess;
import com.example.portal.model.exception.WebException;
import com.example.portal.repository.SignedOutTokenRepository;
import com.example.portal.repository.UserRepository;
import com.example.portal.service.TokenService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Aspect
public class Aspects {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final SignedOutTokenRepository signedOutTokenRepository;
    @Value("${token.expiration.window}")
    private String tokenExpirationWindow;

    public Aspects(TokenService tokenService, UserRepository userRepository, SignedOutTokenRepository signedOutTokenRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.signedOutTokenRepository = signedOutTokenRepository;
    }

    @Around("@annotation(RequiresAuthorization)")
    public Object aroundAuthorization(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        String authorizationHeader = request.getHeader("Authorization");
        if ((authorizationHeader == null || authorizationHeader.isEmpty()))
            throw new UnauthorizedAccess("Access denied");
        String token = authorizationHeader.replace("Bearer", "").replace(" ", "");
        AccessToken accessToken = tokenService.parseToken(token);
        if (accessToken.getIssuanceDate().toInstant().plus(Integer.parseInt(tokenExpirationWindow), ChronoUnit.DAYS).isBefore(new Date().toInstant()))
            throw new UnauthorizedAccess("Token expired");
        SignedOutToken signedOutToken = signedOutTokenRepository.findByTokenId(accessToken.getTokenId());
        if (signedOutToken != null) throw new UnauthorizedAccess("Invalid token");

        User user = userRepository.findByEmailAddress(accessToken.getUserId());

        if (user == null)
            throw new UnauthorizedAccess("Invalid token");

        args[0] = accessToken;
        request.setAttribute("userId", accessToken.getUserId());
        return pjp.proceed(args);
    }

    @Around("@annotation(RequiresUserByUsername)")
    public Object aroundUserFetchByUsername(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        User user = userRepository.findByEmailAddress((String) args[0]);
        if (user == null) throw new WebException("user not found");
        args[0] = user;
        return pjp.proceed(args);
    }

    @Around("@annotation(RequiresUserByToken)")
    public Object aroundUserFetchByToken(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        AccessToken accessToken = (AccessToken) args[0];
        User user = userRepository.findByEmailAddress(accessToken.getUserId());
        if (user == null) throw new WebException("user not found");
        args[0] = user;
        return pjp.proceed(args);
    }

    @Around("@annotation(RequiresOnlyNormalUser)")
    public Object aroundOnlyNormalUser(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        AccessToken accessToken = (AccessToken) args[0];
        if (!accessToken.getUserType().equals(UserType.NORMAL))
            throw new WebException("only normal user allowed");
        return pjp.proceed(args);
    }

    @Around("@annotation(RequiresOnlyAdminUser)")
    public Object aroundOnlyAdminUser(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        AccessToken accessToken = (AccessToken) args[0];
        if (!accessToken.getUserType().equals(UserType.ADMIN))
            throw new WebException("only admin user allowed");
        return pjp.proceed(args);
    }
}