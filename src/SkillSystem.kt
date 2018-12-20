class SkillSystem{

    private fun logFactor(level: Int): Double {
        return 1 + Math.log(level.toDouble()) / Math.log(3.toDouble())
    }

    fun exponentialFactor(level: Int): Double {
        return Math.pow((1.02), level - 1.toDouble())
    }

    fun increaseExp(skill: Skill, exp: Int) {
        skill.experience += exp
        skill.totalExperience += exp
        if (skill.experience >= skill.experienceGap) {
            skill.level++
            skill.experience = 0
        }
    }

    interface Skill {
        var experience: Int
        val name: String
        val maxLevel: Int
        var level: Int
        var experienceGap: Int
        var totalExperience: Int
    }

    inner class NormalSkill (
        override var level: Int,
        override val name: String,
        override val maxLevel: Int = 100,
        override var experience: Int = 0,
        override var experienceGap: Int = (1000*exponentialFactor(level)).toInt(),
        override var totalExperience: Int = 0
    ) : Skill

    inner class FightSkill (
        override var level: Int,
        override val name: String,
        override val maxLevel: Int = 100,
        override var experience: Int = 0,
        override var experienceGap: Int = (1000*exponentialFactor(level)).toInt(),
        override var totalExperience: Int = 0
    ) : Skill {
        var damageLevelMultiplier: Double = level*logFactor(level)
    }


}