package user;

import java.io.Serializable;

/**
 *  Type of users in the application.
 */
public enum UserType implements Serializable {
    UNREGISTERED ,
    CUSTOMER,
    BRANCH_EMPLOYEE,
    BRANCH_MANAGER,
    SERVICE_EMPLOYEE,
    EXPERT_SERVICE_EMPLOYEE,
    DELIVERY_OPERATOR,
    CEO

}
