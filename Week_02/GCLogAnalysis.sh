#!/bin/sh

PROJECT_PATH=$(cd `dirname $0`; pwd)

cd $PROJECT_PATH
javac -encoding GBK GCLogAnalysis.java

# SerialGC
java -Xmx512m -Xms512m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis > SerialGC_512.log
java -Xmx1024m -Xms1024m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis > SerialGC_1024.log
java -Xmx2048m -Xms2048m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis > SerialGC_2048.log
java -Xmx4096m -Xms4096m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis > SerialGC_4096.log

# ParallelGC
java -Xmx512m -Xms512m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis > ParallelGC_512.log
java -Xmx1024m -Xms1024m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis > ParallelGC_1024.log
java -Xmx2048m -Xms2048m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis > ParallelGC_2048.log
java -Xmx4096m -Xms4096m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis > ParallelGC_4096.log

# ConcMarkSweepGC
java -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis > ConcMarkSweepGC_512.log
java -Xmx1024m -Xms1024m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis > ConcMarkSweepGC_1024.log
java -Xmx2048m -Xms2048m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis > ConcMarkSweepGC_2048.log
java -Xmx4096m -Xms4096m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis > ConcMarkSweepGC_4096.log

# Garbage First GC
java -Xmx512m -Xms512m -XX:+UseG1GC -XX:+PrintGCDetails GCLogAnalysis > G1GC_512.log
java -Xmx1024m -Xms1024m -XX:+UseG1GC -XX:+PrintGCDetails GCLogAnalysis > G1GC_1024.log
java -Xmx2048m -Xms2048m -XX:+UseG1GC -XX:+PrintGCDetails GCLogAnalysis > G1GC_2048.log
java -Xmx4096m -Xms4096m -XX:+UseG1GC -XX:+PrintGCDetails GCLogAnalysis > G1GC_4096.log
