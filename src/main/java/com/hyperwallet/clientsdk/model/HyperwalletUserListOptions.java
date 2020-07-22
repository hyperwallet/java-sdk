package com.hyperwallet.clientsdk.model;

import java.util.HashMap;
import java.util.Map;

public class HyperwalletUserListOptions extends HyperwalletPaginationOptions
{
    /**
     * Return the user with this particular client-defined identifier.
     */
    private String clientUserId;

    /**
     * Returns users with this email address.
     */
    private String email;

    /**
     * Returns users belonging to program specified by the provided token.
     */
    private String programToken;

    /**
     * Returns users with this account status.
     */
    private HyperwalletUser.Status status;

    /**
     * The user's verification status. A user may be required to verify their identity after a certain
     * threshold of payments is reached.
     */
    private HyperwalletUser.VerificationStatus verificationStatus;

    public String getClientUserId() {
        return clientUserId;
    }

    public HyperwalletUserListOptions setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public HyperwalletUserListOptions setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getProgramToken() {
        return programToken;
    }

    public HyperwalletUserListOptions setProgramToken(String programToken) {
        this.programToken = programToken;
        return this;
    }

    public HyperwalletUser.Status getStatus() {
        return status;
    }

    public HyperwalletUserListOptions setStatus(HyperwalletUser.Status status) {
        this.status = status;
        return this;
    }

    public HyperwalletUser.VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public HyperwalletUserListOptions setVerificationStatus(HyperwalletUser.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
        return this;
    }

    public Map<String, String> getParameters() {
        return new HashMap<String, String>() {{
            put("clientUserId", clientUserId);
            put("email", email);
            put("programToken", programToken);
            put("status", status == null ? null : status.name());
            put("verificationStatus", verificationStatus == null ? null : verificationStatus.name());
        }};
    }
}
