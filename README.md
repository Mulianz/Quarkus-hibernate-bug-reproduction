# Quarkus accesses 'this' before super() - Reproduction

> Quarkus causes a VerifyError with Java 25 flexible constructors because it adds code that uses this before the super() call.

## Environment

| | Version |
|---|---|
| OS | macOS 26 & windows 11 25H2|
| Java version | Java 27 (> 25) |
| Packages | Quarkus, Hibernate |

| Composant | Version |
|---|---|
| Apache Maven | 3.9.16 |
| Quarkus | 3.40.1 |
| Hibernate ORM | 7.4.9.Final |
| HSQLDB | 2.7.4 |

## Installation

```sh
git clone https://github.com/Mulianz/Quarkus-hibernate-bug-reproduction.git
cd Quarkus-hibernate-bug-reproduction
mvn clean verify
```

## Steps to reproduce

1. Keep the constructor with the super after the constructor body.

```java
public Todo(String title) {
        Objects.requireNonNull(title);
        if (title.isBlank() || title.length() > 255) {
            throw new IllegalArgumentException("Title must contain between 1 and 255 characters.");
        }
        this.title = title;
        super();
    }
```

2. Start the application with Quarkus in development mode.

```sh
mvn quarkus:dev
```

## Expected behavior

The server should start.

## Observed behavior

The reported startup fails with the following exception (excerpt from the original report): 

```text
2026-10-21 20:54:10,548 ERROR [io.quarkus.runtime.Quarkus] (Quarkus Main Thread) Error running Quarkus: java.lang.ExceptionInInitializerError
at java.base/jdk.internal.misc.Unsafe.allocateInstance(Native Method)
at java.base/java.lang.invoke.DirectMethodHandle.allocateInstance(DirectMethodHandle.java:503)
at io.quarkus.runtime.Quarkus.run(Quarkus.java:78)
at io.quarkus.runtime.Quarkus.run(Quarkus.java:50)
at io.quarkus.runtime.Quarkus.run(Quarkus.java:143)
at io.quarkus.runner.GeneratedMain.main(Unknown Source)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:583)
at io.quarkus.runner.bootstrap.StartupActionImpl$1.run(StartupActionImpl.java:166)
at java.base/java.lang.Thread.run(Thread.java:1527)
Caused by: java.lang.RuntimeException: Failed to start quarkus
at io.quarkus.runner.ApplicationImpl.<clinit>(Unknown Source)
... 10 more
Caused by: java.lang.VerifyError: Bad type on operand stack
Exception Details:
Location:
fr/uge/todolist/entity/Todo.<init>(Ljava/lang/String;)V @34: invokevirtual
Reason:
Type uninitializedThis (current frame, stack[0]) is not assignable to 'fr/uge/todolist/entity/Todo'
Current Frame:
bci: @34
flags: { flagThisUninit }
locals: { uninitializedThis, 'java/lang/String' }
stack: { uninitializedThis, 'java/lang/String' }
Bytecode:
0000000: 2bb8 0007 572b b600 0d9a 000d 2bb6 0013
0000010: 1100 ffa4 000d bb00 1759 1219 b700 1bbf
0000020: 2a2b b600 5b2a b700 01b1               
Stackmap Table:
same_frame(@22)
same_frame(@32)
```

## Working comparison

Moving `super()` to the beginning of the constructor allows startup, according to the original report:

```java
public Todo(String title) {
    super();
    Objects.requireNonNull(title);
    if (title.isBlank() || title.length() > 255) {
        throw new IllegalArgumentException("Title must contain between 1 and 255 characters.");
    }
    this.title = title;
}
```

Restart with `mvn clean quarkus:dev` after changing the constructor. 
Removing the explicit `super()` also places the implicit superclass invocation before the constructor body.

## Collaborator
- [Julian PLAZER-MOREAU](https://github.com/Mulianz)
- [Yasmine BEN YOUSSEF](https://github.com/yasminebyb)
