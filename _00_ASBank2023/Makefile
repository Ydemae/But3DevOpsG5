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

supp_container_maven:
	-docker rm $(CONTAINER_MAVEN_NAME)

run_maven:
	-docker run --network 00_asbank2023_default --name $(CONTAINER_MAVEN_NAME) $(IMAGE_MAVEN_NAME) mvn install && \
	docker cp $(CONTAINER_MAVEN_NAME):/app/target ./target
	
# Construit et lance le contenaire compose
start:
	docker compose up --build

# Supprime les ressources du docker composer
down:
	docker compose down -v

# Stop le contenaire sans supprimer les ressources
stop:
	docker compose stop

# Buil le projet, il faut utiliser make start avant
build: delete_target build-docker-maven run_maven rebootTomcat supp_container_maven