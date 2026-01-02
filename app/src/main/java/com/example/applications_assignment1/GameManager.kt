package com.example.applications_assignment1

enum class CellType {
    EMPTY,
    MONSTER,
    MONSTER_MIKE,
    GIFT
}
class GameManager(
    private val NUM_OF_ROWS: Int,
    private val NUM_OF_ROADS: Int
) {
    var lives: Int = 3
    var childPosition: Int = 1
    var addObject: Boolean = true
    val activeObjects = mutableListOf<FallingObject>()
    var isCrash: Boolean = false
    var isBonus: Boolean = false

    fun goLeft() {
        if (childPosition > 0) {
            childPosition--
        }
    }

    fun goRight() {
        if (childPosition < NUM_OF_ROADS - 1) {
            childPosition++
        }
    }

    fun moveTheObjects() {
        // move down the objects
        isCrash = false
        isBonus = false
        val it = activeObjects.iterator()
        while (it.hasNext()) {
            val obj = it.next()
            obj.row += 1
            if (obj.row >= NUM_OF_ROWS) {
                it.remove()
                if ( obj.col == childPosition) {
                    if(obj.type != CellType.GIFT) {
                        isCrash = true
                        lives--
                    }else{
                        isBonus = true
                    }
                }

            }
        }

        // add new object
        if (addObject) {
            val newCol = (0 until NUM_OF_ROADS).random()
            val randNum = (0..2).random()
            var newType: CellType = CellType.EMPTY
            when(randNum){
                0 -> newType = CellType.MONSTER
                1 -> newType = CellType.MONSTER_MIKE
                2 -> newType = CellType.GIFT
            }
            activeObjects.add(FallingObject(row = 0, col = newCol, type = newType))
            addObject = false
        } else {
            addObject = true
        }
    }

    fun getCellType(row: Int, col: Int): CellType {
        val obj = activeObjects.find { it.row == row && it.col == col }
        return obj?.type ?: CellType.EMPTY
    }

//    fun checkCrash(): Boolean {
//        var crashed = false
//        for (obj in activeObjects) {
//            if (obj.row == NUM_OF_ROWS - 1 && obj.col == childPosition) {
//                crashed = true
//                lives--
//            }
//        }
//        return crashed
//    }

    fun clearObjects() {
        activeObjects.clear()
    }

    fun getIsCrash(): Boolean{
        return  isCrash
    }


    fun getIsBonus(): Boolean{
        return isBonus
    }
}
