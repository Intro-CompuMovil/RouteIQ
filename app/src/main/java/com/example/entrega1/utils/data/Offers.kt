package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.Agency
import com.example.entrega1.utils.schemas.Offer
import java.util.Date

class Offers {

    companion object {
        val offers : ArrayList<Offer> = ArrayList()

        fun seed() {
            for (a in 0..10) {
                offers.add(
                    Offer(
                        Agency(
                            "Agencia papel",
                            "Una agencia linda"
                        ),
                        23.7,
                        "Eligenos",
                        Date()
                    )
                )
            }
        }
    }

}