class Location {

    data class District(val name: String) {
        var locationDescription = ""
    }
    
    interface Place {
        val name: String
        val npcs: MutableList<Npc.Person>
        var placeDescription: String
        val mobs: MutableList<Mob>
        val district: District
    }

    data class Inn(
        override val name: String,
        override val npcs: MutableList<Npc.Person>,
        override val district: District
    ) : Place {
        override val mobs = mutableListOf<Mob>()
        override var placeDescription = ""
    }

    data class Shop(
        override val name: String,
        override val district: District
    ) : Place {
        override val npcs = mutableListOf<Npc.Person>()
        override val mobs = mutableListOf<Mob>()
        override var placeDescription = ""
    }

    data class Other(
        override val name: String,
        override val district: District
    ) : Place {
        override val mobs = mutableListOf<Mob>()
        override var placeDescription = ""
        override val npcs: MutableList<Npc.Person> = mutableListOf()
    }
}