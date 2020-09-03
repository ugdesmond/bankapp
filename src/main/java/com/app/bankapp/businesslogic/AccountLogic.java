package com.app.bankapp.businesslogic;

import com.app.bankapp.model.Account;
import com.app.bankapp.repository.AbstractJpaDao;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class AccountLogic extends AbstractJpaDao<Account> {
    private ModelMapper modelMapper;


    public AccountLogic(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

    }
    public List<Account> getByColumnName(String columnName, Object value) {
        List<Account> userList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Account> cr = cb.createQuery(Account.class);
            Root<Account> root = cr.from(Account.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Account> query = getCurrentSession().createQuery(cr);
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

    public String generateAccountNumber() throws Exception {
        char[] VALID_CHARACTERS = "0123456789".toCharArray();
       List<Account> accounts;
        String accountNumber = null;
        SecureRandom secureRandom = new SecureRandom();
        char[] buff = new char[10];
        Random rand = new Random();

        boolean check = true;
        while (check) {
            for (int i = 0; i < 10; ++i) {
                if ((i % 9) == 0) {
                    rand.setSeed(secureRandom.nextLong());
                }
                buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
            }
            accountNumber = new String(buff);
            accounts = getByColumnName("accountNumber",accountNumber);
            if (accounts.isEmpty()) {
                check = false;
            }
        }
        return accountNumber;
    }
}
