class Item (player: Player) {

    private val osmium = WeaponTier("Osmium",50,5.0,5000)
    private val rhodium = WeaponTier("Rhodium",30,3.5,2500)
    private val manganese = WeaponTier("Manganese",20,2.5,1000)
    private val steel = WeaponTier("Steel",10,2.0,500)
    private val iron = WeaponTier("Iron",1,1.5,200)
    private val meleeWeaponTiers = listOf(osmium,rhodium,manganese,steel,iron)

    private val sword = WeaponType("Sword",1.0,0.10)
    private val warAxe = WeaponType("War axe",0.8,0.3)
    private val spear = WeaponType("Spear",1.2,0.03)
    private val meleeWeaponTypes = listOf(sword,warAxe,spear)

    private val dioptase = WeaponTier("Dioptase",50,5.0,5000)
    private val kyanite = WeaponTier("Kyanite",30,3.5,2500)
    private val erythrite = WeaponTier("Erythrite",20,2.5,1000)
    private val moldavite = WeaponTier("Moldavite",10,2.0,500)
    private val amethyst = WeaponTier("Amethyst",1,1.5,200)
    private val magicWeaponTiers = listOf(dioptase,kyanite,erythrite,moldavite,amethyst)

    private val wand = WeaponType("Wand",1.0,0.10)
    private val staff = WeaponType("Staff",0.8,0.3)
    private val orb = WeaponType("Orb",1.2,0.03)
    private val magicWeaponTypes = listOf(wand,staff,orb)

    private val rosewood = WeaponTier("Rosewood",50,5.0,5000)
    private val sycamore = WeaponTier("Sycamore",30,3.5,2500)
    private val cedar = WeaponTier("Cedar",20,2.5,1000)
    private val bone = WeaponTier("Bone",10,2.0,500)
    private val oak = WeaponTier("Oak",1,1.5,200)
    private val archeryWeaponTiers = listOf(oak,bone,cedar,sycamore,rosewood)

    private val longBow = WeaponType("Longbow",1.0,0.10)
    private val shortBow = WeaponType("Shortbow",0.8,0.3)
    private val crossbow = WeaponType("Crossbow",1.2,0.03)
    private val archeryWeaponTypes = listOf(longBow,shortBow,crossbow)

    val meleeWeapons = createWeapons(meleeWeaponTiers,meleeWeaponTypes,player.melee)
    val archeryWeapons = createWeapons(archeryWeaponTiers,archeryWeaponTypes,player.archery)
    val magicWeapons = createWeapons(magicWeaponTiers,magicWeaponTypes,player.magic)

    val shrimp = Food("Shrimp",20,20)
    val corn = Food("Corn",10, 8)

    val chicken = Food("Chicken",100,80)
    val grayGoo = Food("Gray goo",300,1)
    val honey = Food("Honey",150,40)
    val banana = Food("Banana",30,15)
    val bread = Food("Bread",15,5)
    val potato = Food("Potato",70,40)
    val rockFish = Food("Rock fish",150,100)
    val beef = Food("Beef",50,30)
    val lambMeat = Food("Lamb meat", 75, 50)
    val goatMeat = Food("Goat meat", 100, 75)

    val shopFoods = arrayListOf(shrimp,corn,chicken,grayGoo,honey,banana,bread,potato,rockFish)

    val copperCoin = Currency("Copper coin",1)

    private fun createWeapons(tiers: List<WeaponTier>, types: List<WeaponType>, skill: SkillSystem.FightSkill):
            ArrayList<Weapon> {
        val weapons = arrayListOf<Weapon>()
        tiers.forEach { a ->
            types.forEach { b ->
                val name = "${a.name} ${b.name.toLowerCase()}"
                val value = a.cost
                val levelReq = a.levelReq
                val damageFactor = a.damageFactor*b.damageFactor
                val critProbablity = b.critProbablity
                weapons.add(Weapon(name,value,levelReq,skill,damageFactor,critProbablity))
            }
        }
        return weapons
    }

    interface Storable {
        val name: String
        val value: Int
    }

    data class WeaponTier (
        val name: String,
        val levelReq: Int,
        val damageFactor: Double,
        val cost: Int
    )

    data class WeaponType (
        val name: String,
        val damageFactor: Double,
        val critProbablity: Double
    )

    data class Armour (
        override val name: String,
        override val value: Int,
        val levelReq: Int,
        val damageReduceFactor: Double
    ) : Storable

    data class Weapon (
        override val name: String,
        override val value: Int,
        val levelReq: Int,
        val skill: SkillSystem.FightSkill,
        val damageFactor: Double,
        val critProbablity: Double
    ) : Storable

    data class Food (
        override val name: String,
        override val value: Int,
        val healing: Int
    ) : Storable

    data class Currency (
        override val name: String,
        override val value: Int
    ) : Storable


}

