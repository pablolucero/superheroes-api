# Superhero API

## Autor

Pablo Lucero

## Prerrequisitos

- Git
- Docker
- Clonar el repositorio `git clone https://github.com/pablolucero/superheroes-api`
- Navegar al mismo `cd superheroes-api`

## Docker

Para crear la imagen 
```bash
./mvnw clean install
```
```bash
docker build -t superheros-api .
```

Para levantar un contenedor
```bash
docker run --rm --name superheros-api -p 8080:8080 superheros-api
```
## Tests

Para correrlos ejecutar `./mvnw clean test`

## Base de datos

Consola H2: http://localhost:8080/h2-console

Las credenciales están en el [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)

## Documentación de la API

Por defecto la base se inicializa con 3 superheroes: Superman, Batman y Flash.

Las request para listar y buscar están cacheadas y se refrescan cuando se crea o edita un héroe.

### Swagger

http://localhost:8080/swagger-ui/index.html

### Postman

Ver colección en [superheroes.postman_collection.json](superheroes.postman_collection.json)

### Ejemplos

---
Listar
```bash
curl --location 'http://localhost:8080/api/superheroes'
```

Buscar por id

```bash
curl --location 'http://localhost:8080/api/superheroes/1'
```

Buscar por nombre

```bash
curl --location 'http://localhost:8080/api/superheroes/search?name=man'
```

Crear

```bash
curl --location 'http://localhost:8080/api/superheroes' \
--header 'Content-Type: application/json' \
--data '{
  "name": "Deadpool"
}'
```

Actualizar

```bash
curl --location --request PUT 'http://localhost:8080/api/superheroes/1' \
--header 'Content-Type: application/json' \
--data '{
  "name": "Superman Bizarro"
}'
```

Borrar

```bash
curl --location --request DELETE 'http://localhost:8080/api/superheroes/1'
```
