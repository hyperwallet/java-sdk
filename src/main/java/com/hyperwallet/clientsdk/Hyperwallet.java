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
	private final String version = "0.0.1";
	private final String url;

	public Hyperwallet(final String username, final String password, final String programToken, final String url) {
		util = new HyperwalletUtil(username, password, programToken, version);
		this.url = url == null ?  "https://beta.paylution.com/rest/v3" : url;
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
		if (user.token != null) {
			throw new HyperwalletException("User token may not be present");
		}
		user = util.clean(user);
		user.status = null;
		user.createdOn = null;
		return util.post(url + "/users", user, HyperwalletUser.class);
	}

	public HyperwalletUser getUser(String token) {
		return util.get(url + "/users/" + token, HyperwalletUser.class);
	}

	public HyperwalletUser updateUser(HyperwalletUser user) {
		if (user == null) {
			throw new HyperwalletException("User is required");
		}
		if (user.token == null) {
			throw new HyperwalletException("User token is required");
		}
		user = util.clean(user);
		user.status = null;
		user.createdOn = null;
		user.programToken = null;
		return util.put(url + "/users/" + user.token, user, HyperwalletUser.class);
	}

	public HyperwalletList<HyperwalletUser> listUsers() {
		return listUsers(null);
	}

	public HyperwalletList<HyperwalletUser> listUsers(HyperwalletPaginationOptions options) {
		String url = paginate(this.url + "/users", options);
		return util.get(url, new TypeReference<HyperwalletList<HyperwalletUser>>() {});
	}

	// Transfer Methods

	public HyperwalletTransferMethod createUserTransferMethod(HyperwalletTransferMethod method) {
		if (method == null) {
			throw new HyperwalletException("Transfer Method is required");
		}
		if (method.token != null) {
			throw new HyperwalletException("Transfer Method token may not be present");
		}
		if (method.userToken == null) {
			throw new HyperwalletException("User token is required");
		}
		method = util.clean(method);
		method.createdOn = null;
		method.status = null;
		method.cardType = null;
		return util.post(url + "/user/" + method.userToken + "/transfer-methods", method, HyperwalletTransferMethod.class);
	}

	public HyperwalletTransferMethod getUserTransferMethod(HyperwalletUser user, String transferMethodToken) {
		if (user == null) {
			throw new HyperwalletException("User is required");
		}
		return getUserTransferMethod(user.token, transferMethodToken);
	}

	public HyperwalletTransferMethod getUserTransferMethod(String userToken, String transferMethodToken) {
		if (userToken == null) {
			throw new HyperwalletException("User token is required");
		}
		if (transferMethodToken == null) {
			throw new HyperwalletException("Transfer Method token is required");
		}
		return util.get(url + "/users/" + userToken + "/transfer-methods/" + transferMethodToken, HyperwalletTransferMethod.class);
	}

	public HyperwalletTransferMethod updateUserTransferMethod(HyperwalletTransferMethod method) {
		if (method == null) {
			throw new HyperwalletException("Transfer Method is required");
		}
		if (method.token == null) {
			throw new HyperwalletException("Transfer Method token is required");
		}
		if (method.userToken == null) {
			throw new HyperwalletException("User token is required");
		}
		method = util.clean(method);
		method.type = null;
		method.status = null;
		method.createdOn = null;
		method.transferMethodCountry = null;
		method.transferMethodCurrency = null;
		method.cardType = null;
		method.cardPackage = null;
		return util.put(url + "/users/" + method.userToken + "/transfer-methods/" + method.token, method, HyperwalletTransferMethod.class);
	}

	public HyperwalletList<HyperwalletTransferMethod> listUserTransferMethods(HyperwalletUser user, HyperwalletPaginationOptions options) {
		if (user == null) {
			throw new HyperwalletException("User is required");
		}
		return listUserTransferMethods(user.token, options);
	}

	public HyperwalletList<HyperwalletTransferMethod> listUserTransferMethods(HyperwalletUser user) {
		if (user == null) {
			throw new HyperwalletException("User is required");
		}
		return listUserTransferMethods(user.token, null);
	}

	public HyperwalletList<HyperwalletTransferMethod> listUserTransferMethods(String userToken) {
		return listUserTransferMethods(userToken, null);
	}

	public HyperwalletList<HyperwalletTransferMethod> listUserTransferMethods(String userToken, HyperwalletPaginationOptions options) {
		if (userToken == null) {
			throw new HyperwalletException("User token is required");
		}
		String url = paginate(this.url + "/users/" + userToken + "/transfer-methods", options);
		return util.get(url, new TypeReference<HyperwalletList<HyperwalletTransferMethod>>() {});
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
		return util.get(url, new TypeReference<HyperwalletList<HyperwalletPayment>>() {});
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
		return util.get(url, new TypeReference<HyperwalletList<HyperwalletTransferMethodConfiguration>>() {});
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
