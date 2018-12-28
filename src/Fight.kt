import kotlin.random.Random

class Fight (private val player: Player, private val combat: Combat){

    fun attack(mob: Mob) {

        val fightSkill: SkillSystem.FightSkill? = player.equippedWeapon?.skill
        val attackOptions = attacks(fightSkill)
        var damageFactor = 1.0
        var defenceExpGained = 0.0

        fightSkill?.let { damageFactor = (it.damageLevelMultiplier*player.equippedWeapon!!.damageFactor)  }

        var mobDamage = mob.damage

        player.equippedArmour?.let {
            mobDamage = (mob.damage*(1-it.damageDeflectionFactor)).toInt()
        }

        println("\n".repeat(8))
        println("You have initiated a fight with: ${mob.name}")

        while(true) {
            val attack = chooseAttack(attackOptions)
            var criticalDamage = 1.0

            player.equippedWeapon?.let {
                if (Random.nextDouble() < it.critProbablity) {
                    criticalDamage += 0.5 + 0.5*Random.nextDouble()
                }
            }

            val playerDamage = (attack.damage*damageFactor*criticalDamage).toInt()

            println()
            if (Random.nextDouble() < mob.blockAbility) println("${mob.name} blocked your attack!")

            else {
                mob.hpLeft -= playerDamage
                if (mob.hpLeft <= 0) {

                    val drops = player.pickUpDrop(mob.drop)

                    println("You killed ${mob.name}. Fight over.")
                    drops?.let { it.forEach { it -> println("You picked up ${it.value}: ${it.key.name}") } }

                    fightSkill?.let {
                        player.increaseExp(it, mob.exp)
                        println("${mob.exp} experience points gained in ${it.name}")
                    }

                    player.increaseExp(player.defence, (defenceExpGained+(mob.exp/4)).toInt())
                    println("${defenceExpGained.toInt()} experience points gained in ${player.defence.name}")


                    respawnMob(mob)
                    break
                }
                else {
                    println("You hit ${mob.name} for $playerDamage damage, it now has ${mob.hpLeft} hp left.")
                }

            }
            if (Random.nextDouble() - attack.blockAbility < 0) println("You blocked the attack!")
            else {
                player.hpLeft -= mobDamage
                defenceExpGained += (3*mobDamage)/4
                if (player.hpLeft <= 0) {
                    print(player.die())
                    respawnMob(mob)
                    break
                }
                else println("""
                    You got hit for $mobDamage. You now have ${player.hpLeft} hp left.
                """.trimIndent())
            }
        }
    }

    private fun chooseAttack(attacks: List<Combat.Attack>): Combat.Attack {

        println("\nSelect your style of attack:")
        printAttacks(attacks)

        var input = readLine()!!.toLowerCase()

        while (true) {

            val attack = attacks.find { it.name.toLowerCase() == input || (attacks.indexOf(it)+1).toString() == input }
            attack?.let {return it}

            println("That's not an available attack. These are your options:")
            printAttacks(attacks)
            input = readLine()!!.toLowerCase()
        }
    }

    private fun printAttacks(attacks: List<Combat.Attack>) {
        val repeat = "-".repeat(60)
        println(repeat)
        attacks.forEachIndexed { index, attack ->
            println("(${index+1}) ${attack.name}")
        }
        println(repeat)
    }

    private fun attacks(skill: SkillSystem.FightSkill?): List<Combat.Attack> {
        val attackList = mutableListOf<Combat.Attack>()
        combat.attackOptions.forEach { if (it.fightSkill == skill) attackList.add(it) }
        return attackList
    }

    private fun respawnMob(mob: Mob) {
        mob.hpLeft = mob.hp
    }
}