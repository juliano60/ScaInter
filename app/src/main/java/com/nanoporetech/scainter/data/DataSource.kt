package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.Examination
import com.nanoporetech.scainter.model.FamilyMember
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.model.Provider

object DataSource {
    fun hospitalisations(): List<Hospitalisation> = listOf (
        Hospitalisation(
            id = 9139,
            fullname = "HIE GNEPA CARLOS",
            internalId = "9MA27710",
            coverPercentage = "80%",
            subscriberName = "C.R. SAN-PEDRO",
            contractType = "Société",
            type = "Médicale",
            status = "Accord du Medecin",
            reason = "PALUDISME   VERTIGES   CEPHALEES   VOMISSEMENTS",
            durationDays = 1,
            roomType = "Chambre Simple",
            prolongationReason = "Complications",
            prolongationDays = 2,
            providerName = "Clinique Medicale notre Dame",
            creationDate = "2025-04-29T21:11:39+00:00",
            roomCost = 20_000.0,
            hospitalisationCost = 56_569.0,
            dateOfBirth = "1950-01-01T00:00:00-05:00"
        )
    )

    fun examinations(): List<Examination> = listOf(
        Examination(
            id = 86088,
            fullname = "N GOUAN BROU ANDERSON",
            internalId = "2MA33904",
            coverPercentage = "80%",
            subscriberName = "CONSEIL REGIONAL DU HAUT SASSANDRA DALOA",
            contractType = "Société",
            status = "Accord du Medecin",
            controller = "Dr Kouassi Romaric",
            provenance = null,
            doctor = "DR SANGARE KASSIM",
            specialty = "OPHTALMOLOGISTE ",
            reason = "AMETROPIE ",
            exams = listOf(
                "GOUTTE EPAISSE",
                "NUMERATION FORMULE SANGUINE",
                "PRELEVEMENT SANGUIN",
                "",
                "",
                "",
                "",
                ""
            ),
            answers = listOf(
                "Validé",
                "Validé",
                "Validé",
                "blank",
                "blank",
                "blank",
                "blank",
                "blank"
            ),
            rejectionReason = "",
            creationDate = "2025-04-14T10:59:34+00:00",
            total = 12000.0,
            totalSca = 9600.0,
            totalUser = 2400.0,
            dateOfBirth = "1950-01-01T00:00:00-05:00"
        ),
        Examination(
            id = 86088,
            fullname = "N GOUAN BROU ANDERSON",
            internalId = "2MA33904",
            coverPercentage = "80%",
            subscriberName = "CONSEIL REGIONAL DU HAUT SASSANDRA DALOA",
            contractType = "Société",
            status = "",
            controller = null,
            provenance = null,
            doctor = null,
            specialty = null,
            reason = "",
            exams = listOf(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            ),
            answers = listOf(
                "",
                "",
                "",
                "blank",
                "blank",
                "blank",
                "blank",
                "blank"
            ),
            rejectionReason = "",
            creationDate = "2025-04-14T10:59:34+00:00",
            total = 12000.0,
            totalSca = 9600.0,
            totalUser = 2400.0,
            dateOfBirth = "1950-01-01T00:00:00-05:00"
        )
    )

