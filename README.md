# Guild Management System (System Zarządzania Gildią)

Aplikacja desktopowa typu CRUD stworzona w języku Java, służąca do zarządzania strukturami gildii w grach MMO/RPG. Projekt implementuje wzorzec architektoniczny **MVC (Model-View-Controller)** oraz łączy się z relacyjną bazą danych MariaDB.

## Kluczowe funkcjonalności

* **Zarządzanie Gildiami:** Tworzenie nowych gildii, przypisywanie liderów.
* **Zarządzanie Graczami:** Dodawanie graczy, edycja ich statystyk i ról.
* **System Uprawnień:** Rozbudowany moduł przydzielania uprawnień (`PermissionsController`).
* **Finanse:** Rejestrowanie transakcji finansowych wewnątrz gildii.
* **Interfejs GUI:** Responsywny interfejs użytkownika oparty na JavaFX (`.fxml`).

##  Architektura i Technologie

* **Język:** Java 21
* **GUI:** JavaFX (FXML)
* **Baza danych:** MariaDB
* **Wzorzec:** MVC (Model - View - Controller)
* **Build Tool:** Maven
* **ORM/Driver:** MariaDB Java Client

##  Struktura katalogów (MVC)

* `src/main/java/application/models` - Klasy reprezentujące dane (Guild, Player, FinancialTransaction).
* `src/main/java/application/views` - Pliki FXML definiujące wygląd interfejsu.
* `src/main/java/application/controllers` - Logika sterująca aplikacją (np. `MainController`, `PlayersController`).
* `src/main/java/application/services` - Logika biznesowa i połączenie z bazą (`DatabaseConnection`).

##  Jak uruchomić

1.  **Wymagania:**
    * JDK 21 lub nowszy.
    * Maven.
    * Serwer bazy danych MariaDB.
2.  **Konfiguracja Bazy Danych:**
    * Upewnij się, że serwer MariaDB działa.
    * Sprawdź ustawienia połączenia w klasie `DatabaseConnection.java`.
3.  **Budowanie i uruchomienie:**
    ```bash
    mvn clean install
    mvn javafx:run
    ```
