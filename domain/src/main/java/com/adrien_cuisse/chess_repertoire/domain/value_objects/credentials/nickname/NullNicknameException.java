
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname;

public final class NullNicknameException extends RuntimeException
{
	public NullNicknameException()
	{
		super("Nickname is null");
	}
}
