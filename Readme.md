**InstantSearchLucene**

***Instructions***

`cd <Dir>`

`mvn clean install`

`java -jar target\lucene-0.0.1-SNAPSHOT.jar`

_Results that start with the query term must be given priority_
 
 * eg: http://127.0.0.1:8080/SuggestNames?Keyword=bar
 
_The shortest (in length) result is preferred_
 
* eg: http://127.0.0.1:8080/SuggestNames?Keyword=kab


***Find Swagger Documentation below once installation is done:***

`http://127.0.0.1:8080/swagger-ui.html`