# JavaFX Setup in Eclipse

### Steps to configure JavaFX Setup

1. **Verify Java Version**  
Make sure you are using Java 13 or higher in Eclipse:
   - Go to Eclipse → Help → About Eclipse IDE to check the Java version.

2. **Install JavaFX SDK**  
Download the JavaFX SDK from [GluonHQ](https://gluonhq.com/products/javafx/) and select the correct version for your operating system.

3. **Create JavaFX User Library**  
   - Open Eclipse and navigate to: Window → Preferences → Java → Build Path → User Libraries.
   - Click New and name the library `JavaFX`.
   - Click Add External JARs and navigate to the lib folder in your JavaFX SDK directory, then add all `.jar` files in this folder.

4. Add JavaFX to the Build Path
   - Right-click on your project in the Package Explorer.
   - Select Build Path → Configure Build Path.
   - Under the Libraries tab, click Add Library → User Library → Next.
   - Select JavaFX and click Finish.

5. **Set Up Run Configuration**  
To run your JavaFX applications, you need to configure the VM arguments in the Run Configuration:

   - Right-click on your main class (e.g., `Player.java`) in the Package Explorer.
   - Select Run As → Run Configurations.
   - In the dialog, under Java Application, click New Configuration.
   - In the VM arguments field, enter the following:

      ``` bash
      --module-path "/path/to/javafx-sdk-22.0.1/lib" --add-modules javafx.controls
      ```

    Replace `FILEPATH` with the path to your JavaFX SDK directory. 
    Adjust the version of the SDK accordingly.

   - **macOS Users**: Ensure the `-XstartOnFirstThread` option is unchecked.


