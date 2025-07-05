#!/bin/bash

echo "Compilando o Sistema de Coleta de Lixo Inteligente..."

mkdir -p out
mkdir -p lib

if [ ! -f "lib/junit-4.13.2.jar" ]; then
    echo "Baixando JUnit..."
    wget -q -O lib/junit-4.13.2.jar https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar
    wget -q -O lib/hamcrest-core-1.3.jar https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
fi

cd src
echo "Compilando classes principais..."
javac -d ../out *.java modelo/*.java observer/*.java estrategia/*.java fabrica/*.java infra/*.java gui/*.java

if [ $? -eq 0 ]; then
    echo "Compilando testes JUnit..."
    javac -cp ../out:../lib/junit-4.13.2.jar:../lib/hamcrest-core-1.3.jar -d ../out test/*.java
    
    if [ $? -eq 0 ]; then
        echo "Compilação concluída com sucesso!"
        echo ""
        echo "Para executar:"
        echo "  1. Modo console: cd out && java Main"
        echo "  2. Interface gráfica: cd out && java gui.MainWindow"
        echo "  3. Testes JUnit: cd out && java -cp .:../lib/junit-4.13.2.jar:../lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.AllTests"
    else
        echo "Erro na compilação dos testes!"
    fi
else
    echo "Erro na compilação!"
fi