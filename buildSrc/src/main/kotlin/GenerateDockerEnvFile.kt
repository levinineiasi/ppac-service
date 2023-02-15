import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class GenerateDockerEnvFile: DefaultTask() {

    @Input
    var filePath: String = ""

    @TaskAction
    fun generateEnvFile() {
        val userHome = System.getProperty("user.home")
        val file = File(filePath)
        if(!file.exists()){
            file.createNewFile()
        }
        file.writeText("HOME_USER_PATH=$userHome")
    }
}