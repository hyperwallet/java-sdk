package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Hyperwallet {

    private final HyperwalletUtil util;
    private final String version = "0.0.2";
    private final String url;

    public Hyperwallet(final String username, final String password, final String programToken, final String url) {
        util = new HyperwalletUtil(username, password, programToken, version);
        this.url = url == null ? "https://beta.paylution.com/rest/v3" : url;
    }

    public Hyperwallet(final String username, final String password, final String programToken) {
        this(username, password, programToken, null);
    }

    public Hyperwallet(final String username, final String password) {
        this(username, password, null);
    }

    // Users

    public HyperwalletUser createUser(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (user.getToken() != null) {
            throw new HyperwalletException("User token may not be present");
        }
        user = util.clean(user);
        user.setStatus(null);
        user.setCreatedOn(null);
        return util.post(url + "/users", user, HyperwalletUser.class);
    }

    public HyperwalletUser getUser(String token) {
        return util.get(url + "/users/" + token, HyperwalletUser.class);
    }

    public HyperwalletUser updateUser(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (user.getToken() == null) {
            throw new HyperwalletException("User token is required");
        }
        user = util.clean(user);
        user.setStatus(null);
        user.setCreatedOn(null);
        user.setProgramToken(null);
        return util.put(url + "/users/" + user.getToken(), user, HyperwalletUser.class);
    }

    public HyperwalletList<HyperwalletUser> listUsers() {
        return listUsers(null);
    }

    public HyperwalletList<HyperwalletUser> listUsers(HyperwalletPaginationOptions options) {
        String url = paginate(this.url + "/users", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletUser>>() {
        });
    }

    // Transfer Methods - Prepaid Card

    public HyperwalletPrepaidCard createPrepaidCard(HyperwalletPrepaidCard prepaidCard) {
        if (prepaidCard == null) {
            throw new HyperwalletException("Card is required");
        }
        if (prepaidCard.token != null) {
            throw new HyperwalletException("Card token may not be present");
        }
        if (prepaidCard.userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        prepaidCard = util.clean(prepaidCard);
        prepaidCard.createdOn = null;
        prepaidCard.status = null;
        prepaidCard.cardType = null;
        return util.post(url + "/users/" + prepaidCard.userToken + "/prepaid-cards", prepaidCard, HyperwalletPrepaidCard.class);
    }

    public HyperwalletPrepaidCard getPrepaidCard(HyperwalletUser user, String transferMethodToken) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        return getPrepaidCard(user.getToken(), transferMethodToken);
    }

    public HyperwalletPrepaidCard getPrepaidCard(String userToken, String transferMethodToken) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (transferMethodToken == null) {
            throw new HyperwalletException("Card token is required");
        }
        return util.get(url + "/users/" + userToken + "/prepaid-cards/" + transferMethodToken, HyperwalletPrepaidCard.class);
    }

    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(HyperwalletUser user, HyperwalletPaginationOptions options) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        return listPrepaidCards(user.getToken(), options);
    }

    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        return listPrepaidCards(user.getToken(), null);
    }

    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken) {
        return listPrepaidCards(userToken, null);
    }

    public HyperwalletList<HyperwalletPrepaidCard> listPrepaidCards(String userToken, HyperwalletPaginationOptions options) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletPrepaidCard>>() {
        });
    }

    // Transfer Methods - Bank Account

    public HyperwalletBankAccount createUserBankAccount(HyperwalletBankAccount bankAccount) {
        if (bankAccount == null) {
            throw new HyperwalletException("Transfer Method is required");
        }
        if (bankAccount.token != null) {
            throw new HyperwalletException("Transfer Method token may not be present");
        }
        if (bankAccount.userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        bankAccount = util.clean(bankAccount);
        bankAccount.createdOn = null;
        bankAccount.status = null;
        return util.post(url + "/users/" + bankAccount.userToken + "/bank-accounts", bankAccount, HyperwalletBankAccount.class);
    }

    public HyperwalletBankAccount getUserBankAccount(HyperwalletUser user, String transferMethodToken) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        return getUserBankAccount(user.getToken(), transferMethodToken);
    }

    public HyperwalletBankAccount getUserBankAccount(String userToken, String transferMethodToken) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (transferMethodToken == null) {
            throw new HyperwalletException("Bank account token is required");
        }
        return util.get(url + "/users/" + userToken + "/bank-accounts/" + transferMethodToken, HyperwalletBankAccount.class);
    }

    public HyperwalletBankAccount updateUserBankAccount(HyperwalletBankAccount bankAccount) {
        if (bankAccount == null) {
            throw new HyperwalletException("Bank account is required");
        }
        if (bankAccount.token == null) {
            throw new HyperwalletException("Bank account token is required");
        }
        if (bankAccount.userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        bankAccount = util.clean(bankAccount);
        bankAccount.type = null;
        bankAccount.status = null;
        bankAccount.createdOn = null;
        bankAccount.transferMethodCountry = null;
        bankAccount.transferMethodCurrency = null;
        return util.put(url + "/users/" + bankAccount.userToken + "/bank-accounts/" + bankAccount.token, bankAccount, HyperwalletBankAccount.class);
    }

    public HyperwalletList<HyperwalletBankAccount> listUserBankAccounts(HyperwalletUser user, HyperwalletPaginationOptions options) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        return listUserBankAccounts(user.getToken(), options);
    }

    public HyperwalletList<HyperwalletBankAccount> listUserBankAccounts(HyperwalletUser user) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        return listUserBankAccounts(user.getToken(), null);
    }

    public HyperwalletList<HyperwalletBankAccount> listUserBankAccounts(String userToken) {
        return listUserBankAccounts(userToken, null);
    }

    public HyperwalletList<HyperwalletBankAccount> listUserBankAccounts(String userToken, HyperwalletPaginationOptions options) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletBankAccount>>() {
        });
    }

    // Status transitions

    public HyperwalletStatusTransition createStatusTransition(String userToken, HyperwalletPrepaidCard prepaidCard, HyperwalletStatusTransition transition) {
        return createPrepaidCardStatusTransition(userToken, prepaidCard, transition);
    }

    public HyperwalletStatusTransition createStatusTransition(String userToken, HyperwalletPrepaidCard prepaidCard, HyperwalletStatusTransition.Status status) {
        return createPrepaidCardStatusTransition(userToken, prepaidCard, new HyperwalletStatusTransition(status));
    }

    public HyperwalletStatusTransition createPrepaidCardStatusTransition(String userToken, HyperwalletPrepaidCard prepaidCard, HyperwalletStatusTransition transition) {
        if (prepaidCard == null) {
            throw new HyperwalletException("Card is required");
        }
        return createPrepaidCardStatusTransition(userToken, prepaidCard.token, transition);
    }

    public HyperwalletStatusTransition createPrepaidCardStatusTransition(String userToken, String prepaidCardToken, HyperwalletStatusTransition transition) {
        if (prepaidCardToken == null) {
            throw new HyperwalletException("Card token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        return util.post(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }

    public HyperwalletStatusTransition getPrepaidCardStatusTransition(HyperwalletUser user, HyperwalletPrepaidCard card, String statusTransitionToken) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (card == null) {
            throw new HyperwalletException("Card is required");
        }
        return getPrepaidCardStatusTransition(user.getToken(), card.token, statusTransitionToken);
    }

    public HyperwalletStatusTransition getPrepaidCardStatusTransition(String userToken, String prepaidCardToken, String statusTransitionToken) {
        if (prepaidCardToken == null) {
            throw new HyperwalletException("Card token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (statusTransitionToken == null) {
            throw new HyperwalletException("Transition token is required");
        }
        return util.get(url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions" + statusTransitionToken, HyperwalletStatusTransition.class);
    }

    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(HyperwalletUser user, HyperwalletPrepaidCard card, HyperwalletPaginationOptions options) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (card == null) {
            throw new HyperwalletException("Card is required");
        }
        return listPrepaidCardStatusTransitions(user.getToken(), card.token, options);
    }

    public HyperwalletList<HyperwalletStatusTransition> listPrepaidCardStatusTransitions(String userToken, String prepaidCardToken, HyperwalletPaginationOptions options) {
        if (prepaidCardToken == null) {
            throw new HyperwalletException("Card token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/prepaid-cards/" + prepaidCardToken + "/status-transitions", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    public HyperwalletStatusTransition deactivate(String userToken, HyperwalletBankAccount bankAccount) {
        return createStatusTransition(userToken, bankAccount, HyperwalletStatusTransition.Status.DE_ACTIVATED);
    }

    public HyperwalletStatusTransition activate(String userToken, HyperwalletBankAccount bankAccount) {
        return createStatusTransition(userToken, bankAccount, HyperwalletStatusTransition.Status.ACTIVATED);
    }

    public HyperwalletStatusTransition createStatusTransition(String userToken, HyperwalletBankAccount bankAccount, HyperwalletStatusTransition transition) {
        return createBankAccountStatusTransition(userToken, bankAccount, transition);
    }

    public HyperwalletStatusTransition createStatusTransition(String userToken, HyperwalletBankAccount bankAccount, HyperwalletStatusTransition.Status status) {
        return createBankAccountStatusTransition(userToken, bankAccount, new HyperwalletStatusTransition(status));
    }

    public HyperwalletStatusTransition createBankAccountStatusTransition(String userToken, HyperwalletBankAccount bankAccount, HyperwalletStatusTransition transition) {
        if (bankAccount == null) {
            throw new HyperwalletException("Account is required");
        }
        return createBankAccountStatusTransition(userToken, bankAccount.token, transition);
    }

    public HyperwalletStatusTransition createBankAccountStatusTransition(String userToken, String bankAccountToken, HyperwalletStatusTransition transition) {
        if (bankAccountToken == null) {
            throw new HyperwalletException("Account token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (transition == null) {
            throw new HyperwalletException("Transition is required");
        }
        return util.post(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", transition, HyperwalletStatusTransition.class);
    }

    public HyperwalletStatusTransition getBankAccountStatusTransition(HyperwalletUser user, HyperwalletBankAccount card, String statusTransitionToken) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (card == null) {
            throw new HyperwalletException("Account is required");
        }
        return getBankAccountStatusTransition(user.getToken(), card.token, statusTransitionToken);
    }

    public HyperwalletStatusTransition getBankAccountStatusTransition(String userToken, String bankAccountToken, String statusTransitionToken) {
        if (bankAccountToken == null) {
            throw new HyperwalletException("Account token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (statusTransitionToken == null) {
            throw new HyperwalletException("Transition token is required");
        }
        return util.get(url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions" + statusTransitionToken, HyperwalletStatusTransition.class);
    }


    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(HyperwalletUser user, HyperwalletBankAccount card, HyperwalletPaginationOptions options) {
        if (user == null) {
            throw new HyperwalletException("User is required");
        }
        if (card == null) {
            throw new HyperwalletException("Account is required");
        }
        return listBankAccountStatusTransitions(user.getToken(), card.token, options);
    }

    public HyperwalletList<HyperwalletStatusTransition> listBankAccountStatusTransitions(String userToken, String bankAccountToken, HyperwalletPaginationOptions options) {
        if (bankAccountToken == null) {
            throw new HyperwalletException("Account token is required");
        }
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/users/" + userToken + "/bank-accounts/" + bankAccountToken + "/status-transitions", options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {
        });
    }

    // Payments

    public HyperwalletPayment createPayment(HyperwalletPayment payment) {
        if (payment == null) {
            throw new HyperwalletException("Payment is required");
        }
        if (payment.token != null) {
            throw new HyperwalletException("Payment token may not be present");
        }
        payment = util.clean(payment);
        payment.createdOn = null;
        return util.post(url + "/payments/", payment, HyperwalletPayment.class);
    }

    public HyperwalletPayment getPayment(String token) {
        return util.get(url + "/payments/" + token, HyperwalletPayment.class);
    }

    public HyperwalletList<HyperwalletPayment> listPayments() {
        return listPayments(null);
    }

    public HyperwalletList<HyperwalletPayment> listPayments(HyperwalletPaymentListOptions options) {
        String url = paginate(this.url + "/payments", options);
        if (options != null) {
            url = addParameter(url, "releasedOn", convert(options.releasedOn));
            url = addParameter(url, "currency", options.currency);
        }
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletPayment>>() {
        });
    }

    // Transfer Method Configurations

    public HyperwalletTransferMethodConfiguration getTransferMethodConfiguration(String userToken, String country, String currency, HyperwalletTransferMethod.Type type, HyperwalletUser.ProfileType profileType) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        if (country == null) {
            throw new HyperwalletException("Country is required");
        }
        if (currency == null) {
            throw new HyperwalletException("Currency is required");
        }
        if (type == null) {
            throw new HyperwalletException("Type is required");
        }
        if (profileType == null) {
            throw new HyperwalletException("Profile type is required");
        }
        return util.get(url + "/transfer-method-configurations"
                        + "?userToken=" + userToken
                        + "&country=" + country
                        + "&currency=" + currency
                        + "type=" + type.name()
                        + "profileType=" + profileType.name(),
                HyperwalletTransferMethodConfiguration.class);
    }

    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken) {
        return listTransferMethodConfigurations(userToken, null);
    }

    public HyperwalletList<HyperwalletTransferMethodConfiguration> listTransferMethodConfigurations(String userToken, HyperwalletPaginationOptions options) {
        if (userToken == null) {
            throw new HyperwalletException("User token is required");
        }
        String url = paginate(this.url + "/transfer-method-configurations?userToken=" + userToken, options);
        return util.get(url, new TypeReference<HyperwalletList<HyperwalletTransferMethodConfiguration>>() {
        });
    }

    String paginate(String url, HyperwalletPaginationOptions options) {
        if (options == null) {
            return url;
        }
        url = addParameter(url, "createdAfter", convert(options.createdAfter));
        url = addParameter(url, "createdBefore", convert(options.createdBefore));
        url = addParameter(url, "sortBy", options.sortBy);
        url = addParameter(url, "offset", options.offset);
        url = addParameter(url, "limit", options.limit);
        return url;
    }

    String addParameter(String url, String key, Object value) {
        if (url == null || key == null || value == null) {
            return url;
        }
        return url + (url.indexOf("?") == -1 ? "?" : "&") + key + "=" + value;
    }

    String convert(Date in) {
        if (in == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(in);
    }
}
