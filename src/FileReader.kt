import java.io.File

class FileReader {

    fun readFiles(
        locations: List<Location>,
        visitables: List<Place.Visitable>,
        characters: List<Npc.SupportingCharacter>
    ) {
        locationDescriptions(locations)
        placeDescriptions(visitables)
        sideCharacterScript(characters)
    }

    private fun locationDescriptions(locations: List<Location>) {
        locations.forEach {
            val name = it.name.replace("\\s".toRegex(), "")
            val directory = File("assets/Location/$name/")
            directory.mkdirs()
            val file = File(directory, name+"_description.txt")
            file.createNewFile()
            it.locationDescription = file.bufferedReader().readText()
        }
    }

    private fun placeDescriptions(visitables: List<Place.Visitable>) {
        visitables.forEach {
            val name = it.name.replace("\\s".toRegex(), "")
            val directory = File("assets/Place/$name/")
            directory.mkdirs()
            val file = File(directory, name+"_description.txt")
            file.createNewFile()
            it.placeDescription = file.bufferedReader().readText()
        }
    }

    private fun sideCharacterScript(characters: List<Npc.SupportingCharacter>) {
        characters.forEach {
            val name = it.name.replace("\\s".toRegex(), "")
            val directory = File("assets/Supporting Character/$name/")
            directory.mkdirs()
            val file = File(directory, name+"_script.txt")
            file.createNewFile()
            it.characterScript = file.bufferedReader().readText()
        }
    }

}