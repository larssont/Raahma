import java.io.*

class FileReader {

    val saveDir = File("save/")
    val saveFile = File(saveDir,"player.ser")

    fun loadSave(): Player? {
        val save: Player
        saveDir.mkdir()
        saveFile.createNewFile()

        try {
            val fileIn = FileInputStream(saveFile)
            val `in` = ObjectInputStream(fileIn)
            save = `in`.readObject() as Player
            `in`.close()
            fileIn.close()
            return save
        } catch (E: EOFException) {
        } catch (i: IOException) {
            i.printStackTrace()
        }
        return null
    }

    fun save(playerSave: Player) {
        try {
            val fileOut = FileOutputStream(saveFile)
            val out = ObjectOutputStream(fileOut)
            out.writeObject(playerSave)
            out.close()
            fileOut.close()
        } catch (i: IOException) {
            i.printStackTrace()
        }

    }

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
            it.districtDescription = file.bufferedReader().readText()
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