    fun consultations(): List<Consultation> = listOf(
        Consultation(
            id = 173117,
            policyHolderId = 19561,
            fullname = "HAMMOND  KEVIN CHARLES ABEKU",
            subscriberName = "SCA Inter A",
            contractType = "Société",
            act = "Cardiologie",
            creationDate = "2025-07-31T10:07:14+00:00",
            doctor = "Dr Benie Kouame",
            affliction = "360",
            prescription = "METROL SIROP 125MG 5ML FL 120ML",
            quantity = 1,
            posology = "1x par jour",
            prescription1 = "",
            quantity1 = 0,
            posology1 = "2x par jour",
            prescription2 = "",
            quantity2 = 0,
            posology2 = "3x par jour",
            prescription3 = "",
            quantity3 = 0,
            posology3 = "4x par jour",
            total = 8000.0,
            totalSca = 6400.0,
            totalUser = 1600.0,
            internalId = "9MA05006F0110A1",
            percentageCoverage = "80%",
            dateOfBirth = "1986-05-28T00:00:00+00:00"
        ),
        Consultation(
            id = 172011,
            policyHolderId = 25139,
            fullname = "WOGNIN EBAH ODILE",
            subscriberName = "CENT OPH KAMI",
            contractType = "Societe",
            act = "Opthalmologie",
            creationDate = "2025-04-28T17:04:52+00:00",
            doctor = "DR BENIE KOUAME",
            affliction = "306/320",
            prescription = "ARTEFAN CP 40MG/240MG B/6",
            quantity = 1,
            posology = "1x par jour",
            prescription1 = "BRUSTFAN SUSP BUV FL/100 ML",
            quantity1 = 1,
            posology1 = "2x par jour",
            prescription2 = "ENTAMIZOLE SUSP BUV 250 MG/200 MG FL/100 ML",
            quantity2 = 1,
            posology2 = "3x par jour",
            prescription3 = null,
            quantity3 = 0,
            posology3 = "4x par jour",
            total = 12000.0,
            totalSca = 9600.0,
            totalUser = 2400.0,
            internalId = "2MA33904",
            percentageCoverage = "80%",
            dateOfBirth = "1950-01-01T00:00:00-05:00"
        ),
        Consultation(
            id = 172750,
            policyHolderId = 18624,
            fullname = "DIAKITE ADJARATOU",
            subscriberName = "Assemblée Nationale",
            contractType = "Société",
            act = "Controle",
            creationDate = "2025-05-02T16:56:00+00:00",
            doctor = null,
            affliction = null,
            prescription = null,
            quantity = 0,
            posology = "1x par jour",
            prescription1 = null,
            quantity1 = 0,
            posology1 = "2x par jour",
            prescription2 = null,
            quantity2 = 0,
            posology2 = "3x par jour",
            prescription3 = null,
            quantity3 = 0,
            posology3 = "4x par jour",
            total = 0.0,
            totalSca = 0.0,
            totalUser = 0.0,
            internalId = "9MA21013M0051A1",
            percentageCoverage = "70%",
            dateOfBirth = "1950-01-01T00:00:00-05:00"
        )
    )

    fun healthProviders(): List<Provider> {
        return listOf(
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
            Provider(
                id = 465,
                role = "etablissement",
                name = "Clinique Medicale notre Dame"
            )
        )
    }

    fun policyHolderFamilyMembers(): List<FamilyMember> {
        return listOf(
            FamilyMember(
                id = 15244,
                fullname = "KOUAME KRA",
                internalId = "9MA18002B0054A1",
                link = "Assuré Principal"
            ),
            FamilyMember(
                id = 15245,
                fullname = "KRA  KOUASSI JOEL",
                internalId = "2MA18002B0054C1",
                link = "KOUAME KRA"
            )
        )
    }

