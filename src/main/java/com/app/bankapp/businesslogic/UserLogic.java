package com.app.bankapp.businesslogic;

import com.app.bankapp.jwt.JwtService;
import com.app.bankapp.model.User;
import com.app.bankapp.model.dto.UserDTO;
import com.app.bankapp.repository.AbstractJpaDao;
import org.hibernate.query.Query;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.modelmapper.ModelMapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Component
public class UserLogic extends AbstractJpaDao<User> {
    private JwtService jwtService;
    private ModelMapper modelMapper;


    public UserLogic(JwtService jwtService, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;

    }

    public User isLoginValid(String email, String pass) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(pass)) {
            return null;
        }
        List<User> userList = getByColumnName("email", email);
        if (userList == null || userList.isEmpty()) {
            return null;
        }
        User u = userList.get(0);
        if (!u.getPassword().equals(pass)) {
            return null;
        }

        return u;
    }
    public UserDTO createUserToken(String username, String secret) {
        User u = getByColumnName("email", username).get(0);
        Map<String, Object> userMap = new HashMap<>();
        UserDTO userDTO =convertToDto(u, UserDTO.class);
        userDTO.setRole(u.getRole().name());
        userMap.put("user", userDTO);
        String token = jwtService.createToken(u.getEmail(), userMap,secret,getExpirationDate());
        userDTO.setToken(token);
        userDTO.setTokenExpiryTime(getExpirationDate());
        return userDTO;
    }

    public User validateUser(String token, String secret) {
        return jwtService.isValid(token, secret);
    }


    public List<User> getByColumnName(String columnName, Object value) {
        List<User> userList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<User> query = getCurrentSession().createQuery(cr);
            userList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }


    public <T> T convertToDto(Object entityObject, final Class<T> tClass) {
        T postDto = modelMapper.map(entityObject, tClass);
        return postDto;
    }

    public Date getExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 1);
        return c.getTime();
    }

    public String generate() {
        return BCrypt.gensalt();
    }
}
