
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.account;

import com.adrien_cuisse.chess_repertoire.application.dto.account.CredentialsDTO;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;

import java.util.Optional;

public final class FindCredentialsByTakenNicknameFake implements FindCredentialsByNicknameQuery.IHandler
{
	private final static String MATCHING_NICKNAME = "taken nickname";

	public Optional<CredentialsDTO> execute(final FindCredentialsByNicknameQuery query)
	{
		if (query.nickname().equals(MATCHING_NICKNAME))
			return Optional.of(new CredentialsDTO("", MATCHING_NICKNAME, "", ""));

		return Optional.empty();
	}

	public String matchingNickname()
	{
		return MATCHING_NICKNAME;
	}
}
