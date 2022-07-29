
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public final class PositionName implements IValueObject
{
	private static final int MINIMUM_LENGTH = 2;

	private static final int MAXIMUM_LENGTH = 80;

	private final String name;

	public PositionName(final String name)
	{
		if (name == null)
			throw new NullPositionNameException();

		this.name = name.trim().replaceAll("\\s\\s*", " ");
		if (this.name.length() < MINIMUM_LENGTH)
			throw new PositionNameTooShortException(name, MINIMUM_LENGTH);
		else if (this.name.length() > MAXIMUM_LENGTH)
			throw new PositionNameTooLongException(name, MAXIMUM_LENGTH);
	}

	public boolean equals(final IValueObject other)
	{
		if (other instanceof PositionName)
			return this.name.equals(((PositionName) other).name);
		return false;
	}

	public String toString()
	{
		return this.name;
	}
}
