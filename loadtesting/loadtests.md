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

We have two fields, name and surname, lets check theirs selectivity:

select count(distinct name) from user; --102

select count(distinct surname) from user; --168957

Surname is much more selective, so it will be in first order:

create index user_surname_name_idx on user (surname, name);

Explain plan with index - Index range scan on use, cost = 2.

There is no need in index user_name_idx, which will handle situation with surname=null, because both fields are required. 



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
