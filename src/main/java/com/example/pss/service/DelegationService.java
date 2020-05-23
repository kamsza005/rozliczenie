package com.example.pss.service;

import com.example.pss.model.Delegation;
import com.example.pss.model.User;
import com.example.pss.repository.DelegationRep;
import com.example.pss.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelegationService {

    DelegationRep delegationRep;
    UserRep userRep;

    @Autowired
    public DelegationService(DelegationRep delegationRep, UserRep userRep) {
        this.delegationRep = delegationRep;
        this.userRep = userRep;
    }

    public List<Delegation> getAllDelegations() {
        return delegationRep.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public List<Delegation> getAllByUserId(long id) {
        return delegationRep.findAllByUser(userRep.findById(id).get());
    }

    public Delegation getDelegation(long id) {
        return delegationRep.findById(id).get();
    }

    public Delegation createDelegation(Delegation delegation) {
        return delegationRep.save(delegation);
    }

    public Delegation updateDelegation(Delegation delegation) {
        return delegationRep.save(delegation);
    }

    public void deleteDelegation(long userId, long delegationId) {
        Delegation delegation = delegationRep.findById(delegationId).get();

        User user = userRep.findById(userId).get();
        user.getDelegations().remove(delegation);
        userRep.save(user);

        delegationRep.deleteById(delegationId);
    }

    public void deleteEmptyDelegation(Delegation delegation) {
        delegationRep.delete(delegation);
    }
}
