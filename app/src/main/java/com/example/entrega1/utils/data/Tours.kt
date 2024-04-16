package com.example.entrega1.utils.data

import com.example.entrega1.utils.schemas.Tour
import com.example.entrega1.utils.schemas.User

class Tours {

    companion object ToursManager {

        private val tours : ArrayList<Tour> = ArrayList()

        fun addTour(newTour: Tour) {
            newTour.id = tours.size
            tours.add(newTour)
        }

        fun approveTourById(id: Int) {
            tours.find { it.id == id }?.approved = true
        }

        fun getTours() : ArrayList<Tour> {
            return tours.clone() as ArrayList<Tour>
        }

        fun seed() {
            for (i in 0 .. 4) {
                tours.add(
                    Tour(
                        LoginStub.anonymousUser,
                        "Viaje a la Javeriana",
                        "Es solo ir a la javeriana",
                        i+1,
                        false
                    )
                )
            }
        }

        fun getById(id: Int) : Tour? {
            return tours.find { it.id == id }
        }
    }

}