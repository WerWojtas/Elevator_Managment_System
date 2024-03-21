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

### Opis ważniejszych obiektów
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
#### SimulationInitializer
Uruchamia silnik, oraz jego prezentera.
#### SimulationPresenter
Prezenter symulacji odpowiada za komunikację między silnikiem i GUI
#### WeightErrorPresenter
Odpowiada za wyświetlenie okna błędu o zbyt dużym obciążeniu.
#### FloorCompare/FloorComparator
Interfejs, oraz klasa implementująca komparator do kolejek używanych w programie.
#### Engine
Odpowiada za działanie symulacji, oraz komunikację z prezenterem.

### Opis algorytmu.
Algorytm opiera się na jak najszybszym dotarciu do czekającej osoby z uwzględnieniem obsługi każdego zgłoszenia. Algorytm opiera się na wybranym sposobie działania windy: winda jedzie w jednym kierunku tak długo, aż jej bufor przystanków się nie wyczerpie - aby zoptymalizować koszty uruchomienia windy. Wybieranie windy, która zostanie wezwana odbywa się w następujących krokach:
- sprawdzenie czy winda już nie stoi na danym piętrze 
- wyszukanie najbliższej windy udającej się w kierunku naszego wezwania
- porównanie prędkości dotarcia do celu dla windy udającej się w kierunku wezwania, oraz najbliższej windy wolnostojącej (jeśli oba przypadki istnieją), przypisanie wezwania szybszej
- jeśli jeden z przypadków nie istnieje przypisanie wezwania do drugiego
- jeśli żadna z opcji nie była dostępna przypisanie zgłoszenia ostatniej windzie w odpowiedniej kolejki (kolejka w górę utrzymywana jest w porządku rosnącym, kolejka w dół w malejącym) ostatnia winda z danej kolejki jest tą która prawdopodobnie najwcześniej zmieni kierunek.
Oprócz powyższego algorytmu pewnymi uaktualnieniami zajmuje się LiftManager - opcją "reserved".
Wartość argumentu reserved windy zmieniana jest wtedy, gdy się nie ruszała i została wezwana z kierunku przeciwnego do celu wezwania np. winda stała na piętrze 0 i została wezwana na piętro 4 aby pojechać w dół. Nie powinna ona dostawać wtedy żądań w przeciwnym kierunku dlatego klasa LiftManager odpowiednio zmienia jej parametry (target, reserved) aby mogła ona dojechać do celu i ruszyć w przeciwnym kierunku.

Wywoływanie wind ze środka dodaje wywołane przystanki do odpowiednich kolejek w windach. Aktualizacja kierunku windy następuje w momencie gdy winda ma kierunek None lub po zamknięciu drzwi. Jeśli teoretyczna osoba zawoła windę w górę i zaraz po wejściu do niej wybierze odpowiednie piętro winda pojedzie do pożądanej destynacji. Jeśli natomiast teoretyczna osoba zawoła windę w górę, a będzie chciała pojechać w dół, uda jej się to jeśli nikt do momentu zamknięcia drzwi nie wywoła windy wyżej. Jeśli w windzie znajdzie się parę osób o róznych destynacjach winda dalej będzie podążała za swoim kierunkiem aż nie skończą jej się przystanki a następnie go zmieni.
