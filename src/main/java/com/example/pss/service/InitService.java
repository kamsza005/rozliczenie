package com.example.pss.service;

import com.example.pss.model.*;
import com.example.pss.repository.DelegationRep;
import com.example.pss.repository.RoleRep;
import com.example.pss.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;

@Service
public class InitService {

    private UserRep userRep;
    private RoleRep roleRep;
    private DelegationRep delegationRep;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitService(UserRep userRep, RoleRep roleRep,
                       DelegationRep delegationRep, PasswordEncoder passwordEncoder) {
        this.userRep = userRep;
        this.roleRep = roleRep;
        this.delegationRep = delegationRep;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        roleRep.deleteAllInBatch();
        delegationRep.deleteAllInBatch();
        userRep.deleteAllInBatch();

        Role r1 = new Role("Role1");
        Role r2 = new Role("Role2");
        Role r3 = new Role("Role3");

        Delegation d1 = new Delegation("Delegacja nr 1", LocalDate.now(), LocalDate.now().plusDays(5),
                50, 1, 1, 2, TransportEnum.auto, 0,
                AutoCapacityEnum.ponad_900, 100.00, 100, 50, 50);
        Delegation d2 = new Delegation("Delegacja nr 2", LocalDate.now(), LocalDate.now().plusDays(10),
                60, 2, 1, 0, TransportEnum.bus, 0,
                AutoCapacityEnum.mniej_row_900, 200.00, 100, 50, 50);
        Delegation d3 = new Delegation("Delegacja nr 3", LocalDate.now(), LocalDate.now().plusDays(15),
                50, 1, 1, 1, TransportEnum.pociag, 34.50,
                AutoCapacityEnum.NONE, 0.0, 100, 50, 50);

        User u1 = new User("abc", "Bydgoszcz", "nip1",
                "Jan", "Kowalski", "kowalski@gmail.com", passwordEncoder.encode("abc123"));
        User u2 = new User("def", "Warszawa", "nip2",
                "Jerzy", "Nowak", "nowak@wp.pl", passwordEncoder.encode("nowak123"));


        r1.addUser(u1);
        r2.addUser(u1);
        r2.addUser(u2);
        r3.addUser(u2);

        d1.addUser(u1);
        d2.addUser(u1);
        d3.addUser(u2);

        userRep.saveAll(Arrays.asList(u1, u2));
        roleRep.saveAll(Arrays.asList(r1, r2, r3));
        delegationRep.saveAll(Arrays.asList(d1, d2));

        userRep.findAll().forEach(user -> {
            System.out.println(user);
            System.out.println(user.getRoles());
            System.out.println(user.getDelegations());
        });


    }
}
