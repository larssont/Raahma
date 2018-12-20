class Game {
    private val ilerda = Location("Ilerda")
    private val westdale = Location("Westdale")
    private val sageHall = Location("Sage Hall")
    private val kelheim = Location("Kelheim")

//    private val guild = Visitable("Guild")
//    private val beginnerTrainingHall = Visitable("Beginner training hall")
//    private val carrriage = Visitable("Carriage")
//    private val townhall = Visitable("Town hall")
//    private val royalPalace = Visitable("Royal palace")
//    private val blackMarket = Visitable("Black market")
//    private val townSquare = Visitable("Town square")
//    private val botanistGarden = Visitable("Botanist garden")
//    private val expertTraining = Visitable("Expert training")
//    private val barracks = Visitable("Barracks")
//    private val docks = Visitable("Docks")

    private val farm = Place.Other("Farm",westdale)
    private val docks = Place.Other("Docks",westdale)
    private val library = Place.Other("Library", kelheim)
    private val grandChurch = Place.Other("Grand church",ilerda)
    private val church = Place.Other("Church",kelheim)
    private val graveyardOfLostSouls = Place.Other("Graveyard of lost souls",ilerda)
    private val templeOfKelheim = Place.Other("Temple of Kelheim",kelheim)

    private val archeryShop = Place.Shop("Archery shop",ilerda)
    private val meleeShop = Place.Shop("Melee shop", ilerda)
    private val magicShop = Place.Shop("Magic shop", kelheim)
    private val foodMarket = Place.Shop("Food market",westdale)

    private val locations = listOf(ilerda,westdale,sageHall,kelheim)
    private val visitables = listOf(grandChurch,church,templeOfKelheim,archeryShop,meleeShop,magicShop,
        foodMarket,graveyardOfLostSouls,farm,docks,library)

    private val player = Player(ilerda,church)
    private val combat = Combat(player)
    private val item = Item(player)
    private val fight = Fight(player,combat)
    private val parser = Parser(player,visitables,locations,fight)
    private val fileReader = FileReader()

    private val lowMoneyDrop = item.copperCoin to Triple(30,0.25,0.9)
    private val mediumMoneyDrop = item.copperCoin to Triple(100,0.25,0.9)
    private val highMoneyDrop = item.copperCoin to Triple(200,0.25,0.9)

    private val meleeShopNpc = Npc.Shopkeeper("Thomas", item.meleeWeapons, "Thomas the Feud Counselor",item)
    private val archeryShopNpc = Npc.Shopkeeper("Arthur",item.archeryWeapons, "Arthur's Archery",item)
    private val magicShopNpc = Npc.Shopkeeper("Quinn",item.magicWeapons, "Quinn's Magic Boutique",item)
    private val foodMarketNpc = Npc.Shopkeeper("Alexia",item.foods, "No money, no honey", item)
    private val homelessNpc = Npc.SupportingCharacter("Homeless guy")

    private val supportingCharacters = listOf(homelessNpc)

    private val rat = Mob("Rat", 100, 5, 0.1, 100, mapOf(lowMoneyDrop))
    private val guard = Mob("Guard", 200,15,0.15,200, mapOf(mediumMoneyDrop))
    private val supremeGuard = Mob("Supreme guard",350,25,0.2,300, mapOf(highMoneyDrop))
    private val zombie = Mob("Zombie",250,20,0.1,175, mapOf(mediumMoneyDrop))
    private val ghost = Mob("Ghost",150,15,0.15,125, mapOf(lowMoneyDrop))
    private val cursedGhost = Mob("Cursed ghost",300,30,0.25,275, mapOf(highMoneyDrop))

    internal fun play() {
        welcome()
        spawnNPCs()
        spawnMobs()
        println()
        player.addToInventory(item.copperCoin,1500)
        fileReader.readFiles(locations,visitables,supportingCharacters)
        while (!parser.quit) {
            parser.processCommand()
        }
    }

    private fun spawnNPCs() {
        archeryShop.npcs.add(archeryShopNpc)
        magicShop.npcs.add(magicShopNpc)
        meleeShop.npcs.add(meleeShopNpc)
        foodMarket.npcs.add(foodMarketNpc)
        grandChurch.npcs.add(homelessNpc)
    }

    private fun welcome() {

        fun pressEnter() {
            println("\nPress any key to continue...")
            readLine()!!
            println("\n".repeat(8))
        }

        println("""

            Welcome to Warisoga! Warisoga is an interactive fan-fiction role-playing game, developed to be challenging
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
            The game consists of two different types of geographical elements, locations and places.
            Locations consists mostly of different cities which you can visit, and each location contains
            multiple places.

            In order to travel to destination x, type "travel x". If you just want to find you where you can
            travel, type "travel".
        """.trimIndent())

        pressEnter()

        println("""
            In order to visit place x in a certain location, type "visit x". If you just want to find out
            which places you can visit, type "visit". Keep in mind that you can only visit places in
            your current location.

            If you want to find you current location and place, type "position".
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

    private fun spawnMobs() {

        church.mobs.addAll(listOf(rat))
        templeOfKelheim.mobs.addAll(listOf(guard,supremeGuard))
        graveyardOfLostSouls.mobs.addAll(listOf(ghost,cursedGhost,zombie))

    }

}

fun main(args: Array<String>) {
    val game = Game()
    game.play()
}
