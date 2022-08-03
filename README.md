# swe challenge

- [swe challenge](#swe-challenge)
  - [disclaimer](#disclaimer)
  - [installation and run](#installation-and-run)
    - [system configuration](#system-configuration)
    - [to run](#to-run)
    - [api documention](#api-documention)
    - [questions](#questions)
    - [getchas](#getchas)
    - [better design philisophy](#better-design-philisophy)
    - [discrepency](#discrepency)

## disclaimer

as previously mentioned, i have no prior knowledge of the springboot framework. this is my best attempt within this period. i would be omitting test from the challenge as it is out of my capacity within this short period of time and would rather explain to you the procedure

## installation and run

### system configuration


```bash
>mvn --version

Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
Maven home: /usr/local/Cellar/maven/3.8.6/libexec
Java version: 18.0.2, vendor: Homebrew, runtime: /usr/local/Cellar/openjdk/18.0.2/libexec/openjdk.jdk/Contents/Home
Default locale: en_GB, platform encoding: UTF-8
OS name: "mac os x", version: "12.4", arch: "x86_64", family: "mac"
```

### to run
```bash
mvn spring-boot:run
```

data would be seeded from data.sql and closing the program will wipe out the initalization. it is running in mem.

to persis change settings to path directory for spring.datasource.url.

### api documention

you can chain argument on api to fliter the details

for example 

- min 2500
- max 3500
- sort salary
- limit 2
- offset 1

```bash
curl -v localhost:8080/users\?\&min=2500\&max=3500\&sort=salary\&limit=2\&offset=1 | jsonpp
```

if no parameters are passed, default values would be taken as mentioned in the assignment

for post, i suggest using insomnia/postman to upload file to `http://localhost:8080/upload`, curl command may not work. (did not work on my machine

uploading testfail.csv should not upload any result from that csv.

### questions

what kind of testing would you perform on the application in addition to unit testing?
A: integration test should be done for the file upload. simulate upload csv file with different configuration and expect certain results. load test should also be done by simulating concurrent upload to endpoint to see the load.

how would you scale up the system to handle potentially millions of files?
A: the api service should be horizontally scaled up. you can run in it kubelets or docker swarm mode and scale it depending on load. add a load balancer infront to direct traffic. Alternatively you can use cloud services and run it as serverless function which has inbuild scaling support.

csv files can be very large and take a long time to process. this can be a problem using HTTP POST calls.
How would you update the design to handle HTTP POST requests that can potentially take a long time to
execute?
A: ideally there should be a limit set on the file size, else to speed up the process, the backend can shard the file and distribute the load to multiple workers to process. 

### getchas

- values with more decimal points would be rounded. (should it be allowed?)
- names ideally should be pre-formatted before inserted into db. currently it is not formatted and taken as new entry.

### better design philisophy

i would have prefered to have 2 tables and 2 db where the 2 tables are latest record and history record. 2 db where one is a read replica of the written db.

get request to look up the data would read from the read replica of the latest record. this would be a lot faster thought not up to date if the replica is set as asynchronous replication. 

the api would then write to both tables where all records are written to history table with time stamp, updates latest table if name exist else insert into table.

### discrepency

it was mentioned that api return should include error code, however /upload mentioned that it should return "success":0 