class Combat (val player: Player) {

    private val spell = MagicAttack("Spell",10.0,0.2)
    private val curse = MagicAttack("Curse",10.0,0.2)
    private val blast = MagicAttack("Blast",10.0,0.2)


    private val slash = MeleeAttack("Slash",10.0,0.2)
    private val stab = MeleeAttack("Stab",10.0,0.2)
    private val strike = MeleeAttack("Strike",10.0,0.2)

    private val fire = ArcheryAttack("Fire",10.0,0.2)
    private val rapidFire = ArcheryAttack("Rapid fire",10.0,0.2)
    private val deflect = ArcheryAttack("Deflect",10.0,0.2)

    private val punch = NoWeaponAttack("Punch",10.0,0.2)
    private val slap = NoWeaponAttack("Slap",10.0,0.2)
    private val kick = NoWeaponAttack("Kick",10.0,0.2)

    val attackOptions = listOf(spell,curse,blast,slash,stab,strike,fire,rapidFire,deflect,punch,slap,kick)

    interface Attack {
        val name: String
        val fightSkill: SkillSystem.FightSkill?
        val damage: Double
        val blockAbility: Double
    }

    inner class MagicAttack(
        override val name: String,
        override val damage: Double,
        override val blockAbility: Double
    ) : Attack {
        override val fightSkill: SkillSystem.FightSkill = player.magic
    }

    inner class MeleeAttack(
        override val name: String,
        override val damage: Double,
        override val blockAbility: Double
    ) : Attack {
        override val fightSkill: SkillSystem.FightSkill = player.melee
    }

    inner class ArcheryAttack(
        override val name: String,
        override val damage: Double,
        override val blockAbility: Double
    ) : Attack {
        override val fightSkill: SkillSystem.FightSkill = player.archery
    }

    inner class NoWeaponAttack(
        override val name: String,
        override val damage: Double,
        override val blockAbility: Double
    ) : Attack {
        override val fightSkill: SkillSystem.FightSkill? = null
    }

}


