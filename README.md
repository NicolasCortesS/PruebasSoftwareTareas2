## Comando para ejecutar tarea

```bash
mvn compile
mvn exec:java "-Dexec.mainClass=cl.ucn.estacionamiento.Main" -q
```

## Comando para correr los tests

```bash
mvn clean test
```

## Ver Cobertura con JaCoCo

```bash
start target\site\jacoco\index.html
```
<img width="1357" height="233" alt="Captura de pantalla 2025-12-11 023852" src="https://github.com/user-attachments/assets/59243fda-72ed-44a3-b456-c7d514109fe9" />

## Diagrama de Clases

<img width="748" height="853" alt="image" src="https://github.com/user-attachments/assets/f2b245df-1ab5-4dfa-ad65-2ce49ecb385e" />

## Ejemplo de Salida de Tests

```
[INFO] Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
```

## ¿Qué tipo de cobertura he medido y por qué?

Medí cobertura de líneas y cobertura de ramas con JaCoCo. La de líneas me dice qué porcentaje del código se ejecutó durante los tests, y la de ramas me asegura que pasé por todas las condiciones if/else. Usé ambas porque juntas me dan una idea más completa de qué tan bien están testeadas las distintas situaciones del programa.

## Licencia

MIT License - Copyright (c) 2025
