
import java.text.SimpleDateFormat
import java.util.*

class Game {

    private val fileReader = FileReader()

    private lateinit var player: Player
    private lateinit var gc: GameConstants

    internal fun play() {
        askNewGame()
        do {
            gc.parser.processCommand()
            fileReader.save(player)
        } while (!gc.parser.quit)
    }


    private fun askNewGame() {

        val playerSave = fileReader.loadSave()

        val lastModSave = fileReader.saveFile.lastModified()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val formattedDate = dateFormat.format(lastModSave)

        fun printSaveMessage() {
            println("""
                Do you want to resume from your latest save or start a new game?
                Last saved: $formattedDate
                (1) Resume
                (2) New game
            """.trimIndent())
        }

        fun loadGame() {
            gc = GameConstants(player)
            gc.spawnNPCs()
            gc.spawnMobs()
            fileReader.readFiles(gc.districts,gc.places,gc.supportingCharacters)
        }

        fun resumeGame(playerSave: Player) {
            player = playerSave
            println("""

                Welcome back, have fun!
            """.trimIndent())
            loadGame()
        }

        fun newGame() {
            player = Player()
            gc = GameConstants(player)
            player.spawnDistrict = gc.spawnDistrict
            player.spawnPlace = gc.spawnPlace
            player.respawn()
            player.addToInventory(gc.item.copperCoin,10000)
            loadGame()
            welcome()
        }

        if (playerSave != null) {

            printSaveMessage()

            var input = readLine()!!.toLowerCase()

            loop@ while (true) {
                when (input) {
                    "resume","1" -> resumeGame(playerSave)
                    "new game","2" -> newGame()
                    else -> {
                        printSaveMessage()
                        input = readLine()!!.toLowerCase()
                        continue@loop
                    }
                }
                break@loop
            }
            return
        }
        newGame()

    }

    private fun welcome() {

        fun pressEnter() {
            println("\nPress any key to continue...")
            readLine()!!
            println("\n".repeat(8))
        }

        println("""

            Welcome to Raahma! Raahma is an interactive fan-fiction role-playing game, developed to be challenging
            yet intriguing with lots of interesting things to be discovered. This game is all about creating your own
            story, with your own ending.

            Let's start out with something simple.

            What's your name?
        """.trimIndent())

        player.name = readLine()!!.toLowerCase().capitalize()

        println("\n".repeat(8))
        println("""
            You chose the name: ${player.name}

            Before you can play, let's get some basics down.
        """.trimIndent())

        pressEnter()

        println("""
            The game consists of two different types of geographical elements, districts and places.
            Locations consists mostly of different cities which you can visit, and each district contains
            multiple places.

            In order to travel to destination x, type "travel x". If you just want to find you where you can
            travel, type "travel".
        """.trimIndent())

        pressEnter()

        println("""
            In order to visit place x in a certain district, type "visit x". If you just want to find out
            which places you can visit, type "visit". Keep in mind that you can only visit places in
            your current district.

            If you want to find you current district and place, type "position".
        """.trimIndent())

        pressEnter()

        println("""
            You can use the command "talk x", in order to talk to person x.
            If you want to find out which people you can interact with, just type "talk".
        """.trimIndent())

        pressEnter()

        println("""
            You can see your inventory by typing "inventory". If you want to equip object x, type "equip x".
            If you wanna find your currently equipped items, just type "equip".
        """.trimIndent())

        pressEnter()

        println("""
            You can also attack mobs using the command "attack x". If you want to find out which mobs
            you can attack, just type "attack".
        """.trimIndent())

        pressEnter()

        println("""
            In order to leave the game, type "quit".
            You can find more commands and alternative commands by typing "help".

            Have fun!
        """.trimIndent())

    }

}

fun main(args: Array<String>) {
    val game = Game()
    game.play()
}