    fun policyHolders(): List<PolicyHolder> {
        return listOf(
            PolicyHolder(
                id = 1,
                fullname = "HOMA ANGELINE ELOGNE",
                internalId = "9MA05006F0001A1",
                subscriberName = "SCA Inter A",
                contractType = "Société",
                coverExternal = "100%",
                dateOfBirth = "1950-01-01T00:00:00-05:00",
                insuranceType = "ECO",
                plafonnage = "non",
                insuranceStatus = "Actif",
                picture = "9MA05006F0001A1.jpg",
                providerStatus = "Actif",
                lastConsultationType = "Générale",
                lastConsultationDate = "2025-04-28T16:43:54-04:00",
                providerName = "Bon SCA Inter A",
                consommationTotal = 254160.0,
                plafond = 2500000.0,
                isConsommationLimitReached = false,
                isStatusInactive = false,
                consultationList = listOf(
                    "Générale",
                    "Renouv. Ordonnance",
                    "Autre Spécialité",
                    "Cardiologie",
                    "Dermatologie",
                    "Dentaire",
                    "Diabétologie",
                    "Gynécologie",
                    "Hématologie",
                    "Nephrologie",
                    "Neurologie",
                    "Opthamologie",
                    "ORL",
                    "Pédiatrie",
                    "Pneumologie",
                    "Prénatale",
                    "Rhumatologie",
                    "Urologie",
                    "Stomatologie",
                    "Traumatologie"
                ),
                costs = listOf(
                    "Renouv. Ordonnance: 0 FCFA",
                    "Consult. Générale: 10000 FCFA",
                    "Consult. Générale Mutualiste:  FCFA",
                    "Consult. Spécialiste: 12000 FCFA",
                    "Consult. Spécialiste Mutualiste:  FCFA"
                ),
                isPriceFixed = false
            ),

            // CONSUMPTION LIMIT IS REACHED
            PolicyHolder(
                id = 1,
                fullname = "HOMA ANGELINE ELOGNE",
                internalId = "9MA05006F0001A1",
                subscriberName = "SCA Inter A",
                contractType = "Société",
                coverExternal = "100%",
                dateOfBirth = "1950-01-01T00:00:00-05:00",
                insuranceType = "ECO",
                plafonnage = "non",
                insuranceStatus = "Actif",
                picture = "9MA05006F0001A1.jpg",
                providerStatus = "Actif",
                lastConsultationType = "Générale",
                lastConsultationDate = "2025-04-28T16:43:54-04:00",
                providerName = "Bon SCA Inter A",
                consommationTotal = 254160.0,
                plafond = 2500000.0,
                isConsommationLimitReached = true,
                isStatusInactive = false,
                consultationList = listOf(
                    "Générale",
                    "Renouv. Ordonnance",
                    "Autre Spécialité",
                    "Cardiologie",
                    "Dermatologie",
                    "Dentaire",
                    "Diabétologie",
                    "Gynécologie",
                    "Hématologie",
                    "Nephrologie",
                    "Neurologie",
                    "Opthamologie",
                    "ORL",
                    "Pédiatrie",
                    "Pneumologie",
                    "Prénatale",
                    "Rhumatologie",
                    "Urologie",
                    "Stomatologie",
                    "Traumatologie"
                ),
                costs = listOf(
                    "Renouv. Ordonnance: 0 FCFA",
                    "Consult. Générale: 10000 FCFA",
                    "Consult. Générale Mutualiste:  FCFA",
                    "Consult. Spécialiste: 12000 FCFA",
                    "Consult. Spécialiste Mutualiste:  FCFA"
                ),
                isPriceFixed = false
            ),

            // STATUS IS INACTIVE
            PolicyHolder(
                id = 1,
                fullname = "HOMA ANGELINE ELOGNE",
                internalId = "9MA05006F0001A1",
                subscriberName = "SCA Inter A",
                contractType = "Société",
                coverExternal = "100%",
                dateOfBirth = "1950-01-01T00:00:00-05:00",
                insuranceType = "ECO",
                plafonnage = "non",
                insuranceStatus = "Actif",
                picture = "9MA05006F0001A1.jpg",
                providerStatus = "Actif",
                lastConsultationType = "Générale",
                lastConsultationDate = "2025-04-28T16:43:54-04:00",
                providerName = "Bon SCA Inter A",
                consommationTotal = 254160.0,
                plafond = 2500000.0,
                isConsommationLimitReached = false,
                isStatusInactive = true,
                consultationList = emptyList(),
                costs = emptyList(),
                isPriceFixed = false
            ),

            // PRICE IS FIXED
            PolicyHolder(
                id = 1,
                fullname = "HOMA ANGELINE ELOGNE",
                internalId = "9MA05006F0001A1",
                subscriberName = "SCA Inter A",
                contractType = "Société",
                coverExternal = "100%",
                dateOfBirth = "1950-01-01T00:00:00-05:00",
                insuranceType = "ECO",
                plafonnage = "non",
                insuranceStatus = "Actif",
                picture = "9MA05006F0001A1.jpg",
                providerStatus = "Actif",
                lastConsultationType = "Générale",
                lastConsultationDate = "2025-04-28T16:43:54-04:00",
                providerName = "Bon SCA Inter A",
                consommationTotal = 254160.0,
                plafond = 2500000.0,
                isConsommationLimitReached = false,
                isStatusInactive = false,
                consultationList = listOf(
                    "Générale",
                    "Renouv. Ordonnance",
                    "Autre Spécialité",
                    "Cardiologie",
                    "Dermatologie",
                    "Dentaire",
                    "Diabétologie",
                    "Gynécologie",
                    "Hématologie",
                    "Nephrologie",
                    "Neurologie",
                    "Opthamologie",
                    "ORL",
                    "Pédiatrie",
                    "Pneumologie",
                    "Prénatale",
                    "Rhumatologie",
                    "Urologie",
                    "Stomatologie",
                    "Traumatologie"
                ),
                costs = emptyList(),
                isPriceFixed = true
            )
        )
    }
}