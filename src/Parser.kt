class Parser (val player: Player, val visitables: List<Place.Visitable>, val locations: List<Location>, val fight: Fight) {

    var quit: Boolean = false

    val commands = arrayListOf(Travel(),Stats(),Position(),Inventory(),
        Attack(),Info(),Quit(),Eat(),Health(),Equip(),Visit(),Talk(),Help(),Map())

    val commandsWords = commands.map { it.subClassName().capitalize() to it.altCommand }.toMap()

    val locationsList = locations.map { it.name }

    private fun Any.subClassName(): String {
        return this::class.java.name.toLowerCase().removePrefix("parser$")
    }

    fun processCommand() {
        val input = readLine()!!.toLowerCase().split(" ", limit=2)
        for (command in commands) {
            val className = command.subClassName()
            when {
                input[0] == className -> {
                    command.run(input)
                    return
                }
                input[0] in command.altCommand -> {
                    command.run(input)
                    return
                }
            }
        }
        println("The command ${input[0]} does not exist.")
    }

    fun printNumberedList(list: List<String>) {

        val repeat = "-".repeat(60)
        println(repeat)
        list.forEachIndexed { index, s ->
            println("(${index+1}) $s")
        }
        println(repeat)
    }

    interface Command {
        val altCommand: List<String>
            get() = listOf()

        fun run(input: List<String>)
    }

    inner class Map : Command {
        override val altCommand: List<String> = listOf("m")
        override fun run(input: List<String>) {

            val line = "-".repeat(105)
            val columnSize = "%-27s"

            val visitablesCopy = ArrayList(visitables)

            println(line)
            locationsList.forEach {
                System.out.printf(columnSize, it)
            }
            println()
            println(line)

            var i = 0
            loop@while (true) {
                for (location in locations) {
                    val locationMatch = visitablesCopy.find { it.location == location }

                    System.out.printf(columnSize, locationMatch?.name ?: "")
                    visitablesCopy.remove(locationMatch)
                    i++
                    if (i % locations.size == 0) println()
                    if (visitablesCopy.size == 0) break@loop
                }
            }
            println(line)
        }
    }

    inner class Travel : Command {
        override val altCommand: List<String> = listOf("t")
        override fun run(input: List<String>) {
            if (input.size < 2) {
                println("You can currently travel to these locations:")
                printNumberedList(locationsList)
            }
            else {
                locations.forEachIndexed { index, location ->
                    if (input[1] == location.name.toLowerCase() || input[1] == (index+1).toString()) {
                        player.currentLocation = location
                        println("You are now in: ${location.name}")
                        return
                    }
                }
                println("${input[1].capitalize()} does not exist.")
            }


        }
    }

    inner class Visit : Command {
        override val altCommand: List<String> = listOf("v")
        override fun run(input: List<String>) {

            val places = visitables.filter { it.location == player.currentLocation }
            val placesList = places.map { it.name }

            if (input.size < 2) {
                println("You can visit these places in ${player.currentLocation.name}:")
                printNumberedList(placesList)
            }
            else {
                places.forEachIndexed { index, visitable ->
                    if (input[1] == visitable.name.toLowerCase() || input[1] == (index+1).toString()) {
                        player.currentPlace = visitable
                        println("You are now visiting: ${visitable.name}")
                        return
                    }
                }
                println("${input[1].capitalize()} does not exist in ${player.currentLocation.name}")
            }
        }
    }

    inner class Help : Command {
        override val altCommand: List<String> = listOf("h","commands")
        override fun run(input: List<String>) {

            val line = "-".repeat(53)

            println(line)
            System.out.printf("%-20s %-20s ", "Command","Alt command")
            println()
            println(line)
            commandsWords.forEach {
                System.out.format(
                    "%-20s %-20s",
                    it.key,it.value.joinToString(prefix = "", postfix = "", separator = ", ")
                )
                println()
            }
            println(line)
        }
    }

    inner class Stats : Command  {
        override val altCommand: List<String> = listOf("playerstats","s")
        override fun run(input: List<String>) {

            val skillTotal = player.skills.sumBy { it.level }
            val expTotal = player.skills.sumBy { it.totalExperience }
            val line = "-".repeat(68)

            println(line)
            System.out.printf("%10s %13s %18s %18s", "Skill","Level","Exp","Total exp")
            println()
            println(line)
            player.skills.forEach {
                System.out.format(
                    "%10s %13s %18s %18s",
                    it.name,it.level,"${it.experience}/${it.experienceGap}",it.totalExperience
                )
                println()
            }
            println(line)
            System.out.format("%10s %13s %18s %18s", "Total", skillTotal,"N/A", expTotal).println()
            println(line)
        }
    }

    inner class Position: Command {
        override val altCommand: List<String> = listOf("pos")
        override fun run(input: List<String>) {
            println("You are currently in ${player.currentLocation.name}: ${player.currentPlace.name}.")
        }
    }

    inner class Inventory: Command {
        override val altCommand: List<String> = listOf("i")
        override fun run(input: List<String>) {

            println("-".repeat(55))
            System.out.printf("%20s %15s %15s", "Item","Value ea", "Quantity")
            println()
            println("-".repeat(55))
            player.inventory.forEach {
                System.out.format(
                    "%20s %15s %15s",
                    it.key.name, it.key.value, it.value
                )
                println()
            }
            println("-".repeat(55))
        }
    }

    inner class Attack: Command {
        override val altCommand: List<String> = listOf("confront","hit","a")
        override fun run(input: List<String>) {

            val mobs = player.currentPlace.mobs
            val mobsList = mobs.map { it.name }

            if (input.size < 2) {
                if (mobs.size == 0) println("There's no mobs here.")
                else {
                    println("You can see the following mobs in ${player.currentPlace.name}:")
                    printNumberedList(mobsList)
                }
            }
            else {
                mobs.forEachIndexed { index, mob ->
                    if (mob.name.toLowerCase() == input[1] || input[1] == (index+1).toString()) {
                        fight.attack(mob)
                        return
                    }
                }
                println("${input[1]} does not exist here.")

            }

        }

    }

    inner class Info: Command {
        override val altCommand: List<String> = listOf("information")
        override fun run(input: List<String>) {

            fun whenInput(input: String): Boolean {
                when (input){
                    "location" -> {
                        println(player.currentLocation.locationDescription)
                        return true
                    }
                    "1" -> {
                        println(player.currentLocation.locationDescription)
                        return true
                    }
                    "place" -> {
                        println(player.currentPlace.placeDescription)
                        return true
                    }
                    "2" -> {
                        println(player.currentPlace.placeDescription)
                        return true
                    }
                }
                println("That not an option. Please choose either place or location.")
                return false
            }

            if (input.size < 2) {
                println("""
                        What would you like information on?
                        (1) Location
                        (2) Place
                        """.trimIndent())
                while (true) {
                    val secondInput = readLine()!!.toLowerCase().split(" ")
                    if (whenInput(secondInput[0])) return
                }
            }

            else {
                if (whenInput(input[1])) return
            }


        }
    }

    inner class Quit: Command {
        override val altCommand: List<String> = listOf()
        override fun run(input: List<String>) {
            println("""
                Thanks for playing!
            """.trimIndent())
            quit = true
        }
    }

    inner class Eat: Command {
        override val altCommand = listOf("consume","snack")
        override fun run(input: List<String>) {
            if (input.size < 2) {
                println("Eat what?")
                return
            }

            player.inventory.forEach {
                if (it.key.name.toLowerCase() == input[1]) {
                    if (it.key !is Item.Food) println("You can't eat that!")
                    else {
                        player.eat(it.key as Item.Food)
                        println("You gained ${(it.key as Item.Food).healing} hp.")
                        return
                    }
                }
            }

            println("You don't seem to have any ${input[1]} in your inventory.")
        }
    }

    inner class Health: Command {
        override val altCommand = listOf("hp")
        override fun run(input: List<String>) {
            println("You have ${player.hpLeft} / ${player.hp} hp.")
            println("You can increase your maximum hp by training combat")
        }
    }

    inner class Equip: Command {
        override val altCommand: List<String> = listOf("wield","e")
        override fun run(input: List<String>) {
            if (input.size < 2) {
                if (player.equippedWeapon == null && player.equippedArmour == null) {
                    println("You have no items equipped currently.")
                    return
                }
                else {
                    val repeat = "-".repeat(60)
                    println(repeat)
                    System.out.printf("%15s %20s %18s","Equipped","Name", "Level req")
                    println()
                    println(repeat)
                    player.equippedWeapon?.let {
                        System.out.format("%15s %20s %18s", "Weapon", it.name, it.levelReq)
                        println()
                    }
                    player.equippedArmour?.let {
                        System.out.format("%15s %20s %18s", "Armour", it.name, it.levelReq)
                        println()
                    }
                    println(repeat)
                    return
                }
            }
            player.inventory.forEach {
                if (input[1] == it.key.name.toLowerCase()) {
                    if (it.key is Item.Armour) {
                        val armour = it.key as Item.Armour
                        when {
                            armour == player.equippedArmour -> {
                                println("You are already wearing that.")
                                return
                            }
                            !player.equipArmour(armour) -> {
                                println("""
                                    You can't equip that.
                                    You need a ${player.defence.name} level of at least ${armour.levelReq}.
                                """.trimIndent())
                                return
                            }
                            else -> {
                                player.equipArmour(armour)
                                println("You equipped ${armour.name}.")
                                return
                            }
                        }
                    }
                    else if (it.key is Item.Weapon) {
                        val weapon = it.key as Item.Weapon
                        when {
                            weapon == player.equippedWeapon -> {
                                println("You are already wielding that.")
                                return
                            }
                            player.equipWeapon(weapon) != null -> {
                                println("""
                                    You can't equip that.
                                    You need a ${weapon.skill.name} level of at least ${weapon.levelReq}.
                                """.trimIndent())
                                return
                            }
                            else -> {
                                println("You equipped ${weapon.name}.")
                                return
                            }
                        }
                    }
                    else {
                        println("You can't equip that! I think...")
                        return
                    }

                }
            }
            println("You don't seem to have ${input[1]}.")
        }
    }

    inner class Talk: Command {
        override val altCommand = listOf("speak","ask")
        override fun run(input: List<String>) {

            val people = HashMap<Int, Npc.Person>()
            var i = 1
            player.currentPlace.npcs.forEach {
                people[i] = it
                i++
            }

            if (input.size < 2) {
                if (player.currentPlace.npcs.size == 0) {
                    println("Sorry, there's no one here to talk to.")
                    return
                }
                val npcs = player.currentPlace.npcs.map { it.name }
                println("You can talk to these people here:")
                printNumberedList(npcs)

            }
            else {
                player.currentPlace.npcs.forEachIndexed { index, person ->
                    if ((index+1).toString() == input[1] || person.name.toLowerCase() == input[1]) {
                        person.run(player)
                        return
                    }
                }
                println("Sorry, there's no one here ${input[1]}")
            }
        }
    }

}

