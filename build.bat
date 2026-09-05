@echo off
setlocal

set "ROOT=%~dp0"
set "BUILD=%ROOT%build"
set "CLASSES=%BUILD%\classes"
set "INPUT=%BUILD%\package-input"
set "DIST=%ROOT%dist"

where javac >nul 2>&1 || (
    echo Error: javac was not found. Install a JDK and add it to PATH.
    exit /b 1
)
where jar >nul 2>&1 || (
    echo Error: jar was not found. Install a JDK and add it to PATH.
    exit /b 1
)
where jpackage >nul 2>&1 || (
    echo Error: jpackage was not found. Install JDK 14 or newer and add it to PATH.
    exit /b 1
)
if not exist "%ROOT%src\GUI\MainGUI.java" (
    echo Error: src\GUI\MainGUI.java was not found beside this script.
    exit /b 1
)

if exist "%BUILD%" rmdir /s /q "%BUILD%"
if exist "%DIST%" rmdir /s /q "%DIST%"
mkdir "%CLASSES%"
mkdir "%INPUT%"
mkdir "%DIST%"

pushd "%ROOT%"
javac -encoding UTF-8 -d "%CLASSES%" src\GUI\MainGUI.java src\GUI\OnHoldPlayerCard.java src\GUI\PlayerRowPanel.java src\GUI\QueueMatchCard.java src\Collection\OnHoldList.java src\Collection\QueueList.java src\Management\CourtManager.java src\Management\MatchMaker.java src\Management\QueueService.java src\Model\Court.java src\Model\Match.java src\Model\MatchFormat.java src\Model\MatchStatus.java src\Model\Player.java src\Model\PlayerStatus.java src\Model\SkillLevel.java
popd
if errorlevel 1 exit /b 1

jar --create --file "%INPUT%\SQS.jar" --main-class GUI.MainGUI -C "%CLASSES%" .
if errorlevel 1 exit /b 1

echo JAR created: %INPUT%\SQS.jar

jpackage --type exe --name SQS --app-version 1.0.0 --vendor "SQS" --input "%INPUT%" --main-jar SQS.jar --main-class GUI.MainGUI --win-shortcut --win-menu --win-dir-chooser --dest "%DIST%"
if errorlevel 1 exit /b 1

echo.
echo Installer created: %DIST%\SQS-1.0.0.exe
endlocal
