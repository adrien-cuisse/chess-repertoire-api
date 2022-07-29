
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public final class HashedPasswordTest
{
	@Test
	public void isNotNull()
	{
		// given no password
		final String invalidPassword = null;

		// when trying to make an instance of it
		Executable instantiation = () -> new HashedPassword(invalidPassword);

		// then there should be an error
		assertThrows(
			NullPasswordException.class,
			instantiation,
			"Password shouldn't be null"
		);
	}

	@Test
	public void isHashed()
	{
		// given a hashed password
		final HashedPassword password = new HashedPassword("hashed");

		// when checking if it's hashed
		final boolean isHashed = password.isHashed();

		// then it should be
		assertTrue(
			isHashed,
			"Hashed password should tell it's hashed"
		);
	}

	public static Object[][] comparison()
	{
		final HashedPassword password = new HashedPassword("uM3@xR3{hM4.yP6)pU4?cA3%cV1{nR9+");

		final IValueObject otherValueObject = new IValueObject() {
			public boolean equals(IValueObject other) { return false; }
		};

		return new Object[][] {
			{ password, password, true, "Password should equal itself" },
			{ password, new HashedPassword("uM3@xR3{hM4.yP6)pU4?cA3%cV1{nR9+"), true, "Password should equal if it represents the same value" },
			{ password, new HashedPassword("xH2+vE5_eU8^xZ6*aE8.uN4>nJ9<hQ2?"), false, "Password shouldn't equal if the value is different" },
			{ password, otherValueObject, false, "Password shouldn't equal when not given a password" }
		};
	}

	@ParameterizedTest
	@MethodSource("comparison")
	public void matchesSamePassword(
		final HashedPassword password,
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

	@Test
	public void printsAsString()
	{
		// given a hashed password
		final HashedPassword hash = new HashedPassword("hash");

		// when checking its string representation
		final String format = hash.toString();

		// then it should be the one used for instantiation
		assertEquals(
			format,
			"hash",
			"Password should be unchanged"
		);
	}
}
