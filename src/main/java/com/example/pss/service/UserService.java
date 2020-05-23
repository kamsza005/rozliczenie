package com.example.pss.service;

import com.example.pss.repository.DelegationRep;
import com.example.pss.repository.RoleRep;
import com.example.pss.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.pss.model.User;

import java.util.List;

@Transactional
@Service
public class UserService {

    private UserRep userRep;
    private RoleRep roleRep;
    private DelegationRep delegationRep;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRep userRep, RoleRep roleRep,
                       DelegationRep delegationRep, PasswordEncoder passwordEncoder) {
        this.userRep = userRep;
        this.roleRep = roleRep;
        this.delegationRep = delegationRep;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRep.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public User getUser(long id) {
        return userRep.findById(id).get();
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRep.save(user);
    }

    public User updateUser(User user) {
        return userRep.save(user);
    }

    public void deleteUserById(long id) {
        roleRep.deleteFrom_ROLE_USER_ByUserId(id);
        delegationRep.deleteFrom_DELEGATION_ByUserId(id);
        userRep.deleteById(id);
    }

    public void changePassword(long id, String password) {
        User user = userRep.findById(id).get();
        user.setPassword(passwordEncoder.encode(password));
        userRep.save(user);
    }

    public List<User> findAllByEmail(String email) {
        return userRep.findAllByEmail(email);
    }
}
