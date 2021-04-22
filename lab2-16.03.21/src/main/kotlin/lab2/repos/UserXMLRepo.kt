package lab2.repos

import java.io.File
import lab2.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import java.beans.XMLDecoder
import java.beans.XMLEncoder
import java.io.FileInputStream
import java.io.FileOutputStream

object UserXMLRepo {

    @Autowired
    private var crutchPath: String = "./crutch"
    private var idGen: Long

    init {
        File(crutchPath).mkdirs()
        idGen = findAll().map(User::userId).maxOrNull()?.toLong() ?: 0
    }

    private fun fileFactory(user: User) = File("$crutchPath/${user.email}.xml")

    infix fun save(user: User) {
        val write: (User) -> Unit = {
            XMLEncoder(FileOutputStream(
                fileFactory(user).apply { createNewFile() })
            ).use { it.writeObject(user) }
        }
        val file = fileFactory(user)
        if (file.exists()) {
            file.delete()
            write(user)
        } else write(user.apply {
            idGen++
            userId = idGen
        })
    }

    infix fun getByEmail(email: String) =
        try {
            val userFile = File(crutchPath).listFiles { _, name -> name.contains(email) }.first()
            XMLDecoder(FileInputStream(userFile)).use { it.readObject() as User }
        } catch (e: Exception) {
            throw EmptyResultDataAccessException(1)
        }

    infix fun getByUserId(id: Long) =
        findAll().find { it.userId == id } ?: throw EmptyResultDataAccessException(1)

    fun findAll(): List<User> =
        File(crutchPath).listFiles()?.mapNotNull { file ->
            try {
                XMLDecoder(FileInputStream(file)).use { it.readObject() as User }
            } catch (e: Exception) {
                null
            }
        }?.toList() ?: emptyList()

    fun delete(user: User) = fileFactory(user).delete()
}