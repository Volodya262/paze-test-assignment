import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	java
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.1.0"
	id("io.freefair.lombok") version "6.6.3"
}

group = "com.volodya262"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2021.0.5"

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	implementation("org.apache.httpcomponents:httpclient:4.5.14")
	implementation("org.joda:joda-money:1.0.3")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda-money:2.13.4")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")

	compileOnly("org.projectlombok:lombok:1.18.20")
	annotationProcessor("org.projectlombok:lombok:1.18.20")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
	testImplementation("com.github.tomakehurst:wiremock-jre8:2.35.0")
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test> {
	jvmArgs(listOf("--enable-preview"))
	useJUnitPlatform()
}

tasks.withType<BootRun> {
	jvmArgs(listOf("--enable-preview"))
}