package com.app.bankapp.businesslogic;

import com.app.bankapp.model.User;
import com.app.bankapp.model.enums.UserRoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainSeederLogic implements CommandLineRunner {

    private UserLogic userLogic;

    public MainSeederLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @Override
    public void run(String... args) {
        List<User> userList = userLogic.getByColumnName("email", "admin@gmail.com");
        if (userList.isEmpty()) {
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setPassword("admin");
            user.setFullName("admin");
            user.setRole(UserRoleEnum.ADMIN);
            userLogic.create(user);
        }
    }
}