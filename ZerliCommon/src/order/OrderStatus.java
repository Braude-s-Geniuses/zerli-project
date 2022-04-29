package order;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    NORMAL_PENDING,
    NORMAL_CONFIRMED,
    NORMAL_COMPLETED,
    EXPRESS_PENDING,
    EXPRESS_CONFIRMED,
    EXPRESS_COMPLETED,
    CANCEL_PENDING,
    CANCEL_CONFIRMED
}
