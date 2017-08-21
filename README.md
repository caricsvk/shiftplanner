# shiftplanner
Simple Spring Boot + Angular 4 shiftplanner WebApp. Easy to run executable jar with in memory database.

Running demo http://relowl.com

Run it by yourself

	1. Download JAR from one of the releases https://github.com/caricsvk/shiftplanner/releases
	2. java -jar shiftplanner-0.9.0.jar
	3. Open browser: http://localhost:8092

Build and run

	1. Clone repo: git clone https://github.com/caricsvk/shiftplanner.git
	2. Pull all submodules: `git submodule init` `git submodule update`
	3. Build frontend: `cd app/src/main/resources/static/` and check manual https://github.com/caricsvk/shiftplanner-frontend
	4. Build all together: `mvn install`
	5. Run: `java -jar app/target/shiftplanner-0.9.0.jar`
	6. Open browser: http://localhost:8092