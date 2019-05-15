# Memory analyzer

## Modules

- analyzer
- app
- sandbox

## Module `analyzer`

Contains the created dump analyzing library. There's an integration test that expects the `sandbox/data/test-heapdump-10.hprof` file to exists as a part of the test suite.

## Module `app`

Contains the app that interacts with the user. The app is CLI and contains the following parameters:

- `-p` / `--path` - Path to the source HPROF file to analyze (required).
- `-l` / `--list` - If specified, the "list namespaces" action will be performed.
- `-n` / `--namespace` - If specified, the "waste analysis" action will be performed. As a parameter it expects the namespace to filter. e.g. `com.example`.
- `-f` / `--fields` - Add field values to the found instances (works only with `-n`).
- `-c` / `--csv` - Prints the results also to the `results.csv` file, along with the console output (only works with `-n`).
- `-h` / `--help` - Help.

## Module `sandbox`

Contains source test data. In `data`, there is several generated HPROF files -- all files beginning with `test-heapdump` come from the laboratory app, the other one from the Spring Boot app. In `example-app`, there's a source code for the laboratory app I created for purpose of testing. In `spring-boot-example`, there's a source code for Spring Boot Hello World app.

## Directory `test`

In this directory, there's the output of the test suite I run for the document. They contain redirected output from the console. They were run via the attached Bash files - `test.sh` runs the analysis of the `test-heapdump-*` files (all of them) I mentioned above. The `test-spring-boot.sh` runs the Spring Boot file analysis (for different namespaces). The test suite is run 3 times for all the commands and the results are given a suffix with the number of run.

## Examples

All examples expect the CWD to be the root of the project (`memory-analyzer`).

### Compile

```bash
mvn clean package
```

### List the namespaces

```bash
java -jar app/target/app-1.0-SNAPSHOT.jar -p sandbox/data/test-heapdump-10.hprof -l
```

### Run the analysis

```bash
java -jar app/target/app-1.0-SNAPSHOT.jar -p sandbox/data/test-heapdump-10.hprof -n cz.mxmx.memoryanalyzer.example
```

### Run the analysis and print out the fields

```bash
java -jar app/target/app-1.0-SNAPSHOT.jar -p sandbox/data/test-heapdump-10.hprof -n cz.mxmx.memoryanalyzer.example -f
```

### Run the analysis, print out the fields and produce CSV output

```bash
java -jar app/target/app-1.0-SNAPSHOT.jar -p sandbox/data/test-heapdump-10.hprof -n cz.mxmx.memoryanalyzer.example -f -c
```