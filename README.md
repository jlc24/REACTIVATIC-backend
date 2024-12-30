# SECRETARIA DE DESARROLLO PRODUCTIVO E INDUSTRIA
## DIRECCION DE PROMOCION ECONOMICA INDUSTRIA Y COMERCIO
## PROYECTO DE APOYO A LA PRODUCCION DE LA MICRO Y PEQUEÑA EMPRESA DEL DEPARTAMENTO DE ORURO REACTIVA TIC

## `DESARROLLO DE SISTEMA DE TIENDA VIRTUAL`

### BACKEND

Este backend está desarrollado en Java con Spring Boot y utiliza Maven como herramienta de construcción. A continuación, se proporciona información sobre las versiones de las herramientas necesarias, los comandos para poner en funcionamiento el backend, los enlaces de descarga de las herramientas requeridas y cómo conectarse al backend.

#### Instalación y Configuración para S.O. WINDOWS

##### Requisitos:
Antes de comenzar, asegúrate de tener instalados los siguientes programas:

- PostgreSQL: v16.
- Apache Maven: v3.9.7.
- Java JDK: v11.0.22.

##### Enlaces de descarga:
- [PostgreSQL]
- [Apache Maven]
- [Java JDK]

##### Configuración del entorno:

1. Clonar el repositorio:
    ```sh
       git clone https://github.com/jlc24/REACTIVATIC-backend.git
       cd REACTIVATIC-backend
    ```
2. Revisar y/o configurar el archivo **application.properties**, la ruta de la base de datos, usuario y contraseña.
    ```sh
       spring.datasource.url=jdbc:postgresql://localhost:5432/reactivaticdb
       spring.datasource.username=postgres
       spring.datasource.password=RH1x3fNo0b
    ```
3. Descargar y ejecutar **PostgreSQL v16**, seguir las instrucciones del asistente de instalación.
4. Establecer la contraseña del superusuario según la información que se menciona en **application.properties** o cambiar por otra que sea segura.
    ```sh
       spring.datasource.password=RH1x3fNo0b
    ```
5. Abrir **PGAdmin** y conectarse al servidor usando el usuario y la contraseña que se estableció.
6. Crear la base de datos **reactivaticdb**.
7. Ralizar una restauracion de la base de datos reactivativdb.sql tomando en cuenta lo siguiente:
    ```sh
       Format: Custom or Jar
       Filename: "buscar el archivo reactivaticdbbackup<fecha>"
       Role name: postgres
    ```
8. Descargar y ejecutar **jdk-11.0.22_windows-x64_bin.exe**, seguir las instrucciones del asistente de instalación.
9. Abrir el **Panel de Control** y busca **"Variables de entorno"**.
10. En **"Variables del sistema"**, hacer clic en **"Nuevo"** y agregar las siguientes variables:
    - JAVA_HOME:
        - Nombre de variable: _JAVA_HOME_.
        - Valor de la variable: _<directorio de instalacion de java>_.
    - PATH:
        -  Busca la variable **Path** y haz clic en **"Editar"**.
        -  Agrega al final del valor: _%JAVA_HOME%\bin_.
11. Descargar **Maven**, el archivo ZIP para Windows.
12. Descomprimir en un directorio llamado **Apache**, renombrar **apache-maven-3.9.7** a **Maven**.
13. Abrir el **Panel de Control** y busca **"Variables de entorno"**.
14. En **"Variables del sistema"**, hacer clic en **"Nuevo"** y agregar las siguientes variables:
    - MAVEN_HOME:
        - Nombre de variable: _MAVEN_HOME_.
        - Valor de la variable: _<directorio de instalacion de maven>_.
    - PATH:
        -  Busca la variable **Path** y haz clic en **"Editar"**.
        -  Agrega al final del valor: _%MAVEN_HOME%\bin_.
15. Verificar la instalación abriendo una terminal (cmd o similar) y ejecutar:
    ```sh
       mvn -version
    ```
16. Debería mostrar similar a lo siguiente:
    ```sh
    Apache Maven 3.9.8 (36645f6c9b5079805ea5009217e36f2cffd34256)
    Maven home: C:\Apache\Maven
    Java version: 11.0.22, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-11
    Default locale: es_BO, platform encoding: Cp1252
    OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
    ```
17. Construir el proyecto **Backend**:
    ```sh
    mvn clean install
    ```
18. Iniciar el servidor:
    Para desarrollo:
    ```sh
    mvn spring-boot:run
    ```
    Para produccion:
    ```sh
    mvn clean package -DskipTests
    ```
    Para ejecutar en Servidor:
    ```sh
    nohup java -jar <nombre-del-archivo>.jar &
    ```

##### Conexión al Backend
###### Usando Postman o similar
Para probar los endpoints del backend con Postman:
1. Inicia el servidor como se describe anteriormente.
2. Abre Postman o similar y crea una nueva solicitud.
3. Configura la solicitud con la URL del backend ( _http://localhost:8678/reactivaticapp/apirest/_).
4. Selecciona el método HTTP adecuado (GET, POST, PUT, DELETE) y agrega los parámetros necesarios.
5. Envía la solicitud y revisa la respuesta.

## License

MIT

**Free Software**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [Apache Maven]: <https://maven.apache.org/download.cgi>
   [Java JDK]: <https://www.oracle.com/java/technologies/javase-downloads.htm>
   [PostgreSQL]: <https://www.postgresql.org/download/>
   
