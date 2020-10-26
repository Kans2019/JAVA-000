# GCAnalysis

## SerialGC
* [SerialGC 512m 运行结果](./SerialGC_512.log)   执行结束!共生成对象次数:9508
* [SerialGC 1024m 运行结果](./SerialGC_1024.log) 执行结束!共生成对象次数:10517
* [SerialGC 2048m 运行结果](./SerialGC_2048.log) 执行结束!共生成对象次数:10577
* [SerialGC 4096m 运行结果](./SerialGC_4096.log) 执行结束!共生成对象次数:9168

## ParallelGC
* [ParallelGC 512m 运行结果](./Parallel_512.log)   执行结束!共生成对象次数:8230
* [ParallelGC 1024m 运行结果](./Parallel_1024.log) 执行结束!共生成对象次数:15146
* [ParallelGC 2048m 运行结果](./Parallel_2048.log) 执行结束!共生成对象次数:12446
* [ParallelGC 4096m 运行结果](./Parallel_4096.log) 执行结束!共生成对象次数:13297

## ConcMarkSweepGC
* [ConcMarkSweepGC 512m 运行结果](./ConcMarkSweepGCGC_512.log)   执行结束!共生成对象次数:10745
* [ConcMarkSweepGC 1024m 运行结果](./ConcMarkSweepGCGC_1024.log) 执行结束!共生成对象次数:12656
* [ConcMarkSweepGC 2048m 运行结果](./ConcMarkSweepGCGC_2048.log) 执行结束!共生成对象次数:13662
* [ConcMarkSweepGC 4096m 运行结果](./ConcMarkSweepGCGC_4096.log) 执行结束!共生成对象次数:17178

## Garbage First GC
* [G1GC 512m 运行结果](./G1GC_512.log)   执行结束!共生成对象次数:11156
* [G1GC 1024m 运行结果](./G1GC_1024.log) 执行结束!共生成对象次数:14297
* [G1GC 2048m 运行结果](./G1GC_2048.log) 执行结束!共生成对象次数:13056
* [G1GC 4096m 运行结果](./G1GC_4096.log) 执行结束!共生成对象次数:13040

**综上所述，SerialGC在内存较小的时候比 ParallelGC 更有优势，ParallelGC在1g左右内存较有优势，而在更大内存的时候由于需要管理的空间增加，ParallelGC和SerialGC的性能会下降**
**而CMSGC和G1这样的现代化GC, 性能与内存呈现一定的正相关的关系**


# gateway-server
## SerialGC
###  堆内存为512m
```markdown
RPS: 902 (requests/second)
Max: 616ms
Min: 2ms
Avg: 21ms

  50%   below 27ms
  60%   below 28ms
  70%   below 30ms
  80%   below 31ms
  90%   below 33ms
  95%   below 36ms
  98%   below 44ms
  99%   below 52ms
99.9%   below 102ms
```

### 堆内存为1024m
```markdown
RPS: 842.1 (requests/second)
Max: 303ms
Min: 2ms
Avg: 22.6ms

  50%   below 25ms
  60%   below 29ms
  70%   below 31ms
  80%   below 34ms
  90%   below 39ms
  95%   below 45ms
  98%   below 55ms
  99%   below 65ms
99.9%   below 183ms
```

### 堆内存为2048m
```markdown
RPS: 830.7 (requests/second)
Max: 181ms
Min: 2ms
Avg: 22.9ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 36ms
  90%   below 44ms
  95%   below 50ms
  98%   below 55ms
  99%   below 60ms
99.9%   below 84ms
```

### 堆内存4096m
```markdown
RPS: 835 (requests/second)
Max: 126ms
Min: 2ms
Avg: 22.8ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 49ms
  98%   below 55ms
  99%   below 60ms
99.9%   below 84ms
```

## ParallelGC
###  堆内存为512m
```markdown
RPS: 822 (requests/second)
Max: 243ms
Min: 2ms
Avg: 23.1ms

  50%   below 27ms
  60%   below 30ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 50ms
  98%   below 56ms
  99%   below 60ms
99.9%   below 88ms
```

