@echo off
echo Compilando o Sistema de Coleta de Lixo Inteligente...

REM Criar diretorio de saida se nao existir
if not exist out mkdir out

REM Compilar todos os arquivos Java
cd src
javac -d ../out *.java modelo/*.java observer/*.java estrategia/*.java fabrica/*.java infra/*.java

if %errorlevel% equ 0 (
    echo.
    echo Compilacao concluida com sucesso!
    echo.
    echo Para executar o programa:
    echo   cd out
    echo   java Main
) else (
    echo.
    echo Erro na compilacao!
)

cd ..
pause