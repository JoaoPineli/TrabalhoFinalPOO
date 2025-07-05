# Instruções de Compilação e Execução

## Pré-requisito: Java JDK

Você precisa instalar o Java Development Kit (JDK) para compilar o projeto.

### Instalação do JDK no Ubuntu/Debian:

```bash
sudo apt update
sudo apt install openjdk-8-jdk
```

### Verificar instalação:

```bash
javac -version
```

## Estrutura dos Pacotes

O projeto está organizado em pacotes Java:

- `modelo` - Classes de domínio
- `observer` - Padrão Observer
- `estrategia` - Padrão Strategy
- `fabrica` - Padrão Factory
- `infra` - Infraestrutura e Singleton
- `test` - Testes

## Compilação Manual

### Opção 1: Usar o script (após instalar JDK)

```bash
./compile.sh
```

### Opção 2: Compilar manualmente

```bash
# Criar diretório de saída
mkdir -p out

# Entrar no diretório src
cd src

# Compilar todos os arquivos
javac -d ../out *.java modelo/*.java observer/*.java estrategia/*.java fabrica/*.java infra/*.java test/*.java
```

## Execução

Após compilar com sucesso:

```bash
cd out
java Main
```

## Executar Testes JUnit

Para executar os testes JUnit, você precisa das bibliotecas JUnit no classpath:

```bash
# O script compile.sh baixa automaticamente o JUnit
./compile.sh

# Executar todos os testes
cd out
java -cp .:../lib/junit-4.13.2.jar:../lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.AllTests
```

## Interface Gráfica

O projeto inclui uma interface gráfica completa em Swing:

```bash
cd out
java gui.MainWindow
```

A interface possui:

- Monitoramento visual em tempo real
- Controles para simulação
- Geração de relatórios
