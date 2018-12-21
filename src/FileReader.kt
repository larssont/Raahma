import java.io.File

class FileReader {

    fun readFiles(
        districts: List<Location.District>,
        places: List<Location.Place>,
        characters: List<Npc.SupportingCharacter>
    ) {
        districtDescriptions(districts)
        placeDescriptions(places)
        sideCharacterScript(characters)
    }

    private fun districtDescriptions(districts: List<Location.District>) {
        districts.forEach {
            val name = it.name.replace("\\s".toRegex(), "")
            val directory = File("assets/District/$name/")
            directory.mkdirs()
            val file = File(directory, name+"_description.txt")
            file.createNewFile()
            it.locationDescription = file.bufferedReader().readText()
        }
    }

    private fun placeDescriptions(places: List<Location.Place>) {
        places.forEach {
            val name = it.name.replace("\\s".toRegex(), "")
            val directory = File("assets/Location/$name/")
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