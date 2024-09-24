# Adding JAR Libraries to the Classpath in Eclipse

### Steps to Add JAR Files to the Classpath

1. **Open Eclipse**  
Make sure your project is open in Eclipse. If not, open it using the Eclipse IDE.

2. **Right-click on Your Project**  
In the Package Explorer (typically on the left-hand side), find your project, and right-click on it.

3. **Navigate to Libraries in Build Path Options**  
In the context menu, select:
**Build Path** → **Configure Build Path** → **Libraries**

4. **Add JARs**  
   - Click on the Add Jars button.
   - A new window will open. Browse to the location where the JAR files are stored (in `lib` folder), select all of them, and click OK.

5. **Ensure JARs are Added to the Classpath**  
Make sure the JARs are added to the Classpath, not the Module Path. This is critical for proper configuration.

6. **Apply and Close**  
Once you've added all the required JARs, click Apply and Close to save the changes.


### Important Notes:
- Ensure all the required JAR libraries are available and correctly referenced.
- Adding libraries to the Module Path may cause issues in certain configurations, so always add them to the Classpath.

### Troubleshooting:
- If you encounter issues with missing classes or errors, double-check the classpath configuration and verify that all JARs are correctly linked.