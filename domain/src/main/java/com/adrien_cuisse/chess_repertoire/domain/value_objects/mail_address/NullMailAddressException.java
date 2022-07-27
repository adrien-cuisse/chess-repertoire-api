
package com.adrien_cuisse.chess_repertoire.domain.value_objects.mail_address;

public final class NullMailAddressException extends RuntimeException
{
	public NullMailAddressException()
	{
		super("Mail address is null");
	}
}
