package com.christophermicallef.poc.statemachines;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class InMemoryPersist
        implements StateMachinePersist<States, Events, String> {

    private HashMap<String, StateMachineContext<States, Events>> storage
            = new HashMap<>();

    @Override
    public void write(StateMachineContext<States, Events> context,
                      String contextObj) {
        storage.put(contextObj, context);
    }

    @Override
    public StateMachineContext<States, Events> read(String contextObj) throws Exception {
        return storage.get(contextObj);
    }
}