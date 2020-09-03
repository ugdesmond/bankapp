package com.app.bankapp.filter;


import com.app.bankapp.Secfactory.UsernamePasswordAuthenticationTokenFactory;
import com.app.bankapp.businesslogic.UserLogic;
import com.app.bankapp.model.User;
import com.app.bankapp.security.SecurityAppContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String UTF_8 = "UTF-8";
    //Begin index for bear. bc index of bearer is 7
    private static final int BEGIN_INDEX = 7;
    private final Logger logger = Logger.getLogger(this.getClass());



    private UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory;

    private SecurityAppContext securityAppContext;
    private UserLogic userLogic;


    @Autowired
    public JwtAuthenticationTokenFilter( SecurityAppContext securityAppContext, UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory, UserLogic userLogic) {
        this.securityAppContext = securityAppContext;
        this.usernamePasswordAuthenticationTokenFactory = usernamePasswordAuthenticationTokenFactory;
        this.userLogic = userLogic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(AUTHORIZATION);

        SecurityContext context = securityAppContext.getContext();
        if (authToken != null) {
            try {
                authToken = new String(authToken.substring(BEGIN_INDEX).getBytes(), UTF_8);

                if (context.getAuthentication() == null) {
                    logger.info("Checking authentication for token " + authToken);
                    User u = userLogic.validateUser(authToken, request.getRemoteAddr());
                    if (u != null) {
                        Authentication authentication = usernamePasswordAuthenticationTokenFactory.create(u);
                        context.setAuthentication(authentication);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                logger.error(e.getMessage());
            }

        }

        chain.doFilter(request, response);
    }

}