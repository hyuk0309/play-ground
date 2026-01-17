package org.personal.jmxsping;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * A custom Actuator endpoint to demonstrate how they work.
 * Access via: http://localhost:8080/actuator/custom
 */
@Component
@Endpoint(id = "custom")
public class CustomEndpoint {

    private String status = "Ready";

    @ReadOperation
    public Map<String, Object> getCustomData() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", status);
        data.put("timestamp", System.currentTimeMillis());
        data.put("message", "This is a custom actuator endpoint for debugging!");
        return data;
    }

    @WriteOperation
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
