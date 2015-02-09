Installation:
Das Programm nutzt sbt zur Verwaltung sämtlicher Abhängigkeiten und sollte
beim ersten Aufruf von sbt alles nötige installieren. Abhängigkeiten sind
lediglich das play-framework und scalatest (für die Testcases)

$ sbt run 
startet den Server, welcher seine Dienste von Haus aus auf
localhost:9000 (play standard) bereitstellen sollte. Auf diesen Port
verbindet sich der client, welcher mit
$ sbt "project client" run
gestartet werden kann. Beim ersten starten kann es zu einem timeout
kommen, weil der Server erst die Classes compiliert, dann muss der client
nochmal gestartet werden.
Wenn das Spiel beendet wurde muss der server neu gestartet werden um ein
neues Spiel zu beginnen

Es existieren Test-Cases für die library, welche mit
$ cd lib; sbt test
ausgeführt werden können.
