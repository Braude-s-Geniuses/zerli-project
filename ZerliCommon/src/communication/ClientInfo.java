package communication;

/** Represents a Client in the <code>ZerliServer</code> connected clients table.
 *
 */
public class ClientInfo {
    /**
     * IP Address of the Client
     */
    private final String ipAddress;

    /**
     * Hostname of the Client
     */
    private final String hostname;

    /**
     * Connection Status of the Client
     */
    private String status;

    /**
     * Creates a new instance on connected Client Information.
     * This constructor is used on first time connection when Client is as a guest
     * @param ipAddress - the IP Address he connected with
     * @param hostname - the Hostname he connected with
     */
    public ClientInfo(String ipAddress, String hostname) {
        this.ipAddress = ipAddress;
        this.hostname = hostname;
        this.status = "Connected (Guest)";
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
