
package com.adrien_cuisse.chess_repertoire.application.dto.position;

import java.util.Optional;

public record FindPositionByUserAndNameQuery(String ownerIdentity, String name)
{
	public interface IHandler
	{
		Optional<PositionDTO> execute(final FindPositionByUserAndNameQuery query);
	}
}
