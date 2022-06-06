package order;

import java.io.Serializable;

/**
 * This class is used to describe an order status
 */
public enum OrderStatus implements Serializable {
    NORMAL_PENDING{
        public String toString(){
            return "Type: Normal\nStatus: Pending";
        }
    },
    NORMAL_CONFIRMED{
        public String toString(){
            return "Type: Normal\nStatus: Confirmed";
        }
    },
    NORMAL_COMPLETED{
        public String toString(){
            return "Type: Normal\nStatus: Completed";
        }
    },
    EXPRESS_PENDING{
        public String toString(){
            return "Type: Express\nStatus: Pending";
        }
    },
    EXPRESS_CONFIRMED{
        public String toString(){
            return "Type: Express\nStatus: Confirmed";
        }
    },
    EXPRESS_COMPLETED{
        public String toString(){
            return "Type: Express\nStatus: Completed";
        }
    },
    CANCEL_PENDING{
        public String toString(){
            return "Type: Canceled\nStatus: Pending";
        }
    },
    CANCEL_CONFIRMED{
        public String toString(){
            return "Type: Canceled\nStatus: Confirmed";
        }
    }


}