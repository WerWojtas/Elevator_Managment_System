# Elevator Managment System
Designed and implemented by Weronika Wojtas
### Wykorzystane narzędzia
- Java 19
- JavaFx 17
- JUnit 5.9.1
### Uruchomienie programu
Należy sklonować repozytorium
```
git clone https://github.com/WerWojtas/Elevator_Managment_System
```
Przejść do folderu z aplikacją
```
cd Elevator_Managment_System
```
Uruchomić symulator za pomocą komendy
```
./gradlew run
```
### Panel sterujący
W menu opcji dostępne są 3 do wyboru: 
- liczba wind od 1-16
- liczba pięter od 2-25
- maksymalne obiążenie windy w zakresie od -1 do 1
  Wybranie obiążenia na poziomie -1 sprawia, że winda na każdym postoju będzie przeciążona - wyskoczy okienko z komunikatem.
W menu symulacji siatka w centrum reprezentuje windy:
- winda zielona : sprawna bez zadań
- winda żółta : porusza się w górę
- winda niebieska : porusza się w dół
- winda różowa : zatrzymuje się na piętrze (otwarte drzwi)
- winda czerwona : winda wymaga serwisowania
Podczas przebiegu symulacji możliwe jest dowolne przywoływanie wind z panelu po lewej stronie reprezentującym przyciski z zewnątrz, oraz panelu po prawej stronie
reprezentującym przyciski z wewnątrz. Możliwe jest także dowolne psucie/naprawa windy za pomocą przycisku kill/revive.

### Opis obiektów
#### Lift
Klasa windy odpowiedzialna za przyporządkowanie zgłoszenia do odpowiedniej kolejki up lub down, oraz za wykonanie jednego kroku podczas symulacji. Klasa odpowiada także za
zmianę swoich podstawowych stanów takich jak otwarcie/zamknięcie drzwi, zepsucie się.
#### LiftManager
Klasa odpowiedzialna za komunikację pomiędzy LiftShedulerem i Lift. LiftManager aktualizuje kierunek windy, aktualizuje listy wind LiftShedulera, a także odpowiada za ponowne przypisanie
do LiftShedulera wezwań w systemie w przypadku śmierci windy. Klasa zajmuje się także "rezerwacją" windy czyli sytuacją w której winda która została przywołana z dołu zaraz ponownie ma
zjechać w dół.
#### LiftSheduler
Klasa odpowiedzialna za przydzielanie wezwań
#### LiftStatus/GuiElement
Pomocniczy Enum używany przy rysowaniu kolorów wind w wizualizacji, oraz klasa rysująca windę.
#### MapChangeListener
Interfejs identyfikujący kontrakt obserwatora symulacji.
#### Menu
Klasa dziedzicząca po aplikacji uruchamiająca okno opcji.
####
