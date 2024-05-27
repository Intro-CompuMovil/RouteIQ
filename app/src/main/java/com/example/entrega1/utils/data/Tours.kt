package com.example.entrega1.utils.data

import android.util.Log
import android.widget.ArrayAdapter
import com.example.entrega1.utils.schemas.Tour
import com.example.entrega1.utils.schemas.User
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Tours {

    companion object ToursManager {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        private val tours : ArrayList<Tour> = ArrayList()
        private val db : RealTimeCRUD = RealTimeCRUD()
        fun addTour(newTour: Tour) {
            val key = db.getUniqueKey("tours")
            newTour.id = key!!
            db.writeData("tours/${key}", newTour) {
                tours.add(newTour)
            }
        }

        fun approveTourById(id: String) {
            db.writeData("tours/${id}/approved", true) {}
        }

        fun getTours(callback : (ArrayList<Tour>?)->Unit) {
            db.readData<HashMap<String, Tour>>("tours") {
                if (it != null) {
                    Log.i("TOURS [GET TOURS]", it.toString())
                    callback(it.map { it.value } as ArrayList<Tour>)
                } else callback(null)
            }
        }

        fun getById(id: String, callback : (Tour?) -> Unit) {
            db.readData<Tour>("tours/${id}", callback)
        }

        fun getByIdTask(id: String) : Tour? {
            var res : Tour? = null
            scope.launch {
                val a = async { db.readDataTask<Tour>("tours/${id}") }
                res = a.await()
            }
            return res
        }
    }

}