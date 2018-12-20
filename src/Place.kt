class Place {

    interface Visitable {
        val name: String
        val npcs: MutableList<Npc.Person>
        var placeDescription: String
        val mobs: MutableList<Mob>
        val location: Location
    }

    data class Inn(
        override val name: String,
        override val npcs: MutableList<Npc.Person>,
        override val location: Location
    ) : Visitable {
        override val mobs = mutableListOf<Mob>()
        override var placeDescription = ""
    }

    data class Shop(
        override val name: String,
        override val location: Location
        ) : Visitable {
        override val npcs = mutableListOf<Npc.Person>()
        override val mobs = mutableListOf<Mob>()
        override var placeDescription = ""
    }

    data class Other(
        override val name: String,
        override val location: Location
    ) : Visitable {
        override val mobs = mutableListOf<Mob>()
        override var placeDescription = ""
        override val npcs: MutableList<Npc.Person> = mutableListOf()
    }
}