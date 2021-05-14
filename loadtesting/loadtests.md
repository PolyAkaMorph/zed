SQL select * from user where name like 'abc%' and surname like 'cde%';

First step, tests w\o index.
Explain plan - Table scan on user, cost = 100k. 

1 thread, ~1.5s timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/latency/1_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/throughput/1_thread.png?raw=true)

10 threads, ~5s timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/latency/10_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/throughput/10_thread.png?raw=true)

100 threads, ~25s timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/latency/100_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/throughput/100_thread.png?raw=true)

1000 threads, ~120s timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/latency/1000_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/throughput/1000_thread.png?raw=true)

With 1000 threads things go bad and there are some 500 and 403 errors:
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/no_index/1000_thread_403_500.png?raw=true)


Second step, adding index. 

create index user_name_idx on user (name, surname);

create index user_surname_idx on user (surname,name);

I have to use two indexes instead of one composite or two single indexes because of various SQL's. If I make two single indexes - only one of them will work, so plan will be worse. If I make only one composite index - it will be useless if SQL have only one field.

Explain plan with index - Index range scan on use, cost = 2.


1 thread, ~2ms timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/latency/1_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/throughput/1_thread.png?raw=true)

10 threads, ~20ms timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/latency/10_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/throughput/10_thread.png?raw=true)

100 threads, ~600ms timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/latency/100_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/throughput/100_thread.png?raw=true)

1000 threads, ~8s timeout
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/latency/1000_thread.png?raw=true)
![alt text](https://github.com/PolyAkaMorph/zed/blob/main/loadtesting/+index/throughput/1000_thread.png?raw=true)
