package communication;

/** Represents a Client in the <code>ZerliServer</code> connected clients table.
 *
 */
public class ClientInfo {
    private final String ipAddress;
    private final String hostname;
    private String status;

    public ClientInfo(String ipAddress, String hostname) {
        this.ipAddress = ipAddress;
        this.hostname = hostname;
        this.status = "Connected (Guest)";
    }

    public ClientInfo(String ipAddress, String hostname, String status) {
        this.ipAddress = ipAddress;
        this.hostname = hostname;
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    public String getHostname() {
        return hostname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClientInfo [ipAddress=" + ipAddress + ", hostname=" + hostname + ", status=" + status+ "]";
    }
}
