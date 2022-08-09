
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address;

public final class InvalidMailAddressException extends RuntimeException
{
	public InvalidMailAddressException(final String mailAddress)
	{
		super(String.format("Invalid mail address '%s'", mailAddress));
	}
}
