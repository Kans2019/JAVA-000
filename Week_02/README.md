# GCAnalysis

## SerialGC
* [SerialGC 512m 运行结果](./SerialGC_512.log)   执行结束!共生成对象次数:9508
* [SerialGC 1024m 运行结果](./SerialGC_1024.log) 执行结束!共生成对象次数:10517
* [SerialGC 2048m 运行结果](./SerialGC_2048.log) 执行结束!共生成对象次数:10577
* [SerialGC 4096m 运行结果](./SerialGC_4096.log) 执行结束!共生成对象次数:9168

## ParallelGC
* [ParallelGC 512m 运行结果](./ParallelGC_512.log)   执行结束!共生成对象次数:8230
* [ParallelGC 1024m 运行结果](./ParallelGC_1024.log) 执行结束!共生成对象次数:15146
* [ParallelGC 2048m 运行结果](./ParallelGC_2048.log) 执行结束!共生成对象次数:12446
* [ParallelGC 4096m 运行结果](./ParallelGC_4096.log) 执行结束!共生成对象次数:13297

## ConcMarkSweepGC
* [ConcMarkSweepGCGC 512m 运行结果](./ConcMarkSweepGCGC_512.log)   执行结束!共生成对象次数:10745
* [ConcMarkSweepGCGC 1024m 运行结果](./ConcMarkSweepGCGC_1024.log) 执行结束!共生成对象次数:12656
* [ConcMarkSweepGCGC 2048m 运行结果](./ConcMarkSweepGCGC_2048.log) 执行结束!共生成对象次数:13662
* [ConcMarkSweepGCGC 4096m 运行结果](./ConcMarkSweepGCGC_4096.log) 执行结束!共生成对象次数:17178

## Garbage First GC
* [G1GC 512m 运行结果](./G1GC_512.log)   执行结束!共生成对象次数:11156
* [G1GC 1024m 运行结果](./G1GC_1024.log) 执行结束!共生成对象次数:14297
* [G1GC 2048m 运行结果](./G1GC_2048.log) 执行结束!共生成对象次数:13056
* [G1GC 4096m 运行结果](./G1GC_4096.log) 执行结束!共生成对象次数:13040

**综上所述，SerialGC在内存较小的时候比 ParallelGC 更有优势，ParallelGC在1g左右内存较有优势，而在更大内存的时候由于需要管理的空间增加，ParallelGC和SerialGC的性能会下降**
**而CMS和G1这样的现代化GC, 性能与内存呈现一定的正相关的关系**
