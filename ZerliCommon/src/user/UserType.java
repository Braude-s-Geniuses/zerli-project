package user;

import java.io.Serializable;

/**
 *  This class describes the type of users in the system
 */
public enum UserType implements Serializable {
    /**
     * A Customer who has not been registered as a customer in the system
     */
    UNREGISTERED {
        @Override
        public String toString() { return "Unauthorized"; }
    },

    /**
     * A regular customer who can log in and perform Customer actions
     */
    CUSTOMER {
        @Override
        public String toString() {
            return "Customer";
        }
    },

    /**
     * A Branch Employee who is assigned to a specific branch
     */
    BRANCH_EMPLOYEE{
        @Override
        public String toString() {
            return "Branch Employee";
        }
    },

    /**
     * A manager of a branch
     */
    BRANCH_MANAGER{
        @Override
        public String toString() {
            return "Branch Manager";
        }
    },

    /**
     * A Customer Service Employee who handles Customer's Complaints
     */
    SERVICE_EMPLOYEE{
        @Override
        public String toString() {
            return "Customer Service Employee";
        }
    },

    /**
     * An Expert Customer Service Employee who handles generating conclusions from Customer Complaints
     */
    EXPERT_SERVICE_EMPLOYEE{
        @Override
        public String toString() {
            return "Expert Customer Service Employee";
        }
    },

    /**
     * Delivery Operator will deliver order and confirm their acceptance
     */
    DELIVERY_OPERATOR{
        @Override
        public String toString() {
            return "Delivery Operator";
        }
    },

    /**
     * The CEO of Zerli
     */
    CEO{
        @Override
        public String toString() {
            return "CEO";
        }
    }
}
