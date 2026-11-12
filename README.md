# cics-java-liberty-springboot-jdbc-multi
[![License](https://img.shields.io/badge/License-EPL%202.0-green.svg)](https://www.eclipse.org/legal/epl-2.0/)

This sample demonstrates a Spring Boot JDBC application that connects to IBM Db2 for z/OS using **two simultaneous DataSources** — one using JDBC type 2 (native z/OS) connectivity, and one using JDBC type 4 (TCP/IP) connectivity. The application is deployed to a CICS Liberty JVM server and uses the Db2 employee sample table (`EMP`).

## Versions

Select the branch for your target environment:

| Spring Boot | Branch | Min Java | Jakarta EE | Build Status | |
|-------------|--------|----------|------------|--------------|---|
| 4.x | [springboot/v4](../../tree/springboot/v4) | 21 | EE 11 | [![Build](https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi/actions/workflows/build.yaml/badge.svg?branch=springboot%2Fv4)](https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi/actions/workflows/build.yaml) | ⚠️ Preview |
| 3.x | [springboot/v3](../../tree/springboot/v3) | 17 | EE 10 | [![Build](https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi/actions/workflows/build.yaml/badge.svg?branch=springboot%2Fv3)](https://github.com/cicsdev/cics-java-liberty-springboot-jdbc-multi/actions/workflows/build.yaml) | |

All branches target CICS TS V6.1 or later.

> ⚠️ **The `springboot/v4` branch is a work-in-progress preview.** It targets Spring Boot 4.x and Jakarta EE 11, which requires Liberty `servlet-6.1` support — not yet available in CICS TS. It is not yet recommended for use.

## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE).

## Contributing

This sample is maintained by IBM CICS development. We welcome bug reports and feature requests via GitHub Issues. Contributions are welcome and reviewed on a case-by-case basis — please read the [contributing guidelines](https://github.com/cicsdev/.github/blob/main/CONTRIBUTING.md) before opening a pull request. For CICS product questions, contact IBM Support.
