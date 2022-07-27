
package com.adrien_cuisse.chess_repertoire.application.commands.account;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.nickname.Nickname;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.password.HashedPassword;

public record RegisterAccountCommand(
	IIdentity<?> ownerIdentity,
	Nickname nickname,
	MailAddress mailAddress,
	HashedPassword hashedPassword
) {
	public interface IHandler
	{
		void execute(final RegisterAccountCommand command);
	}
}
