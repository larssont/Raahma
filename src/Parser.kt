class Parser (
    val player: Player,
    val places: List<Location.Place>,
    val districts: List<Location.District>,
    val fight: Fight
) {

    var quit: Boolean = false

    val commands = arrayListOf(Travel(),Stats(),Position(),Inventory(),
        Attack(),Info(),Quit(),Eat(),Health(),Equip(),Visit(),Talk(),Help(),Map(),Unequip())

    val commandsWords = commands.map { it.subClassName().capitalize() to it.altCommand }.toMap()

    val districtsList = districts.map { it.name }

    private fun Any.subClassName(): String {
        return this::class.java.name.toLowerCase().removePrefix("parser$")
    }

    fun processCommand() {
        val input = readLine()!!.toLowerCase().split(" ", limit=2)
        println()
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

    fun printStringOptions(list: List<String>) {
        list.forEachIndexed { index, s ->
            println("(${index+1}) $s")
        }
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

            val visitablesCopy = ArrayList(places)

            println(line)
            districtsList.forEach {
                System.out.printf(columnSize, it)
            }
            println()
            println(line)

            var i = 0
            loop@while (true) {
                for (location in districts) {
                    val locationMatch = visitablesCopy.find { it.district == location }

                    System.out.printf(columnSize, locationMatch?.name ?: "")
                    visitablesCopy.remove(locationMatch)
                    i++
                    if (i % districts.size == 0) println()
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
                println("You can currently travel to these districts:")
                printNumberedList(districtsList)
            }
            else {
                districts.forEachIndexed { index, location ->
                    if (input[1] == location.name.toLowerCase() || input[1] == (index+1).toString()) {
                        player.currentDistrict = location
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

            val places = places.filter { it.district == player.currentDistrict }
            val placesList = places.map { it.name }

            if (input.size < 2) {
                println("You can visit these places in ${player.currentDistrict.name}:")
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
                println("${input[1].capitalize()} does not exist in ${player.currentDistrict.name}")
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
            println("You are currently in ${player.currentDistrict.name}: ${player.currentPlace.name}.")
        }
    }

    inner class Inventory: Command {
        override val altCommand: List<String> = listOf("i")
        override fun run(input: List<String>) {

            println("-".repeat(55))
            System.out.printf("%-18s %15s %15s", "Item","Value ea", "Quantity")
            println()
            println("-".repeat(55))

            player.inventory.forEach { item ->
                System.out.format(
                    "%-18s %15s %15s",
                    "(${player.returnInventoryIndex(item.key)}) ${item.key.name}", item.key.value, item.value
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

            val infoList = listOf("District","Place")
            val info: Any

            if (input.size < 2) {
                println("Information on what?")
                printStringOptions(infoList)

            }

            else {
                info = when (input[1]) {
                    infoList[0].toLowerCase(), "1" -> {
                        player.currentDistrict
                    }
                    infoList[1].toLowerCase(), "2" -> {
                        player.currentPlace
                    }
                    else -> {
                        println("That not an option. Type \"info x\".")
                        printStringOptions(infoList)
                        return
                    }
                }


                if (info is Location.District) {
                    if (info.districtDescription == "")  {
                        println("There's currently no information available for ${info.name}.")
                    }
                    else println(info.districtDescription)
                }

                else if (info is Location.Place) {
                    if (info.placeDescription == "") {
                        println("There's currently no information available for ${info.name}.")
                    }
                    else println(info.placeDescription)
                }

            }
        }
    }

    inner class Quit: Command {
        override val altCommand: List<String> = listOf()
        override fun run(input: List<String>) {

            val query = """
                "Are you sure you want to quit?" The game will be saved nevertheless.
                (1) Yes
                (2) No
            """.trimIndent()

            println(query)

            loop@ while (true) {
                val secondInput = readLine()!!.toLowerCase()
                when (secondInput) {
                    "1","yes","y" -> {
                        quit = true
                        println("Thanks for playing!")
                    }
                    "2","no","n" -> {
                        quit = false
                        println("")
                    }
                    else -> {
                        println(query)
                        continue@loop
                    }
                }
                return
            }
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
                if (input[1] == it.key.name.toLowerCase() ||
                    input[1] == player.returnInventoryIndex(it.key).toString()) {
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
                    System.out.printf("%-15s %-15s %18s","Equipped","Name", "Level req")
                    println()
                    println(repeat)
                    player.equippedWeapon?.let {
                        System.out.format("%-15s %-15s %18s", "Weapon", it.name, it.levelReq)
                        println()
                    }
                    player.equippedArmour?.let {
                        System.out.format("%-15s %-15s %18s", "Armour", it.name, it.levelReq)
                        println()
                    }
                    println(repeat)
                    return
                }
            }
            player.inventory.forEach {
                if (input[1] == it.key.name.toLowerCase() ||
                    input[1] == player.returnInventoryIndex(it.key).toString()) {
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

    inner class Unequip: Command {
        override val altCommand: List<String> = listOf()
        override fun run(input: List<String>) {
            if (input.size >= 2) {
                if (input[1] == "armour") {
                    player.equippedArmour?.let {
                        player.addToInventory(it, 1)
                        player.equippedArmour = null
                        println("You have unequipped ${it.name}.")
                        return
                    }
                    println("You do not have any armour equipped.")
                } else if (input[1] == "weapon") {
                    player.equippedWeapon?.let {
                        player.addToInventory(it, 1)
                        player.equippedWeapon = null
                        println("You have unequipped ${it.name}.")
                        return
                    }
                    println("You do not have any weapon equipped.")
                }
                return
            }
            println("Type \"unequip armour\" or \"unequip weapon\".")

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

