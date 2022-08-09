
package com.adrien_cuisse.chess_repertoire.application.services;

public interface IPasswordHasher
{
	String hashPassword(final String plainPassword);
}
