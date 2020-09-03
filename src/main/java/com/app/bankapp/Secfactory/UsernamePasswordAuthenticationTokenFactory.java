package com.app.bankapp.Secfactory;



import com.app.bankapp.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsernamePasswordAuthenticationTokenFactory {

    public UsernamePasswordAuthenticationToken create(User u) {
        List<String> stringList = new ArrayList<>();
        stringList.add(u.getRole().name());

        List<GrantedAuthority> grantedAuthorityList = stringList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword(), grantedAuthorityList);
    }

}
