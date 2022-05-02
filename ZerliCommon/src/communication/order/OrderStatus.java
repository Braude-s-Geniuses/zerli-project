package communication.order;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    NORMAL_PENDING{
        public String toString(){
            return "Pending";
        }
    },
    NORMAL_CONFIRMED{
        public String toString(){
            return "Confirmed";
        }
    },
    NORMAL_COMPLETED{
        public String toString(){
            return "Completed";
        }
    },
    EXPRESS_PENDING{
        public String toString(){
            return "Express Pending";
        }
    },
    EXPRESS_CONFIRMED{
        public String toString(){
            return "Express Confirmed";
        }
    },
    EXPRESS_COMPLETED{
        public String toString(){
            return "Express Completed";
        }
    },
    CANCEL_PENDING{
        public String toString(){
            return "Cancel Pending";
        }
    },
    CANCEL_CONFIRMED{
        public String toString(){
            return "Cancel Confirmed";
        }
    };



}
