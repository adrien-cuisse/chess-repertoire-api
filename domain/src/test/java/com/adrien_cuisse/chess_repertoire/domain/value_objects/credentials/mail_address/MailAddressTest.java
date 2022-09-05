
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address;

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
		final Executable instantiation = () -> new MailAddress(invalidMailAddress);

		// then there should be an error
		assertThrows(
			NullMailAddressException.class,
			instantiation,
			"Mail address shouldn't be null"
		);
	}

	@Test
	public void isNotEmpty()
	{
		// given an empty mail address
		final String emptyMailAddress = "";

		// when trying to make an instance of it
		final Executable instantiation = () -> new MailAddress(emptyMailAddress);

		// then there should be an error
		assertThrows(
			EmptyMailAddressException.class,
			instantiation,
			"Mail address shouldn't be empty"
		);
	}

	@Test
	public void compliesWithFormat()
	{
		// given an invalid mail address
		final String invalidMailAddress = "invalid mail address";

		// when trying to make an instance of it
		final Executable instantiation = () -> new MailAddress(invalidMailAddress);

		// then there should be an error
		assertThrows(
			InvalidMailAddressException.class,
			instantiation,
			"Mail address should comply with format"
		);
	}

	public static Object[][] comparison()
	{
		final var mailAddress = new MailAddress("foo@bar.org");

		final var otherValueObject = new IValueObject() {
			@Override public boolean equals(IValueObject other) { return false; }
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

	@Test
	public void hasNoWhitespaces()
	{
		// given a mail address with useless spaces
		final var mail = new MailAddress(" f o o @  ba r .  org ");

		// when checking its actual format
		final String format = mail.toString();

		assertEquals(
			"foo@bar.org",
			format,
			"Mail address shouldn't contain whitespaces"
		);
	}
}
