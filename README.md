# java-office-demo

- 响应慢,内存消耗慢
```
-server
-Xmx5g
-Xms3g
-Xmn2g
-Xss256k
-XX:ParallelGCThreads=2
-XX:+UseParallelGC
-XX:+UseAdaptiveSizePolicy
-XX:MaxGCPauseMillis=100
-XX:+DisableExplicitGC
-XX:LargePageSizeInBytes=128m
-XX:+UseFastAccessorMethods
-Duser.timezone=GMT+8

```
- 响应快，内存消耗快
```
-server
-Xmx5g
-Xms3g
-Xmn2g
-Xss256k
-XX:ParallelGCThreads=2
-XX:+UseParNewGC
-XX:+DisableExplicitGC
-XX:+UseConcMarkSweepGC
-XX:+CMSParallelRemarkEnabled
-XX:LargePageSizeInBytes=128m
-XX:+UseFastAccessorMethods
-XX:+UseCMSInitiatingOccupancyOnly
-XX:CMSInitiatingOccupancyFraction=60
-Duser.timezone=GMT+8
```
