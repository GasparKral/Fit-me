package es.gaspardev.core.domain.usecases.update

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.SportsmanRepository
import es.gaspardev.interfaces.repositories.TrainerRepository

class PairTrainerSportsmanUseCase(
    private val sportsmanRepository: SportsmanRepository,
    private val trainerRepository: TrainerRepository
) : UseCase<Pair<Sportsman, Trainer>, Pair<Sportsman, Trainer>>() {
    override suspend fun run(params: Pair<Sportsman, Trainer>): Either<Exception, Pair<Sportsman, Trainer>> {
        (params.second.sportmans as ArrayList<Sportsman>).add(params.first)
        params.first.trainer = params.second

        val res1 = sportsmanRepository.update(params.first)

        if (res1.isFailure) {
            return Either.Failure((res1 as Either.Failure<Exception>).error)
        }

        val res2 = trainerRepository.update(params.second)

        if (res2.isFailure) {
            return Either.Failure((res2 as Either.Failure<Exception>).error)
        }

        return Either.Success(params)
    }


}