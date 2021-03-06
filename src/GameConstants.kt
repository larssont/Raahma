class GameConstants (player: Player) {

    private val ilerda = Location.District("Ilerda")
    private val westdale = Location.District("Westdale")
    private val farkari = Location.District("Farkari")
    private val kelheim = Location.District("Kelheim")

    private val alley = Location.Other("Alley", kelheim)
    private val ruins = Location.Other("Temple ruins", westdale)
    private val dungeon = Location.Other("Dungeon", ilerda)
    private val mountains = Location.Other("Mountains", farkari)
    private val forest = Location.Other("Forest", kelheim)
    private val swamp = Location.Other("Swamp", westdale)
    private val cave = Location.Other("Cave", kelheim)
    private val farm = Location.Other("Farm", westdale)
    private val docks = Location.Other("Docks", westdale)
    private val library = Location.Other("Library", kelheim)
    private val grandChurch = Location.Other("Grand church", ilerda)
    private val church = Location.Other("Church", kelheim)
    private val graveyardOfLostSouls = Location.Other("Graveyard of lost souls", ilerda)
    private val templeOfKelheim = Location.Other("Temple of Kelheim", kelheim)
    private val casino = Location.Other("Casino", kelheim)

    val spawnDistrict = ilerda
    val spawnPlace = grandChurch

    private val archeryShop = Location.Shop("Archery shop", ilerda)
    private val meleeShop = Location.Shop("Melee shop", ilerda)
    private val magicShop = Location.Shop("Magic shop", kelheim)
    private val foodMarket = Location.Shop("Food market", westdale)
    private val armourShop = Location.Shop("Armour shop", farkari)

    val districts = listOf(ilerda, westdale, farkari, kelheim)
    val places = listOf(
        grandChurch,
        church,
        templeOfKelheim,
        archeryShop,
        meleeShop,
        magicShop,
        foodMarket,
        graveyardOfLostSouls,
        farm,
        docks,
        library,
        casino,
        alley,
        ruins,
        dungeon,
        mountains,
        forest,
        swamp,
        cave,
        armourShop
    ).sortedBy { it.name }

    private val combat = Combat(player)
    private val fight = Fight(player, combat)

    val item = Item(player)
    val parser = Parser(player, places, districts, fight)


    private val lowMoneyDrop = item.copperCoin to Triple(50, 0.25, 0.9)
    private val mediumMoneyDrop = item.copperCoin to Triple(150, 0.25, 0.9)
    private val highMoneyDrop = item.copperCoin to Triple(300, 0.25, 0.9)
    private val hugeMoneyDrop = item.copperCoin to Triple(1000, 0.25, 0.9)
    private val beefDrop = item.beef to Triple(1, 0.0, 1.0)
    private val lambMeatDrop = item.lambChop to Triple(1, 0.0, 1.0)
    private val goatMeatDrop = item.goatMeat to Triple(1, 0.0, 1.0)

    private val meleeShopNpc = Npc.Shopkeeper("Thomas", item.meleeWeapons, "Thomas the Feud Counselor", item)
    private val archeryShopNpc = Npc.Shopkeeper("Arthur", item.archeryWeapons, "Arthur's Archery", item)
    private val magicShopNpc = Npc.Shopkeeper("Quinn", item.magicWeapons, "Quinn's Magic Boutique", item)
    private val foodMarketNpc = Npc.Shopkeeper("Alexia", item.shopFoods, "No money, no honey", item)
    private val armourShopNpc = Npc.Shopkeeper("Gunner", item.armour, "Gunner's armour shop", item)
    private val homelessNpc = Npc.SupportingCharacter("Homeless guy")

    val supportingCharacters = listOf(homelessNpc)

    private val rat = Mob("Rat", 50, 5, 0.03, 50, mapOf(lowMoneyDrop))
    private val guard = Mob("Guard", 200, 15, 0.12, 200, mapOf(mediumMoneyDrop))
    private val supremeGuard = Mob("Supreme guard", 350, 25, 0.15, 300, mapOf(highMoneyDrop))
    private val zombie = Mob("Zombie", 250, 20, 0.12, 175, mapOf(mediumMoneyDrop))
    private val ghost = Mob("Ghost", 150, 15, 0.1, 125, mapOf(lowMoneyDrop))
    private val cursedGhost = Mob("Cursed ghost", 300, 30, 0.15, 275, mapOf(highMoneyDrop))
    private val banshee = Mob("Banshee", 400, 30, 0.25, 350, mapOf(highMoneyDrop))
    private val alicorn = Mob("Alicorn", 500, 35, 0.2, 400, mapOf(highMoneyDrop))
    private val centaur = Mob("Centaur", 350, 25, 0.25, 300, mapOf(mediumMoneyDrop))
    private val cyclope = Mob("Cyclope", 450, 30, 0.1, 400, mapOf(mediumMoneyDrop))
    private val cow = Mob("Cow", 150, 5, 0.0, 100, mapOf(beefDrop, lowMoneyDrop))
    private val sheep = Mob("Sheep", 100, 5, 0.0, 50, mapOf(lambMeatDrop, lowMoneyDrop))
    private val goat = Mob("Goat", 125, 10, 0.0, 75, mapOf(goatMeatDrop, lowMoneyDrop))
    private val demon = Mob("Demon", 400, 35, 0.2, 400, mapOf(mediumMoneyDrop))
    private val elf = Mob("Elf", 150, 20, 0.15, 200, mapOf(lowMoneyDrop))
    private val fairy = Mob("Fairy", 100, 15, 0.05, 100, mapOf(lowMoneyDrop))
    private val golem = Mob("Golem", 500, 40, 0.1, 400, mapOf(highMoneyDrop))
    private val mutatedGolem = Mob("Mutated Golem", 1000, 80, 0.15, 800, mapOf(hugeMoneyDrop))
    private val minotaur = Mob("Minotaur", 250, 25, 0.3, 300, mapOf(mediumMoneyDrop))
    private val ogre = Mob("Ogre", 200, 20, 0.1, 225, mapOf(mediumMoneyDrop))
    private val werewolf = Mob("Werewolf", 550, 45, 0.15, 500, mapOf(highMoneyDrop))
    private val wraith = Mob("Wraith", 400, 40, 0.1, 300, mapOf(highMoneyDrop))

    fun spawnNPCs() {
        archeryShop.npcs.add(archeryShopNpc)
        magicShop.npcs.add(magicShopNpc)
        meleeShop.npcs.add(meleeShopNpc)
        foodMarket.npcs.add(foodMarketNpc)
        grandChurch.npcs.add(homelessNpc)
        armourShop.npcs.add(armourShopNpc)
    }

    fun spawnMobs() {
        church.mobs.addAll(listOf(rat))
        templeOfKelheim.mobs.addAll(listOf(guard,supremeGuard))
        graveyardOfLostSouls.mobs.addAll(listOf(ghost,cursedGhost,zombie))
        farm.mobs.addAll(listOf(cow,goat,sheep))
        mountains.mobs.addAll(listOf(alicorn,werewolf))
        ruins.mobs.addAll(listOf(banshee,wraith))
        alley.mobs.addAll(listOf(golem,mutatedGolem))
        forest.mobs.addAll(listOf(elf,fairy))
        dungeon.mobs.addAll(listOf(centaur,minotaur,cyclope))
        swamp.mobs.addAll(listOf(ogre))
        cave.mobs.addAll(listOf(demon))
    }


}