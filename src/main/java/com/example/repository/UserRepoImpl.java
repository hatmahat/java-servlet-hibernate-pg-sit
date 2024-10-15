package com.example.repository;

import com.example.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void saveUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @Transactional
    public List<User> listUsers() {
        return sessionFactory.getCurrentSession().createQuery("from User", User.class).list();
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }
}
