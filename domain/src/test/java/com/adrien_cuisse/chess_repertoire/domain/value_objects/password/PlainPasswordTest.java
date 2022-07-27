
package com.adrien_cuisse.chess_repertoire.domain.value_objects.password;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public final class PlainPasswordTest
{
	@Test
	public void isNotNull()
	{
		// given no password
		final String invalidPassword = null;

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be an error
		assertThrows(
			NullPasswordException.class,
			instanciation,
			"Password shouldn't be null"
		);
	}

	@Test
	public void isNotHashed()
	{
		// given a plain password
		final PlainPassword password = new PlainPassword("qB1)dH3:dO1(aI8@mT2^rZ8#tM2.fD0}");

		// when checking if it's hashed
		final boolean isHashed = password.isHashed();

		// then it shouldn't be
		assertFalse(
			isHashed,
			"Plain password shouldn't tell it's hashed"
		);
	}

	@Test
	public void requiresLowercase()
	{
		// given a password without lowercase
		final String invalidPassword = "ABCDEF123456#@$~{<";

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be a "password too weak" error
		assertThrows(
			PasswordWithoutLowercaseException.class,
			instanciation,
			"Password should contain lowercase"
		);
	}

	@Test
	public void requiresUppercase()
	{
		// given a password without uppercase
		final String invalidPassword = "abcdef123456#@$~{<";

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be a "password too weak" error
		assertThrows(
			PasswordWithoutUppercaseException.class,
			instanciation,
			"Password should contain uppercase"
		);
	}


	@Test
	public void requiresDigits()
	{
		// given a password without digits
		final String invalidPassword = "abcdefABCDEF#@$~{<";

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be a "password too weak" error
		assertThrows(
			PasswordWithoutDigitsException.class,
			instanciation,
			"Password should contain digits"
		);
	}

	@Test
	public void requiresSymbols()
	{
		// given a password without symbols
		final String invalidPassword = "abcdefABCDEF123456";

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be a "password too weak" error
		assertThrows(
			PasswordWithoutSymbolsException.class,
			instanciation,
			"Password should contain symbols"
		);
	}

	@Test
	public void isLongEnough()
	{
		// given a too short password
		final String invalidPassword = "aB#3";

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be a "password too short" error
		assertThrows(
			PasswordTooShortException.class,
			instanciation,
			"Password should be long enough"
		);
	}

	@Test
	public void isShortEnough()
	{
		// given a too long password
		final String invalidPassword = "aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3 67";

		// when trying to make an instance of it
		Executable instanciation = () -> new PlainPassword(invalidPassword);

		// then there should be a "password too long" error
		assertThrows(
			PasswordTooLongException.class,
			instanciation,
			"Password should be short enough"
		);
	}

	public static Object[][] comparison()
	{
		final PlainPassword password = new PlainPassword("hO5*oU4%hC6<jG3<mC0~yI3}fR3{bS5.");

		final IValueObject otherValueObject = new IValueObject() {
			public boolean equals(IValueObject other) { return false; }
		};

		return new Object[][] {
			{ password, password, true, "Password should equal itself" },
			{ password, new PlainPassword("hO5*oU4%hC6<jG3<mC0~yI3}fR3{bS5."), true, "Password should equal if it represents the same value" },
			{ password, new PlainPassword("mW1_oU6:oV3%qS1>aJ1,hI5~dK8!iN0%"), false, "Password shouldn't equal if the value is different" },
			{ password, otherValueObject, false, "Password shouldn't equal when not given a password" }
		};
	}

	@ParameterizedTest
	@MethodSource("comparison")
	public void matchesSamePassword(
		final PlainPassword password,
		final IValueObject other,
		final boolean expectedEquality,
		final String errorMessage
	) {
		// given a password, and another value object to compare with

		// when comparing them
		final boolean areTheSame = password.equals(other);

		// then it should be the expected equality
		assertEquals(expectedEquality, areTheSame, errorMessage);
	}
}
