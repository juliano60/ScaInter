package com.nanoporetech.scainter.ui.fake

import com.nanoporetech.scainter.model.Provider

object FakeDataSource {
    val providers: List<Provider> = listOf(
        Provider(
            id = 620,
            role = "etablissement",
            name = "Centre d Ophtalmologie de Kami"
        ),
        Provider(
            id = 109,
            role = "etablissement",
            name = "Centre Médical Les Bleuets" // has some rejections
        ),
    )
}