package user;

import java.io.Serializable;

/**
 *  Type of users in the application.
 */
public enum UserType implements Serializable {
    CUSTOMER {
        @Override
        public String toString() {
            return "Customer";
        }
    },
    BRANCH_EMPLOYEE{
        @Override
        public String toString() {
            return "Branch Employee";
        }
    },
    BRANCH_MANAGER{
        @Override
        public String toString() {
            return "Branch Manager";
        }
    },
    SERVICE_EMPLOYEE{
        @Override
        public String toString() {
            return "Customer Service Employee";
        }
    },
    EXPERT_SERVICE_EMPLOYEE{
        @Override
        public String toString() {
            return "Expert Customer Service Employee";
        }
    },
    DELIVERY_OPERATOR{
        @Override
        public String toString() {
            return "Delivery Operator";
        }
    },
    CEO{
        @Override
        public String toString() {
            return "CEO";
        }
    }
}
