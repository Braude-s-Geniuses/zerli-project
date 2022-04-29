package communication;

/** Represents a Client in the <code>ZerliServer</code> connected clients table.
 *
 */
public class ClientInfo {
    private String ipAddress;
    private String hostname;
    private String status;

    public ClientInfo(String ipAddress, String hostname) {
        this.ipAddress = ipAddress;
        this.hostname = hostname;
        this.status = "Connected";
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
        return "ClientInfo [ipAddress=" + ipAddress + ", hostname=" + hostname + ", status=" + status
                + "]";
    }
}
