package complaint;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class is used to describe a Complaint made by a Customer in the System
 */
public class Complaint implements Serializable {
    /**
     * The Identification number of the complaint within the database
     */
    private int complaintId;

    /**
     * The Government Identification number of the customer complaining within the database
     */
    private String customerId;

    /**
     * The Identification number of the Service Employee who created the complaint within the database
     */
    private int serviceId;

    /**
     * The Identification number of the order related to the complaint within the database
     */
    private int orderId;

    /**
     * The current complaint status
     */
    private ComplaintStatus complaintStatus;

    /**
     * The timestamp of when the complaint was created at
     */
    private Timestamp createdAt;

    /**
     * Describes what exactly the customer is complaining about
     */
    private String description;

    public Complaint(int complaintId, String customerId, int serviceId, int orderId, ComplaintStatus complaintStatus, Timestamp createdAt, String description) {
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
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