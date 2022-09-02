
package com.adrien_cuisse.chess_repertoire.application.dto.position;

import java.util.Optional;

public record FindPositionByUserAndFenQuery(String ownerIdentity, String fen)
{
	public interface IHandler
	{
		Optional<PositionDTO> execute(final FindPositionByUserAndFenQuery query);
	}
}
