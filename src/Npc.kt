class Npc {

    interface Person {
        val name: String
        fun run(player: Player)
    }

    data class Shopkeeper (
        override val name: String,
        val stock: List<Item.Storable>,
        val shopName: String,
        val item: Item

        ) : Person {

        val sortedStock = stock.sortedBy { it.value }

        override fun run(player: Player) {

            val options = """
                (1) What do you have in stock?
                (2) Leave
            """.trimIndent()

            println("Welcome to $shopName! My name is $name, what can i do for you?")
            println(options)

            loop@ while (true) {
                val input = readLine()!!.toLowerCase()
                when (input) {
                    "1" -> {
                        showStock(player)
                        return
                    }
                    "2" -> {
                        println("Good bye!")
                        return
                    }
                    else -> {
                        println("I don't understand. These are your options.")
                        println(options)
                    }
                }
            }
        }

        private fun showStock(player: Player) {
            val line = "-".repeat(55)

            println(line)
            System.out.printf("%-30s %15s", "Item","Cost")
            println()
            println(line)
            sortedStock.forEachIndexed { index, storable ->
                System.out.format(
                    "%-30s %15s",
                    "(${index+1}) ${storable.name}",storable.value
                )
                println()
            }
            println(line)
            println("If you want to buy something, just type \"buy X\". If you changed your mind, type \"leave\".")
            buySequence(player)

        }

        private fun buySequence(player: Player) {
            val input = readLine()!!.toLowerCase().split(" ", limit=2)
            when {
                input[0] == "leave" -> {
                    println("Good bye!")
                    return
                }
                input[0] == "buy" -> {
                    sortedStock.forEachIndexed { index, storable ->
                        if (input[1] == storable.name.toLowerCase() || input[1] == (index+1).toString()) {

                            val money = player.inventory[item.copperCoin]

                            if (money != null && money >= storable.value) {
                                player.inventory[item.copperCoin] = money - storable.value
                                if (player.inventory[item.copperCoin] == 0) player.inventory.remove(item.copperCoin)
                                player.addToInventory(storable, 1)
                                println("You purchased 1 ${storable.name.toLowerCase()}")
                                println("Do you wanna buy anything else?")
                                buySequence(player)
                                return
                            } else if (money != null) {
                                println("Sorry, you need ${storable.value - money} more copper coins.")
                                return
                            } else {
                                println("You have no copper coins. GET OUT!")
                                return
                            }
                        }
                        }

                    println("I'm afraid we don't have that item. Can you try again?")
                    buySequence(player)

                    }
                else -> {
                    println("Umm, i don't quite understand you. What are you trying to say?")
                    buySequence(player)
                }
            }
        }

    }

    data class SupportingCharacter (
        override val name: String
    ) : Person {

        var characterScript = ""

        override fun run(player: Player) {
            println(characterScript)
        }
    }
}