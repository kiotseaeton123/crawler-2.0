## Instructions 
1. clone branch `git clone --branch javacrawler --single-branch <repository_url>
2. open project with maven configured IDE
3. `mvn clean package` to clean build artifacts (if there are any) and package code and dependencies to jar file
4. uber jar `crawler-1.0-SNAPSHOT.jar` will contain all code and dependencies
5. run the jar: `java -jar target/crawler-1.0-SNAPSHOT.jar`


