import java.io.Serializable
import java.lang.IllegalArgumentException
import kotlin.random.Random

class Player : Serializable {

    private val skillSystem = SkillSystem()

    var name = ""

    var inventory = mutableMapOf<Item.Storable, Int>()

    fun returnInventoryIndex(key: Item.Storable):Int? {
        var index = 1
        for (item in inventory) {
            if (item.key == key) break
            index++
        }
        return index
    }

    var equippedWeapon: Item.Weapon? = null
    var equippedArmour: Item.Armour? = null

    lateinit var currentDistrict: Location.District
    lateinit var currentPlace: Location.Place

    lateinit var spawnDistrict: Location.District
    lateinit var spawnPlace: Location.Place

    val magic = skillSystem.FightSkill(1,"Magic")
    val archery = skillSystem.FightSkill(1,"Archery")
    val melee = skillSystem.FightSkill(1,"Melee")

    val luck = skillSystem.NormalSkill(1,"Luck")
    val smithing = skillSystem.NormalSkill(1,"Smithing")
    val defence = skillSystem.NormalSkill(1,"Defence")

    val skills = arrayListOf(magic,melee,archery,luck,smithing,defence)
    val fightSkills = skills.filter { it is SkillSystem.FightSkill }

    var hp = updateHp()
    var hpLeft = hp

    fun increaseExp(skill: SkillSystem.Skill, exp: Int) {
        skill.experience += exp
        skill.totalExperience += exp
        if (skill.experience >= skill.experienceGap) {
            skill.level++
            skill.experience = 0
        }
        hp = updateHp()
    }

    fun updateHp(): Int {
        return 100 + ((fightSkills.map { it.level } + defence.level).average()*8).toInt()
    }

    fun addToInventory(item: Item.Storable, amount: Int) {
        if (item in inventory) inventory[item] = inventory[item]!! + amount
        else inventory[item] = amount
    }

    fun pickUpDrop(drops: Map<Item.Storable,Triple<Int,Double,Double>>): MutableMap<Item.Storable, Int>? {

        val pickedUpDrops: MutableMap<Item.Storable, Int> = mutableMapOf()

        fun dropAmount(dropBaseAmount: Int, factor: Double): Int {

            val random: Double = try {
                Random.nextDouble(from = 1-factor, until = 1+factor)
            } catch (i: IllegalArgumentException) {
                1.0
            }
            return (dropBaseAmount*random).toInt()
        }

        for (drop in drops) {
            if (Random.nextDouble() > drop.value.third) continue
            val dropAmount = dropAmount(drop.value.first,drop.value.second)
            addToInventory(drop.key,dropAmount)
            pickedUpDrops[drop.key] = dropAmount
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
        return if (defence.level >= armour.levelReq ){
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

    fun respawn() {
        currentPlace = spawnPlace
        currentDistrict = spawnDistrict
    }

    fun die(): String {
        hpLeft = hp
        respawn()
        return """
            Oh no, you died!
            You have respawned in ${currentDistrict.name}.
        """.trimIndent()

    }

}



