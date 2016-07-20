package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.util.HyperwalletApiClient;
import com.hyperwallet.clientsdk.util.HyperwalletJsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Hyperwallet Client
 */
public class Hyperwallet {

    public static final String VERSION = "0.1.0";

    private final HyperwalletApiClient apiClient;
    private final String programToken;
    private final String url;

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username     API key assigned
     * @param password     API Password assigned
     * @param programToken API program token
     * @param server          API serer url
     */
    public Hyperwallet(final String username, final String password, final String programToken, final String server) {
        apiClient = new HyperwalletApiClient(username, password, VERSION);
        this.programToken = programToken;
        this.url = StringUtils.isEmpty(server) ? "https://api.sandbox.hyperwallet.com/rest/v3" : server + "/rest/v3";
    }

    /**
     * Create Hyperwallet SDK instance
     *
     * @param username     API key assigned
     * @param password     API password
     * @param programToken API program token assigned
     */
    public Hyperwallet(final String username, final String password, final String programToken) {
        this(username, password, programToken, null);
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
    public HyperwalletList<HyperwalletUser> listUsers(HyperwalletPaginationOptions options) {
        String url = paginate(this.url + "/users", options);
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletUser>>() {
        });
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
    public HyperwalletPrepaidCard createPrepaidCard(HyperwalletPrepaidCard prepaidCard) {
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
            prepaidCard.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
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
    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards", options);
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletPrepaidCard>>() {
        });
    }

    /**
     * Suspend a prepaid card
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition suspendPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.SUSPENDED));
    }

    /**
     * Unsuspend a prepaid card
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition unsuspendPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNSUSPENDED));
    }

    /**
     * Mark a prepaid card as lost or stolen
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition lostOrStolenPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.LOST_OR_STOLEN));
    }

    /**
     * Deactivate a prepaid card
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition deactivatePrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Lock a prepaid card
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition lockPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.LOCKED));
    }

    /**
     * Unlock a prepaid card
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return The status transition
     */
    public HyperwalletStatusTransition unlockPrepaidCard(String userToken, String prepaidCardToken) {
        return createPrepaidCardStatusTransition(userToken, prepaidCardToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLOCKED));
    }

    /**
     * Create Prepaid Card Status Transition
     *
     * @param userToken        Usert token
     * @param prepaidCardToken Prepaid Card token
     * @param transition       Status transition information
     * @return HyperwalletStatusTransition new status for Prepaid Card
     */
    public HyperwalletStatusTransition createPrepaidCardStatusTransition(String userToken, String prepaidCardToken, HyperwalletStatusTransition transition) {
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
        return apiClient.post(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
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
        return apiClient.get(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions/" + statusTransitionToken, HyperwalletStatusTransition.class);
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
    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(String userToken, String prepaidCardToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", options);
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
        return apiClient.put(url + "/users/" + bankAccount.getUserToken() + "/bank-accounts/" + bankAccount.getToken(), bankAccount, HyperwalletBankAccount.class);
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
    public HyperwalletList<HyperwalletBankAccount> listBankAccounts(String userToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts", options);
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
        return createBankAccountStatusTransition(userToken, bankAccountToken, new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED));
    }

    /**
     * Create Bank Account Status Transition
     *
     * @param userToken        User token
     * @param bankAccountToken Bank Account token
     * @param transition       Status transition information
     * @return HyperwalletStatusTransition
     */
    public HyperwalletStatusTransition createBankAccountStatusTransition(String userToken, String bankAccountToken, HyperwalletStatusTransition transition) {
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
        return apiClient.post(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
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
    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(String userToken, String bankAccountToken, HyperwalletPaginationOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(bankAccountToken)) {
            throw new HyperwalletException("Bank Account token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", options);
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
            url = addParameter(url, "offset", options.getOffset());
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
    public HyperwalletList<HyperwalletBalance> listBalancesForPrepaidCard(String userToken, String prepaidCardToken, HyperwalletBalanceListOptions options) {
        if (StringUtils.isEmpty(userToken)) {
            throw new HyperwalletException("User token is required");
        }
        if (StringUtils.isEmpty(prepaidCardToken)) {
            throw new HyperwalletException("Prepaid Card token is required");
        }
        String url = this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/balances";
        if (options != null) {
            url = addParameter(url, "sortBy", options.getSortBy());
            url = addParameter(url, "offset", options.getOffset());
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
            url = addParameter(url, "releasedOn", convert(options.getReleasedOn()));
            url = addParameter(url, "currency", options.getCurrency());
        }
        return apiClient.get(url, new TypeReference<HyperwalletList<HyperwalletPayment>>() {
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
    public HyperwalletTransferMethodConfiguration getTransferMethodConfiguration(String userToken, String country, String currency, HyperwalletTransferMethod.Type type, HyperwalletUser.ProfileType profileType) {
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
    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken, HyperwalletPaginationOptions options) {
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
    public HyperwalletList<HyperwalletReceipt> listProgramAccountReceipts(String programToken, String accountToken) {
        return listProgramAccountReceipts(programToken, accountToken, null);
    }

    /**
     * List all program account receipts
     *
     * @param programToken Program token
     * @param accountToken Program account token
     * @param options List filter options
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listProgramAccountReceipts(String programToken, String accountToken, HyperwalletReceiptPaginationOptions options) {
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
    public HyperwalletList<HyperwalletReceipt> listUserReceipts(String userToken) {
        return listUserReceipts(userToken, null);
    }

    /**
     * List all user receipts
     *
     * @param userToken Program token
     * @param options List filter options
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listUserReceipts(String userToken, HyperwalletReceiptPaginationOptions options) {
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
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listPrepaidCardReceipts(String userToken, String prepaidCardToken) {
        return listPrepaidCardReceipts(userToken, prepaidCardToken, null);
    }

    /**
     * List all prepaid card receipts
     *
     * @param userToken User token
     * @param prepaidCardToken Prepaid card token
     * @param options List filter options
     * @return HyperwalletList of HyperwalletReceipt
     */
    public HyperwalletList<HyperwalletReceipt> listPrepaidCardReceipts(String userToken, String prepaidCardToken, HyperwalletReceiptPaginationOptions options) {
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
    // Internal utils
    //--------------------------------------

    private String paginate(String url, HyperwalletPaginationOptions options) {
        if (options == null) {
            return url;
        }
        url = addParameter(url, "createdAfter", convert(options.getCreatedAfter()));
        url = addParameter(url, "createdBefore", convert(options.getCreatedBefore()));
        url = addParameter(url, "sortBy", options.getSortBy());
        url = addParameter(url, "offset", options.getOffset());
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

    private HyperwalletBankAccount copy(HyperwalletBankAccount method) {
        method = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(method), HyperwalletBankAccount.class);
        return method;
    }

    private HyperwalletStatusTransition copy(HyperwalletStatusTransition statusTransition) {
        statusTransition = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(statusTransition), HyperwalletStatusTransition.class);
        return statusTransition;
    }

}
