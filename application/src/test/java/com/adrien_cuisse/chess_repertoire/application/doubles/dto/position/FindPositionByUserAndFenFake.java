package com.adrien_cuisse.chess_repertoire.application.doubles.dto.position;

import com.adrien_cuisse.chess_repertoire.application.dto.position.FindPositionByUserAndFenQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.position.PositionDTO;

import java.util.Optional;

public class FindPositionByUserAndFenFake implements FindPositionByUserAndFenQuery.IHandler
{
	// valid arbitrary FEN, not expected to be created some day
	private final static String MATCHING_FEN = "5q1k/p1Bp2p1/Rp1n1Rqp/1b1n4/4N1B1/PQr1N1Pr/1P2Pb1P/K1Q5 w - - 0 1";

	public Optional<PositionDTO> execute(FindPositionByUserAndFenQuery query)
	{
		final String fen = query.fen();

		if (!fen.equals(MATCHING_FEN))
			return Optional.empty();

		return Optional.of(new PositionDTO(
			"owner identity",
			fen,
			"name"
		));
	}

	public String matchingFen()
	{
		return MATCHING_FEN;
	}
}

/*
public Optional<PositionDTO> execute(FindPositionByUserAndNameQuery query)
	{
		final String name = query.name();

		if (name == null || !name.equals("taken name"))
			return Optional.empty();

		return Optional.of(new PositionDTO(
			new UuidV4().toString(),
			"taken name",
			"fen"
		));
	}
 */