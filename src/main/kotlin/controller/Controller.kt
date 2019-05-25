package controller


import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.input.ClipboardContent
import javafx.scene.input.Dragboard
import javafx.scene.input.TransferMode
import javafx.stage.Stage
import model.DNA
import model.RNA
import java.io.File

/**
 * Initialized By JavaFX Loader when loading the FXML document.
 *
 * fx:controller attribute in the FXML document maps a "controller" class with an FXML document.
 *
 * A controller is a compiled class that implements the "code behind" the object hierarchy defined by the document.
 */
class Controller {



    @FXML lateinit var DNAListView: ListView<DNA>
    @FXML lateinit var RNAListView: ListView<RNA>
    @FXML lateinit var fileListView: ListView<File>

    // model
    val dnastrands = FXCollections.observableArrayList<DNA>()
    val rnastrands = FXCollections.observableArrayList<RNA>()
    val fileList = FXCollections.observableArrayList<File>()

    @FXML
    fun closeApp() {
        val stage = DNAListView.scene.window as Stage
        stage.close()
    }

    /**
     * Called after JavaFX initialized and document loaded
     */
    @FXML
    fun initialize() {

        dnastrands.add(DNA("AGCTCGGCTCGGCTCGCTCGCTCG"))
        dnastrands.add(DNA("AGCTCGGCTCGGCTCGCTCGCTCG"))
        dnastrands.add(DNA("AGCTCGGCTCGGCTCGCTCGCTCG"))
        dnastrands.add(DNA("AGCTCGGCTCGGCTCGCTCGCTCGAGCTCGGCTCGGCTCGCTCGCTCGAGCTCGGCTCGGCTCGCTCGCTCG"))
        dnastrands.add(DNA("AGCTCGGCTCGGCTCGCTCGCTCGAGCTCGGCTCGGCTCGCTCGCTCG"))
        dnastrands.add(DNA("AGCTCGGCGCTCGCTCG"))

        // Ensure the lists are always sorted
        DNAListView.items = dnastrands.sorted()
        RNAListView.items = rnastrands.sorted()
        fileListView.items = fileList.sorted()

        setupDragAndDrop()
        initPlaceHolders()
    }

    /**
     * Shown when lists are empty
     */
    private fun initPlaceHolders() {
        val filePlaceHolder = Label("Drag Files Here")
        filePlaceHolder.styleClass.add("file-placeholder")
        fileListView.placeholder = filePlaceHolder

        val rnaPlaceholder = Label("Drag RNA samples Here")
        rnaPlaceholder.styleClass.add("general-placeholder")
        RNAListView.placeholder = rnaPlaceholder

        val dnaPlaceholder = Label("DNA samples here")
        dnaPlaceholder.styleClass.add("general-placeholder")
        DNAListView.placeholder = dnaPlaceholder
    }

    /**
     * Register all the Drag and Drop event handlers
     */
    private fun setupDragAndDrop() {

        DNAListView.onDragDetected = EventHandler { event ->
            val db = DNAListView.startDragAndDrop(TransferMode.MOVE)
            addSelectedDnasampleToDragbord(DNAListView, db)
            //db.dragView = happy
            event.consume()
        }

        RNAListView.onDragDetected = EventHandler { event ->
            val db = RNAListView.startDragAndDrop(TransferMode.MOVE)
            addSelectedRnasampleToDragbord(RNAListView, db)
            //db.dragView = sad
            event.consume()
        }

        // DragHelper.registerAsDragSource()
        DNAListView.onDragOver = EventHandler { event ->
            event.acceptTransferModes(TransferMode.MOVE)
            event.consume()
        }

        // accept references
        RNAListView.onDragOver = EventHandler { event ->
            event.acceptTransferModes(TransferMode.MOVE)
            event.consume()
        }

        DNAListView.onDragDropped = EventHandler { event ->
            val newDnaSample = getSelectedRNAFromDragboard(rnastrands, event.dragboard)
            System.out.println("Started converting RNA to DNA")
            //TODO: Convert RNA string to DNA first
            dnastrands.add(DNA(newDnaSample.toString()))
            rnastrands.remove(newDnaSample)
            event.isDropCompleted = true
            event.consume()
        }

        RNAListView.onDragDropped = EventHandler { event ->
            val newRnaSample = getSelectedDNAFromDragboard(dnastrands, event.dragboard)
            System.out.println("Started converting DNA to RNA")
            //For example, you can add some code here to convert a DNA into an RNA when dropped into the RNA field
            //TODO: Convert DNA string to RNA first
            rnastrands.add(RNA(newRnaSample.toString()))
            dnastrands.remove(newRnaSample)
            event.isDropCompleted = true
            event.consume()
        }

        // accept references
        fileListView.onDragOver = EventHandler { event ->
            event.acceptTransferModes(TransferMode.LINK)
            event.consume()
        }

        fileListView.onDragDropped = EventHandler { event ->
            if (event.gestureSource == null && event.acceptedTransferMode == TransferMode.LINK) {
                // FROM OUTSIDE THIS APP
                val files = event.dragboard.files
                System.out.println("Got ${files.size} files")
                files.map { fileList.add(File(it.canonicalPath)) }
            }
            event.isDropCompleted = true
            event.consume()
        }
    }

    private fun getSelectedDNAFromDragboard(sourceList: ObservableList<DNA>, db: Dragboard): DNA {
        val strandID = db.string.toInt()
        return sourceList.find { it.id == strandID } ?: throw RuntimeException("DNA Not Found in Source List After Drag")
    }

    private fun getSelectedRNAFromDragboard(sourceList: ObservableList<RNA>, db: Dragboard): RNA {
        val strandID = db.string.toInt()
        return sourceList.find { it.id == strandID } ?: throw RuntimeException("RNA Not Found in Source List After Drag")
    }

    /**
     * Helper method to put ID of selected person into Dragboard content
     */
    private fun addSelectedDnasampleToDragbord(listView: ListView<DNA>, db: Dragboard) {
        val content = ClipboardContent()
        content.putString(getSelectedDnaID(listView))
        db.setContent(content)
    }

    private fun addSelectedRnasampleToDragbord(listView: ListView<RNA>, db: Dragboard) {
        val content = ClipboardContent()
        content.putString(getSelectedRnaID(listView))
        db.setContent(content)
    }

    private fun getSelectedDnaID(listView: ListView<DNA>): String {
        if (listView.selectionModel.isEmpty)
            throw RuntimeException("No DNA sample selected")
        val item = listView.selectionModel.selectedItem
        return item.id.toString()
    }

    private fun getSelectedRnaID(listView: ListView<RNA>): String {
        if (listView.selectionModel.isEmpty)
            throw RuntimeException("No RNA sample selected")
        val item = listView.selectionModel.selectedItem
        return item.id.toString()
    }


}