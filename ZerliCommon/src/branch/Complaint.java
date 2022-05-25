package branch;

import java.io.Serializable;
import java.sql.Timestamp;

public class Complaint implements Serializable {
    private int complaintId;
    private int customerId;
    private int serviceId;
    private int orderId;
    private ComplaintStatus complaintStatus;
    private Timestamp createdAt;
    private String description;

    public Complaint(int complaintId, int customerId, int serviceId, int orderId, ComplaintStatus complaintStatus, Timestamp createdAt, String description) {
        this.complaintId = complaintId;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.complaintStatus = complaintStatus;
        this.createdAt = createdAt;
        this.description = description;
    }
    public Complaint(){}


    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "complaintId=" + complaintId +
                ", customerId=" + customerId +
                ", serviceId=" + serviceId +
                ", orderId=" + orderId +
                ", complaintStatus=" + complaintStatus +
                ", createdAt=" + createdAt +
                ", description='" + description + '\'' +
                '}';
    }
}