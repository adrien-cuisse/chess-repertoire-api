package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address;

public class EmptyMailAddressException extends IllegalArgumentException
{
	public EmptyMailAddressException()
	{
		super("Mail address is empty");
	}
}
