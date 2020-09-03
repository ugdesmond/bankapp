package com.app.bankapp.businesslogic;

import com.app.bankapp.model.Account;
import com.app.bankapp.model.Transaction;
import com.app.bankapp.repository.AbstractJpaDao;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionLogic  extends AbstractJpaDao<Transaction> {
    private ModelMapper modelMapper;

    public TransactionLogic(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T> T convertToDto(Object entityObject, final Class<T> tClass) {
        T postDto = modelMapper.map(entityObject, tClass);
        return postDto;
    }

    public List<Transaction> getByColumnName(String columnName, Object value) {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Transaction> cr = cb.createQuery(Transaction.class);
            Root<Transaction> root = cr.from(Transaction.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Transaction> query = getCurrentSession().createQuery(cr);
            transactionList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    public List<Transaction> getUserTransactions( Account accountNumber) {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Transaction> cr = cb.createQuery(Transaction.class);
            Root<Transaction> root = cr.from(Transaction.class);
            Predicate senderRestriction = cb.equal(root.get("accountTransferFrom"), accountNumber);
            Predicate recipientRestriction = cb.equal(root.get("accountTransferTo"), accountNumber);
            Predicate depositWithdrawalRestriction = cb.equal(root.get("depositWithdrawalAccount"), accountNumber);
            Predicate restrictions = cb.or(senderRestriction,recipientRestriction,depositWithdrawalRestriction);
            cr.select(root).where(restrictions).orderBy(cb.asc(root.get("id")));
            Query<Transaction> query = getCurrentSession().createQuery(cr);
            transactionList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionList;
    }
}
