import java.io.Serializable

class SkillSystem : Serializable {

    private fun logFactor(level: Int): Double {
        return 1 + Math.log(level.toDouble()) / Math.log(3.toDouble())
    }

    interface Skill : Serializable {
        var experience: Int
        val name: String
        val maxLevel: Int
        var level: Int
        var experienceGap: Int
        var totalExperience: Int

        fun calcExperienceGap(): Int
    }

    inner class NormalSkill (
        override var level: Int,
        override val name: String,
        override val maxLevel: Int = 100,
        override var experience: Int = 0,
        override var totalExperience: Int = 0
    ) : Skill {
        override fun calcExperienceGap(): Int {
           return (500*Math.pow((1.02), this.level - 1.toDouble())).toInt()
        }

        override var experienceGap = calcExperienceGap()
    }

    inner class FightSkill (
        override var level: Int,
        override val name: String,
        override val maxLevel: Int = 100,
        override var experience: Int = 0,
        override var totalExperience: Int = 0
    ) : Skill {
        var damageLevelMultiplier: Double = logFactor(level)

        override fun calcExperienceGap(): Int {
            return (500*Math.pow((1.02), this.level - 1.toDouble())).toInt()
        }

        override var experienceGap = calcExperienceGap()
    }


}