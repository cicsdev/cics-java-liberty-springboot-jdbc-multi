# cics-java-liberty-springboot-jdbc-multi

[![Build](https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi/actions/workflows/build.yaml/badge.svg)](https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi/actions/workflows/build.yaml)
[![License](https://img.shields.io/badge/License-EPL%202.0-green.svg)](https://www.eclipse.org/legal/epl-2.0/)

## Overview

This sample demonstrates a Spring Boot JDBC application that connects to IBM Db2 for z/OS using **two simultaneous DataSources** — one using JDBC type 2 (native z/OS) connectivity, and one using JDBC type 4 (TCP/IP) connectivity. The application is deployed to a CICS Liberty JVM server and uses the Db2 employee sample table (`EMP`).

**Key Features:**
- Multiple Spring `DataSource` beans wired via JNDI from Liberty `server.xml`
- REST endpoints routed by connection type (`/type2/...` and `/type4/...`)
- Global (XA) transaction support via Spring `@Transactional` — coordinates CICS UOW with Db2
- Full CRUD operations: list, add, update, delete employees
- Supports CICS Bundle Plugin deployment (Gradle and Maven) and direct WAR deployment

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Downloading](#downloading)
3. [Check dependencies](#check-dependencies)
4. [Building the Sample](#building-the-sample)
5. [Deploying to a CICS Liberty JVM server](#deploying-to-a-cics-liberty-jvm-server)
6. [Running the Sample](#running-the-sample)
7. [Troubleshooting](#troubleshooting)
8. [License](#license)
9. [Additional Resources](#additional-resources)
10. [Contributing](#contributing)

## Prerequisites

- CICS TS V6.1 or later
- A configured Liberty JVM server in CICS
- Java SE 17 or later on the workstation
- IBM Db2 V12 or later on z/OS with the `EMP` sample table
- A CICS DB2CONN resource installed and connected (for type 2 connectivity)
- Either Gradle or Apache Maven on the workstation (optional — wrappers are provided)
- Eclipse with IBM CICS Explorer SDK (optional — for Eclipse import and bundle export)

## Downloading

Clone the repository using your IDE's Git support, or download as a ZIP:

```shell
git clone https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi.git
```

> **Tip:** Eclipse Git provides an **Import existing Projects** checkbox when cloning a repository.

## Check dependencies

Add the following features to your Liberty `server.xml`:

```xml
<featureManager>
    <feature>servlet-6.0</feature>
    <feature>jdbc-4.3</feature>
    <feature>transaction-1.2</feature>
    <feature>cicsts:core-1.0</feature>
    <feature>cicsts:security-1.0</feature>
</featureManager>
```

Add two `dataSource` definitions — one for type 2 and one for type 4 connectivity. A sample `server.xml` is provided in [`etc/config/liberty/server.xml`](etc/config/liberty/server.xml). Substitute your Db2 connection details before deploying.

## Building the Sample

You can build using Gradle or Maven from the command line, or using Eclipse.

### Gradle Wrapper

```shell
./gradlew clean build
```

### Maven Wrapper

```shell
./mvnw clean verify
```

The WAR file is produced at:
- Gradle: `cics-java-liberty-springboot-jdbc-multi-app/build/libs/cics-java-liberty-springboot-jdbc-multi.war`
- Maven: `cics-java-liberty-springboot-jdbc-multi-app/target/cics-java-liberty-springboot-jdbc-multi.war`

### Eclipse import

1. **Git Repositories** view → right-click Working Tree → **Import Projects** (imports root)
2. Switch to **Java EE** perspective
3. Right-click `cics-java-liberty-springboot-jdbc-multi-app` → **Import Projects**
4. Right-click `cics-java-liberty-springboot-jdbc-multi-cicsbundle` → **Import Projects**
5. Right-click `cics-java-liberty-springboot-jdbc-multi-cicsbundle-eclipse` → **Import Projects**
6. Right-click root project → **Gradle → Refresh Gradle Project** (or **Maven → Update Project**)

> **Note:** If you see compile errors after import, run a Gradle Refresh or Maven Update — this resolves the Spring Boot dependencies on the classpath.

## Deploying to a CICS Liberty JVM server

### CICS Bundle Plugin Deployment (Gradle/Maven)

Build the CICS bundle ZIP using the CICS Bundle Plugin:

```shell
./gradlew clean build
./mvnw clean verify
```

The bundle ZIP is produced at:
- Gradle: `cics-java-liberty-springboot-jdbc-multi-cicsbundle/build/distributions/cics-java-liberty-springboot-jdbc-multi-cicsbundle-1.0.0.zip`
- Maven: `cics-java-liberty-springboot-jdbc-multi-cicsbundle/target/cics-java-liberty-springboot-jdbc-multi-cicsbundle-1.0.0.zip`

Upload the ZIP to zFS and define a CICS BUNDLE resource pointing to the extracted directory.

### CICS Explorer SDK Deployment

1. In Eclipse, import all projects as described in [Building the Sample](#building-the-sample)
2. Right-click `cics-java-liberty-springboot-jdbc-multi-cicsbundle-eclipse` → **Export Bundle Project to z/OS UNIX File System**
3. Follow the wizard to transfer the bundle to your z/OS system
4. Install and enable the CICS BUNDLE resource

### Direct Liberty Application Deployment

1. Build the WAR using Gradle or Maven as above
2. Upload `cics-java-liberty-springboot-jdbc-multi.war` to a directory on zFS (e.g. `${server.config.dir}/springapps/`)
3. Add the following `<application>` element to your Liberty `server.xml`:

```xml
<application id="cics-java-liberty-springboot-jdbc-multi"
             location="${server.config.dir}/springapps/cics-java-liberty-springboot-jdbc-multi.war"
             name="cics-java-liberty-springboot-jdbc-multi"
             type="war">
    <application-bnd>
        <security-role name="cicsAllAuthenticated">
            <special-subject type="ALL_AUTHENTICATED_USERS"/>
        </security-role>
    </application-bnd>
</application>
```

4. Also add the two `dataSource` definitions from [`etc/config/liberty/server.xml`](etc/config/liberty/server.xml) with your Db2 connection details

## Running the Sample

Verify the application started successfully by checking for message `CWWKT0016I` in `messages.log`:

```
CWWKT0016I: Web application available (default_host): http://myzos.mycompany.com:httpPort/cics-java-liberty-springboot-jdbc-multi
```

Then access the root endpoint in your browser — it will prompt for basic authentication (use your RACF userid and password):

```
http://myzos.mycompany.com:httpPort/cics-java-liberty-springboot-jdbc-multi/
```

This returns a usage page listing all available endpoints. Example requests:

| Operation | Type 2 (native z/OS) | Type 4 (TCP/IP) |
|-----------|---------------------|-----------------|
| List all employees | `/type2/allEmployees` | `/type4/allEmployees` |
| List one employee | `/type2/listEmployee/{empno}` | `/type4/listEmployee/{empno}` |
| Add employee | `/type2/addEmployee/{first}/{last}` | `/type4/addEmployee/{first}/{last}` |
| Add (XA transaction) | `/type2/addEmployeeTx/{first}/{last}` | `/type4/addEmployeeTx/{first}/{last}` |
| Delete employee | `/type2/deleteEmployee/{empNo}` | `/type4/deleteEmployee/{empNo}` |
| Update salary | `/type2/updateEmployee/{empNo}/{salary}` | `/type4/updateEmployee/{empNo}/{salary}` |

> **Note:** Ensure your CICS DB2CONN resource is installed and connected before testing type 2 endpoints.

## Troubleshooting

**Application fails to start — `CWWKZ0014W: application could not be found`**
The `location` in `server.xml` does not match where you uploaded the WAR. Update the `location` attribute to the actual zFS path.

**`JNDI lookup failed` for `jdbc/t2DataSource` or `jdbc/t4DataSource`**
The `dataSource` definitions are missing from `server.xml`. Add both `dataSource` elements from [`etc/config/liberty/server.xml`](etc/config/liberty/server.xml) with your Db2 connection details.

**Type 2 update operations roll back unexpectedly**
This is expected behaviour — the Liberty connection manager rolls back T2 connections on close when not in a global transaction. Use the `...Tx` endpoints (e.g. `/type2/addEmployeeTx`) to wrap operations in a global XA transaction and get consistent commit behaviour across both DataSources.

**`CWWKS9112W` or authentication errors**
Ensure `cicsts:security-1.0` is in your `featureManager` and your RACF user has the `cicsAllAuthenticated` security role.

## License

This project is licensed under the [Eclipse Public License - v 2.0](LICENSE).

## Additional Resources

- [CICS and JDBC documentation](https://www.ibm.com/docs/en/cics-ts/latest?topic=services-jdbc)
- [Developing Spring Boot applications for CICS](https://www.ibm.com/docs/en/cics-ts/latest?topic=liberty-developing-spring-boot-applications)
- [CICS Bundle Maven Plugin](https://github.com/IBM/cics-bundle-maven)
- [CICS Bundle Gradle Plugin](https://github.com/IBM/cics-bundle-gradle)

## Contributing

This sample is maintained by IBM CICS development. We welcome bug reports and feature requests via GitHub Issues. Contributions are welcome and reviewed on a case-by-case basis — please read the [contributing guidelines](https://github.com/cicsdev/.github/blob/main/CONTRIBUTING.md) before opening a pull request. For CICS product questions, contact IBM Support.
