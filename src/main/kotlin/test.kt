import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * A Basic Drag and Drop Application demonstrating
 *
 *   1) Dragging between Controls
 *   2) Dragging Items from the Desktop
 *   3) Automatic Sorting of Items in Lists
 *   4) Observable Lists for Managing Model-View updates
 */
class Main : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("AppSetup/BasicSetup.fxml"))
        primaryStage.title = "Reversed DNA and RNA"
        primaryStage.scene = Scene(root, 650.0, 550.0)
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java, *args)
        }
    }
}