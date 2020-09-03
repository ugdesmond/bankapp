
package com.app.bankapp.handler;


import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns a 401 error code (Unauthorized) to the client, when Ajax authentication fails.
 */
@Component
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    HeaderHandler headerHandler;
    Logger logger = Logger.getLogger(AjaxAuthenticationFailureHandler.class);
    Gson gson = new Gson();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        logger.debug("Authentication Failure");
        Map<Object, Object> map = new HashMap<>();
        map.put("isSuccessful", Boolean.FALSE);
        map.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("message", "Authentication failure!,Username/Password is incorrect");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        headerHandler.process(request, response);
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(map));

    }

}