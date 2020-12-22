package com.christophermicallef.poc.statemachines;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableStateMachineFactory
public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
            .withStates()
            .initial(States.SHOW_REGISTRATION_FORM)
            .end(States.USER_REGISTERED)
            .states(
                    new HashSet<>(Arrays.asList(States.WAITING_CONFIRMATION)));
    }

    @Bean
    public Action<States, Events> sendConfirmationEmail() {
        return ctx -> System.out.println("* * * Sending email ... * * *");
    }

    @Bean
    public Action<States, Events> registerUser() {
        return ctx -> System.out.println("* * * Registering user ... * * *");
    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {

        transitions.withExternal()
            .source(States.SHOW_REGISTRATION_FORM).target(States.WAITING_CONFIRMATION).event(Events.ENTER_CORRECT_CUSTOMER_DETAILS)
                .action(sendConfirmationEmail()).and()
            .withExternal()
            .source(States.WAITING_CONFIRMATION).target(States.USER_REGISTERED).event(Events.RECEIVE_EMAIL_CONFIRMATION)
                .action(registerUser());
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
            .withConfiguration()
            .autoStartup(true)
            .listener(new StateMachineListener());
    }

    private static final class StateMachineListener extends StateMachineListenerAdapter<States, Events> {
        @Override
        public void stateChanged(State<States, Events> from, State<States, Events> to) {
            System.out.printf("Customer state changed to %s%n", to.getId());
        }
        @Override
        public void eventNotAccepted(Message<Events> event) {
            System.out.printf("Event not accepted %s%n", event.toString());
        }
        @Override
        public void transition(Transition<States, Events> transition) {
            System.out.printf("Transition %s -> %s%n", transition.getSource() != null ? transition.getSource().getId() : "null", transition.getTarget().getId());
        }
    }

    @Bean
    public StateMachinePersist<States, Events, String> inMemoryPersist() {
        return new InMemoryPersist();
    }

    @Bean
    public StateMachinePersister<States, Events, String> persister(
            StateMachinePersist<States, Events, String> defaultPersist) {

        return new DefaultStateMachinePersister<>(defaultPersist);
    }
}