
package com.adrien_cuisse.chess_repertoire.application.dto.account;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname.Nickname;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password.HashedPassword;

public record CredentialsDTO(
	IIdentity<?> ownerIdentity,
	Nickname nickname,
	MailAddress mailAddress,
	HashedPassword hashedPassword
) { }
