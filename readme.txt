Installation:
Das Programm nutzt sbt zur Verwaltung sämtlicher Abhängigkeiten und sollte
beim ersten Aufruf von sbt alles nötige installieren. Abhängigkeiten sind
lediglich das play-framework und scalatest (für die Testcases)

$ sbt run 
startet den Server, welcher seine Dienste von Haus aus auf
localhost:9000 (play standard) bereitstellen sollte. Auf diesen Port
verbindet sich der client, welcher mit
$ sbt "project client" run
gestartet werden kann.

Es existieren Test-Cases für die library, welche mit
$ sbt "project lib" test
ausgeführt werden können.
