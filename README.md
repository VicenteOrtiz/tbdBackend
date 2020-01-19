# backend TBD grupo 8

Aquí se resumira como preparar el sistema para poder ejecutar el backend.

## Tecnologías

para ejecutar el backend hay que instalar lo siguiente:

* **maven** - *Para correr el back*.
* **postgres** - *Como base de datos*.
* **postGIS** - *Para almacenar datos geométricos, instalar la version correspondiente con la de postgres*.

para visualizar los datos geometricos se recomienda instalar **qgis**.

## Preparacion y Ejecución

### Configurar base de datos (desde la terminal)

1. Ejecutar postgres.
2. Ingresar a postgres con el usuario predeterminado.

```
$ sudo -u postgress psql
```

3. En postgres crear la base de datos *tbd*.

```
postgres=# create database tbd;
```

4. Crear el usuario *tbduser*.

```
postgres=# create user tbduser with encrypted password 'tbdpass';
```

5. Darle permisos a la DB.

```
postgres=# grant privileges on database tbd to tbduser;
```

6. Habilitar la extención de postGIS en la DB y verificar su funcionamiento.

```
postgres=# create extension postgis;
CREATE EXTENSION

postgres=# select PostGIS_version();
            postgis_version            
---------------------------------------
 2.4 USE_GEOS=1 USE_PROJ=1 USE_STATS=1
(1 row)

```

### ejecución

Para correr el proyecto basta ejecutar lo siguiente:

```
$ mvn spring-boot:run
```

La aplicación correrá en el puerto **8081**. Esto se puede cambiar modificando el campo *server.port* en */src/main/resources/application.properties*