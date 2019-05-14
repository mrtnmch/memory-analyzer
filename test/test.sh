#! /bin/bash
declare -a sizes=(10 20 30 40 50 60 70 80 90 100 150 200)

for i in ${sizes[@]}
do
	for run in {1..3}
	do
		echo Running test set $i, \#$run
		java -jar ../app/target/app-1.0-SNAPSHOT.jar -p ../sandbox/data/test-heapdump-$i.hprof -n cz.mxmx.memoryanalyzer.example -f > $i-$run.txt
	done
done

