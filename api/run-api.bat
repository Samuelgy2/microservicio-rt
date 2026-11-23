@echo off
rem -------------------------------------------------
rem run-api.bat – preparación y arranque del proyecto
rem -------------------------------------------------
rem   1) Borra los lock‑files de Maven
rem   2) Usa un repositorio Maven local (descarga de Maven Central lo que falte)
rem   3) Compila y arranca la API en el puerto 8000
rem -------------------------------------------------

rem ==== 0) Variables ==== 
set "PROJECT_ROOT=C:\Users\ne\Downloads\api\api"
set "M2_HOME=%USERPROFILE%\.m2"
set "LOCAL_REPO=C:\Users\ne\Downloads\api\local-m2\repository"
set "JAVA_HOME=C:\Users\ne\.jdks\ms-21.0.12.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"

rem ==== 1) Dar permisos (solo si el script se ejecuta como Administrador) ====
rem    Si ya eres Administrador no hace nada; si no lo eres, abre una
rem    ventana de CMD como “Ejecutar como administrador”.
if "%USERNAME%"=="" (
    echo Otorgando control total a %USERNAME% sobre %M2_HOME% ...
    icacls "%M2_HOME%" /grant %USERNAME%:F /T >NUL 2>&1
)

rem ==== 2) Borrar lock‑files ====
echo Eliminando lock‑files de Maven...
if exist "%M2_HOME%\repository\.locks" (
    rmdir /s /q "%M2_HOME%\repository\.locks"
)

rem ==== 3) Crear repositorio Maven local limpio ====
if not exist "%LOCAL_REPO%" (
    echo Creando repositorio Maven local en %LOCAL_REPO% ...
    md "%LOCAL_REPO%"
)

rem ==== 4) Cambiar al proyecto y compilar (online) ====
pushd "%PROJECT_ROOT%"
echo.
echo ==== Compilando proyecto ====
call "%PROJECT_ROOT%\mvnw.cmd" -Dmaven.repo.local="%LOCAL_REPO%" clean package -DskipTests
if errorlevel 1 (
    echo *** ERROR: la compilación falló. ***
    echo Revise la conexión a internet: Maven descarga las dependencias faltantes
    echo desde https://repo.maven.apache.org/maven2 hacia %LOCAL_REPO%
    popd
    exit /b 1
)

rem ==== 5) Ejecutar la aplicación ====
echo.
echo ==== Iniciando Spring Boot (puerto 8000) ====
java -jar target\api-0.0.1-SNAPSHOT.jar
popd
