学习笔记

# GCAnalysis
## SerialGC
运行如下程序，生成对象次数大概是 10725
```bash
java -Xmx512m -Xms512m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis
正在执行...
[GC (Allocation Failure) [DefNew: 139776K->17472K(157248K), 0.0180645 secs] 139776K->42239K(506816K), 0.0180909 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157225K->17471K(157248K), 0.0233448 secs] 181992K->78224K(506816K), 0.0233692 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157247K->17466K(157248K), 0.0213033 secs] 218000K->120757K(506816K), 0.0213224 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157225K->17471K(157248K), 0.0211928 secs] 260516K->163034K(506816K), 0.0212138 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157247K->17471K(157248K), 0.0186785 secs] 302810K->203448K(506816K), 0.0186977 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157247K->17472K(157248K), 0.0250576 secs] 343224K->247759K(506816K), 0.0250894 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157248K->17471K(157248K), 0.0212377 secs] 387535K->287698K(506816K), 0.0212635 secs] [Times: user=0.01 sys=0.01, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 157247K->17471K(157248K), 0.0241064 secs] 427474K->335158K(506816K), 0.0241300 secs] [Times: user=0.01 sys=0.01, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 157247K->157247K(157248K), 0.0000098 secs][Tenured: 317687K->262002K(349568K), 0.0325969 secs] 474934K->262002K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0326372 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 139587K->17471K(157248K), 0.0063044 secs] 401589K->312527K(506816K), 0.0063254 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 157247K->17471K(157248K), 0.0156516 secs] 452303K->355983K(506816K), 0.0156703 secs] [Times: user=0.01 sys=0.01, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 157247K->157247K(157248K), 0.0000131 secs][Tenured: 338511K->299719K(349568K), 0.0346797 secs] 495759K->299719K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0347236 secs] [Times: user=0.04 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 139776K->17471K(157248K), 0.0058011 secs] 439495K->345765K(506816K), 0.0058219 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 157247K->157247K(157248K), 0.0000548 secs][Tenured: 328293K->322088K(349568K), 0.0410701 secs] 485541K->322088K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.1125714 secs] [Times: user=0.04 sys=0.00, real=0.11 secs]
[GC (Allocation Failure) [DefNew: 139732K->139732K(157248K), 0.0000132 secs][Tenured: 322088K->315461K(349568K), 0.0403506 secs] 461821K->315461K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0404020 secs] [Times: user=0.04 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0000121 secs][Tenured: 315461K->349525K(349568K), 0.0313607 secs] 455237K->352765K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0314067 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349525K->348025K(349568K), 0.0352144 secs] 506723K->348025K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0352438 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0000133 secs][Tenured: 348025K->349502K(349568K), 0.0390210 secs] 487801K->357842K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0390705 secs] [Times: user=0.04 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349502K->343186K(349568K), 0.0429275 secs] 506727K->343186K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0429544 secs] [Times: user=0.04 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0000115 secs][Tenured: 343186K->349257K(349568K), 0.0267333 secs] 482962K->365212K(506816K), [Metaspace: 2706K->2706K(1056768K)], 0.0267751 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
执行结束!共生成对象次数:10725
Heap
 def new generation   total 157248K, used 74154K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,  53% used [0x00000007a0000000, 0x00000007a486a848, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
  to   space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
 tenured generation   total 349568K, used 349257K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 349568K,  99% used [0x00000007aaaa0000, 0x00000007bffb2400, 0x00000007bffb2400, 0x00000007c0000000)
 Metaspace       used 2712K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 295K, capacity 386K, committed 512K, reserved 1048576K
```

