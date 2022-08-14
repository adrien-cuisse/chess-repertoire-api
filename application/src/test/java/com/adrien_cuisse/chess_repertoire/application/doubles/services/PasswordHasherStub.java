
package com.adrien_cuisse.chess_repertoire.application.doubles.services;

import com.adrien_cuisse.chess_repertoire.application.services.IPasswordHasher;

public class PasswordHasherStub implements IPasswordHasher
{
	public String hashPassword(final String plainPassword)
	{
		return "password hash";
	}
}
