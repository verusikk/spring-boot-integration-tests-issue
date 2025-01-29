# Issue description
Each test overwrites the value of `spring.data.mongodb.database` with the test class name substring, so it is expected that:
* a new context is created
* the value is used everywhere in beans (and as a results tests don't conflict on data)

Then each test put the overwritten value into spring batch parameters, and start a job that checks that value in parameters matches to the current context value.

When run tests in parallel some tests failed because the value of `spring.data.mongodb.database` is unexpected and equal to the value of another test.

# Run the integration tests in parallel to reproduce the issue
```
./gradlew :integration-tests:test
```
Review results:
* `allure serve integration-tests/allure-results`
* Or `cat integration-tests/build/logback-reports/com.azul.avd.kbs.app.integration.tests.ManualMtrTests.log` (change file name if needed)
When the issue is reproduced the logs will contain a message like this with "Expected db name: ..., but got: ...":
```
java.lang.AssertionError: [Execution: JobExecution: id=1, version=2, startTime=2025-01-29T13:16:07.448553, endTime=2025-01-29T13:16:07.456437, lastUpdated=2025-01-29T13:16:07.456616, status=FAILED, exitStatus=exitCode=FAILED;exitDescription=org.springframework.batch.core.JobExecutionException: Flow execution ended unexpectedly
	at org.springframework.batch.core.job.flow.FlowJob.doExecute(FlowJob.java:138)
	at org.springframework.batch.core.job.AbstractJob.execute(AbstractJob.java:307)
	at org.springframework.batch.core.launch.support.TaskExecutorJobLauncher$1.run(TaskExecutorJobLauncher.java:155)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: org.springframework.batch.core.job.flow.FlowExecutionException: Ended flow=DataJob at state=DataJob.decision0 with exception
	at org.springframework.batch.core.job.flow.support.SimpleFlow.resume(SimpleFlow.java:175)
	at org.springframework.batch.core.job.flow.support.SimpleFlow.start(SimpleFlow.java:140)
	at org.springframework.batch.core.job.flow.FlowJob.doExecute(FlowJob.java:132)
	... 9 more
Caused by: java.lang.IllegalStateException: Expected db name: db_ManualMtrTests, but got: app_DataFlowTests
	at com.azul.avd.kbs.app.processing.data.DataUpdateDecider.decide(DataUpdateDecider.kt:22)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:359)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
	at org.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:137)
	at org.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:124)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.aop.framework.C, job=[JobInstance: id=1, version=0, Job=[DataJob]], jobParameters=[{'dataName':'{value=ManualMtrTestsName1, type=class java.lang.String, identifying=true}','startDate':'{value=2025-01-29T13:16:07.409755, type=class java.time.LocalDateTime, identifying=true}','dbName':'{value=db_ManualMtrTests, type=class java.lang.String, identifying=true}'}], steps: [])] 
Expecting actual:
  FAILED
to be in:
  [COMPLETED]
```
# Run the integration tests one by one - not reproducible
```
./gradlew -Pjunit.jupiter.execution.parallel.enabled=false :integration-tests:test
```