### 堆内存为1024m
```markdown
RPS: 817.9 (requests/second)
Max: 161ms
Min: 2ms
Avg: 23.2ms

  50%   below 27ms
  60%   below 29ms
  70%   below 32ms
  80%   below 36ms
  90%   below 44ms
  95%   below 50ms
  98%   below 56ms
  99%   below 61ms
99.9%   below 92ms
```

### 堆内存为2048m
```markdown
RPS: 833.8 (requests/second)
Max: 159ms
Min: 2ms
Avg: 22.8ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 49ms
  98%   below 55ms
  99%   below 59ms
99.9%   below 87ms
```

### 堆内存4096m
```markdown
RPS: 813.4 (requests/second)
Max: 204ms
Min: 2ms
Avg: 23.4ms

  50%   below 23ms
  60%   below 29ms
  70%   below 32ms
  80%   below 37ms
  90%   below 46ms
  95%   below 53ms
  98%   below 64ms
  99%   below 74ms
99.9%   below 153ms
```

## ConcMarkSweepGC
###  堆内存为512m
```markdown
RPS: 823.4 (requests/second)
Max: 198ms
Min: 2ms
Avg: 23.1ms

  50%   below 26ms
  60%   below 30ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 49ms
  98%   below 55ms
  99%   below 60ms
99.9%   below 99ms
```

### 堆内存为1024m
```markdown
RPS: 842.9 (requests/second)
Max: 128ms
Min: 2ms
Avg: 22.5ms

  50%   below 24ms
  60%   below 29ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 49ms
  98%   below 54ms
  99%   below 60ms
99.9%   below 83ms
```

### 堆内存为2048m
```markdown
RPS: 828.6 (requests/second)
Max: 201ms
Min: 2ms
Avg: 22.9ms

  50%   below 26ms
  60%   below 30ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 49ms
  98%   below 55ms
  99%   below 59ms
99.9%   below 85ms
```

### 堆内存4096m
```markdown
RPS: 839.9 (requests/second)
Max: 142ms
Min: 2ms
Avg: 22.6ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 35ms
  90%   below 42ms
  95%   below 48ms
  98%   below 54ms
  99%   below 58ms
99.9%   below 83ms
```

## Garbage First GC
###  堆内存为512m
```markdown
RPS: 831.1 (requests/second)
Max: 209ms
Min: 2ms
Avg: 22.9ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 36ms
  90%   below 43ms
  95%   below 49ms
  98%   below 55ms
  99%   below 58ms
99.9%   below 88ms
```

### 堆内存为1024m
```markdown
RPS: 833.4 (requests/second)
Max: 182ms
Min: 2ms
Avg: 22.8ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 35ms
  90%   below 42ms
  95%   below 49ms
  98%   below 55ms
  99%   below 59ms
99.9%   below 86ms
```

### 堆内存为2048m
```markdown
RPS: 837.7 (requests/second)
Max: 135ms
Min: 1ms
Avg: 22.7ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 35ms
  90%   below 41ms
  95%   below 48ms
  98%   below 53ms
  99%   below 58ms
99.9%   below 86ms
```

### 堆内存4096m
```markdown
RPS: 837.8 (requests/second)
Max: 116ms
Min: 2ms
Avg: 22.7ms

  50%   below 26ms
  60%   below 29ms
  70%   below 32ms
  80%   below 35ms
  90%   below 42ms
  95%   below 48ms
  98%   below 54ms
  99%   below 57ms
99.9%   below 82ms
```

**综上所述 , 在内存较低时,SerialGC和ParallelGC表现较为优异，而CMS和G1在大内存表现较为优秀。**
**内存较大时，CMS和G1在延时和吞吐量上明显优于SerialGC和ParallelGC, 在内存较小时,4种垃圾收集表现差不多(SerialGC和ParallelGC的表现略优于CMS和G1)**
