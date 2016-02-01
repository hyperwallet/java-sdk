package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class HyperwalletTest {

	Hyperwallet hyperwallet;

	@Before
	public void before() {
		hyperwallet = HyperwalletTest.getHyperwallet();
	}

	@Test
	public void testGetUser() throws Exception {
		if (hyperwallet == null) {
			return;
		}
		String token = "usr-a150eb06-bf46-43b8-966f-8fb5ecb88ed0";
		HyperwalletUser user = hyperwallet.getUser(token);
		Assert.assertNotNull(user);
		Assert.assertEquals(token, user.token);
	}

	@Test
	public void testListUsers() throws Exception {
		if (hyperwallet == null) {
			return;
		}
		HyperwalletList<HyperwalletUser> list = hyperwallet.listUsers();
		Assert.assertNotNull(list);
		Assert.assertFalse(list.data.isEmpty());
	}

	@Test
	public void testUpdateUser() throws Exception {
		if (hyperwallet == null) {
			return;
		}
		String token = "usr-a150eb06-bf46-43b8-966f-8fb5ecb88ed0";
		HyperwalletUser user = hyperwallet.getUser(token);
		Assert.assertNotNull(user);
		Assert.assertEquals(token, user.token);
		user.firstName = "" + user.firstName + "X";
		HyperwalletUser user2 = hyperwallet.updateUser(user);
		Assert.assertNotNull(user2);
		Assert.assertEquals(token, user2.token);
	}

	public static Hyperwallet getHyperwallet() {
		final String environmentKey = getEnvironmentKey();
		final String apiSecret = getApiSecret();
		if (environmentKey == null || apiSecret == null) {
			return null;
		}
		return new Hyperwallet(environmentKey, apiSecret);
	}

	private static String getEnvironmentKey() {
		return System.getenv("HYPERWALLET_USERNAME");
	}

	private static String getApiSecret() {
		return System.getenv("HYPERWALLET_PASSWORD");
	}

}
