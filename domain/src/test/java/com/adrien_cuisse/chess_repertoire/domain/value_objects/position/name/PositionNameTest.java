
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.name;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PositionNameTest
{
	@Test
	public void isNotNull()
	{
		// given no name
		final String invalidName = null;

		// when trying to make an instance of it
		final Executable instantiation = () -> new PositionName(invalidName);

		// then there should be an error
		assertThrows(
			NullPositionNameException.class,
			instantiation,
			"Name shouldn't be null"
		);
	}

	@Test
	public void isNotEmpty()
	{
		// given an empty name
		final String emptyName = "";

		// when trying to make an instance of it
		final Executable instantiation = () -> new PositionName(emptyName);

		// then there should be an error
		assertThrows(
			EmptyPositionNameException.class,
			instantiation,
			"Name shouldn't be empty"
		);
	}

	@Test
	public void isTrimmed()
	{
		// given a name with useless spaces
		final String pollutedName = " foo   bar   ";

		// when making an instance of it
		final var name = new PositionName(pollutedName);

		// then it shouldn't contain useless spaces anymore
		assertEquals(
			"foo bar",
			name.toString(),
			"Name shouldn't contain useless spaces"
		);
	}

	public static Object[][] comparison()
	{
		final var name = new PositionName("name");

		final var otherValueObject = new IValueObject() {
			public boolean equals(IValueObject other) { return false; }
		};

		return new Object[][] {
			{ name, name, true, "Name should equal itself" },
			{ name, new PositionName("name"), true, "Name should equal if it represents the same value" },
			{ name, new PositionName("different"), false, "Name shouldn't equal if the value is different" },
			{ name, otherValueObject, false, "Name shouldn't equal when not given a position name" }
		};
	}

	@ParameterizedTest
	@MethodSource("comparison")
	public void equalsSameName(
		final PositionName name,
		final IValueObject other,
		final boolean expectedEquality,
		final String errorMessage
	) {
		// given a name, and another value object to compare with

		// when comparing them
		final boolean areTheSame = name.equals(other);

		// then it should be the expected equality
		assertEquals(expectedEquality, areTheSame, errorMessage);
	}
}
