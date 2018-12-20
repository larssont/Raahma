import kotlin.random.Random
import java.lang.Error

class Player (val spawnLocation: Location, val spawnVisitable: Place.Visitable) {

    private val skillSystem = SkillSystem()

    var name = ""

    var inventory = mutableMapOf<Item.Storable, Int>()
    var equippedWeapon: Item.Weapon? = null
    var equippedArmour: Item.Armour? = null

    var currentLocation = spawnLocation
    var currentPlace = spawnVisitable

    val magic = skillSystem.FightSkill(1,"Magic")
    val archery = skillSystem.FightSkill(1,"Archery")
    val melee = skillSystem.FightSkill(1,"Melee")

    val luck = skillSystem.NormalSkill(1,"Luck")
    val smithing = skillSystem.NormalSkill(1,"Smithing")
    val defence = skillSystem.NormalSkill(1,"Defence")

    val skills = arrayListOf(magic,melee,archery,luck,smithing,defence)
    val fightSkills = skills.filter { it is SkillSystem.FightSkill }


    var hp = 100 + calcHp()
    var hpLeft = hp


    private fun calcHp(): Int {
        return (fightSkills.map { it.level }.average()*7).toInt()
    }

    fun addToInventory(item: Item.Storable, amount: Int) {
        if (item in inventory) inventory[item] = inventory[item]!! + amount
        else inventory[item] = amount
    }

    fun pickUpDrop(drop: Map<Item.Storable,Triple<Int,Double,Double>>): MutableMap<Item.Storable, Int>? {

        val pickedUpDrops: MutableMap<Item.Storable, Int> = mutableMapOf()



        fun dropAmount(dropBaseAmount: Int, factor: Double): Int {
            val random = Random.nextDouble(from = 1-factor, until = 1+factor)
            return (dropBaseAmount*random).toInt()
        }

        for (it in drop) {
            if (Random.nextDouble() < it.value.third) continue
            val dropAmount = dropAmount(it.value.first,it.value.second)
            addToInventory(it.key,dropAmount)
            pickedUpDrops[it.key] = dropAmount
        }
        return pickedUpDrops
    }

    fun equipWeapon(weapon: Item.Weapon): SkillSystem.FightSkill? {

        return if (weapon.skill.level < weapon.levelReq) weapon.skill
        else {

            if (equippedWeapon != null) addToInventory(equippedWeapon!!,1)
            if (inventory[weapon] == 1) inventory.remove(weapon)
            else inventory[weapon] = inventory[weapon]!! - 1

            equippedWeapon = weapon
            null
        }
    }

    fun equipArmour(armour: Item.Armour): Boolean {
        return if (armour.levelReq > defence.level ){
            equippedArmour = armour
            true
        } else false
    }

    fun eat(food: Item.Food) {
        hpLeft += food.healing
        if (hpLeft > hp) hpLeft = hp
        if (inventory[food] == 1) inventory.remove(food)
        else inventory[food] = inventory[food]!! - 1
    }

    fun respawn(): String {
        currentLocation = spawnLocation
        currentPlace = spawnVisitable
        hpLeft = hp

        return """
            Oh no, you died!
            You have respawned in ${currentLocation.name}.
        """.trimIndent()

    }

}



