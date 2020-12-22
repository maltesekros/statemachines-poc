POC Using Spring Statemachines
-------------------------------

Statemachine:<br>
SHOW_REGISTRATION_FORM -> WAITING_CONFIRMATION -> USER_REGISTERED


Try:
- http://localhost:8080/register?name=Chris

[* * * Sending email ... * * *
 Transition SHOW_REGISTRATION_FORM -> WAITING_CONFIRMATION
 Customer state changed to WAITING_CONFIRMATION
 Transition null -> SHOW_REGISTRATION_FORM
 Customer state changed to SHOW_REGISTRATION_FORM]

Followed By:
- http://localhost:8080/confirm?name=Chris 

[* * * Registering user ... * * *
Transition WAITING_CONFIRMATION -> USER_REGISTERED
Customer state changed to USER_REGISTERED
]