
package com.app.bankapp.handler;


import com.app.bankapp.businesslogic.UserLogic;
import com.app.bankapp.model.MessageResponse;
import com.app.bankapp.model.dto.UserDTO;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    final HeaderHandler headerHandler;
    private final Gson gson ;
    private final UserLogic userLogic;


    public AjaxAuthenticationSuccessHandler(HeaderHandler headerHandler, Gson gson,UserLogic userLogic) {
        this.headerHandler = headerHandler;
        this.gson = gson;
        this.userLogic= userLogic;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDTO user = userLogic.createUserToken(authentication.getName(), request.getRemoteAddr());
        MessageResponse<UserDTO> messageResponse = new MessageResponse<>();
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("Logged in successfully");
        messageResponse.setData(user);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(messageResponse));
        response.setStatus(HttpServletResponse.SC_OK);
        headerHandler.process(request, response);
    }

}
