## **Translate-Service**

`translate-service` is Spring-boot based application which implements Yandex Translate API to translate the text to target language.

How to use this repository?

- Step 1 : Fork this repository.
- Step 2 : mvn clean install
- Step 3 : java -jar target/translate-service-0.0.1-SNAPSHOT.jar 

Key Instructions : 
- Translate-service uses Java 1.8
- Default is 8080, which can be changed in application.properties or pass as a command line argument, 
	eg : Java -jar target/translate-service-0.0.1-SNAPSHOT.jar -Dserver.port=8989, to run the application at 8989 port.
- API key for Yandex Translate API can be changed in application.properties as well.
- Migrate to `/swagger-ui.html` to get the API documentation.

Implementation:
- translate-service caches (currently in-memory) the requests made to Yandex Translate API each time. This can be changed in future to support distributed caching.
- translate-service asynchronously makes requests for other languages also with the same text. This is driven by an external json file, which has mapping such as `"es":["el","it"]` in a resource file,`Language-mapping.json`
- The project implements Metrics for reporting. Since we are making requests to Third Party Service we would want to monitor if the requests are not too high and hence counter `yandex.requests` keeps the count for the number of requests.
- ThirdPartyService is loosely coupled as it is separately implemented as a service keeping in mind the future requirements that may arise if we want to change the service provider. 