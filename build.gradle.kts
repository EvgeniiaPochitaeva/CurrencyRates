plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {


    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/org.telegram/telegrambots
    implementation("org.telegram:telegrambots:6.8.0")



    implementation("javax.json:javax.json-api:1.1.4")
    implementation("org.glassfish:javax.json:1.1.4")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    // and a dependency on vert.x
    implementation ("io.vertx:vertx-core:3.5.3")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.7")


    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")

    testCompileOnly ("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")

    implementation("org.quartz-scheduler:quartz:2.5.0-rc1")

}

tasks.test {
    useJUnitPlatform()
}