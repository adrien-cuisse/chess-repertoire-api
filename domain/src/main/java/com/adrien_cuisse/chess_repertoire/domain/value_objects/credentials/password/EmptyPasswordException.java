package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public class EmptyPasswordException extends RuntimeException
{
	public EmptyPasswordException()
	{
		super("Password is empty");
	}
}
