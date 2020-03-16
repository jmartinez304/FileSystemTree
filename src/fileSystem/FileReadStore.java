package fileSystem;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * 
 * This program is a file system reader which reads a particular folder and
 * stores it in a tree data structure. Each node in the tree corresponds to a
 * folder in the file system and displays the number of files it contains, the
 * total size of the files it contains, the folder's name, and a list of child
 * folders it contains. The program prints to console and also displays a GUI
 * to select the file and view them in a JavaFx TreeView.
 *
 */

public class FileReadStore extends Application {

	/**
	 * The main method in which we launch the UI and thus the rest of the processes
	 * go into action.
	 * 
	 * @param args an array of command-line arguments for the application
	 */

	public static void main(String[] args) {

		launch(args);
	}

	/**
	 * Method that is called by the main method to create the UI for the program.
	 * Here we set the elements of the UI and the actions that part is going to
	 * perform including the DirectoryChooser.
	 */
	@Override
	public void start(Stage primaryStage) {

		TreeView<String> tree = new TreeView<String>();
		BorderPane bp = new BorderPane();
		Button button = new Button("Select Folder");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				DirectoryChooser dc = new DirectoryChooser();
				dc.setInitialDirectory(new File(System.getProperty("user.home")));
				File choice = dc.showDialog(primaryStage);
				if (choice == null || !choice.isDirectory()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Could not open directory");
					alert.setContentText("The file is invalid.");

					alert.showAndWait();
				} else {
					System.out.println("--------------------------------------------------------------------------");
					System.out.println("Files from directory : " + choice);
					System.out.println("--------------------------------------------------------------------------");
					tree.setRoot(getNodesForDirectory(choice, 0));
				}
			}
		});
		bp.setTop(button);
		BorderPane.setAlignment(button, Pos.CENTER);
		bp.setCenter(tree);
		primaryStage.setScene(new Scene(bp, 600, 400));
		primaryStage.setTitle("File System");
		primaryStage.show();
	}

	/**
	 * Recursion method for getting the folder name, the number of files and file
	 * sizes. We also have the indentation for the different levels of folders.
	 * 
	 * @param directory whose folders will be scanned
	 * @param level     of the folder in the tree hierarchy
	 * @return root TreeItem that will have the file data
	 */
	public TreeItem<String> getNodesForDirectory(File directory, int level) {

		TreeItem<String> root = new TreeItem<String>(directory.getName());

		for (File file : directory.listFiles()) {

//			// Indentation for levels when user wants filenames
//			for (int i = 0; i < level; i++) {
//				System.out.print("\t");
//			}

			if (file.isDirectory()) {
				// Indentation for levels when user does not want filenames
				// If the user wants to add filenames as well then comment out the following for
				// loop
				for (int i = 0; i < level; i++) {
					System.out.print("\t");
				}
				System.out.printf("[" + file.getName() + "] | Number of Files: " + file.listFiles().length
						+ " | Total size of the files: %.2f KB %n", (((double) (folderSize(file))) / 1024));
				root.getChildren().add(getNodesForDirectory(file, level + 1));
			} else {
				// If the user wants to add filenames as well then uncomment the next lines
//				System.out.printf(file.getName() + " | Size: %.2f KB %n", (((double) (file.length())) / 1024));
//				root.getChildren().add(new TreeItem<String>(file.getName()));

			}

		}

		return root;
	}

	/**
	 * Method for counting the total folder size.
	 * 
	 * @param folder name whose total folder size is needed
	 * @return length total folder size
	 */
	public long folderSize(File folder) {
		long length = 0;
		File[] files = folder.listFiles();
		int count = files.length;

		for (int i = 0; i < count; i++) {
			if (files[i].isFile()) {
				length += files[i].length();
			} else {
				length += folderSize(files[i]);
			}
		}
		return length;
	}
}
