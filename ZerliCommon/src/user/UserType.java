package user;

import java.io.Serializable;

public enum UserType implements Serializable {
    CUSTOMER,
    BRANCH_EMPLOYEE,
    BRANCH_MANAGER,
    SERVICE_EMPLOYEE,
    EXPERT_SERVICE_EMPLOYEE,
    DELIVERY_OPERATOR,
    UNREGISTERED,
    CEO
}
