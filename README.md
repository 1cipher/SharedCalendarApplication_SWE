# Shared Calendar Application

Questa repository contiene la relazione e il progetto in Java di Shared Calendar Application, l'applicazione realizzata come elaborato finale dell'esame di Ingegneria del Software di Università degli Studi di Firenze.

## Considerazioni finali
### Use case
In applicazioni con Use Cases piuttosto vari, è opportuno dividere gli Use Cases di tipo User Goal da quelli di tipo Function. E' preferibile che i nomi degli Use Case presentati nel diagramma inizino per lettera minuscola. E' utile anche numerare gli Use Cases, che è molto utile anche nel caso si voglia fare dei riferimenti nei MockUps.

In applicazioni che prevedono una fase di login, è utile indicare lo Use Case riferito all'accesso ai dati con lo _stereotype_ di _'summary'_.

### Database
Quella implementata non è una mappatura di classi su Database, ma qualcosa di più simile a una persistenza su Database dei dati rappresentati dalle classi. In generale la mappatura è da preferire in applicazioni simili.

E' utile, in fase di progettazione, partire dal diagramma delle classi per poi generare automaticamente le tabelle, realizzando di fatto una mappatura

Esistono soluzioni applicative che permettono di gestire nel miglior modo possibile mappatura e persistenza, come Java Enterprise JPA, che offre tra le altre cose la possibilità di gestire anche la fase transiente del trasferimento dei dati su Database (come il meccanismo di UUID, implementazione standard per la gestione di chiavi primarie). Un'altra importante soluzione è Hibernate.

I Gateway vengono spesso identificati anche come DAO (Data Access Object), che possono anche essere multipli in un'applicazione, al fine di gestire oggetti di diverso tipo.

### Tests
Uno strumento che può risultare utile per il testing dell'interfaccia grafica in modalità Black Box è Selenium. In generale il passaggio fondamentale è però dividere il software in piccole unità da testare indipendentemente. In particolare, quando si ha a che fare con un Database, è buona norma che ogni _query_ sia testata.

### Altre Considerazioni
Quando la complessità dell'interfaccia grafica cresce, soprattuto se si ha a disposizione un diagramma di navigazione delle finestre, è utile realizzare un controller per pagina. Questa scelta deve essere naturalmente affiancata da un meccanismo di trasferimento di controllo da controller a controller.
