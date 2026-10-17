package com.bettertravel.networking.routes

import kotlinx.serialization.Serializable

@Serializable
internal data class ValhallaResponseDto(
    val trip: ValhallaTripDto,
)

@Serializable
internal data class ValhallaTripDto(
    val status: Int,
    val legs: List<ValhallaLegDto> = emptyList(),
)

@Serializable
internal data class ValhallaLegDto(
    val shape: String,
)
