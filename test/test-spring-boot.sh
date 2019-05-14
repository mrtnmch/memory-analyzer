#! /bin/bash
declare -a sizes=("org" "org.springframework" "org.springframework.boot" "org.springframework.core" "org.springframework.web" "org.springframework.boot.web")

for i in ${sizes[@]}
do
	for run in {1..3}
	do
		echo Running test namespace $i, \#$run
		java -jar ../app/target/app-1.0-SNAPSHOT.jar -p ../sandbox/data/heapdump-spring-boot-example.hprof -n $i -f > $i-$run.txt
	done
done

