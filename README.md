# shiftplanner
This is a demonstration of a simple web-app which can be used for planning shifts. 

* Master branch contains Java EE 8 project, Docker descriptor file and a run.sh script which can easily build and deploy the app on glassfish 5 docker. 
* spring-boot branch is branched out from master and with very few changes transformed to Spring Boot project which can be easily built and run as a jar file.

Running demo http://shiftplanner.nu-media.sk/

Run released jar

	1. Download JAR from one of the releases https://github.com/caricsvk/shiftplanner/releases
	2. java -jar shiftplanner-0.9.0.jar
	3. Open browser: http://localhost:8092

Build and run spring boot jar

	1. Clone repo: `git clone https://github.com/caricsvk/shiftplanner.git`
	2. Checkout right branch: `git checkout spring-boot`
	3. Pull all submodules: `git submodule init` `git submodule update`
	4. Build all together: `mvn install`
	5. Run: `java -jar shiftplanner/target/shiftplanner.jar`
	6. Open browser: http://localhost:8092

Build and run Java EE 8 version on glassfish 5 docker

	1. Prerequisites: installed java 8 jdk, maven & docker 
	2. Clone repo: `git clone https://github.com/caricsvk/shiftplanner.git`
	3. run bash script `/run.sh`
	4. open browser http://localhost:8080
