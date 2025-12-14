package com.example.applications_assignment1

class GameManager(
    private val NUM_OF_ROWS: Int,
    private val NUM_OF_ROADS: Int
) {
    var lives: Int = 3
    var childPosition: Int = 1
    var addMonster: Boolean = true
    val activeMonsters = mutableListOf<Monster>()
    var isCrash: Boolean = false

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

    fun moveTheMonsters() {
        // move down the monsters
        isCrash = false
        val it = activeMonsters.iterator()
        while (it.hasNext()) {
            val monster = it.next()
            monster.row += 1
            if (monster.row >= NUM_OF_ROWS) {
                it.remove()
                if ( monster.col == childPosition) {
                    isCrash = true
                    lives --
                }

            }
        }

        // add new monster
        if (addMonster) {
            val newCol = (0 until NUM_OF_ROADS).random()
            activeMonsters.add(Monster(row = 0, col = newCol))
            addMonster = false
        } else {
            addMonster = true
        }
    }

    fun checkCrash(): Boolean {
        var crashed = false
        for (monster in activeMonsters) {
            if (monster.row == NUM_OF_ROWS - 1 && monster.col == childPosition) {
                crashed = true
                lives--
            }
        }
        return crashed
    }

    fun clearMonsters() {
        activeMonsters.clear()
    }

    fun getIsCrash(): Boolean{
        return  isCrash
    }
}
