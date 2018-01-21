## **Translate-Service**

`translate-service` is Spring-boot based application which implements Yandex Translate API to translate the text to target language.

How to use this repository?

- Step 1 : Clone this repository.
- Step 2 : mvn clean install
- Step 3 : java -jar target/translate-service-0.0.1-SNAPSHOT.jar 

Key Instructions : 
- Translate-service uses Java 1.8
- Default is 8080, which can be changed in application.properties or pass as a command line argument, 
	eg : Java -jar target/translate-service-0.0.1-SNAPSHOT.jar -Dserver.port=8989, to run the application at 8989 port.
- API key for Yandex Translate API can be changed in application.properties as well.
- Migrate to `/swagger-ui.html` to get the API documentation.
- Create a Yandex translation API Key from here, tech.yandex.com/translate.

How to Contribute :
1. Fork the repository. 
2. Create issues 
3. Create Pull Request.
