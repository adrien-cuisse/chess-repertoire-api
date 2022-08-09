package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname;

public class EmptyNicknameException extends RuntimeException
{
	public EmptyNicknameException()
	{
		super("Nickname is empty");
	}
}
