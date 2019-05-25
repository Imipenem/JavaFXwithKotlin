# JavaFXwithKotlin
Basic project setup with Kotlin and JavaFX for practice purposes

#Setup

I used OpenJDK11 together with Openjfx 11 (altough there´s a newer version 12 available).

When setting this project up for the first time:

* Make sure you added openjfx/JavaFX 11/12 to projects libs
* You may need to specify VM options: ```
                                      Run -> Edit Configurations
                                      ```
```
--module-path="path\to\javaFx\lib" --add-modules=javafx.controls,javafx.fxml
```