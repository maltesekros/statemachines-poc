package com.christophermicallef.poc.statemachines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegistrationService {

    @Autowired
    StateMachineFactory<States, Events> factory;

    @Autowired
    private StateMachinePersister<States, Events, String> persister;

    public boolean saveCorrectCustomerDetails(String name) throws Exception {
        StateMachine stateMachine = getStateMachine(name);
        boolean result = stateMachine.sendEvent(Events.ENTER_CORRECT_CUSTOMER_DETAILS);
        persister.persist(stateMachine, name);
        return result;
    }

    public boolean receiveEmailConfirmation(String name) throws Exception {
        StateMachine stateMachine = getStateMachine(name);
        boolean result = stateMachine.sendEvent(Events.RECEIVE_EMAIL_CONFIRMATION);
        persister.persist(stateMachine, name);
        return result;
    }

    private StateMachine getStateMachine(String name) throws Exception {
        return persister.restore(factory.getStateMachine(), name);
    }
}
