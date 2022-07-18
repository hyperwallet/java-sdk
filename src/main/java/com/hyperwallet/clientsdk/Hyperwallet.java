package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.util.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Hyperwallet Client
 */
public class Hyperwallet {

    private static final String SANDBOX_API_BASE = "https://api.sandbox.hyperwallet.com/rest/v4";
    private static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
    public static final String VERSION = "2.4.3";
    private final HyperwalletApiClient apiClient;
    private final String programToken;
    private final String url;

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username              API key assigned
     * @param password              API Password assigned
     * @param programToken          API program token
     * @param server                API server url
     * @param hyperwalletEncryption API encryption data
     * @param connectionTimeout     the timeout value that will be used for making new connections to the Hyperwallet API (in milliseconds).
     * @param readTimeout           the timeout value that will be used when reading data from an established connection to
     *                              the Hyperwallet API (in milliseconds).
     */
    public Hyperwallet(final String username, final String password, final String programToken, final String server,
            final HyperwalletEncryption hyperwalletEncryption, int connectionTimeout, int readTimeout) {
        apiClient = new HyperwalletApiClient(username, password, VERSION, hyperwalletEncryption, connectionTimeout, readTimeout);
        this.programToken = programToken;
        this.url = StringUtils.isEmpty(server) ? SANDBOX_API_BASE : server + "/rest/v4";
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username              API key assigned
     * @param password              API Password assigned
     * @param programToken          API program token
     * @param server                API server url
     * @param hyperwalletEncryption API encryption data
     */
    public Hyperwallet(final String username, final String password, final String programToken, final String server,
            final HyperwalletEncryption hyperwalletEncryption) {
        this(username, password, programToken, server, hyperwalletEncryption, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username          API key assigned
     * @param password          API Password assigned
     * @param programToken      API program token
     * @param server            API server url
     * @param connectionTimeout the timeout value that will be used for making new connections to the Hyperwallet API (in milliseconds).
     * @param readTimeout       the timeout value that will be used when reading data from an established connection to
     *                          the Hyperwallet API (in milliseconds).
     */
    public Hyperwallet(final String username, final String password, final String programToken, final String server, int connectionTimeout,
            int readTimeout) {
        this(username, password, programToken, server, null, connectionTimeout, readTimeout);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username     API key assigned
     * @param password     API Password assigned
     * @param programToken API program token
     * @param server       API serer url
     */
    public Hyperwallet(final String username, final String password, final String programToken, final String server) {
        this(username, password, programToken, server, null);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username          API key assigned
     * @param password          API Password assigned
     * @param programToken      API program token
     * @param connectionTimeout the timeout value that will be used for making new connections to the Hyperwallet API (in milliseconds).
     * @param readTimeout       the timeout value that will be used when reading data from an established connection to
     *                          the Hyperwallet API (in milliseconds).
     */
    public Hyperwallet(final String username, final String password, final String programToken, int connectionTimeout, int readTimeout) {
        this(username, password, programToken, null, null, connectionTimeout, readTimeout);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username     API key assigned
     * @param password     API password
     * @param programToken API program token assigned
     */
    public Hyperwallet(final String username, final String password, final String programToken) {
        this(username, password, programToken, null, null);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username              API key assigned
     * @param password              API password
     * @param programToken          API program token assigned
     * @param hyperwalletEncryption API encryption data
     */
    public Hyperwallet(final String username, final String password, final String programToken,
            final HyperwalletEncryption hyperwalletEncryption) {
        this(username, password, programToken, null, hyperwalletEncryption);
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username API key assigned
     * @param password API password
     */
    public Hyperwallet(final String username, final String password) {
        this(username, password, null);
    }

    /**
     * Create Proxy setting for Hyperwallet API Client instance
     *
     * @param url  url of Proxy
     * @param port port of Proxy
     */
    public void setHyperwalletProxy(final String url, final Integer port) {
        apiClient.setProxy(url, port);
    }

    /**
     * Create Proxy setting for Hyperwallet API Client instance
     *
     * @param proxy value of Proxy
     */
    public void setHyperwalletProxy(final Proxy proxy) {
        apiClient.setProxy(proxy);
    }

    /**
     * Returns Proxy setting for Hyperwallet API Client instance
     *
     * @return Proxy current Proxy config of client
     */
    public Proxy getHyperwalletProxy() {
        return apiClient.getProxy();
    }

    /**
     * Checks if current Hyperwallet API Client instance uses a proxy
     *
     * @return Boolean if client has a proxy config
     */
    public Boolean usesHyperwalletProxy() {
        return apiClient.usesProxy();
    }

    /**
     * Create Proxy Username setting for Authentication
     *
     * @param proxyUsername username of Proxy
     */
    public void setHyperwalletProxyUsername(final String proxyUsername) {
        apiClient.setProxyUsername(proxyUsername);
    }

    /**
     * Returns Proxy Username for Hyperwallet API Client instance
     *
     * @return current ProxyUsername
     */
    public String getHyperwalletProxyUsername() {
        return apiClient.getProxyUsername();
    }

    /**
     * Create Proxy Password setting for Authentication
     *
     * @param proxyPassword username of Proxy
     */
    public void setHyperwalletProxyPassword(final String proxyPassword) {
        apiClient.setProxyPassword(proxyPassword);
    }

    /**
     * Returns Proxy Password for Hyperwallet API Client instance
     *
     * @return current ProxyUsername
     */
    public String getHyperwalletProxyPassword() {
        return apiClient.getProxyPassword();
    }

    //--------------------------------------
    // Users
    //--------------------------------------

    /**
     * Create a User
     *
     * @param user Hyperwallet user representation
     * @return HyperwalletUser created User
     */
    public HyperwalletUser createUser(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (!StringUtils.isEmpty(user.getToken())) {
            throw new HyperwalletException("User token may not be present");
        }
        user = copy(user);
        user.setStatus(null);
        user.setCreatedOn(null);
        return apiClient.post(url + "/users", user, HyperwalletUser.class);
    }

    /**
     * Get User
     *
     * @param token user account token
     * @return HyperwalletUser retreived user
     */
    public HyperwalletUser getUser(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new HyperwalletException("User token is required");
        }
        return apiClient.get(url + "/users/" + token, HyperwalletUser.class);
    }

    /**
     * Update User
     *
     * @param user Hyperwallet User representation object
     * @return HyperwalletUser updated user object
     */
    public HyperwalletUser updateUser(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (StringUtils.isEmpty(user.getToken())) {
            throw new HyperwalletException("User token is required");
        }
        return apiClient.put(url + "/users/" + user.getToken(), user, HyperwalletUser.class);
    }

    /**
     * List Users
     *
     * @return HyperwalletList of HyperwalletUser
     */
    public HyperwalletList<HyperwalletUser> listUsers() {
        return listUsers(null);
    }

    /**
     * List Users
     *
     * @param options List filter option
     * @return HyperwalletList of HyperwalletUser
     */
    public HyperwalletList<HyperwalletUser> listUsers(HyperwalletUsersListPaginationOptions options) {
        String url = paginate(this.url + "/users", options);
        if (options != null) {
            url = addParameter(url, "clientUserId", options.getClientUserId());
            url = addParameter(url, "email", options.getEmail());
            url = addParameter(url, "programToken", options.getProgramToken());
            url = addParameter(url, "status", options.getStatus());
            url = addParameter(url, "verificationStatus", options.getVerificationStatus());
            url = addParameter(url, "taxVerificationStatus", options.getTaxVerificationStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletUser>>() {
        });
    }

    /**
     * Create Business Stakeholder
     *
     * @param stakeholder Hyperwallet Stakeholder representation
     * @param userToken   String
     * @return HyperwalletBusinessStakeholder created Stakeholder
     */
    public HyperwalletBusinessStakeholder createBusinessStakeholder(String userToken, HyperwalletBusinessStakeholder stakeholder) {
        if (stakeholder == null) {
            throw new HyperwalletException("Stakeholder is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        stakeholder = copy(stakeholder);
        return apiClient.post(url + "/users/" + userToken + "/business-stakeholders", stakeholder, HyperwalletBusinessStakeholder.class);
    }

    /**
     * Update Business Stakeholder
     *
     * @param userToken   String
     * @param stakeholder Hyperwallet Stakeholder representation
     * @return HyperwalletBusinessStakeholder updated Stakeholder
     */
    public HyperwalletBusinessStakeholder updateBusinessStakeholder(String userToken, HyperwalletBusinessStakeholder stakeholder) {
        if (stakeholder == null) {
            throw new HyperwalletException("Stakeholder is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        if (stakeholder.getToken() == null || stakeholder.getToken().equals("")) {
            throw new HyperwalletException("Stakeholder token may not be present");
        }
        stakeholder = copy(stakeholder);
        return apiClient.put(url + "/users/" + userToken + "/business-stakeholders/" + stakeholder.getToken(), stakeholder,
                HyperwalletBusinessStakeholder.class);
    }

    /**
     * List BusinessStakeholders
     *
     * @param userToken String
     * @return HyperwalletList of HyperwalletBusinessStakeholder
     */
    public HyperwalletList<HyperwalletBusinessStakeholder> listBusinessStakeholders(String userToken) {
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        return listBusinessStakeholders(userToken, null);
    }

    /**
     * List BusinessStakeholders
     *
     * @param userToken String
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletBusinessStakeholder
     */
    public HyperwalletList<HyperwalletBusinessStakeholder> listBusinessStakeholders(String userToken, HyperwalletPaginationOptions options) {
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        String url = paginate(this.url + "/users/" + userToken + "/business-stakeholders", options);
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletBusinessStakeholder>>() {
        });
    }

    /**
     * Uploading documents for Business Stakeholder
     *
     * @param userToken                String
     * @param businessStakeholderToken Hyperwallet Stakeholder representation
     * @param uploadData               HyperwalletVerificationDocument to get uploaded
     * @return HyperwalletBusinessStakeholder updated Stakeholder with document status
     */
    public HyperwalletBusinessStakeholder uploadStakeholderDocuments(String userToken, String businessStakeholderToken,
            List<HyperwalletVerificationDocument> uploadData) {
        Multipart multipart = new Multipart();
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        if (businessStakeholderToken == null) {
            throw new HyperwalletException("BusinessStakeholderToken may not be present");
        }
        if (uploadData == null || uploadData.size() < 1) {
            throw new HyperwalletException("Data for upload is missing");
        }
        if (uploadData.get(0).getUploadFiles() == null || uploadData.get(0).getUploadFiles().size() < 1) {
            throw new HyperwalletException("Upload Files are missing");
        }
        try {
            multipart = HyperwalletMultipartUtils.convert(uploadData);
        } catch (IOException e) {
            throw new HyperwalletException("Unable to convert to Multipart formdata");
        }

        return apiClient.put(url + "/users/" + userToken + "/business-stakeholders/" + businessStakeholderToken, multipart,
                HyperwalletBusinessStakeholder.class);
    }

    /**
     * Create Business Stakeholder Status transition
     *
     * @param userToken        String
     * @param stakeholderToken Hyperwallet Stakeholder token
     * @param transition       Hyperwallet Status Transition
     * @return HyperwalletStatusTransition new status for Business Stakeholder
     */
    public HyperwalletStatusTransition createBusinessStakeholderStatusTransition(String userToken, String stakeholderToken,
            HyperwalletStatusTransition transition) {
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        if (stakeholderToken == null) {
            throw new HyperwalletException("StakeholderToken is required");
        }
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }

        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);

        return apiClient.post(url + "/users/" + userToken + "/business-stakeholders/" + stakeholderToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get Business Stakeholder Status transition
     *
     * @param userToken             String
     * @param stakeholderToken      Hyperwallet Stakeholder token
     * @param statusTransitionToken Hyperwallet Status Transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getBusinessStakeholderStatusTransition(String userToken, String stakeholderToken,
            String statusTransitionToken) {
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        if (stakeholderToken == null) {
            throw new HyperwalletException("StakeholderToken is required");
        }
        if (statusTransitionToken == null) {
            throw new HyperwalletException("Status Transition token may not be present");
        }

        return apiClient
                .get(url + "/users/" + userToken + "/business-stakeholders/" + stakeholderToken + "/status-transitions/" + statusTransitionToken,
                        HyperwalletStatusTransition.class);
    }

    /**
     * List Business Stakeholder Status transition
     *
     * @param userToken        String
     * @param stakeholderToken Hyperwallet Stakeholder token
     * @param options          a {@link  HyperwalletPaginationOptions}
     * @return HyperwalletList of  HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listBusinessStakeholderStatusTransition(String userToken, String stakeholderToken,
            HyperwalletPaginationOptions options) {
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        if (stakeholderToken == null) {
            throw new HyperwalletException("StakeholderToken is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/business-stakeholders/" + stakeholderToken + "/status-transitions", options);
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    /**
     * List Business Stakeholder Status transition
     *
     * @param userToken        String
     * @param stakeholderToken Hyperwallet Stakeholder token
     * @return HyperwalletList of  HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listBusinessStakeholderStatusTransition(String userToken, String stakeholderToken) {
        return listBusinessStakeholderStatusTransition(userToken, stakeholderToken, null);
    }

    /**
     * Activate a business stakeholder
     *
     * @param userToken        User token
     * @param stakeholderToken Business Stakeholder token
     * @return The status transition
     */
    public HyperwalletStatusTransition activateBusinessStakeholder(String userToken, String stakeholderToken) {
        return createBusinessStakeholderStatusTransition(userToken, stakeholderToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.ACTIVATED));
    }

    /**
     * De-activate a business stakeholder
     *
     * @param userToken        User token
     * @param stakeholderToken Business Stakeholder token
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivateBusinessStakeholder(String userToken, String stakeholderToken) {
        return createBusinessStakeholderStatusTransition(userToken, stakeholderToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Get Authentication Token
     *
     * @param token user account token
     * @return HyperwalletAuthenticationToken retreived authentication token
     */
    public HyperwalletAuthenticationToken getAuthenticationToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new HyperwalletException("User token is required");
        }
        String urlString = url + "/users/" + token + "/authentication-token";
        return apiClient.post(urlString, null, HyperwalletAuthenticationToken.class);
    }

    /**
     * Get User Status Transition
     *
     * @param userToken             User token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getUserStatusTransition(String userToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/status-transitions/" + statusTransitionToken, HyperwalletStatusTransition.class);
    }

    /**
     * List All User Status Transition information
     *
     * @param userToken User token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listUserStatusTransitions(String userToken) {
        return listUserStatusTransitions(userToken, null);
    }

    /**
     * List Prepaid Card Status Transition information
     *
     * @param userToken User token
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listUserStatusTransitions(String userToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/status-transitions", options);
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    /**
     * Create User Status transition
     *
     * @param userToken  String
     * @param transition Hyperwallet Status Transition
     * @return HyperwalletStatusTransition new status for Hyperwallet User
     */
    public HyperwalletStatusTransition createUserStatusTransition(String userToken, HyperwalletStatusTransition transition) {
        if (userToken == null) {
            throw new HyperwalletException("User token may not be present");
        }
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }

        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);

        return apiClient.post(url + "/users/" + userToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }

    /**
     * Activate a User
     *
     * @param userToken User token
     * @return The status transition
     */
    public HyperwalletStatusTransition activateUser(String userToken) {
        return createUserStatusTransition(userToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.ACTIVATED));
    }

    /**
     * De-activate a User
     *
     * @param userToken User token
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivateUser(String userToken) {
        return createUserStatusTransition(userToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Lock a User account
     *
     * @param userToken User token
     * @return The status transition
     */
    public HyperwalletStatusTransition lockUser(String userToken) {
        return createUserStatusTransition(userToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.LOCKED));
    }

    /**
     * Freeze a User account
     *
     * @param userToken User token
     * @return The status transition
     */
    public HyperwalletStatusTransition freezeUser(String userToken) {
        return createUserStatusTransition(userToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.FROZEN));
    }

    /**
     * Pre-activate a User account
     *
     * @param userToken User token
     * @return The status transition
     */
    public HyperwalletStatusTransition preactivateUser(String userToken) {
        return createUserStatusTransition(userToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.PRE_ACTIVATED));
    }

    //--------------------------------------
    // Prepaid Cards
    //--------------------------------------

    /**
     * Create Prepaid Card
     *
     * @param prepaidCard Prepaid Card object to create
     * @return HyperwalletPrepaidCard Prepaid Card object created
     */
    public HyperwalletPrepaidCard createOrReplacePrepaidCard(HyperwalletPrepaidCard prepaidCard) {
        if (prepaidCard == null) {
            throw new HyperwalletException("Prepaid Card is required");
        }
        if (StringUtils.isEmpty(prepaidCard.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (!StringUtils.isEmpty(prepaidCard.getToken())) {
            throw new HyperwalletException("Prepaid Card token may not be present");
        }
        if (prepaidCard.getType() == null) {
            prepaidCard.setType(HyperwalletPrepaidCard.Type.PREPAID_CARD);
        }
        prepaidCard = copy(prepaidCard);
        prepaidCard.setStatus(null);
        prepaidCard.setCardType(null);
        prepaidCard.setCreatedOn(null);
        prepaidCard.setTransferMethodCountry(null);
        prepaidCard.setTransferMethodCurrency(null);
        prepaidCard.setCardNumber(null);
        prepaidCard.setCardBrand(null);
        prepaidCard.setDateOfExpiry(null);
        return apiClient.post(url + "/users/" + prepaidCard.getUserToken() + "/prepaid-cards", prepaidCard, HyperwalletPrepaidCard.class);
    }

    /**
     * Get Prepaid Card
     *
     * @param userToken        User token assigned
     * @param prepaidCardToken Prepaid Card token
     * @return HyperwalletPrepaidCard Prepaid Card
     */
    public HyperwalletPrepaidCard getPrepaidCard(String userToken, String prepaidCardToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken, HyperwalletPrepaidCard.class);
    }

    /**
     * Update Prepaid Card
     *
     * @param prepaidCard Prepaid Card object to create
     * @return HyperwalletPrepaidCard Prepaid Card object created
     */
    public HyperwalletPrepaidCard updatePrepaidCard(HyperwalletPrepaidCard prepaidCard) {
        if (prepaidCard == null) {
            throw new HyperwalletException("Prepaid Card is required");
        }
        if (StringUtils.isEmpty(prepaidCard.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCard.getToken())) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        return apiClient.put(url + "/users/" + prepaidCard.getUserToken() + "/prepaid-cards/" + prepaidCard.getToken(),
                prepaidCard,
                HyperwalletPrepaidCard.class);
    }

    /**
     * List User's Prepaid Card
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletPrepaidCard
     */
    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken) {
        return listPrepaidCards(userToken, null);
    }

    /**
     * List User's Prepaid Card
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletPrepaidCard
     */
    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken, HyperwalletPrepaidCardListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards", options);
        if (options != null && options.getStatus() != null) {
            url = addParameter(url, "status", options.getStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletPrepaidCard>>() {
        });
    }

    /**
     * Suspend a prepaid card
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition suspendPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.SUSPENDED));
    }

    /**
     * Unsuspend a prepaid card
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition unsuspendPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNSUSPENDED));
    }

    /**
     * Mark a prepaid card as lost or stolen
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition lostOrStolenPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.LOST_OR_STOLEN));
    }

    /**
     * Deactivate a prepaid card
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivatePrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Lock a prepaid card
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition lockPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.LOCKED));
    }

    /**
     * Unlock a prepaid card
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition unlockPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLOCKED));
    }

    /**
     * Create Prepaid Card Status Transition
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid Card token
     * @param transition       Status transition information
     * @return HyperwalletStatusTransition new status for Prepaid Card
     */
    public HyperwalletStatusTransition createPrepaidCardStatusTransition(String userToken, String prepaidCardToken,
            HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get Prepaid Card Status Transition
     *
     * @param userToken             User token
     * @param prepaidCardToken      Prepaid Card token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getPrepaidCardStatusTransition(String userToken, String prepaidCardToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions/" + statusTransitionToken,
                HyperwalletStatusTransition.class);
    }

    /**
     * List All Prepaid Card Status Transition information
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid Card token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(String userToken, String prepaidCardToken) {
        return listPrepaidCardStatusTransitions(userToken, prepaidCardToken, null);
    }

    /**
     * List Prepaid Card Status Transition information
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid Card token
     * @param options          List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(String userToken, String prepaidCardToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", options);
        if (options != null && options.getTransition() != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Bank Cards
    //--------------------------------------

    /**
     * Create Bank Card
     *
     * @param bankCard Bank Card object to create
     * @return HyperwalletBankCard Bank Card object created
     */
    public HyperwalletBankCard createBankCard(HyperwalletBankCard bankCard) {
        if (bankCard == null) {
            throw new HyperwalletException("Bank Card is required");
        }
        if (StringUtils.isEmpty(bankCard.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (!StringUtils.isEmpty(bankCard.getToken())) {
            throw new HyperwalletException("Bank Card token may not be present");
        }
        if (bankCard.getType() == null) {
            bankCard.setType(HyperwalletBankCard.Type.BANK_CARD);
        }
        bankCard = copy(bankCard);
        bankCard.setStatus(null);
        bankCard.setCardType(null);
        bankCard.setCreatedOn(null);
        bankCard.setCardBrand(null);
        return apiClient.post(url + "/users/" + bankCard.getUserToken() + "/bank-cards", bankCard, HyperwalletBankCard.class);
    }

    /**
     * Get Bank Card
     *
     * @param userToken     User token assigned
     * @param bankCardToken Bank Card token
     * @return HyperwalletBankCard Bank Card
     */
    public HyperwalletBankCard getBankCard(String userToken, String bankCardToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankCardToken)) {
            throw new HyperwalletException("Bank Card token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/bank-cards/" + bankCardToken, HyperwalletBankCard.class);
    }

    /**
     * Update Bank Card
     *
     * @param bankCard Bank Card object to create
     * @return HyperwalletBankCard Bank Card object created
     */
    public HyperwalletBankCard updateBankCard(HyperwalletBankCard bankCard) {
        if (bankCard == null) {
            throw new HyperwalletException("Bank Card is required");
        }
        if (StringUtils.isEmpty(bankCard.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankCard.getToken())) {
            throw new HyperwalletException("Bank Card token is required");
        }
        return apiClient.put(url + "/users/" + bankCard.getUserToken() + "/bank-cards/" + bankCard.getToken(), bankCard, HyperwalletBankCard.class);
    }

    /**
     * List User's Bank Card
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletBankCard
     */
    public HyperwalletList<HyperwalletBankCard> listBankCards(String userToken) {
        return listBankCards(userToken, null);
    }

    /**
     * List User's Bank Card
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletBankCard
     */
    public HyperwalletList<HyperwalletBankCard> listBankCards(String userToken, HyperwalletBankCardListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-cards", options);
        if (options != null) {
            url = addParameter(url, "status", options.getStatus());

        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletBankCard>>() {
        });
    }

    /**
     * Deactivate a bank card
     *
     * @param userToken     User token
     * @param bankCardToken Bank card token
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivateBankCard(String userToken, String bankCardToken) {
        return deactivateBankCard(userToken, bankCardToken, null);
    }

    /**
     * Deactivate a bank card
     *
     * @param userToken     User token
     * @param bankCardToken Bank card token
     * @param notes         Comments regarding the status change
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivateBankCard(String userToken, String bankCardToken, String notes) {
        return createBankCardStatusTransition(userToken,
                bankCardToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED).notes(notes));
    }

    /**
     * Create Bank Card Status Transition
     *
     * @param userToken     User token
     * @param bankCardToken Bank Card token
     * @param transition    Status transition information
     * @return HyperwalletStatusTransition new status for Bank Card
     */
    public HyperwalletStatusTransition createBankCardStatusTransition(String userToken, String bankCardToken,
            HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankCardToken)) {
            throw new HyperwalletException("Bank Card token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/users/" + userToken + "/bank-cards/" + bankCardToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get Bank Card Status Transition
     *
     * @param userToken             User token
     * @param bankCardToken         Bank Card token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getBankCardStatusTransition(String userToken, String bankCardToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankCardToken)) {
            throw new HyperwalletException("Bank Card token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/bank-cards/" + bankCardToken + "/status-transitions/" + statusTransitionToken,
                HyperwalletStatusTransition.class);
    }

    /**
     * List All Bank Card Status Transition information
     *
     * @param userToken     User token
     * @param bankCardToken Bank Card token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listBankCardStatusTransitions(String userToken, String bankCardToken) {
        return listBankCardStatusTransitions(userToken, bankCardToken, null);
    }

    /**
     * List Bank Card Status Transition information
     *
     * @param userToken     User token
     * @param bankCardToken Bank Card token
     * @param options       List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listBankCardStatusTransitions(String userToken, String bankCardToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankCardToken)) {
            throw new HyperwalletException("Bank Card token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-cards/" + bankCardToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Paper Checks
    //--------------------------------------

    /**
     * Create Paper Check
     *
     * @param paperCheck Paper Check object to create
     * @return HyperwalletPaperCheck Paper Check object created
     */
    public HyperwalletPaperCheck createPaperCheck(HyperwalletPaperCheck paperCheck) {
        if (paperCheck == null) {
            throw new HyperwalletException("Paper Check is required");
        }
        if (StringUtils.isEmpty(paperCheck.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (!StringUtils.isEmpty(paperCheck.getToken())) {
            throw new HyperwalletException("Paper Check token may not be present");
        }
        if (paperCheck.getType() == null) {
            paperCheck.setType(HyperwalletPaperCheck.Type.PAPER_CHECK);
        }
        paperCheck = copy(paperCheck);
        paperCheck.setStatus(null);
        paperCheck.setCreatedOn(null);
        return apiClient.post(url + "/users/" + paperCheck.getUserToken() + "/paper-checks", paperCheck, HyperwalletPaperCheck.class);
    }

    /**
     * Get Paper Check
     *
     * @param userToken       User token assigned
     * @param paperCheckToken Paper Check token
     * @return HyperwalletPaperCheck Paper Check
     */
    public HyperwalletPaperCheck getPaperCheck(String userToken, String paperCheckToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(paperCheckToken)) {
            throw new HyperwalletException("Paper Check token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/paper-checks/" + paperCheckToken, HyperwalletPaperCheck.class);
    }

    /**
     * Update Paper Check
     *
     * @param paperCheck Paper Check object to create
     * @return HyperwalletPaperCheck Paper Check object created
     */
    public HyperwalletPaperCheck updatePaperCheck(HyperwalletPaperCheck paperCheck) {
        if (paperCheck == null) {
            throw new HyperwalletException("Paper Check is required");
        }
        if (StringUtils.isEmpty(paperCheck.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(paperCheck.getToken())) {
            throw new HyperwalletException("Paper Check token is required");
        }
        return apiClient
                .put(url + "/users/" + paperCheck.getUserToken() + "/paper-checks/" + paperCheck.getToken(), paperCheck, HyperwalletPaperCheck.class);
    }

    /**
     * List User's Paper Check
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletPaperCheck
     */
    public HyperwalletList<HyperwalletPaperCheck> listPaperChecks(String userToken) {
        return listPaperChecks(userToken, null);
    }

    /**
     * List User's Paper Check
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletPaperCheck
     */
    public HyperwalletList<HyperwalletPaperCheck> listPaperChecks(String userToken, HyperwalletPaperCheckListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/paper-checks", options);
        if (options != null) {
            url = addParameter(url, "status", options.getStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletPaperCheck>>() {
        });
    }

    /**
     * Deactivate a Paper Check
     *
     * @param userToken       User token
     * @param paperCheckToken Paper Check token
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivatePaperCheck(String userToken, String paperCheckToken) {
        return deactivatePaperCheck(userToken, paperCheckToken, null);
    }

    /**
     * Deactivate a Paper Check
     *
     * @param userToken       User token
     * @param paperCheckToken Paper Check token
     * @param notes           user notes
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivatePaperCheck(String userToken, String paperCheckToken, String notes) {
        return createPaperCheckStatusTransition(userToken,
                paperCheckToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED).notes(notes));
    }

    /**
     * Create Paper Check Status Transition
     *
     * @param userToken       User token
     * @param paperCheckToken Paper Check token
     * @param transition      Status transition information
     * @return HyperwalletStatusTransition new status for Paper Check
     */
    public HyperwalletStatusTransition createPaperCheckStatusTransition(String userToken, String paperCheckToken,
            HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(paperCheckToken)) {
            throw new HyperwalletException("Paper Check token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/users/" + userToken + "/paper-checks/" + paperCheckToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get Paper Check Status Transition
     *
     * @param userToken             User token
     * @param paperCheckToken       Paper Check token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getPaperCheckStatusTransition(String userToken, String paperCheckToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(paperCheckToken)) {
            throw new HyperwalletException("Paper Check token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/paper-checks/" + paperCheckToken + "/status-transitions/" + statusTransitionToken,
                HyperwalletStatusTransition.class);
    }

    /**
     * List All Paper Check Status Transition information
     *
     * @param userToken       User token
     * @param paperCheckToken Paper Check token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPaperCheckStatusTransitions(String userToken, String paperCheckToken) {
        return listPaperCheckStatusTransitions(userToken, paperCheckToken, null);
    }

    /**
     * List Paper Check Status Transition information
     *
     * @param userToken       User token
     * @param paperCheckToken Paper Check token
     * @param options         List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPaperCheckStatusTransitions(String userToken, String paperCheckToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(paperCheckToken)) {
            throw new HyperwalletException("Paper Check token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/paper-checks/" + paperCheckToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Transfers
    //--------------------------------------

    /**
     * Create Transfer Request
     *
     * @param transfer HyperwalletTransfer object to create
     * @return HyperwalletTransfer Transfer object created
     */
    public HyperwalletTransfer createTransfer(HyperwalletTransfer transfer) {
        if (transfer == null) {
            throw new HyperwalletException("Transfer is required");
        }
        if (StringUtils.isEmpty(transfer.getSourceToken())) {
            throw new HyperwalletException("Source token is required");
        }
        if (StringUtils.isEmpty(transfer.getDestinationToken())) {
            throw new HyperwalletException("Destination token is required");
        }
        if (StringUtils.isEmpty(transfer.getClientTransferId())) {
            throw new HyperwalletException("ClientTransferId is required");
        }
        transfer = copy(transfer);
        transfer.setStatus(null);
        transfer.setCreatedOn(null);
        transfer.setExpiresOn(null);
        return apiClient.post(url + "/transfers", transfer, HyperwalletTransfer.class);
    }

    /**
     * Get Transfer Request
     *
     * @param transferToken Transfer token assigned
     * @return HyperwalletTransfer Transfer
     */
    public HyperwalletTransfer getTransfer(String transferToken) {
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }
        return apiClient.get(url + "/transfers/" + transferToken, HyperwalletTransfer.class);
    }

    /**
     * List Transfer Requests
     *
     * @param options List filter option
     * @return HyperwalletList of HyperwalletTransfer
     */
    public HyperwalletList<HyperwalletTransfer> listTransfers(HyperwalletTransferListOptions options) {
        String url = paginate(this.url + "/transfers", options);
        if (options != null) {
            url = addParameter(url, "clientTransferId", options.getClientTransferId());
            url = addParameter(url, "sourceToken", options.getSourceToken());
            url = addParameter(url, "destinationToken", options.getDestinationToken());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletTransfer>>() {
        });
    }

    /**
     * List Transfer Requests
     *
     * @return HyperwalletList of HyperwalletTransfer
     */
    public HyperwalletList<HyperwalletTransfer> listTransfers() {
        return listTransfers(null);
    }

    /**
     * Create Transfer Status Transition
     *
     * @param transferToken Transfer token assigned
     * @param transition    transition value
     * @return HyperwalletStatusTransition new status for Transfer Request
     */
    public HyperwalletStatusTransition createTransferStatusTransition(String transferToken, HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/transfers/" + transferToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }

    /**
     * Get Transfer Status Transition
     *
     * @param transferToken         Transfer token assigned
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition new status for Transfer Request
     */
    public HyperwalletStatusTransition getTransferStatusTransition(String transferToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("statusTransitionToken is required");
        }
        return apiClient.get(url + "/transfers/" + transferToken + "/status-transitions/" + statusTransitionToken, HyperwalletStatusTransition.class);
    }

    /**
     * List Transfer Status Transition
     *
     * @param transferToken Transfer token assigned
     * @param options       List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listTransferStatusTransition(String transferToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }
        String url = paginate(this.url + "/transfers/" + transferToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Transfer refunds
    //--------------------------------------

    /**
     * Create Transfer Refund
     *
     * @param transferToken  Transfer token assigned
     * @param transferRefund Transfer Refund object to create
     * @return Created Transfer Refund
     */
    public HyperwalletTransferRefund createTransferRefund(String transferToken, HyperwalletTransferRefund transferRefund) {
        if (transferRefund == null) {
            throw new HyperwalletException("Transfer Refund is required");
        }
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }
        if (StringUtils.isEmpty(transferRefund.getClientRefundId())) {
            throw new HyperwalletException("ClientRefundId is required");
        }

        transferRefund = copy(transferRefund);
        transferRefund.clearStatus();
        transferRefund.clearCreatedOn();
        return apiClient.post(url + "/transfers/" + transferToken + "/refunds", transferRefund, HyperwalletTransferRefund.class);
    }

    /**
     * Get Transfer Refund
     *
     * @param transferToken       Transfer token assigned
     * @param transferRefundToken Transfer Refund token assigned
     * @return Transfer Refund object
     */
    public HyperwalletTransferRefund getTransferRefund(String transferToken, String transferRefundToken) {
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }
        if (StringUtils.isEmpty(transferRefundToken)) {
            throw new HyperwalletException("Transfer Refund token is required");
        }

        return apiClient.get(url + "/transfers/" + transferToken + "/refunds/" + transferRefundToken, HyperwalletTransferRefund.class);
    }

    /**
     * List Transfer Refund Requests
     *
     * @param options       List filter option
     * @param transferToken Transfer token assigned
     * @return HyperwalletList of HyperwalletTransferRefund
     */
    public HyperwalletList<HyperwalletTransferRefund> listTransferRefunds(String transferToken, HyperwalletTransferRefundListOptions options) {
        if (StringUtils.isEmpty(transferToken)) {
            throw new HyperwalletException("Transfer token is required");
        }

        String url = paginate(this.url + "/transfers/" + transferToken + "/refunds", options);
        if (options != null) {
            url = addParameter(url, "clientRefundId", options.getClientRefundId());
            url = addParameter(url, "sourceToken", options.getSourceToken());
            url = addParameter(url, "status", options.getStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletTransferRefund>>() {
        });
    }

    //--------------------------------------
    // PayPal Accounts
    //--------------------------------------

    /**
     * Create PayPal Account Request
     *
     * @param payPalAccount HyperwalletPayPalAccount object to create
     * @return HyperwalletPayPalAccount created PayPal account for the specified user
     */
    public HyperwalletPayPalAccount createPayPalAccount(HyperwalletPayPalAccount payPalAccount) {
        if (payPalAccount == null) {
            throw new HyperwalletException("PayPal Account is required");
        }
        if (StringUtils.isEmpty(payPalAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(payPalAccount.getTransferMethodCountry())) {
            throw new HyperwalletException("Transfer Method Country is required");
        }
        if (StringUtils.isEmpty(payPalAccount.getTransferMethodCurrency())) {
            throw new HyperwalletException("Transfer Method Currency is required");
        }
        if (StringUtils.isEmpty(payPalAccount.getEmail())) {
            throw new HyperwalletException("Email is required");
        }
        if (!StringUtils.isEmpty(payPalAccount.getToken())) {
            throw new HyperwalletException("PayPal Account token may not be present");
        }
        if (payPalAccount.getType() == null) {
            payPalAccount.setType(HyperwalletPayPalAccount.Type.PAYPAL_ACCOUNT);
        }
        payPalAccount = copy(payPalAccount);
        payPalAccount.setStatus(null);
        payPalAccount.setCreatedOn(null);
        return apiClient.post(url + "/users/" + payPalAccount.getUserToken() + "/paypal-accounts", payPalAccount, HyperwalletPayPalAccount.class);
    }

    /**
     * Get PayPal Account Request
     *
     * @param userToken          User token assigned
     * @param payPalAccountToken PayPal Account token assigned
     * @return HyperwalletPayPalAccount PayPal Account
     */
    public HyperwalletPayPalAccount getPayPalAccount(String userToken, String payPalAccountToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(payPalAccountToken)) {
            throw new HyperwalletException("PayPal Account token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/paypal-accounts/" + payPalAccountToken, HyperwalletPayPalAccount.class);
    }

    /**
     * List PayPal Accounts
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletPayPalAccount
     */
    public HyperwalletList<HyperwalletPayPalAccount> listPayPalAccounts(String userToken, HyperwalletPayPalAccountListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/paypal-accounts", options);
        if (options != null) {
            url = addParameter(url, "status", options.getStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletPayPalAccount>>() {
        });
    }

    /**
     * List PayPal Accounts
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletPayPalAccount
     */
    public HyperwalletList<HyperwalletPayPalAccount> listPayPalAccounts(String userToken) {
        return listPayPalAccounts(userToken, null);
    }

    /**
     * Deactivate PayPal Account
     *
     * @param userToken          User token
     * @param payPalAccountToken PayPal Account token
     * @return HyperwalletStatusTransition deactivated PayPal account
     */
    public HyperwalletStatusTransition deactivatePayPalAccount(String userToken, String payPalAccountToken) {
        return deactivatePayPalAccount(userToken, payPalAccountToken, null);
    }

    /**
     * Deactivate PayPal Account
     *
     * @param userToken          User token
     * @param payPalAccountToken PayPal Account token
     * @param notes              Comments regarding the status change
     * @return HyperwalletStatusTransition deactivated PayPal account
     */
    public HyperwalletStatusTransition deactivatePayPalAccount(String userToken, String payPalAccountToken, String notes) {
        return createPayPalAccountStatusTransition(userToken,
                payPalAccountToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED).notes(notes));
    }

    /**
     * Create PayPal Account Status Transition
     *
     * @param userToken          User token
     * @param payPalAccountToken PayPal Account token
     * @param transition         Status transition information
     * @return HyperwalletStatusTransition new status for PayPal Account
     */
    public HyperwalletStatusTransition createPayPalAccountStatusTransition(String userToken, String payPalAccountToken,
            HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(payPalAccountToken)) {
            throw new HyperwalletException("PayPal Account token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/users/" + userToken + "/paypal-accounts/" + payPalAccountToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get PayPal Account Status Transition
     *
     * @param userToken             User token
     * @param payPalAccountToken    PayPal Account token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getPayPalAccountStatusTransition(String userToken, String payPalAccountToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(payPalAccountToken)) {
            throw new HyperwalletException("PayPal Account token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/paypal-accounts/" + payPalAccountToken + "/status-transitions/" + statusTransitionToken,
                HyperwalletStatusTransition.class);
    }

    /**
     * List All PayPal Account Status Transition information
     *
     * @param userToken          User token
     * @param payPalAccountToken PayPal Account token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPayPalAccountStatusTransitions(String userToken, String payPalAccountToken) {
        return listPayPalAccountStatusTransitions(userToken, payPalAccountToken, null);
    }

    /**
     * List PayPal Account Status Transition information
     *
     * @param userToken          User token
     * @param payPalAccountToken PayPal Account token
     * @param options            List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPayPalAccountStatusTransitions(String userToken, String payPalAccountToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(payPalAccountToken)) {
            throw new HyperwalletException("PayPal Account token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/paypal-accounts/" + payPalAccountToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    /**
     * Update PayPal Account
     *
     * @param payPalAccount PayPal Account to Update.
     * @return HyperwalletPayPalAccount Updated PayPal Account
     */
    public HyperwalletPayPalAccount updatePayPalAccount(HyperwalletPayPalAccount payPalAccount) {
        if (payPalAccount == null) {
            throw new HyperwalletException("PayPal Account is required");
        }
        if (StringUtils.isEmpty(payPalAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(payPalAccount.getToken())) {
            throw new HyperwalletException("PayPal Account token is required");
        }
        return apiClient.put(url + "/users/" + payPalAccount.getUserToken() + "/paypal-accounts/" + payPalAccount.getToken(), payPalAccount,
                HyperwalletPayPalAccount.class);
    }

    //--------------------------------------
    // Venmo Accounts
    //--------------------------------------

    /**
     * Create Venmo Account
     *
     * @param venmoAccount HyperwalletVenmoAccount object to create
     * @return HyperwalletVenmoAccount created Venmo account for the specified user
     */
    public HyperwalletVenmoAccount createVenmoAccount(HyperwalletVenmoAccount venmoAccount) {
        if (venmoAccount == null) {
            throw new HyperwalletException("Venmo Account is required");
        }
        if (StringUtils.isEmpty(venmoAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(venmoAccount.getTransferMethodCountry())) {
            throw new HyperwalletException("Transfer Method Country is required");
        }
        if (StringUtils.isEmpty(venmoAccount.getTransferMethodCurrency())) {
            throw new HyperwalletException("Transfer Method Currency is required");
        }
        if (StringUtils.isEmpty(venmoAccount.getAccountId())) {
            throw new HyperwalletException("Account is required");
        }
        if (StringUtils.isNotEmpty(venmoAccount.getToken())) {
            throw new HyperwalletException("Venmo Account token may not be present");
        }
        if (venmoAccount.getType() == null) {
            venmoAccount.setType(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT);
        }
        venmoAccount = copy(venmoAccount);
        venmoAccount.setStatus(null);
        venmoAccount.setCreatedOn(null);
        return apiClient.post(url + "/users/" + venmoAccount.getUserToken() + "/venmo-accounts", venmoAccount, HyperwalletVenmoAccount.class);
    }

    /**
     * Get Venmo Account
     *
     * @param userToken         User token assigned
     * @param venmoAccountToken Venmo Account token assigned
     * @return HyperwalletVenmoAccount Venmo Account
     */
    public HyperwalletVenmoAccount getVenmoAccount(String userToken, String venmoAccountToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(venmoAccountToken)) {
            throw new HyperwalletException("venmo Account token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/venmo-accounts/" + venmoAccountToken, HyperwalletVenmoAccount.class);
    }

    /**
     * List Venmo Accounts
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletVenmoAccount
     */
    public HyperwalletList<HyperwalletVenmoAccount> listVenmoAccounts(String userToken, HyperwalletVenmoAccountListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/venmo-accounts", options);
        if (options != null) {
            url = addParameter(url, "type", options.getType());
            url = addParameter(url, "status", options.getStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletVenmoAccount>>() {
        });
    }

    /**
     * List Venmo Accounts
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletVenmoAccount
     */
    public HyperwalletList<HyperwalletVenmoAccount> listVenmoAccounts(String userToken) {
        return listVenmoAccounts(userToken, null);
    }

    /**
     * Update Venmo Account
     *
     * @param venmoAccount Venmo Account to Update.
     * @return HyperwalletVenmoAccount Updated Venmo Account
     */
    public HyperwalletVenmoAccount updateVenmoAccount(HyperwalletVenmoAccount venmoAccount) {
        if (venmoAccount == null) {
            throw new HyperwalletException("Venmo Account is required");
        }
        if (StringUtils.isEmpty(venmoAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(venmoAccount.getToken())) {
            throw new HyperwalletException("Venmo Account token is required");
        }
        return apiClient.put(url + "/users/" + venmoAccount.getUserToken() + "/venmo-accounts/" + venmoAccount.getToken(), venmoAccount,
                HyperwalletVenmoAccount.class);
    }

    /**
     * Deactivate Venmo Account
     *
     * @param userToken         User token
     * @param venmoAccountToken Venmo account token
     * @return HyperwalletStatusTransition deactivated venmo account
     */
    public HyperwalletStatusTransition deactivateVenmoAccount(String userToken, String venmoAccountToken) {
        return deactivateVenmoAccount(userToken, venmoAccountToken, null);
    }

    /**
     * Deactivate Venmo Account
     *
     * @param userToken         User token
     * @param venmoAccountToken Venmo account token
     * @param notes             Comments regarding the status change
     * @return HyperwalletStatusTransition deactivated Venmo account
     */
    public HyperwalletStatusTransition deactivateVenmoAccount(String userToken, String venmoAccountToken, String notes) {
        return createVenmoAccountStatusTransition(userToken,
                venmoAccountToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED).notes(notes));
    }

    /**
     * Create Venmo Account Status Transition
     *
     * @param userToken         User token
     * @param venmoAccountToken Venmo account token
     * @param transition        Status transition information
     * @return HyperwalletStatusTransition new status for Venmo Account
     */
    public HyperwalletStatusTransition createVenmoAccountStatusTransition(String userToken, String venmoAccountToken,
            HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(venmoAccountToken)) {
            throw new HyperwalletException("Venmo account token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/users/" + userToken + "/venmo-accounts/" + venmoAccountToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get Venmo Account Status Transition
     *
     * @param userToken             User token
     * @param venmoAccountToken     Venmo account token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getVenmoAccountStatusTransition(String userToken, String venmoAccountToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(venmoAccountToken)) {
            throw new HyperwalletException("Venmo account token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/venmo-accounts/" + venmoAccountToken + "/status-transitions/" + statusTransitionToken,
                HyperwalletStatusTransition.class);
    }

    /**
     * List All Venmo Account Status Transition information
     *
     * @param userToken         User token
     * @param venmoAccountToken Venmo account token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listVenmoAccountStatusTransitions(String userToken, String venmoAccountToken) {
        return listVenmoAccountStatusTransitions(userToken, venmoAccountToken, null);
    }

    /**
     * List Venmo Account Status Transition information
     *
     * @param userToken         User token
     * @param venmoAccountToken Venmo account token
     * @param options           List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listVenmoAccountStatusTransitions(String userToken, String venmoAccountToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(venmoAccountToken)) {
            throw new HyperwalletException("Venmo account token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/venmo-accounts/" + venmoAccountToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Bank Accounts
    //--------------------------------------

    /**
     * Create Bank Account
     *
     * @param bankAccount bank account representation
     * @return HyperwalletBankAccount created bank account for the specicic user
     */
    public HyperwalletBankAccount createBankAccount(HyperwalletBankAccount bankAccount) {
        if (bankAccount == null) {
            throw new HyperwalletException("Bank Account is required");
        }
        if (StringUtils.isEmpty(bankAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (!StringUtils.isEmpty(bankAccount.getToken())) {
            throw new HyperwalletException("Bank Account token may not be present");
        }
        bankAccount = copy(bankAccount);
        bankAccount.createdOn(null);
        bankAccount.setStatus(null);
        return apiClient.post(url + "/users/" + bankAccount.getUserToken() + "/bank-accounts", bankAccount, HyperwalletBankAccount.class);
    }

    /**
     * Get Bank Account
     *
     * @param userToken           User token assigned
     * @param transferMethodToken Bank account token assigned
     * @return HyperwalletBankAccount bank account information
     */
    public HyperwalletBankAccount getBankAccount(String userToken, String transferMethodToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(transferMethodToken)) {
            throw new HyperwalletException("Bank Account token is required");
        }
        return apiClient.get(url + "/users/" + userToken + "/bank-accounts/" + transferMethodToken, HyperwalletBankAccount.class);
    }

    /**
     * Update Bank Account
     *
     * @param bankAccount Bank Account to update.
     * @return HyperwalletBankAccount updated Bank Account
     */
    public HyperwalletBankAccount updateBankAccount(HyperwalletBankAccount bankAccount) {
        if (bankAccount == null) {
            throw new HyperwalletException("Bank Account is required");
        }
        if (StringUtils.isEmpty(bankAccount.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankAccount.getToken())) {
            throw new HyperwalletException("Bank Account token is required");
        }
        return apiClient.put(url + "/users/" + bankAccount.getUserToken() + "/bank-accounts/" + bankAccount.getToken(), bankAccount,
                HyperwalletBankAccount.class);
    }

    /**
     * List Bank Accounts
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletBankAccount
     */
    public HyperwalletList<HyperwalletBankAccount> listBankAccounts(String userToken) {
        return listBankAccounts(userToken, null);
    }

    /**
     * List Bank Accounts
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletBankAccount
     */
    public HyperwalletList<HyperwalletBankAccount> listBankAccounts(String userToken, HyperwalletBankAccountsListPaginationOptions options) {

        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts", options);
        if (options != null) {
            url = addParameter(url, "type", options.getType());
            url = addParameter(url, "status", options.getStatus());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletBankAccount>>() {
        });
    }

    /**
     * Deactivate Bank Account
     *
     * @param userToken        User token
     * @param bankAccountToken Bank Account token
     * @return HyperwalletStatusTransition deactivated bank account
     */
    public HyperwalletStatusTransition deactivateBankAccount(String userToken, String bankAccountToken) {
        return createBankAccountStatusTransition(userToken, bankAccountToken,
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Create Bank Account Status Transition
     *
     * @param userToken        User token
     * @param bankAccountToken Bank Account token
     * @param transition       Status transition information
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition createBankAccountStatusTransition(String userToken, String bankAccountToken,
            HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Bank Account token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", transition,
                HyperwalletStatusTransition.class);
    }

    /**
     * Get Bank Account Status Transition
     *
     * @param userToken             User token
     * @param bankAccountToken      Bank Account token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getBankAccountStatusTransition(String userToken, String bankAccountToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Bank Account token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        return apiClient.get(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions/" + statusTransitionToken,
                HyperwalletStatusTransition.class);
    }

    /**
     * List All Bank Account Status Transition
     *
     * @param userToken        User token
     * @param bankAccountToken Bank Account token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(String userToken, String bankAccountToken) {
        return listBankAccountStatusTransitions(userToken, bankAccountToken, null);
    }

    /**
     * List Bank Account Status Transition
     *
     * @param userToken        User token
     * @param bankAccountToken Bank Account token
     * @param options          List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(String userToken, String bankAccountToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Bank Account token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Balances
    //--------------------------------------

    /**
     * List all User's Balances
     *
     * @param userToken User token assigned
     * @return HyperwalletList of HyperwalletBalance
     */
    public HyperwalletList<HyperwalletBalance> listBalancesForUser(String userToken) {
        return listBalancesForUser(userToken, null);
    }

    /**
     * List all User's Balances
     *
     * @param userToken User token assigned
     * @param options   List filter option
     * @return HyperwalletList list of HyperwalletBalance
     */
    public HyperwalletList<HyperwalletBalance> listBalancesForUser(String userToken, HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = this.url + "/users/" + userToken + "/balances";
        if (options != null) {
            url = addParameter(url, "currency", options.getCurrency());
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "limit", options.getLimit());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletBalance>>() {
        });
    }

    /**
     * List all Program account balances
     *
     * @param accountToken Account token assigned
     * @param programToken Program token assigned
     * @return HyperwalletList of HyperwalletBalance
     */
    public HyperwalletList<HyperwalletBalance> listBalancesForAccount(String programToken, String accountToken) {
        return listBalancesForAccount(programToken, accountToken, null);
    }

    /**
     * List all Program account balances
     *
     * @param accountToken Account token assigned
     * @param programToken Program token assigned
     * @param options      List filter option
     * @return HyperwalletList list of HyperwalletBalance
     */
    public HyperwalletList<HyperwalletBalance> listBalancesForAccount(String programToken, String accountToken,
            HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(programToken)) {
            throw new HyperwalletException("Program token is required");
        }
        if (StringUtils.isEmpty(accountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        String url = this.url + "/programs/" + programToken + "/accounts/" + accountToken + "/balances";
        if (options != null) {
            url = addParameter(url, "currency", options.getCurrency());
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "limit", options.getLimit());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletBalance>>() {
        });
    }

    /**
     * List all User's Prepaid Card Balances
     *
     * @param userToken        User token assigned
     * @param prepaidCardToken Prepaid Card token assigned from User's Prepaid Card
     * @return HyperwalletList of HyperwalletBalances
     */
    public HyperwalletList<HyperwalletBalance> listBalancesForPrepaidCard(String userToken, String prepaidCardToken) {
        return listBalancesForPrepaidCard(userToken, prepaidCardToken, null);
    }

    /**
     * List all User's Prepaid Card Balances
     *
     * @param userToken        User token assigned
     * @param prepaidCardToken Prepaid Card token assigned from User's Prepaid Card
     * @param options          List filter option
     * @return HyperwalletList of HyperwalletBalances
     */
    public HyperwalletList<HyperwalletBalance> listBalancesForPrepaidCard(String userToken, String prepaidCardToken,
            HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        String url = this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/balances";
        if (options != null) {
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "limit", options.getLimit());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletBalance>>() {
        });
    }

    //--------------------------------------
    // Payments
    //--------------------------------------

    /**
     * Create Payment
     *
     * @param payment Payment
     * @return HyperwalletPayment created payment information
     */
    public HyperwalletPayment createPayment(HyperwalletPayment payment) {
        if (payment == null) {
            throw new HyperwalletException("Payment is required");
        }
        if (!StringUtils.isEmpty(payment.getToken())) {
            throw new HyperwalletException("Payment token may not be present");
        }
        payment = copy(payment);
        payment.setCreatedOn(null);
        return apiClient.post(url + "/payments", payment, HyperwalletPayment.class);
    }

    /**
     * Get Payment
     *
     * @param paymentToken Payment token
     * @return HyperwalletPayment
     */
    public HyperwalletPayment getPayment(String paymentToken) {
        if (StringUtils.isEmpty(paymentToken)) {
            throw new HyperwalletException("Payment token is required");
        }
        return apiClient.get(url + "/payments/" + paymentToken, HyperwalletPayment.class);
    }

    /**
     * List all Payments
     *
     * @return HyperwalletList of HyperwalletPayment
     */
    public HyperwalletList<HyperwalletPayment> listPayments() {
        return listPayments(null);
    }

    /**
     * List all Payments
     *
     * @param options List filter option
     * @return HyperwalletList of HyperwalletPayment
     */
    public HyperwalletList<HyperwalletPayment> listPayments(HyperwalletPaymentListOptions options) {
        String url = paginate(this.url + "/payments", options);
        if (options != null) {
            url = addParameter(url, "clientPaymentId", options.getClientPaymentId());
            url = addParameter(url, "releaseDate", convert(options.getReleaseDate()));
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletPayment>>() {
        });
    }


    /**
     * Create Payment Status Transition
     *
     * @param paymentToken Payment token
     * @param transition   Status transition information
     * @return HyperwalletStatusTransition new status for Payment
     */
    public HyperwalletStatusTransition createPaymentStatusTransition(String paymentToken, HyperwalletStatusTransition transition) {
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        if (StringUtils.isEmpty(paymentToken)) {
            throw new HyperwalletException("Payment token is required");
        }
        if (!StringUtils.isEmpty(transition.getToken())) {
            throw new HyperwalletException("Status Transition token may not be present");
        }
        transition = copy(transition);
        transition.setCreatedOn(null);
        transition.setFromStatus(null);
        transition.setToStatus(null);
        return apiClient.post(url + "/payments/" + paymentToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }

    /**
     * Get Payment Status Transition
     *
     * @param paymentToken          Payment token
     * @param statusTransitionToken Status transition token
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition getPaymentStatusTransition(String paymentToken, String statusTransitionToken) {
        if (StringUtils.isEmpty(paymentToken)) {
            throw new HyperwalletException("Payment token is required");
        }
        if (StringUtils.isEmpty(statusTransitionToken)) {
            throw new HyperwalletException("Transition token is required");
        }
        return apiClient.get(url + "/payments/" + paymentToken + "/status-transitions/" + statusTransitionToken, HyperwalletStatusTransition.class);
    }

    /**
     * List All Payment Status Transition information
     *
     * @param paymentToken Payment token
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPaymentStatusTransitions(String paymentToken) {
        return listPaymentStatusTransitions(paymentToken, null);
    }

    /**
     * List Payment Status Transition information
     *
     * @param paymentToken Payment token
     * @param options      List filter option
     * @return HyperwalletList of HyperwalletStatusTransition
     */
    public HyperwalletList<HyperwalletStatusTransition> listPaymentStatusTransitions(String paymentToken,
            HyperwalletStatusTransitionListPaginationOptions options) {
        if (StringUtils.isEmpty(paymentToken)) {
            throw new HyperwalletException("Payment token is required");
        }
        String url = paginate(this.url + "/payments/" + paymentToken + "/status-transitions", options);
        if (options != null) {
            url = addParameter(url, "transition", options.getTransition());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    //--------------------------------------
    // Programs
    //--------------------------------------

    /**
     * Get Program
     *
     * @param programToken Program token
     * @return HyperwalletProgram
     */
    public HyperwalletProgram getProgram(String programToken) {
        if (StringUtils.isEmpty(programToken)) {
            throw new HyperwalletException("Program token is required");
        }
        return apiClient.get(url + "/programs/" + programToken, HyperwalletProgram.class);
    }

    //--------------------------------------
    // Program Accounts
    //--------------------------------------

    /**
     * Get Programs Account
     *
     * @param programToken Program token
     * @param accountToken Program account token
     * @return HyperwalletAccount
     */
    public HyperwalletAccount getProgramAccount(String programToken, String accountToken) {
        if (StringUtils.isEmpty(programToken)) {
            throw new HyperwalletException("Program token is required");
        }
        if (StringUtils.isEmpty(accountToken)) {
            throw new HyperwalletException("Account token is required");
        }

        return apiClient.get(url + "/programs/" + programToken + "/accounts/" + accountToken, HyperwalletAccount.class);
    }

    //--------------------------------------
    // Transfer Method Configurations
    //--------------------------------------

    /**
     * Get Transfer Method Configuration
     *
     * @param userToken   User token
     * @param country     Country
     * @param currency    Currency
     * @param type        Type of Transfer Method to retrieve
     * @param profileType Type of User profile
     * @return HyperwalletTransferMethodConfiguration
     */
    public HyperwalletTransferMethodConfiguration getTransferMethodConfiguration(String userToken, String country, String currency,
            HyperwalletTransferMethod.Type type, HyperwalletUser.ProfileType profileType) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(country)) {
            throw new HyperwalletException("Country is required");
        }
        if (StringUtils.isEmpty(currency)) {
            throw new HyperwalletException("Currency is required");
        }
        if (type == null) {
            throw new HyperwalletException("Type is required");
        }
        if (profileType == null) {
            throw new HyperwalletException("Profile Type is required");
        }
        return apiClient.get(url + "/transfer-method-configurations"
                        + "?userToken=" + userToken
                        + "&country=" + country
                        + "&currency=" + currency
                        + "&type=" + type.name()
                        + "&profileType=" + profileType.name(),
                HyperwalletTransferMethodConfiguration.class);
    }

    /**
     * List all Transfer Method Configuration associated with User
     *
     * @param userToken User token
     * @return HyperwalletList of HyperwalletTransferMethodConfiguration
     */
    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken) {
        return listTransferMethodConfigurations(userToken, null);
    }

    /**
     * List all Transfer Method Configuration associated with User
     *
     * @param userToken User token
     * @param options   List filter options
     * @return HyperwalletList of HyperwalletTransferMethodConfiguration
     */
    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken,
            HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/transfer-method-configurations?userToken=" + userToken, options);
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletTransferMethodConfiguration>>() {
        });
    }

    //--------------------------------------
    // Receipts
    //--------------------------------------

    /**
     * List all program account receipts
     *
     * @param programToken Program token
     * @param accountToken Program account token
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listReceiptsForProgramAccount(String programToken, String accountToken) {
        return listReceiptsForProgramAccount(programToken, accountToken, null);
    }

    /**
     * List all program account receipts
     *
     * @param programToken Program token
     * @param accountToken Program account token
     * @param options      List filter options
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listReceiptsForProgramAccount(String programToken, String accountToken,
            HyperwalletReceiptPaginationOptions options) {
        if (StringUtils.isEmpty(programToken)) {
            throw new HyperwalletException("Program token is required");
        }
        if (StringUtils.isEmpty(accountToken)) {
            throw new HyperwalletException("Account token is required");
        }
        String url = paginate(this.url + "/programs/" + programToken + "/accounts/" + accountToken + "/receipts", options);
        if (options != null && options.getType() != null) {
            url = addParameter(url, "type", options.getType().name());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletReceipt>>() {
        });
    }

    /**
     * List all user receipts
     *
     * @param userToken User token
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listReceiptsForUser(String userToken) {
        return listReceiptsForUser(userToken, null);
    }

    /**
     * List all user receipts
     *
     * @param userToken Program token
     * @param options   List filter options
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listReceiptsForUser(String userToken, HyperwalletReceiptPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/receipts", options);
        if (options != null && options.getType() != null) {
            url = addParameter(url, "type", options.getType().name());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletReceipt>>() {
        });
    }

    /**
     * List all prepaid card receipts
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listReceiptsForPrepaidCard(String userToken, String prepaidCardToken) {
        return listReceiptsForPrepaidCard(userToken, prepaidCardToken, null);
    }

    /**
     * List all prepaid card receipts
     *
     * @param userToken        User token
     * @param prepaidCardToken Prepaid card token
     * @param options          List filter options
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listReceiptsForPrepaidCard(String userToken, String prepaidCardToken,
            HyperwalletReceiptPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid card token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/receipts", options);
        if (options != null && options.getType() != null) {
            url = addParameter(url, "type", options.getType().name());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletReceipt>>() {
        });
    }

    //--------------------------------------
    // Webhook Notification
    //--------------------------------------

    /**
     * Retrieve webhook event notification
     *
     * @param webhookToken Webhook token
     * @return HyperwalletWebhookNotification
     */
    public HyperwalletWebhookNotification getWebhookEvent(String webhookToken) {
        if (StringUtils.isEmpty(webhookToken)) {
            throw new HyperwalletException("Webhook token is required");
        }
        return apiClient.get(url + "/webhook-notifications/" + webhookToken, HyperwalletWebhookNotification.class);
    }

    /**
     * List all webhook event notifications
     *
     * @return HyperwalletList of HyperwalletWebhookNotification
     */
    public HyperwalletList<HyperwalletWebhookNotification> listWebhookEvents() {
        return listWebhookEvents(null);
    }

    /**
     * List all webhook event notifications
     *
     * @param options List filter options
     * @return HyperwalletList of HyperwalletWebhookNotification
     */
    public HyperwalletList<HyperwalletWebhookNotification> listWebhookEvents(HyperwalletWebhookNotificationPaginationOptions options) {
        String url = paginate(this.url + "/webhook-notifications", options);
        if (options != null) {
            url = addParameter(url, "type", options.getType());
            url = addParameter(url, "programToken", options.getProgramToken());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletWebhookNotification>>() {
        });
    }

    //--------------------------------------
    // Transfer Methods
    //--------------------------------------

    /**
     * Create a Transfer Method
     *
     * @param jsonCacheToken String JSON cache token
     * @param transferMethod TransferMethod object to create
     * @return HyperwalletTransferMethod Transfer Method object created
     */
    public HyperwalletTransferMethod createTransferMethod(String jsonCacheToken, HyperwalletTransferMethod transferMethod) {

        if (transferMethod == null || StringUtils.isEmpty(transferMethod.getUserToken())) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(jsonCacheToken)) {
            throw new HyperwalletException("JSON token is required");
        }
        transferMethod = copy(transferMethod);

        transferMethod.setToken(null);
        transferMethod.setStatus(null);
        transferMethod.setCreatedOn(null);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Json-Cache-Token", jsonCacheToken);

        return apiClient.post(url + "/users/" + transferMethod.getUserToken() + "/transfer-methods", transferMethod, HyperwalletTransferMethod.class,
                headers);
    }


    /**
     * Create a Transfer Method
     *
     * @param jsonCacheToken String JSON cache token
     * @param userToken      String user token
     * @return HyperwalletTransferMethod Transfer Method object created
     */
    public HyperwalletTransferMethod createTransferMethod(String jsonCacheToken, String userToken) {

        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(jsonCacheToken)) {
            throw new HyperwalletException("JSON token is required");
        }

        HyperwalletTransferMethod transferMethod = new HyperwalletTransferMethod();
        transferMethod.setUserToken(userToken);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Json-Cache-Token", jsonCacheToken);

        return apiClient.post(url + "/users/" + transferMethod.getUserToken() + "/transfer-methods", transferMethod, HyperwalletTransferMethod.class,
                headers);
    }

    /**
     * List Transfer Methods
     *
     * @param userToken String user token
     * @param options   List filter option
     * @return HyperwalletList of HyperwalletTransferMethod
     */
    public HyperwalletList<HyperwalletTransferMethod> listTransferMethods(String userToken, HyperwalletTransferMethodListOptions options) {
        String url = paginate(this.url + "/users/" + userToken + "/transfer-methods", options);
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }

        if (options != null && options.getStatus() != null) {
            url = addParameter(url, "status", options.getStatus());
            url = addParameter(url, "type", options.getType());

        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletTransferMethod>>() {
        });
    }

    //--------------------------------------
    // Upload documents for user endpoint
    //--------------------------------------

    /**
     * Upload documents
     *
     * @param userToken  userToken for which documents to be uploaded
     * @param uploadData HyperwalletVerificationDocument to get uploaded
     * @return HyperwalletUser user object with document upload status
     */
    public HyperwalletUser uploadUserDocuments(String userToken, List<HyperwalletVerificationDocument> uploadData) {
        Multipart multipart = new Multipart();
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is not present");
        }
        if (uploadData == null || uploadData.size() < 1) {
            throw new HyperwalletException("Data for upload is missing");
        }
        if (uploadData.get(0).getUploadFiles() == null || uploadData.get(0).getUploadFiles().size() < 1) {
            throw new HyperwalletException("Upload Files are missing");
        }
        try {
            multipart = HyperwalletMultipartUtils.convert(uploadData);
        } catch (IOException e) {
            throw new HyperwalletException("Unable to convert to Multipart formdata");
        }
        return apiClient.put(url + "/users/" + userToken, multipart, HyperwalletUser.class);
    }

    //--------------------------------------
    // Internal utils
    //--------------------------------------

    private String paginate(String url, HyperwalletPaginationOptions options) {
        if (options == null) {
            return url;
        }
        url = addParameter(url, "createdAfter", convert(options.getCreatedAfter()));
        url = addParameter(url, "createdBefore", convert(options.getCreatedBefore()));
        url = addParameter(url, "sortBy", options.getSortBy());
        url = addParameter(url, "limit", options.getLimit());
        return url;
    }

    private String addParameter(String url, String key, Object value) {
        if (url == null || key == null || value == null) {
            return url;
        }
        return url + (url.indexOf("?") == -1 ? "?" : "&") + key + "=" + value;
    }

    private String convert(Date in) {
        if (in == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(in);
    }

    private void setProgramToken(HyperwalletUser user) {
        if (user != null && user.getProgramToken() == null) {
            user.setProgramToken(this.programToken);
        }
    }

    private void setProgramToken(HyperwalletPayment payment) {
        if (payment != null && payment.getProgramToken() == null) {
            payment.setProgramToken(this.programToken);
        }
    }

    private HyperwalletUser copy(HyperwalletUser user) {
        user = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(user), HyperwalletUser.class);
        setProgramToken(user);
        return user;
    }

    private HyperwalletPayment copy(HyperwalletPayment payment) {
        payment = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(payment), HyperwalletPayment.class);
        setProgramToken(payment);
        return payment;
    }

    private HyperwalletPrepaidCard copy(HyperwalletPrepaidCard method) {
        method = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(method), HyperwalletPrepaidCard.class);
        return method;
    }

    private HyperwalletBusinessStakeholder copy(HyperwalletBusinessStakeholder method) {
        method = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(method), HyperwalletBusinessStakeholder.class);
        return method;
    }

    private HyperwalletBankCard copy(HyperwalletBankCard card) {
        card = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(card), HyperwalletBankCard.class);
        return card;
    }

    private HyperwalletPaperCheck copy(HyperwalletPaperCheck check) {
        check = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(check), HyperwalletPaperCheck.class);
        return check;
    }

    private HyperwalletBankAccount copy(HyperwalletBankAccount method) {
        method = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(method), HyperwalletBankAccount.class);
        return method;
    }

    private HyperwalletStatusTransition copy(HyperwalletStatusTransition statusTransition) {
        statusTransition = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(statusTransition), HyperwalletStatusTransition.class);
        return statusTransition;
    }

    private HyperwalletTransferMethod copy(HyperwalletTransferMethod transferMethod) {
        transferMethod = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(transferMethod), HyperwalletTransferMethod.class);
        return transferMethod;
    }

    private HyperwalletTransfer copy(HyperwalletTransfer transfer) {
        transfer = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(transfer), HyperwalletTransfer.class);
        return transfer;
    }

    private HyperwalletPayPalAccount copy(HyperwalletPayPalAccount payPalAccount) {
        payPalAccount = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(payPalAccount), HyperwalletPayPalAccount.class);
        return payPalAccount;
    }

    private HyperwalletTransferRefund copy(HyperwalletTransferRefund transferRefund) {
        return HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(transferRefund), HyperwalletTransferRefund.class);
    }

    private HyperwalletVenmoAccount copy(HyperwalletVenmoAccount venmoAccount) {
        return HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(venmoAccount), HyperwalletVenmoAccount.class);
    }

}
