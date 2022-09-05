
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.position;

import com.adrien_cuisse.chess_repertoire.application.dto.position.FindPositionByUserAndFenQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.position.PositionDTO;

import java.util.Optional;

public class FindPositionByUserAndFenFake implements FindPositionByUserAndFenQuery.IHandler
{
	// valid arbitrary FEN, not expected to be created some day
	private static final String MATCHING_FEN = "5q1k/p1Bp2p1/Rp1n1Rqp/1b1n4/4N1B1/PQr1N1Pr/1P2Pb1P/K1Q5 w - - 0 1";

	@Override
	public Optional<PositionDTO> execute(FindPositionByUserAndFenQuery query)
	{
		final String fen = query.fen();

		if (!fen.equals(MATCHING_FEN))
			return Optional.empty();

		return Optional.of(new PositionDTO("", MATCHING_FEN, ""));
	}

	public String matchingFen()
	{
		return MATCHING_FEN;
	}
}
