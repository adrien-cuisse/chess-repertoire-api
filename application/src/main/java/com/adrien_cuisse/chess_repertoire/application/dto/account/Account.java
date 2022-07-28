
package com.adrien_cuisse.chess_repertoire.application.dto.account;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.nickname.Nickname;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password.HashedPassword;

public record Account(
	IIdentity<?> ownerIdentity,
	Nickname nickname,
	MailAddress mailAddress,
	HashedPassword hashedPassword
) { }
