
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.position;

import com.adrien_cuisse.chess_repertoire.application.dto.position.FindPositionByUserAndNameQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.position.PositionDTO;

import java.util.Optional;

public final class FindPositionByUserAndNameFake implements FindPositionByUserAndNameQuery.IHandler
{
	private final static String MATCHING_NAME = "taken name";

	public Optional<PositionDTO> execute(FindPositionByUserAndNameQuery query)
	{
		final String name = query.name();

		if (!name.equals(MATCHING_NAME))
			return Optional.empty();

		return Optional.of(new PositionDTO(
			"owner identity",
			name,
			"fen"
		));
	}

	public String matchingName()
	{
		return MATCHING_NAME;
	}
}
