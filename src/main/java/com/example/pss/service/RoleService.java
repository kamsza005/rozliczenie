package com.example.pss.service;

import com.example.pss.repository.RoleRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.pss.model.Role;
import com.example.pss.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    RoleRep roleRep;

    @Autowired
    public RoleService(RoleRep roleRep) {
        this.roleRep = roleRep;
    }

    public List<Role> getAllRoles() {
        return roleRep.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Role getRole(long id) {
        return roleRep.findById(id).get();
    }

    public Role createRole(Role role) {
        return roleRep.save(role);
    }

    public void updateRole(Role role) {
        roleRep.save(role);
    }

    public List<Role> findAllByRoleName(String name) {
        return roleRep.findAllByRoleName(name);
    }

    public List<User> getAllUsersByRoleName(String roleName) {
        return roleRep.findAllByRoleName(roleName)
                .stream()
                .flatMap(role -> role.getUsers().stream())
                .collect(Collectors.toList());
    }
}
