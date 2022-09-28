package chess

val arr1 = IntArray(8) { 1 + it}
val arr2 = CharArray(8) { 'a' + it}
val arr3 = CharArray(8) { '1' + it}
var exit = false
var inTwoW = false
var inTwoB = false
var whichCol = 9

fun main() {
    println("Pawns-Only Chess")
    val chessboard = MutableList(8) {
        MutableList(8) { ' ' }
    }
    for (i in 0 until 8) {
        chessboard[6][i] = 'W'
        chessboard[1][i] = 'B'
    }

    println("First Player's name:")
    val firstplayer = readLine()!!.toString()
    println("Second Player's name:")
    val secondplayer = readLine()!!.toString()


    output(chessboard)

    gameplay(firstplayer, secondplayer, chessboard)
}

fun gameplay(firstplayer: String, secondplayer : String, chessboard: MutableList<MutableList<Char>>) {
    val regex = Regex(pattern = "([a-h][1-8]){2}")
    var count = 0
    var turn: String
    loop@do{
        println("${if (count % 2 == 0) firstplayer else secondplayer}'s turn:")
        turn = readLine()!!.toString()
        if (regex.matches(turn)) count++
        else if (turn == "exit") {
            exit = true
            break@loop
        }
        else println("Invalid Input")
        if (!processingBoard(chessboard,turn, count - 1)) count--
        winOrDrawConditions(chessboard)
    }while (!exit)
    println("Bye!")
}

fun winOrDrawConditions(chessboard: MutableList<MutableList<Char>>) {
    var closeWhite = 0
    var closeBlack = 0

    var sumW = 0
    var sumB = 0

    val myList = MutableList(10) {
        MutableList(10) { ' ' }
    }

    for (i in 1 until chessboard.size + 1) {
        for (j in 1 until chessboard.size + 1) {
            myList[i][j] = chessboard[i - 1][j - 1]
        }
    }

    for (i in 0 until chessboard.size) {
        val predicate: (Char) -> Boolean = { it == 'W' }
        val countW = chessboard[i].count(predicate)
        sumW += countW

        val predicateB: (Char) -> Boolean = { it == 'B' }
        val countB = chessboard[i].count(predicateB)
        sumB += countB
    }

    if (chessboard[0].contains('W') || sumB == 0) {
        println("White Wins!")
        exit = true
    }
    if (chessboard[chessboard.size - 1].contains('B') || sumW == 0) {
        println("Black Wins!")
        exit = true
    }


    for (i in 1 until chessboard.size + 1) {
        for (j in 1 until chessboard.size + 1) {
            if (myList[i - 1][j] == 'B' && myList[i][j] == 'W') {
                closeWhite++
                if (myList[i - 1][j + 1] == 'B' || myList[i - 1][j - 1] == 'B') closeWhite--
            }

            if (myList[i + 1][j] == 'W' && myList[i][j] == 'B') {
                closeBlack++
                if (myList[i + 1][j + 1] == 'W' || myList[i + 1][j - 1] == 'W') closeBlack--
            }
        }
    }

    if (sumW == closeWhite && sumW > 0) {
        println("Stalemate!")
        exit = true
    }

    if (sumB == closeBlack && sumB > 0) {
        println("Stalemate!")
        exit = true
    }
}

fun processingBoard(chessboard: MutableList<MutableList<Char>>, turn: String, count: Int): Boolean {
    val piece: Char
    val colorControl: String
    val anotherColor: Char
    if (count % 2 == 0) {
        piece = 'W'
        colorControl = "white"
        anotherColor = 'B'
    }else {
        piece = 'B'
        colorControl = "black"
        anotherColor = 'W'
    }

    var xS = 0
    var yS = 0
    var xF = 0
    var yF = 0
    for (i in arr2.indices) {
        if (turn[0] == arr2[i]) xS = arr1[i] - 1
        if (turn[1] == arr3[i]) yS = 8 - arr1[i]
        if (turn[2] == arr2[i]) xF = arr1[i] - 1
        if (turn[3] == arr3[i]) yF = 8 - arr1[i]
    }

    if ((xS + 1 == xF || xS - 1 == xF) && (yS + 1 == yF || yS - 1 == yF) && chessboard[yF][xF] == anotherColor) {
        chessboard[yF][xF] = piece
        chessboard[yS][xS] = ' '
        inTwoW = false
        inTwoB = false
        whichCol = 9
        output(chessboard)
        return true
    }
    else if (colorControl == "white" && yS == 3 && (xS + 1 == xF || xS - 1 == xF) && chessboard[yF + 1][xF] == 'B' && inTwoB && whichCol == xF) {
        chessboard[yF][xF] = piece
        chessboard[yS][xS] = ' '
        chessboard[yF + 1][xF] = ' '
        whichCol = 9
        inTwoW = false
        inTwoB = false
        output(chessboard)
        return true
    }
    else if (colorControl == "black" && yS == 4 && (xS + 1 == xF || xS - 1 == xF) && chessboard[yF - 1][xF] == 'W' && inTwoW && whichCol == xF) {
        chessboard[yF][xF] = piece
        chessboard[yS][xS] = ' '
        chessboard[yF - 1][xF] = ' '
        inTwoW = false
        inTwoB = false
        whichCol = 9
        output(chessboard)
        return true
    }
    else if (chessboard[yS][xS] != piece) {
        println("No $colorControl pawn at ${turn[0]}${turn[1]}")
        return false
    }
    else if ((colorControl == "black" && yS > yF) ||(colorControl == "white" && yS < yF) || chessboard[yS][xS] != piece) {
        println("Invalid Input")
        return false
    }
    else if (yS - yF !in -2..2 || (turn[0] == turn[2] && turn[1] == turn[3]) || xS != xF || chessboard[yF][xF] != ' ') {
        println("Invalid Input")
        return false
    }
    else if ((piece == 'B' && yS == 1) || (piece == 'W' && yS == 6)) {
        chessboard[yF][xF] = piece
        chessboard[yS][xS] = ' '
        val two = yS - yF
        when (two) {
            -2 -> {
                inTwoB = true
                whichCol = xF
            }
            2 -> {
                inTwoW = true
                whichCol = xF
            }
            else -> {
                inTwoW = false
                inTwoB = false
            }
        }
        output(chessboard)
        return true
    }
    else if (yS - yF !in -1..1) {
        println("Invalid Input")
        return false
    }
    else {
        chessboard[yF][xF] = piece
        chessboard[yS][xS] = ' '
        output(chessboard)
        inTwoW = false
        inTwoB = false
        whichCol = 9
        return true
    }
}

fun output(chessboard: MutableList<MutableList<Char>>) {
    for (i in 0 until chessboard.size) {
        println("  +---+---+---+---+---+---+---+---+")
        print("${arr1[7 - i]} |")
        for (j in 0 until chessboard.size) {
            print(" ${chessboard[i][j]} |")
        }
        println()
    }
    println("  +---+---+---+---+---+---+---+---+")
    print("    ")
    for (i in arr2) print("$i   ")
    println("\n")
}