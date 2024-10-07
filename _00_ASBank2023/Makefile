IMAGE_MAVEN_NAME = image-maven
CONTAINER_MAVEN_NAME = maven-container

ifeq ($(OS),Windows_NT)
delete_target:
	if exist target (rmdir /s /q target)
else
delete_target:
	if [ -d "target" ]; then rm -rf target; fi
endif

build-docker-maven:
	docker build -t $(IMAGE_MAVEN_NAME) -f Docker/Dockerfile.maven .

rebootTomcat:
	docker-compose restart tomcat

run_maven:
	docker run --network 00_asbank2023_default --name $(CONTAINER_MAVEN_NAME) $(IMAGE_MAVEN_NAME) mvn install && \
	docker cp $(CONTAINER_MAVEN_NAME):/app/target ./target && \
	docker rm $(CONTAINER_MAVEN_NAME)
	
# Construit et lance le contenaire compose
start:
	docker compose up --build

# Ferme le contenaire du composer
exit:
	docker compose down

# Buil le projet, il faut utiliser make start avant
build: delete_target build-docker-maven run_maven rebootTomcat