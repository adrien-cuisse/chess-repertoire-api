
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.mail_address;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MailAddressTest
{
	@Test
	public void isNotNull()
	{
		// given no mail address
		final String invalidMailAddress = null;

		// when trying to make an instance of it
		Executable instantiation = () -> new MailAddress(invalidMailAddress);

		// then there should be an error
		assertThrows(
			NullMailAddressException.class,
			instantiation,
			"Mail address shouldn't be null"
		);
	}

	@Test
	public void mustComplyWithFormat()
	{
		// given an invalid mail address
		final String invalidMailAddress = "invalid mail address";

		// when trying to make an instance of it
		Executable instantiation = () -> new MailAddress(invalidMailAddress);

		// then there should be an error
		assertThrows(
			InvalidMailAddressException.class,
			instantiation,
			"Mail address should comply with format"
		);
	}

	public static Object[][] comparison()
	{
		final MailAddress mailAddress = new MailAddress("foo@bar.org");

		final IValueObject otherValueObject = new IValueObject() {
			public boolean equals(IValueObject other) { return false; }
		};

		return new Object[][] {
			{ mailAddress, mailAddress, true, "Mail address should equal itself" },
			{ mailAddress, new MailAddress("foo@bar.org"), true, "Mail address should equal if it represents the same value" },
			{ mailAddress, new MailAddress("different@bar.org"), false, "Mail address shouldn't equal if the value is different" },
			{ mailAddress, otherValueObject, false, "Mail address shouldn't equal when not given a mail address" }
		};
	}

	@ParameterizedTest
	@MethodSource("comparison")
	public void matchesSameMailAddress(
		final MailAddress mailAddress,
		final IValueObject other,
		final boolean expectedEquality,
		final String errorMessage
	) {
		// given a mail address, and another value object to compare with

		// when comparing them
		final boolean areTheSame = mailAddress.equals(other);

		// then it should be the expected equality
		assertEquals(expectedEquality, areTheSame, errorMessage);
	}
}
