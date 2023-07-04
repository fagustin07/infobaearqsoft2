# Estrategias de observabilidad y resiliencia.

## Documentacion de entrega
   Los documentos solicitados se encuentran en la carpeta *[documentacion](https://github.com/fagustin07/infobaearqsoft2/tree/main/documentacion)*.
## Requerimientos para correr la aplicacion
- [Docker](https://docs.docker.com/get-docker/). 🐳
- [Docker Compose](https://docs.docker.com/get-docker/). 🐳

## Deseable
- [Editor de Texto](http://territoriogo.blogspot.com/2018/10/que-editor-utilizar-para-programar-en-go.html). 📝

# Correr la app.
1. Solicitar las credenciales necesarias para que el servicio de Loader funcione correctamente. Luego, colocarlas en `loader-service/main/resources/application.properties` en los parametros correspondientes que son: api key y base de datos.
2. Una vez configurado el archivo mencionado, simplemente debemos ejecutar `docker compose up --build`.
3. En nuestro navegador podremos ingresar a las siguientes urls:
   - http://localhost:9000, donde veremos los protocolos de comunicacion del componente Metric realizado con Swagger.
   - http://localhsot:8080, donde veremos los protocolos de comunicacion del componente Loader realizado con Swagger.
   - http://localhost:9411, donde veremos la UI de Zipkin, la herramienta utilizada para Distributed Tracing.
   - http://localhost:5601, donde veremos la UI de Kibana, la herramienta que muestra los logs centralizados de ambos componentes, que fueron recompilados con el stack ELK.
   - http://localhost:3000, donde veremos la UI de Grafana, la herramienta con la que visualizamos las metricas de los componentes y a su vez realizamos la configuracion del alerting.

## Tests de carga

### Requerimientos

- [JMeter](https://jmeter.apache.org/). 🪶
- Aplicacion levantada con docker compose.

### Ejecutar los tests de carga

Para correr los tests simplemente abrimos nuestro JMeter y le cargamos el archivo que se encuentra en `pruebas de carga/INFOBAE UNQ.jmx` dentro del proyecto. Ahi mismo ya podemos comenzar a ejecutar los tests, o bien reconfigurar los parametros para adaptarlo a lo que uno desee ver.

*Autores: Mauro Bailon, Federico Sandoval.*