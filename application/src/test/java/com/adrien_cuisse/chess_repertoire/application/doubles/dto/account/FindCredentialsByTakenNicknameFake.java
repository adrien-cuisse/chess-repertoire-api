
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.account;

import com.adrien_cuisse.chess_repertoire.application.dto.account.CredentialsDTO;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;

import java.util.Optional;

public final class FindCredentialsByTakenNicknameFake implements FindCredentialsByNicknameQuery.IHandler
{
	public Optional<CredentialsDTO> execute(final FindCredentialsByNicknameQuery query)
	{
		if (query.nickname().equals("taken nickname"))
			return Optional.of(new CredentialsDTO("", "taken nickname", "", ""));

		return Optional.empty();
	}
}
