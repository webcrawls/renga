./gradlew clean build
cp build/libs/renga.jar app/renga.jar
cd app
java -jar renga.jar