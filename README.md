# External Backup
A Maven library for file synchronisation to an external drive.

## Supported functionality
- External drive synchronisation
- External drive backup creation and downloading
- Uploading new files/directories to an external drive
- Updating modified files
- Downloading new files from external drive

## Installation
Add the following lines to your pom.xml file:
<br />
```xml
<project>
    <repositories>
        <repository>
            <id>nexus</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>com.github.bggoranoff</groupId>
            <artifactId>external-backup</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```