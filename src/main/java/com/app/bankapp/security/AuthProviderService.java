package com.app.bankapp.security;


import com.app.bankapp.Secfactory.UsernamePasswordAuthenticationTokenFactory;
import com.app.bankapp.businesslogic.UserLogic;
import com.app.bankapp.handler.HeaderHandler;
import com.app.bankapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class AuthProviderService implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthProviderService.class);

    final UserLogic userLogic;
    final HeaderHandler headerHandler;
    final UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory;

    public AuthProviderService(UserLogic userLogic, HeaderHandler headerHandler, UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory) {
        this.userLogic = userLogic;
        this.headerHandler = headerHandler;
        this.usernamePasswordAuthenticationTokenFactory = usernamePasswordAuthenticationTokenFactory;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        LOGGER.info("Doing login username" + login);
        LOGGER.info("Doing login password " + password);
        User u = userLogic.isLoginValid(login, password);
        if (u != null) {
            LOGGER.info("Login successful. User: " + login);
            return usernamePasswordAuthenticationTokenFactory.create(u);
        }
        throw new UsernameNotFoundException("Not valid login/password");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
