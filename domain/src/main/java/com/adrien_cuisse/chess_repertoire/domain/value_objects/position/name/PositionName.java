
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.name;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public final class PositionName implements IValueObject
{
	private final String name;

	public PositionName(final String name)
	{
		if (name == null)
			throw new NullPositionNameException();

		this.name = name.trim().replaceAll("\\s+", " ");
		if (this.name.equals(""))
			throw new EmptyPositionNameException();
	}

	@Override
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