## ParallelGC
生成对象的次数大概是7002次
```bash
java -Xmx512m -Xms512m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis
正在执行...
[GC (Allocation Failure) [PSYoungGen: 131584K->21499K(153088K)] 131584K->42811K(502784K), 0.0157980 secs] [Times: user=0.02 sys=0.10, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 152889K->21495K(153088K)] 174201K->90584K(502784K), 0.0227009 secs] [Times: user=0.02 sys=0.15, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 153079K->21500K(153088K)] 222168K->131980K(502784K), 0.0171213 secs] [Times: user=0.04 sys=0.10, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 153084K->21501K(153088K)] 263564K->176499K(502784K), 0.0186172 secs] [Times: user=0.05 sys=0.11, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 153085K->21503K(153088K)] 308083K->222097K(502784K), 0.0205467 secs] [Times: user=0.04 sys=0.11, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 153087K->21492K(80384K)] 353681K->256872K(430080K), 0.0152463 secs] [Times: user=0.04 sys=0.09, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 80372K->34557K(116736K)] 315752K->274172K(466432K), 0.0043937 secs] [Times: user=0.03 sys=0.01, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 93435K->47366K(116736K)] 333050K->291829K(466432K), 0.0073764 secs] [Times: user=0.04 sys=0.01, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 106246K->55811K(116736K)] 350709K->310182K(466432K), 0.0094706 secs] [Times: user=0.06 sys=0.02, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 114363K->40227K(116736K)] 368734K->328227K(466432K), 0.0177333 secs] [Times: user=0.04 sys=0.10, real=0.02 secs]
[Full GC (Ergonomics) [PSYoungGen: 40227K->0K(116736K)] [ParOldGen: 287999K->226801K(349696K)] 328227K->226801K(466432K), [Metaspace: 2706K->2706K(1056768K)], 0.0302869 secs] [Times: user=0.25 sys=0.01, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->19348K(116736K)] 285681K->246150K(466432K), 0.0025838 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 77672K->16624K(116736K)] 304473K->261243K(466432K), 0.0039599 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 75504K->21634K(116736K)] 320123K->281878K(466432K), 0.0046040 secs] [Times: user=0.04 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 80343K->18076K(116736K)] 340588K->299808K(466432K), 0.0041856 secs] [Times: user=0.04 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 76744K->23090K(116736K)] 358476K->322292K(466432K), 0.0073691 secs] [Times: user=0.03 sys=0.03, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 23090K->0K(116736K)] [ParOldGen: 299201K->266762K(349696K)] 322292K->266762K(466432K), [Metaspace: 2706K->2706K(1056768K)], 0.0283539 secs] [Times: user=0.25 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->20532K(116736K)] 325642K->287294K(466432K), 0.0026912 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 79329K->22740K(116736K)] 346091K->307935K(466432K), 0.2221004 secs] [Times: user=0.04 sys=0.00, real=0.22 secs]
[GC (Allocation Failure) [PSYoungGen: 81620K->19462K(116736K)] 366815K->324960K(466432K), 0.0062664 secs] [Times: user=0.04 sys=0.02, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 19462K->0K(116736K)] [ParOldGen: 305497K->278249K(349696K)] 324960K->278249K(466432K), [Metaspace: 2706K->2706K(1056768K)], 0.0327999 secs] [Times: user=0.29 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->17808K(116736K)] 337129K->296057K(466432K), 0.0025376 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 76688K->23061K(116736K)] 354937K->317700K(466432K), 0.0043595 secs] [Times: user=0.04 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 81941K->16089K(116736K)] 376580K->332947K(466432K), 0.0075721 secs] [Times: user=0.03 sys=0.03, real=0.00 secs]
[Full GC (Ergonomics) [PSYoungGen: 16089K->0K(116736K)] [ParOldGen: 316857K->290215K(349696K)] 332947K->290215K(466432K), [Metaspace: 2706K->2706K(1056768K)], 0.0299193 secs] [Times: user=0.27 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->22717K(116736K)] 349095K->312933K(466432K), 0.0029714 secs] [Times: user=0.02 sys=0.01, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 81597K->22159K(116736K)] 371813K->334308K(466432K), 0.0048751 secs] [Times: user=0.05 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 80735K->20644K(116736K)] 392884K->353328K(466432K), 0.0091158 secs] [Times: user=0.03 sys=0.04, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 20644K->0K(116736K)] [ParOldGen: 332684K->303434K(349696K)] 353328K->303434K(466432K), [Metaspace: 2706K->2706K(1056768K)], 0.0312859 secs] [Times: user=0.29 sys=0.00, real=0.03 secs]
执行结束!共生成对象次数:7002
Heap
 PSYoungGen      total 116736K, used 2721K [0x00000007b5580000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 58880K, 4% used [0x00000007b5580000,0x00000007b58284d0,0x00000007b8f00000)
  from space 57856K, 0% used [0x00000007bc780000,0x00000007bc780000,0x00000007c0000000)
  to   space 57856K, 0% used [0x00000007b8f00000,0x00000007b8f00000,0x00000007bc780000)
 ParOldGen       total 349696K, used 303434K [0x00000007a0000000, 0x00000007b5580000, 0x00000007b5580000)
  object space 349696K, 86% used [0x00000007a0000000,0x00000007b28528d0,0x00000007b5580000)
 Metaspace       used 2712K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 295K, capacity 386K, committed 512K, reserved 1048576K
```